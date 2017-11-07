package com.varunbarad.bakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.varunbarad.bakingapp.R;

import io.realm.RealmObject;

/**
 * Creator: Varun Barad
 * Date: 02-11-2017
 * Project: BakingApp
 */
public final class Helper {
  public static boolean isLandscape(Context context) {
    return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
  }
  
  public static boolean isDualPane(Context context) {
    return context.getResources().getBoolean(R.bool.is_dualPane);
  }
  
  public static Gson getGsonInstance() {
    return new GsonBuilder()
        .setExclusionStrategies(new ExclusionStrategy() {
          @Override
          public boolean shouldSkipField(FieldAttributes f) {
            return f.getDeclaringClass().equals(RealmObject.class);
          }
          
          @Override
          public boolean shouldSkipClass(Class<?> clazz) {
            return false;
          }
        })
        .create();
  }
  
  public static boolean isConnectedToInternet(Context context) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    
    boolean isConnected;
    
    isConnected = (activeNetwork != null) && activeNetwork.isConnected();
    
    return isConnected;
  }
  
  public static boolean isRecipesUpdateNeeded(Context context) {
    final long ACCEPTABLE_DELAY = 10 * 60 * 1000;
    
    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    
    long lastLoadTime = preferences.getLong(context.getString(R.string.PREFS_KEY_RECIPE_TIMESTAMP), 0);
    return ((System.currentTimeMillis() - lastLoadTime) > ACCEPTABLE_DELAY);
  }
  
  public static void storeRecipesTimestamp(Context context) {
    SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.PREFS_NAME), Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    
    editor.putLong(context.getString(R.string.PREFS_KEY_RECIPE_TIMESTAMP), System.currentTimeMillis());
    editor.apply();
  }
}
