package com.live.location.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

    Context mContext;

    public PreferencesUtil(Context context) {

        mContext = context;
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key) {
        SharedPreferences editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return editor.getString(key, "");
    }

    public void setBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBoolean(String key) {
        SharedPreferences editor = mContext.getSharedPreferences("Pref", Context.MODE_PRIVATE);
        return editor.getBoolean(key, false);
    }
}
