package com.vcco.weather.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.vcco.weather.BuildConfig;
import com.vcco.weather.data.utils.PreferencesConstants;

import javax.inject.Inject;

public class Preference {

    private SharedPreferences sp;

    @Inject
    public Preference(Context context) {
        sp = context.getSharedPreferences(
                BuildConfig.PREFERENCE_FILE,
                Context.MODE_PRIVATE);
    }

    public void saveUnitValue(String unit) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PreferencesConstants.USER_UNIT_PREFERENCE, unit);
        editor.apply();
    }

    public String getUnitValue() {
        return sp.getString(
                PreferencesConstants.USER_UNIT_PREFERENCE,
                PreferencesConstants.DEFAULT_UNIT
        );
    }

    public void saveSearch(String searchResult) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(PreferencesConstants.LAST_WEATHER_SEARCH, searchResult);
        editor.apply();
    }

    public String getLastSearch() {
        return sp.getString(
                PreferencesConstants.LAST_WEATHER_SEARCH,
                PreferencesConstants.EMPTY_STRING
        );
    }

}
