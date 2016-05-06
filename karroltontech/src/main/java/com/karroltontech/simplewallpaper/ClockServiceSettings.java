package com.karroltontech.simplewallpaper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by mranjan on 13-03-2016.
 */


public class ClockServiceSettings extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getPreferenceManager().setSharedPreferencesName(
                ClockService.SHARED_PREFS_NAME);
        addPreferencesFromResource(R.xml.clock_settings);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(
                this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
      getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(
                this);
        super.onDestroy();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {



    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();

    }
}

