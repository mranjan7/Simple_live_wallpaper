package com.karroltontech.simplewallpaper.domain;

import com.karroltontech.simplewallpaper.R;

import java.util.HashMap;

/**
 * Created by mranjan on 17-04-2016.
 */
public class Weatherdata {
    public  HashMap<String,String>  textMap=new HashMap<String,String>();
    public  int[] imageIds=new int[150];
    public HashMap<String,String> textMapForNight=new HashMap<String,String>();
    public int[] imageIdsForNight=new int[150];



    public Weatherdata()
    {

        textMap.put("Sun","Sunny day");
        textMap.put("LightCloud","mainly clear, daily sunny");
        textMap.put("PartlyCloud","Partly Cloudy");
        textMap.put("Cloud","Cloudy");
        textMap.put("LightRainSun","Light rain shower(day)");
        textMap.put("LightRainThunderSun","Light rain and possibility of lightning");
        textMap.put("SleetSun","Sleet shower(day)");
        textMap.put("SnowSun","Partly cloudy with moderate amount of snow");
        textMap.put("LightRain","Light rain");
        textMap.put("Rain","Heavy rain");
        textMap.put("RainThunder","Rain and possibility of lightning");
        textMap.put("Sleet","Sleet");
        textMap.put("Snow","Heavy snow");
        textMap.put("SnowThunder","Snow and possiblity of lightning");
        textMap.put("Fog","Fog");
        textMap.put("SleetSunThunder","Partly cloudy with sleet and possibility of lightning");
        textMap.put("SnowSunThunder","Partly cloudy with snow and possibility of lightning");
        textMap.put("LightRainThunder","Cloudy with rain and possibility of lightning");
        textMap.put("SleetThunder","Cloudy with sleet and possibility of lightning");
        textMap.put("DrizzleThunderSun","Partly cloudy with drizzle and possibility of lightning");
        textMap.put("RainThunderSun","Partly cloudy with heavy rain and possibility of lightning");
        textMap.put("LightSleetThunderSun","Partly cloudy with light sleet and possibility of lightning");
        textMap.put("HeavySleetThunderSun","Partly cloudy with heavy sleet and possibility of lightning");
        textMap.put("LightSnowThunderSun","Partly cloudy with light snow and possibility of lightning");
        textMap.put("HeavySnowThunderSun","Partly cloudy with heavy snow and possibility of lightning");
        textMap.put("DrizzleThunder","Cloudy with drizzle and possibility of lightning");
        textMap.put("LightSleetThunder","Cloudy with light sleet and possibility of lightning");
        textMap.put("HeavySleetThunder","Cloudy with heavy sleet and possibility of lightning");
        textMap.put("LightSnowThunder","Cloudy with light snow and possibility of lightning");
        textMap.put("HeavySnowThunder","Cloudy with heavy snow and possibility of lightning");
        textMap.put("DrizzleSun","Partly cloudy with drizzle");
        textMap.put("RainSun","Partly cloudy with heavy rain");
        textMap.put("LightSleetSun","Partly cloudy with light sleet");
        textMap.put("HeavySleetSun","Partly cloudy with heavy sleet");
        textMap.put("LightSnowSun","Partly cloudy with light snow");
        textMap.put("HeavysnowSun","Partly cloudy with heavy snow");
        textMap.put("Drizzle","Cloudy with drizzle");
        textMap.put("LightSleet","Cloudy with light sleet");
        textMap.put("HeavySleet","Cloudy with heavy sleet");
        textMap.put("LightSnow","Cloudy with light snow");
        textMap.put("HeavySnow","Cloudy with heavy snow");


        textMapForNight.put("Sun","Clear night");
        textMapForNight.put("LightCloud","Mainly clear");
        textMapForNight.put("PartlyCloud","Partly Cloudy");
        textMapForNight.put("Cloud","Cloudy");
        textMapForNight.put("LightRainSun","Light rain shower(day)");
        textMapForNight.put("LightRainThunderSun","Light rain and possibility of lightning");
        textMapForNight.put("SleetSun","Sleet shower(day)");
        textMapForNight.put("SnowSun","Partly cloudy with moderate amount of snow");
        textMapForNight.put("LightRain","Light rain");
        textMapForNight.put("Rain","Heavy rain");
        textMapForNight.put("RainThunder","Rain and possibility of lightning");
        textMapForNight.put("Sleet","Sleet");
        textMapForNight.put("Snow","Heavy snow");
        textMapForNight.put("SnowThunder","Snow and possiblity of lightning");
        textMapForNight.put("Fog","Fog");
        textMapForNight.put("SleetSunThunder","Partly cloudy with sleet and possibility of lightning");
        textMapForNight.put("SnowSunThunder","Partly cloudy with snow and possibility of lightning");
        textMapForNight.put("LightRainThunder","Cloudy with rain and possibility of lightning");
        textMapForNight.put("SleetThunder","Cloudy with sleet and possibility of lightning");
        textMapForNight.put("DrizzleThunderSun","Partly cloudy with drizzle and possibility of lightning");
        textMapForNight.put("RainThunderSun","Partly cloudy with heavy rain and possibility of lightning");
        textMapForNight.put("LightSleetThunderSun","Partly cloudy with light sleet and possibility of lightning");
        textMapForNight.put("HeavySleetThunderSun","Partly cloudy with heavy sleet and possibility of lightning");
        textMapForNight.put("LightSnowThunderSun","Partly cloudy with light snow and possibility of lightning");
        textMapForNight.put("HeavySnowThunderSun","Partly cloudy with heavy snow and possibility of lightning");
        textMapForNight.put("DrizzleThunder","Cloudy with drizzle and possibility of lightning");
        textMapForNight.put("LightSleetThunder","Cloudy with light sleet and possibility of lightning");
        textMapForNight.put("HeavySleetThunder","Cloudy with heavy sleet and possibility of lightning");
        textMapForNight.put("LightSnowThunder","Cloudy with light snow and possibility of lightning");
        textMapForNight.put("HeavySnowThunder","Cloudy with heavy snow and possibility of lightning");
        textMapForNight.put("DrizzleSun","Partly cloudy with drizzle");
        textMapForNight.put("RainSun","Partly cloudy with heavy rain");
        textMapForNight.put("LightSleetSun","Partly cloudy with light sleet");
        textMapForNight.put("HeavySleetSun","Partly cloudy with heavy sleet");
        textMapForNight.put("LightSnowSun","Partly cloudy with light snow");
        textMapForNight.put("HeavysnowSun","Partly cloudy with heavy snow");
        textMapForNight.put("Drizzle","Cloudy with drizzle");
        textMapForNight.put("LightSleet","Cloudy with light sleet");
        textMapForNight.put("HeavySleet","Cloudy with heavy sleet");
        textMapForNight.put("LightSnow","Cloudy with light snow");
        textMapForNight.put("HeavySnow","Cloudy with heavy snow");

        imageIds[0]= R.drawable.img_1_0;
        imageIds[1]=R.drawable.img_2_0;
        imageIds[2]=R.drawable.img_3_0;
        imageIds[3]=R.drawable.img_4_0;
        imageIds[4]=R.drawable.img_5_0;
        imageIds[5]=R.drawable.img_6_0;
        imageIds[6]=R.drawable.img_7_0;
        imageIds[7]=R.drawable.img_8_0;
        imageIds[8]=R.drawable.img_9_0;
        imageIds[9]=R.drawable.img_10_0;
        imageIds[10]=R.drawable.img_11_0;
        imageIds[11]=R.drawable.img_12_0;
        imageIds[12]=R.drawable.img_13_0;
        imageIds[13]=R.drawable.img_14_0;
        imageIds[14]=R.drawable.img_15_0;
        imageIds[19]=R.drawable.img_20_0;
        imageIds[20]=R.drawable.img_21_0;
        imageIds[21]=R.drawable.img_22_0;
        imageIds[22]=R.drawable.img_23_0;
        imageIds[23]=R.drawable.img_24_0;
        imageIds[24]=R.drawable.img_25_0;
        imageIds[25]=R.drawable.img_26_0;
        imageIds[26]=R.drawable.img_27_0;
        imageIds[27]=R.drawable.img_28_0;
        imageIds[28]=R.drawable.img_29_0;
        imageIds[29]=R.drawable.img_30_0;
        imageIds[30]=R.drawable.img_31_0;
        imageIds[31]=R.drawable.img_32_0;
        imageIds[32]=R.drawable.img_33_0;
        imageIds[33]=R.drawable.img_34_0;
        imageIds[39]=R.drawable.img_40_0;
        imageIds[40]=R.drawable.img_41_0;
        imageIds[41]=R.drawable.img_42_0;
        imageIds[42]=R.drawable.img_43_0;
        imageIds[43]=R.drawable.img_44_0;
        imageIds[44]=R.drawable.img_45_0;
        imageIds[45]=R.drawable.img_46_0;
        imageIds[46]=R.drawable.img_47_0;
        imageIds[47]=R.drawable.img_48_0;
        imageIds[48]=R.drawable.img_49_0;
        imageIds[49]=R.drawable.img_50_0;
        imageIds[100]=R.drawable.img_101_0;
        imageIds[101]=R.drawable.img_102_0;
        imageIds[102]=R.drawable.img_103_0;
        imageIds[104]=R.drawable.img_105_0;
        imageIds[105]=R.drawable.img_106_0;
        imageIds[106]=R.drawable.img_107_0;
        imageIds[107]=R.drawable.img_108_0;
        imageIds[119]=R.drawable.img_120_0;
        imageIds[120]=R.drawable.img_121_0;
        imageIds[121]=R.drawable.img_122_0;
        imageIds[122]=R.drawable.img_123_0;
        imageIds[123]=R.drawable.img_124_0;
        imageIds[124]=R.drawable.img_125_0;
        imageIds[125]=R.drawable.img_126_0;
        imageIds[126]=R.drawable.img_127_0;
        imageIds[127]=R.drawable.img_128_0;
        imageIds[128]=R.drawable.img_129_0;
        imageIds[139]=R.drawable.img_140_0;
        imageIds[140]=R.drawable.img_141_0;
        imageIds[141]=R.drawable.img_142_0;
        imageIds[142]=R.drawable.img_143_0;
        imageIds[143]=R.drawable.img_144_0;
        imageIds[144]=R.drawable.img_145_0;

        imageIdsForNight[0]= R.drawable.img_1_1;
        imageIdsForNight[1]=R.drawable.img_2_1;
        imageIdsForNight[2]=R.drawable.img_3_1;
        imageIdsForNight[3]=R.drawable.img_4_1;
        imageIdsForNight[4]=R.drawable.img_5_1;
        imageIdsForNight[5]=R.drawable.img_6_1;
        imageIdsForNight[6]=R.drawable.img_7_1;
        imageIdsForNight[7]=R.drawable.img_8_1;
        imageIdsForNight[8]=R.drawable.img_9_1;
        imageIdsForNight[9]=R.drawable.img_10_1;
        imageIdsForNight[10]=R.drawable.img_11_1;
        imageIdsForNight[11]=R.drawable.img_12_1;
        imageIdsForNight[12]=R.drawable.img_13_1;
        imageIdsForNight[13]=R.drawable.img_14_1;
        imageIdsForNight[14]=R.drawable.img_15_1;
        imageIdsForNight[19]=R.drawable.img_20_1;
        imageIdsForNight[20]=R.drawable.img_21_1;
        imageIdsForNight[21]=R.drawable.img_22_1;
        imageIdsForNight[22]=R.drawable.img_23_1;
        imageIdsForNight[23]=R.drawable.img_24_1;
        imageIdsForNight[24]=R.drawable.img_25_1;
        imageIdsForNight[25]=R.drawable.img_26_1;
        imageIdsForNight[26]=R.drawable.img_27_1;
        imageIdsForNight[27]=R.drawable.img_28_1;
        imageIdsForNight[28]=R.drawable.img_29_1;
        imageIdsForNight[29]=R.drawable.img_30_1;
        imageIdsForNight[30]=R.drawable.img_31_1;
        imageIdsForNight[31]=R.drawable.img_32_1;
        imageIdsForNight[32]=R.drawable.img_33_1;
        imageIdsForNight[33]=R.drawable.img_34_1;
        imageIdsForNight[39]=R.drawable.img_40_1;
        imageIdsForNight[40]=R.drawable.img_41_1;
        imageIdsForNight[41]=R.drawable.img_42_1;
        imageIdsForNight[42]=R.drawable.img_43_1;
        imageIdsForNight[43]=R.drawable.img_44_1;
        imageIdsForNight[44]=R.drawable.img_45_1;
        imageIdsForNight[45]=R.drawable.img_46_1;
        imageIdsForNight[46]=R.drawable.img_47_1;
        imageIdsForNight[47]=R.drawable.img_48_1;
        imageIdsForNight[48]=R.drawable.img_49_1;
        imageIdsForNight[49]=R.drawable.img_50_1;


    }
    public HashMap<String, String> getTextMap() {
        return textMap;
    }

    public void setTextMap(HashMap<String, String> textMap) {
        this.textMap = textMap;
    }

    public int[] getImageIds() {
        return imageIds;
    }

    public void setImageIds(int[] imageIds) {
        this.imageIds = imageIds;
    }
    public HashMap<String, String> getTextMapForNight() {
        return textMapForNight;
    }

    public void setTextMapForNight(HashMap<String, String> textMapForNight) {
        this.textMapForNight = textMapForNight;
    }

    public int[] getImageIdsForNight() {
        return imageIdsForNight;
    }

    public void setImageIdsForNight(int[] imageIdsForNight) {
        this.imageIdsForNight = imageIdsForNight;
    }

}
