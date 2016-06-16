package com.ekasoft.promoexito.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by eka on 28/02/2016.
 */
public class Settings {

    public static String get(Context c, String key){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        return sharedPreferences.getString(key, null);
    }

    public static void set(Context c, String key, String value){
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static boolean isKey(Context c, String key){
        String s = Settings.get(c,key);
        return s!=null;
    }
}
