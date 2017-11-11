package com.varunbarad.bakingapp;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;

import static android.support.test.espresso.matcher.ViewMatchers.assertThat;

/**
 * Creator: Varun Barad
 * Date: 11-11-2017
 * Project: BakingApp
 */
public class RecyclerViewItemCountAssertion implements ViewAssertion {
  private final Matcher<Integer> countMatcher;
  
  public RecyclerViewItemCountAssertion(Matcher<Integer> countMatcher) {
    this.countMatcher = countMatcher;
  }
  
  @Override
  public void check(View view, NoMatchingViewException noViewFoundException) {
    if (noViewFoundException != null) {
      throw noViewFoundException;
    } else {
      RecyclerView recyclerView = (RecyclerView) view;
      assertThat(recyclerView.getAdapter().getItemCount(), this.countMatcher);
    }
  }
}
