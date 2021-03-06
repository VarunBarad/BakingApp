package com.varunbarad.bakingapp;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.model.RecipeStep;
import com.varunbarad.bakingapp.recipedetails.RecipeDetailsActivity;
import com.varunbarad.bakingapp.util.Helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Creator: Varun Barad
 * Date: 15-11-2017
 * Project: BakingApp
 */
@RunWith(AndroidJUnit4.class)
public class PreviousNextStepButtonTest {
  private static final int STEP_NUMBER = 3;
  private static final int STEP_POSITION = STEP_NUMBER + 1;
  
  @Rule
  public ActivityTestRule<RecipeDetailsActivity> activityTestRule = new ActivityTestRule<>(
      RecipeDetailsActivity.class,
      true,
      false
  );
  
  private Recipe sampleRecipe = this.prepareSampleRecipe();
  
  private Recipe prepareSampleRecipe() {
    return Helper.getGsonInstance().fromJson(
        InstrumentationRegistry.getContext().getString(com.varunbarad.bakingapp.test.R.string.sample_recipe_json),
        Recipe.class
    );
  }
  
  private void launchActivity(Recipe recipe) {
    Intent activityStarterIntent = RecipeDetailsActivity.getStarterIntent(
        InstrumentationRegistry.getTargetContext(),
        recipe
    );
    
    activityTestRule.launchActivity(activityStarterIntent);
  }
  
  @Before
  public void initialiseTest() {
    this.launchActivity(this.sampleRecipe);
  }
  
  @Test
  public void clickPreviousButtonTest() {
    // Scroll to desired step-item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.scrollToPosition(STEP_POSITION));
    
    // Click on the step-item item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(
            STEP_POSITION,
            click()
        ));
    
    // Check that previous button is visible
    onView(withId(R.id.button_stepDetails_previousStep))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    
    // Click the previous button
    onView(withId(R.id.button_stepDetails_previousStep))
        .perform(click());
    
    RecipeStep newRecipeStep = this.sampleRecipe.getSteps().get(STEP_NUMBER - 1);
    
    // Check if correct description is set
    onView(withId(R.id.textView_stepDetails_description))
        .check(matches(withText(newRecipeStep.getDescription())));
    
    // Check if video-player is visible/hidden according to scenario
    if ((newRecipeStep.getVideoUrl() != null) && (!newRecipeStep.getVideoUrl().trim().isEmpty())) {
      onView(withId(R.id.playerView_stepDetails_video))
          .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    } else {
      onView(withId(R.id.playerView_stepDetails_video))
          .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
  }
  
  @Test
  public void clickNextButtonTest() {
    // Scroll to desired step-item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.scrollToPosition(STEP_POSITION));
    
    // Click on the step-item item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(
            STEP_POSITION,
            click()
        ));
    
    // Check that previous button is visible
    onView(withId(R.id.button_stepDetails_previousStep))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    
    // Click the previous button
    onView(withId(R.id.button_stepDetails_nextStep))
        .perform(click());
    
    RecipeStep newRecipeStep = this.sampleRecipe.getSteps().get(STEP_NUMBER + 1);
    
    // Check if correct description is set
    onView(withId(R.id.textView_stepDetails_description))
        .check(matches(withText(newRecipeStep.getDescription())));
    
    // Check if video-player is visible/hidden according to scenario
    if ((newRecipeStep.getVideoUrl() != null) && (!newRecipeStep.getVideoUrl().trim().isEmpty())) {
      onView(withId(R.id.playerView_stepDetails_video))
          .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    } else {
      onView(withId(R.id.playerView_stepDetails_video))
          .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }
  }
  
  @Test
  public void checkPreviousButtonAbsentOnFirstStepTest() {
    final int stepNumber = 0;
    
    // Scroll to desired step-item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.scrollToPosition(stepNumber));
    
    // Click on the step-item item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(
            stepNumber + 1,
            click()
        ));
    
    // Check that previous button is invisible
    onView(withId(R.id.button_stepDetails_previousStep))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    
    // Check that next button is visible
    onView(withId(R.id.button_stepDetails_nextStep))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
  }
  
  @Test
  public void checkNextButtonAbsentOnLastStepTest() {
    final int stepNumber = this.sampleRecipe.getSteps().size() - 1;
    
    // Scroll to desired step-item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.scrollToPosition(stepNumber + 1));
    
    // Click on the step-item item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(
            stepNumber + 1,
            click()
        ));
    
    // Check that previous button is visible
    onView(withId(R.id.button_stepDetails_previousStep))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    
    // Check that next button is invisible
    onView(withId(R.id.button_stepDetails_nextStep))
        .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
  }
  
  @After
  public void destroySampleRecipe() {
    this.sampleRecipe = null;
  }
}
