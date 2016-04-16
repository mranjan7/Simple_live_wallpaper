package com.karroltontech.simplewallpaper;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.support.v4.app.ActivityCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.karroltontech.simplewallpaper.domain.Weather;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    public static final String SHARED_PREFS_NAME = "clocksettings";
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
    private String jsonResponse="";
    private Weather weather;
    private Gson gson;
    private final String APIKEY="b2d4e8eee887a2cfe272b08961570107";
    List<Address> mAddresses;
    private long locationRequestTime;



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



/*        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates OuterClass = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        *//*try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                   ClockService.this,
                                    REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }*//*
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });*/

        okHttpClient=new OkHttpClient();
        gson = new Gson();

    }

    private boolean getLocation()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            System.out.println("came here" + ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) + "" + ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));

            return false;
        }
        mGoogleApiClient.connect();
        locationRequestTime=SystemClock.elapsedRealtime();

        if(mGoogleApiClient.isConnected()) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

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
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(15000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        /*if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }*/


    }



    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            System.out.println("No permission");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation=location;
        mLatitude = mLastLocation.getLatitude();
        mLongitude = mLastLocation.getLongitude();


    }



    class ClockEngine extends Engine implements SharedPreferences.OnSharedPreferenceChangeListener{

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
        private SharedPreferences mPrefs;
        private float mWidth;
        private float mHeight;
        private boolean mShowDate;
        private boolean mShowTime;
        private boolean mShowDay;
        private boolean mShowLocation;
        private GestureDetector gestureDetector1;


        ClockEngine() {

            mStartTime = SystemClock.elapsedRealtime();
            mPrefs = ClockService.this.getSharedPreferences(SHARED_PREFS_NAME, 0);
            mPrefs.registerOnSharedPreferenceChangeListener(this);
            onSharedPreferenceChanged(mPrefs, null);
            mGoogleApiClient.connect();
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);



            // By default we don't get touch events, so enable them.
            setTouchEventsEnabled(true);
            gestureDetector1 = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    System.out.println("onSingleTap" + e.toString());
                    /*if (e.getAction() == MotionEvent.ACTION_MOVE) {
                        mTouchX = e.getX();
                        mTouchY = e.getY();
                    } else {
                        mTouchX = -1;
                        mTouchY = -1;
                    }



                    getLocation();
                    mShowLocation=true;*/
                    if(getLocation())
                    {
                        mShowLocation=true;
                    }
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                   System.out.println("onDoubleTap"+ e.toString());
                    //handle double tap
                    return true;
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
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            mWidth=width;
            mHeight=height;
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
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                mTouchX = event.getX();
                mTouchY = event.getY();
            } else {
                mTouchX = -1;
                mTouchY = -1;
            }
            super.onTouchEvent(event);

            gestureDetector1.onTouchEvent(event);
           if(getLocation())
           {
               mShowLocation=true;
           }



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
            }
            catch(Exception e)
                {
                    e.printStackTrace();
                }
           finally {
                if (c != null) holder.unlockCanvasAndPost(c);
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

            if(SystemClock.elapsedRealtime()-locationRequestTime>10000)
            {
                mGoogleApiClient.disconnect();
                mShowLocation=false;
            }

            c.save();
            //c.translate(0, 0);
            //c.drawColor(0xff000000);
            //System.out.println(mCenterX + " " + mCenterY);
            //Log.i("logs", mCenterX + " " + mCenterY);
            //c.drawText(SystemClock.elapsedRealtime() + "", mCenterX, mCenterY, mPaint);


            Paint paint = new Paint();
            int ySize=0;
            int initialPadding=getResources().getDimensionPixelSize(R.dimen.initialPadding);
            int padding=getResources().getDimensionPixelSize(R.dimen.padding);
            if(mColorCode==null||"Default".equals(mColorCode)||"D".equalsIgnoreCase(mColorCode)) {
                paint.setColor(Color.rgb(255, 160, 122));
            }
            else if("T".equalsIgnoreCase(mColorCode))
            {
                paint.setColor(Color.TRANSPARENT);
            }
            else
            {
                paint.setColor(Color.parseColor(mColorCode));
            }
            paint.setStyle(Paint.Style.FILL);
            c.drawPaint(paint);

            paint.setColor(Color.WHITE);

            paint.setTextAlign(Paint.Align.LEFT);




            DateFormat dateFormat=DateFormat.getDateInstance();
            //DateFormat timeFormat=DateFormat.getTimeInstance();

            SimpleDateFormat timeFormat=new SimpleDateFormat("h:mm a");
            SimpleDateFormat dayFormat=new SimpleDateFormat("EEEE");
            ySize=ySize+initialPadding;
            ySize=ySize+padding;

            if(mShowTime) {
                int timeFontSize=getResources().getDimensionPixelSize(R.dimen.timeFontSize);
                paint.setTextSize(timeFontSize);
                ySize=ySize+timeFontSize;
                c.drawText(timeFormat.format(new Date()), 0, ySize, paint);
                ySize=ySize+padding;
            }
            if(mShowDay) {
                int dayFontSize = getResources().getDimensionPixelSize(R.dimen.dayFontSize);
                paint.setTextSize(dayFontSize);
                ySize=ySize+dayFontSize;
                c.drawText(dayFormat.format(new Date()), 0, ySize, paint);
                ySize = ySize + padding;
            }
            if(mShowDate)
            {
                int dateFontSize=getResources().getDimensionPixelSize(R.dimen.dateFontSize);
                paint.setTextSize(dateFontSize);
                ySize=ySize+dateFontSize;
                c.drawText(dateFormat.format(new Date()), 0, ySize, paint);
                ySize=ySize+padding;

            }
            if(mShowLocation) {

               // LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //Criteria criteria = new Criteria();
                //String provider = locationManager.getBestProvider(criteria, false);
                //System.out.println(provider);
                //Location location = locationManager.getLastKnownLocation(provider);

                try {
                    //System.out.println(mGeocoder);
                    //System.out.println(location);
                    mAddresses = mGeocoder.getFromLocation(mLatitude,
                        mLongitude,
                              1);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    // Catch network or other I/O problems.
                    //errorMessage = getString(R.string.service_not_available);
                    //Log.e(TAG, errorMessage, ioException);
                } catch (IllegalArgumentException illegalArgumentException) {
                    illegalArgumentException.printStackTrace();
                    // Catch invalid latitude or longitude values.
                    //errorMessage = getString(R.string.invalid_lat_long_used);
                    //Log.e(TAG, errorMessage + ". " +
                    //      "Latitude = " + location.getLatitude() +
                    //    ", Longitude = " +
                    //  location.getLongitude(), illegalArgumentException);
                }
                int locationFontSize = getResources().getDimensionPixelSize(R.dimen.locationFontSize);
                paint.setTextSize(locationFontSize);
                ySize = ySize + locationFontSize;
                if(mAddresses !=null&& mAddresses.size()!=0)
                {
                    //System.out.println(mAddresses);
                    if(mAddresses.get(0).getAddressLine(0)!=null)
                    {
                        mAddress= mAddresses.get(0).getAddressLine(0);

                        c.drawText(mAddress, 0, ySize, paint);
                    }
                }

                ySize = ySize + padding;
                if(mLatitude!=0&&mLongitude!=0)
                {



                    String latitude=mLatitude+"";
                    String longitude=mLongitude+"";

                    Request request=new Request.Builder().url("https://api.forecast.io/forecast/"+APIKEY+"/"+latitude+","+longitude).build();
                    if(call==null||call.isExecuted()) {
                        call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    jsonResponse=response.body().string();
                                    System.out.println(jsonResponse);
                                    weather=gson.fromJson(jsonResponse,Weather.class);

                                }

                            }
                        });
                    }

                }

                if(weather!=null)
                {
                    ySize = ySize + locationFontSize;
                    c.drawText(weather.getCurrently().getSummary() + ", " + Math.round(weather.getCurrently().getTemperature()) + "° F", 0, ySize, paint);
                    //System.out.println("summary : "+weather.getCurrently().getSummary());
                    //System.out.println("temperature : "+weather.getCurrently().getTemperature());
                }
                ySize = ySize + padding;





            }
            c.restore();
        }



        /*
         * Draw a circle around the current touch point, if any.
         */
        void drawTouchPoint(Canvas c) {
            if (mTouchX >=0 && mTouchY >= 0) {
                c.drawCircle(mTouchX, mTouchY, 80, mPaint);
            }
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

            String color = sharedPreferences.getString("clock_color", "Default");
            mColor=color;
            String colorCode=sharedPreferences.getString("color_code", "Default");
            mColorCode=colorCode;
            mShowDate=sharedPreferences.getBoolean("show_date",true);
            mShowTime=sharedPreferences.getBoolean("show_time",true);
            mShowDay=sharedPreferences.getBoolean("show_day",true);
            //mShowLocation=sharedPreferences.getBoolean("show_location",true);


        }
    }

}




