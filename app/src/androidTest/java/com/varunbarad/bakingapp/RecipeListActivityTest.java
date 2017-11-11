package com.varunbarad.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.varunbarad.bakingapp.recipelist.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.greaterThan;

/**
 * Creator: Varun Barad
 * Date: 10-11-2017
 * Project: BakingApp
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityTest {
  @Rule
  public ActivityTestRule<RecipeListActivity> activityTestRule =
      new ActivityTestRule<>(RecipeListActivity.class);
  
  private IdlingResource idlingResource;
  
  @Before
  public void registerIdlingResource() {
    this.idlingResource = this.activityTestRule.getActivity().getIdlingResource();
    
    Espresso.registerIdlingResources(this.idlingResource);
  }
  
  @Test
  public void loadRecipeListFromApiTest() {
//    try {
//      Thread.sleep(10000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
  
    onView(withId(R.id.recyclerView_recipeList_recipes))
        .check(new RecyclerViewItemCountAssertion(greaterThan(0)));
  }
  
  @After
  public void unregisterIdlingResource() {
    if (this.idlingResource != null) {
      Espresso.unregisterIdlingResources(this.idlingResource);
    }
  }
}
