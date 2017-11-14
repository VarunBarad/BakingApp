package com.varunbarad.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.recipelist.RecipeListActivity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

/**
 * Creator: Varun Barad
 * Date: 14-11-2017
 * Project: BakingApp
 */
@RunWith(AndroidJUnit4.class)
public class ShowRecipeDetailsTest {
  private static final int RECIPE_POSITION = 0;
  
  @Rule
  public IntentsTestRule<RecipeListActivity> activityTestRule = new IntentsTestRule<>(RecipeListActivity.class);
  
  private IdlingResource idlingResource;
  
  @Before
  public void registerIdlingResource() {
    intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    
    this.idlingResource = this.activityTestRule.getActivity().getIdlingResource();
    IdlingRegistry.getInstance().register(this.idlingResource);
  }
  
  @Test
  public void showDetailsOfSelectedRecipeTest() {
    onView(withId(R.id.recyclerView_recipeList_recipes))
        .perform(RecyclerViewActions.actionOnItemAtPosition(
            RECIPE_POSITION,
            click()
        ));
    
    ArrayList<Recipe> recipes = this.activityTestRule.getActivity().getRecipes();
    
    Assert.assertNotNull(recipes);
    
    Recipe r = recipes.get(RECIPE_POSITION);
    
    Assert.assertNotNull(r);
    
    intended(hasExtra("recipe", r.toString()));
  }
  
  @After
  public void unregisterIdlingResource() {
    if (this.idlingResource != null) {
      IdlingRegistry.getInstance().unregister(this.idlingResource);
    }
  }
}
