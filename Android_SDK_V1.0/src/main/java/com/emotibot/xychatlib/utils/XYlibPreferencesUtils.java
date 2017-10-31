package com.emotibot.xychatlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ldy on 2017/2/22.
 */

public class XYlibPreferencesUtils {
    public static final String UID = "userId";
    private SharedPreferences mPrefrences;

    public XYlibPreferencesUtils(Context context) {
        mPrefrences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void setInt(String key, int value) {
        mPrefrences.edit().putInt(key, value).commit();
    }

    public int getInt(String key) {
        return mPrefrences.getInt(key, -1);
    }

    public int getIntWithDefaultValue(String key, int defaultValue) {
        return mPrefrences.getInt(key, defaultValue);
    }

    public void setString(String key, String value) {
        mPrefrences.edit().putString(key, value).commit();
    }

    public String getString(String key) {
        return mPrefrences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return mPrefrences.getBoolean(key, false);
    }

    public void setBoolean(String key, boolean value) {
        mPrefrences.edit().putBoolean(key, value).commit();
    }
}
