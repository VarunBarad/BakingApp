package com.varunbarad.bakingapp.util.eventlistener;

/**
 * Creator: Varun Barad
 * Date: 03-11-2017
 * Project: BakingApp
 */
public interface OnFragmentInteractionListener {
  String TAG_LAUNCH_INGREDIENTS = "launch_ingredients";
  String TAG_LAUNCH_STEP = "launch_step";
  String TAG_LAUNCH_STEP_FROM_STEP = "launch_step_from_step";
  String TAG_SET_TITLE = "set_title";
  
  void onFragmentInteraction(String tag, String data);
}
