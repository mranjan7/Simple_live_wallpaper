package com.karroltontech.simplewallpaper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.util.Xml;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karroltontech.simplewallpaper.domain.Weather;
import com.karroltontech.simplewallpaper.domain.Weatherdata;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mranjan on 12-03-2016.
 */
public class ClockService extends WallpaperService implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String SHARED_PREFS_NAME = "clocksettings";
    public static final String TAG = ClockService.class.getSimpleName();
    private final Handler mHandler = new Handler();
    private Geocoder mGeocoder;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double mLatitude;
    private double mLongitude;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates = true;
    private String mAddress;
    private OkHttpClient okHttpClient;
    private Call call;
    private String jsonResponse = "";
    private Weather weather;

    private final String APIKEY = "b2d4e8eee887a2cfe272b08961570107";
    List<Address> mAddresses;
    private long locationRequestTime;
    private HashMap<String, String> textMap;
    private String temperature;
    private String symbolNumber;
    private int[] imageIds;
    private String symbolText;
    private Weatherdata weatherdata;
    private boolean locationWeatherDisplayed;
    private boolean mShowLocation;
    private boolean isLocationWeatherDataSetUp;
    private boolean  isOnTouchEventCompleted;


    @Override
    public void onCreate() {
        super.onCreate();

        mGeocoder = new Geocoder(this, Locale.getDefault());
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


    }

    private boolean getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(), "You need to give permission to this wallpaper to access the location", Toast.LENGTH_LONG).show();


            return false;
        }
        locationRequestTime = SystemClock.elapsedRealtime();

        Log.d(TAG, "google api client connected" + mGoogleApiClient.isConnected());
        if (mGoogleApiClient.isConnected()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            Log.d(TAG,"mLastlocation"+mLastLocation);

            if (mLastLocation == null) {
                createLocationRequest();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            } else {
                mLatitude = mLastLocation.getLatitude();
                mLongitude = mLastLocation.getLongitude();
                return true;
            }
        }

        return false;
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private void removeLocationUpadtesAndDisconnect() {
        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Engine onCreateEngine() {
        return new ClockEngine();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG,"On connected "+bundle);
    setUpLocationAndWeatherData();

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG,"On location changed "+location);

        isLocationWeatherDataSetUp=false;
        mLastLocation = location;
        if(getAddressFromGeocoder()&&getTemperatureAndSymbol())
        {
            isLocationWeatherDataSetUp=true;
        }



    }

    private void setUpLocationAndWeatherData() {

        if (getLocation()&&getAddressFromGeocoder()&&getTemperatureAndSymbol()) {

            isLocationWeatherDataSetUp=true;

        }
    }
    private boolean getTemperatureAndSymbol() {
        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(new StringReader(jsonResponse));
            int event;
            String text = null;
            event = xmlPullParser.getEventType();
            boolean temperatureObtained = false;
            boolean symbolObtained = false;
            outer:
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = xmlPullParser.getName();

                //System.out.println("name : "+name);
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        if ("temperature".equals(name)) {
                            text = xmlPullParser.getAttributeValue(null, "value");
                        } else if (name != null) {
                            text = xmlPullParser.getAttributeValue(0);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //System.out.println("text"+text);
                        if (name.equals("temperature")) {
                            //System.out.println("temperature"+xmlPullParser.getAttributeValue(null,"value"));
                            temperature = xmlPullParser.getAttributeValue(null, "value");
                            //System.out.println(xmlPullParser);
                            temperatureObtained = true;

                        } else if (name.equals("symbol")) {

                            symbolNumber = xmlPullParser.getAttributeValue(null, "number");
                            symbolText = xmlPullParser.getAttributeValue(null, "id");
                            symbolObtained = true;

                        }
                        break;

                }
                if (temperatureObtained && symbolObtained) {
                    break outer;
                }
                event = xmlPullParser.next();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean getAddressFromGeocoder() {
        try {

            mAddresses = mGeocoder.getFromLocation(mLatitude,
                    mLongitude,
                    1);
            //System.out.println(mAddresses);
            return true;

        } catch (IOException ioException) {
            ioException.printStackTrace();
            return false;

        } catch (IllegalArgumentException illegalArgumentException) {
            illegalArgumentException.printStackTrace();
            return false;

        }
    }


    class ClockEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener {

        private final Paint mPaint = new Paint();
        private float mOffset;
        private float mTouchX = -1;
        private float mTouchY = -1;
        private long mStartTime;
        private float mCenterX;
        private float mCenterY;

        private final Runnable mDrawWallpaper = new Runnable() {
            public void run() {
                drawFrame();
            }
        };
        private boolean mVisible;
        private String mColor;
        private String mColorCode;
        private String mColorCodeText;
        private SharedPreferences mPrefs;
        private float mWidth;
        private float mHeight;
        private boolean mShowDate;
        private boolean mShowTime;
        private boolean mShowDay;

        private GestureDetector gestureDetector1;



        ClockEngine() {

            mStartTime = SystemClock.elapsedRealtime();
            mPrefs = ClockService.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
            mPrefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(mPrefs, null);

            weatherdata = new Weatherdata();
            okHttpClient = new OkHttpClient();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            mGoogleApiClient.connect();


            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
            gestureDetector1 = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    Log.d(TAG,"On Single tap Confirmed "+e);
                    if (e.getAction() == MotionEvent.ACTION_MOVE) {
                        mTouchX = e.getX();
                        mTouchY = e.getY();
                    } else {
                        mTouchX = -1;
                        mTouchY = -1;
                    }

                    mGoogleApiClient.connect();
                    if (getLocation()) {
                        mShowLocation = true;
                    }
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    Log.d(TAG,"On Double tap Confirmed "+e);
                    startSettings();
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    Log.d(TAG,"On Double tap event "+e);
                    return super.onDoubleTapEvent(e);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    Log.d(TAG,"On fling event ");
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            mHandler.removeCallbacks(mDrawWallpaper);
            mGoogleApiClient.disconnect();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            mVisible = visible;
            if (visible) {
                drawFrame();
            } else {
                mHandler.removeCallbacks(mDrawWallpaper);
            }
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            mCenterX = width / 2.0f;
            mCenterY = height / 2.0f;
            mWidth = width;
            mHeight = height;
            drawFrame();
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mVisible = false;
            mHandler.removeCallbacks(mDrawWallpaper);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                                     float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            drawFrame();
        }

        /*
         * Store the position of the touch event so we can use it for drawing later
         */
        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);

            gestureDetector1.onTouchEvent(event);
            Log.d(TAG, "On touch event" + event);
            isOnTouchEventCompleted=false;
            if(!mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }

            setUpLocationAndWeatherData();


        }



        /*
         * Draw one frame of the animation. This method gets called repeatedly
         * by posting a delayed Runnable. You can do any drawing you want in
         * here. This example draws a wireframe cube.
         */
        void drawFrame() {
            final SurfaceHolder holder = getSurfaceHolder();

            Canvas c = null;
            try {
                c = holder.lockCanvas();
                if (c != null) {
                    // draw something
                    drawWallpaper(c);
                    //drawTouchPoint(c);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (c != null) holder.unlockCanvasAndPost(c);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            // Reschedule the next redraw
            mHandler.removeCallbacks(mDrawWallpaper);
            if (mVisible) {
                mHandler.postDelayed(mDrawWallpaper, 1000 / 25);
            }
        }

        /*
         * Draw a wireframe cube by drawing 12 3 dimensional lines between
         * adjacent corners of the cube
         */
        void drawWallpaper(Canvas c) {
            c.save();
            int ySize = 0;
            int initialPadding = getResources().getDimensionPixelSize(R.dimen.initialPadding);
            int padding = getResources().getDimensionPixelSize(R.dimen.padding);
            int imageSpace = getResources().getDimensionPixelSize(R.dimen.imageSpace);
            Paint paint = setUpPaint(c);

            ySize = ySize + initialPadding;
            ySize = ySize + padding;

            if (mShowTime) {
                ySize = drawTime(c, paint, ySize, padding);
            }
            if (mShowDay) {
                ySize = drawDay(c, paint, ySize, padding);
            }
            if (mShowDate) {
                ySize = drawDate(c, paint, ySize, padding);

            }
            if (mShowLocation) {

                if(!isOnTouchEventCompleted) {
                    if (isLocationWeatherDataSetUp) {
                        ySize = drawLocation(c, paint, ySize, padding);

                        drawTemperatureAndWeather(c, paint, ySize, padding, imageSpace);
                    } else {
                        drawProgress(c, ySize, padding, paint);

                    }
                }


            }
            c.restore();
            if (!isOnTouchEventCompleted&&SystemClock.elapsedRealtime() - locationRequestTime > 10000) {
                removeLocationUpadtesAndDisconnect();
                isLocationWeatherDataSetUp=false;
                isOnTouchEventCompleted=true;

            }
        }

        private void drawProgress(Canvas c, int ySize, int padding, Paint paint) {
            int locationFontSize = getResources().getDimensionPixelSize(R.dimen.locationFontSize);
            paint.setTextSize(locationFontSize);
            ySize = ySize + locationFontSize;
            ySize = ySize + padding;
            c.drawText("Gettting location and weather ....", 0, ySize, paint);
        }

        @NonNull
        private Paint setUpPaint(Canvas c) {
            Paint paint = new Paint();
            if (mColorCode == null || "Default".equals(mColorCode) || "D".equalsIgnoreCase(mColorCode)) {
                paint.setColor(Color.rgb(255, 160, 122));
            } else {
                paint.setColor(Color.parseColor(mColorCode));
            }
            paint.setStyle(Paint.Style.FILL);

            c.drawPaint(paint);

            if (mColorCodeText == null || "Default".equals(mColorCodeText) || "D".equalsIgnoreCase(mColorCodeText)) {
                paint.setColor(Color.WHITE);
            } else {
                paint.setColor(Color.parseColor(mColorCodeText));
            }


            paint.setTextAlign(Paint.Align.LEFT);
            return paint;
        }

        private void drawTemperatureAndWeather(Canvas c, Paint paint, int ySize, int padding, int imageSpace) {
            TextPaint textPaint = new TextPaint(paint);


            ySize = ySize + padding;


            String latitude = mLatitude + "";
            String longitude = mLongitude + "";


            Request request = new Request.Builder().url("http://api.met.no/weatherapi/locationforecastlts/1.2/?lat=" + latitude + ";lon=" + longitude).build();
            if (isNetworkAvailable()) {
                if (call == null ) {

                        call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Toast.makeText(getApplicationContext(), "There was an error. Please try again", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {

                                    jsonResponse = response.body().string();
                                    locationWeatherDisplayed = true;



                                    getTemperatureAndSymbol();


                                }

                            }
                        });

                }


                    drawTemperatureWeatherOnCanvas(c, paint, ySize, padding, imageSpace);



            } else {
                Toast.makeText(getApplicationContext(), "Network is not available !", Toast.LENGTH_LONG).show();
            }
        }

        private void drawTemperatureWeatherOnCanvas(Canvas c, Paint paint, int ySize, int padding, int imageSpace) {
            int temperatureFontSize = getResources().getDimensionPixelSize(R.dimen.temperatureFontSize);
            ySize = ySize + temperatureFontSize;
            paint.setTextSize(temperatureFontSize);

            double tempCelsius = Double.parseDouble(temperature);
            double tempFahrenheit = tempCelsius + 32;


            c.drawText(tempFahrenheit + "° F( " + tempCelsius + " ° C )", 0, ySize, paint);


            int weatherFontSize = getResources().getDimensionPixelSize(R.dimen.weatherFontSize);

            ySize = ySize + padding;
            int imageId = -1;
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (hour > 19) {
                imageId = weatherdata.getImageIdsForNight()[Integer.parseInt(symbolNumber) - 1];
            } else {
                imageId = weatherdata.getImageIds()[Integer.parseInt(symbolNumber) - 1];
            }

            Bitmap icon = BitmapFactory.decodeResource(getResources(), imageId);
            c.drawBitmap(icon, 0, ySize, paint);
            paint.setTextSize(weatherFontSize);
            ySize = ySize + padding;
            String weatherText = "";
            if (hour > 19) {
                weatherText = weatherdata.getTextMapForNight().get(symbolText);
            } else {
                weatherText = weatherdata.getTextMap().get(symbolText);
            }


            int paddingForStaticLayout = getResources().getDimensionPixelSize(R.dimen.paddingForStaticLayout);
            int lengthForStaticLayout = getResources().getDimensionPixelSize(R.dimen.lengthForStaticLayout);

            StaticLayout layout = new StaticLayout(weatherText, new TextPaint(paint), lengthForStaticLayout, Layout.Alignment.ALIGN_NORMAL, paddingForStaticLayout, 0, false);
            c.translate(imageSpace, ySize); //position the text
            layout.draw(c);
        }

        private int drawLocation(Canvas c, Paint paint, int ySize, int padding) {

            int locationFontSize = getResources().getDimensionPixelSize(R.dimen.locationFontSize);
            paint.setTextSize(locationFontSize);
            ySize = ySize + locationFontSize;
            ySize = ySize + padding;
            if (mAddresses != null && mAddresses.size() != 0) {

                int maxAddressLineIndex = mAddresses.get(0).getMaxAddressLineIndex();
                String countryName = mAddresses.get(0).getCountryName();
                int addressLineCount = 0;
                for (int i = 0; i <= maxAddressLineIndex && addressLineCount <= 1; i++) {
                    String addressLine = mAddresses.get(0).getAddressLine(i);
                    if (addressLine.indexOf(countryName) != -1) {
                        if (i == maxAddressLineIndex) {
                            ySize = ySize - padding - locationFontSize;
                        }
                        continue;
                    }
                    c.drawText(addressLine, 0, ySize, paint);
                    addressLineCount++;

                    if (i != maxAddressLineIndex && addressLineCount != 2) {
                        ySize = ySize + padding;
                        ySize = ySize + locationFontSize;
                    }

                }



            }
            return ySize;
        }

        private int drawDate(Canvas c, Paint paint, int ySize, int padding) {
            int dateFontSize = getResources().getDimensionPixelSize(R.dimen.dateFontSize);
            DateFormat dateFormat = DateFormat.getDateInstance();
            paint.setTextSize(dateFontSize);
            ySize = ySize + dateFontSize;
            c.drawText(dateFormat.format(new Date()), 0, ySize, paint);
            ySize = ySize + padding;
            return ySize;
        }

        private int drawDay(Canvas c, Paint paint, int ySize, int padding) {
            int dayFontSize = getResources().getDimensionPixelSize(R.dimen.dayFontSize);
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            paint.setTextSize(dayFontSize);
            ySize = ySize + dayFontSize;
            c.drawText(dayFormat.format(new Date()), 0, ySize, paint);
            ySize = ySize + padding;
            return ySize;
        }

        private int drawTime(Canvas c, Paint paint, int ySize, int padding) {
            int timeFontSize = getResources().getDimensionPixelSize(R.dimen.timeFontSize);
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            paint.setTextSize(timeFontSize);
            ySize = ySize + timeFontSize;
            c.drawText(timeFormat.format(new Date()), 0, ySize, paint);
            ySize = ySize + padding;
            return ySize;
        }





        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }


        /*
         * Draw a circle around the current touch point, if any.
         */
        void drawTouchPoint(Canvas c) {
            //Log.d(TAG,"x "+mTouchX+" y "+mTouchY);
            if (mTouchX >= 0 && mTouchY >= 0) {

                c.drawCircle(mTouchX, mTouchY, 80, mPaint);
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

            String color = sharedPreferences.getString("clock_color", "Default");
            mColor = color;
            String colorCode = sharedPreferences.getString("color_code", "Default");
            mColorCode = colorCode;
            String colorCodeText = sharedPreferences.getString("color_code_text", "Default");
            mColorCodeText = colorCodeText;
            mShowDate = sharedPreferences.getBoolean("show_date", true);
            mShowTime = sharedPreferences.getBoolean("show_time", true);
            mShowDay = sharedPreferences.getBoolean("show_day", true);
            //mShowLocation=sharedPreferences.getBoolean("show_location",true);


        }
    }

    private void startSettings() {
        Intent intent=new Intent(this,ClockServiceSettings.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if ((networkInfo != null && networkInfo.isConnected())) {
            isAvailable = true;
        }
        return isAvailable;

    }

}




