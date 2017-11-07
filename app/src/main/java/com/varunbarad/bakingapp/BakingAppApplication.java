package com.varunbarad.bakingapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Creator: Varun Barad
 * Date: 07-11-2017
 * Project: BakingApp
 */
public class BakingAppApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Realm.init(this);
  }
}
