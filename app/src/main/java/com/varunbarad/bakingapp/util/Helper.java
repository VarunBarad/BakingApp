package com.varunbarad.bakingapp.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Creator: Varun Barad
 * Date: 02-11-2017
 * Project: BakingApp
 */
public final class Helper {
  public static boolean isLandscape(Context context) {
    return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
  }
}
