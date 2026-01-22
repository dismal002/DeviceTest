package com.dismal.devicetest;

import android.content.Context;
import android.content.SharedPreferences;

public class LanguagePreference {
    private Context mContext = null;
    private SharedPreferences mPref = null;

    public LanguagePreference(Context context) {
        this.mContext = context;
        this.mPref = this.mContext.getSharedPreferences("lang_pref", 0);
    }
}
