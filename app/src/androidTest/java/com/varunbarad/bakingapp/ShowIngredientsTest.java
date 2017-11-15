package com.varunbarad.bakingapp;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.recipedetails.RecipeDetailsActivity;
import com.varunbarad.bakingapp.util.Helper;
import com.varunbarad.bakingapp.util.RecyclerViewMatcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Creator: Varun Barad
 * Date: 15-11-2017
 * Project: BakingApp
 */
@RunWith(AndroidJUnit4.class)
public class ShowIngredientsTest {
  private static final int INGREDIENTS_POSITION = 0;
  private static final int SELECTED_INGREDIENT_POSITION = 0;
  
  @Rule
  public ActivityTestRule<RecipeDetailsActivity> activityTestRule = new ActivityTestRule<>(
      RecipeDetailsActivity.class,
      true,
      false
  );
  
  private Recipe sampleRecipe;
  
  private void launchActivity(Recipe recipe) {
    Intent activityStarterIntent = RecipeDetailsActivity.getStarterIntent(
        InstrumentationRegistry.getTargetContext(),
        recipe
    );
    
    activityTestRule.launchActivity(activityStarterIntent);
  }
  
  @Before
  public void prepareSampleRecipe() {
    this.sampleRecipe = Helper.getGsonInstance().fromJson(
        InstrumentationRegistry.getContext().getString(com.varunbarad.bakingapp.test.R.string.sample_recipe_json),
        Recipe.class
    );
  }
  
  @Test
  public void showIngredientsTest() {
    this.launchActivity(this.sampleRecipe);
    
    String quantityString =
        InstrumentationRegistry
            .getTargetContext()
            .getString(
                R.string.ingredient_quantity,
                this.sampleRecipe.getIngredients().get(SELECTED_INGREDIENT_POSITION).getQuantity(),
                this.sampleRecipe.getIngredients().get(SELECTED_INGREDIENT_POSITION).getMeasure()
            );
  
    // Scroll to "Ingredients" item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.scrollToPosition(INGREDIENTS_POSITION));
  
    // Click on the "Ingredients" item in steps-list
    onView(withId(R.id.recyclerView_recipeDetails_stepsList_steps))
        .perform(RecyclerViewActions.actionOnItemAtPosition(
            INGREDIENTS_POSITION,
            click()
        ));
  
    // Scroll to desired ingredient in the ingredients list
    onView(withId(R.id.recyclerView_recipeDetails_ingredientsList_ingredients))
        .perform(RecyclerViewActions.scrollToPosition(SELECTED_INGREDIENT_POSITION));
  
    // Check if that ingredient has the correct name
    RecyclerViewMatcher
        .withRecyclerView(R.id.recyclerView_recipeDetails_ingredientsList_ingredients)
        .atPositionOnView(
            SELECTED_INGREDIENT_POSITION,
            R.id.textView_listItemRecipeDetailsStepsIngredient_ingredientTitle
        )
        .matches(withText(
            this.sampleRecipe.getIngredients().get(SELECTED_INGREDIENT_POSITION).getIngredient()
        ));
  
    // Check whether the ingredient's displayed quantity is correct
    RecyclerViewMatcher
        .withRecyclerView(R.id.recyclerView_recipeDetails_ingredientsList_ingredients)
        .atPositionOnView(
            SELECTED_INGREDIENT_POSITION,
            R.id.textView_listItemRecipeDetailsStepsIngredient_ingredientQuantity
        )
        .matches(withText(
            quantityString
        ));
  }
  
  @After
  public void destroySampleRecipe() {
    this.sampleRecipe = null;
  }
}
