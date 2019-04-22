package com.chinaairlines.component;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {

    public void setPrefs(Context context, String name, String key, String value) 
    {
         SharedPreferences settings = context.getSharedPreferences(name, 0);
         settings.edit()
         		 .putString(key, value)
         		 .commit();
    }
    
    public String getPrefs(Context context, String name, String key) 
    {
         SharedPreferences settings = context.getSharedPreferences(name, 0);
         return settings.getString(key, null);
    }
    
    public void clearPrefs(Context context, String name, String key) 
    {
         SharedPreferences settings = context.getSharedPreferences(name, 0);
         settings.edit()
         		 .remove(key)
         		 .commit();
    }
    
}
