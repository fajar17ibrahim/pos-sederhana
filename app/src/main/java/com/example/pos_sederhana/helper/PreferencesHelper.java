package com.example.pos_sederhana.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.pos_sederhana.utils.StringUtils;

public class PreferencesHelper {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public PreferencesHelper(Activity activity) {
        sharedPref = activity.getSharedPreferences(StringUtils.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setTrxId(int id) {
        editor.putInt(StringUtils.TRX_ID, id);
        editor.apply();
    }

    public int getTrxId() {
        return sharedPref.getInt(StringUtils.TRX_ID, 0);
    }

    public void clearTrxId() {
        editor.putInt(StringUtils.TRX_ID, 0);
        editor.apply();
    }
}
