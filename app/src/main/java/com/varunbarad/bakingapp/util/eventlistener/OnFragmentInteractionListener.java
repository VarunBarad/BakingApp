package com.varunbarad.bakingapp.util.eventlistener;

/**
 * Creator: Varun Barad
 * Date: 03-11-2017
 * Project: BakingApp
 */
public interface OnFragmentInteractionListener {
  String TAG_LAUNCH_INGREDIENTS = "launch_ingredients";
  void onFragmentInteraction(String tag, String data);
}
