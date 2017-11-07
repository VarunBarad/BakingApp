package com.varunbarad.bakingapp.recipedetails;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.databinding.ActivityRecipeDetailsBinding;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.recipedetails.recipeingredients.IngredientsListFragment;
import com.varunbarad.bakingapp.recipedetails.recipesteps.StepsListFragment;
import com.varunbarad.bakingapp.recipedetails.stepdetails.StepDetailsFragment;
import com.varunbarad.bakingapp.util.Helper;
import com.varunbarad.bakingapp.util.eventlistener.OnFragmentInteractionListener;

public class RecipeDetailsActivity extends AppCompatActivity implements OnFragmentInteractionListener {
  private static final String KEY_RECIPE = "recipe";
  
  private ActivityRecipeDetailsBinding dataBinding;
  private Recipe recipe;
  
  public static void start(Context context, Recipe recipe) {
    Intent starter = new Intent(context, RecipeDetailsActivity.class);
    starter.putExtra(KEY_RECIPE, recipe.toString());
    context.startActivity(starter);
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_details);
    
    if ((this.getIntent().getExtras() != null) && (this.getIntent().getExtras().containsKey(KEY_RECIPE))) {
      String recipeJson = this.getIntent().getExtras().getString(KEY_RECIPE);
      this.recipe = Helper.getGsonInstance().fromJson(recipeJson, Recipe.class);
      
      if (this.recipe == null) {
        throw new IllegalArgumentException("The activity must be started with a non-null recipe specified.");
      }
    } else {
      throw new IllegalArgumentException("The activity must be started with a non-null recipe specified.");
    }
    
    this.setSupportActionBar(this.dataBinding.toolbar);
    if (this.getSupportActionBar() != null) {
      this.getSupportActionBar().setTitle(this.recipe.getName());
      this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    if (savedInstanceState == null) {
      StepsListFragment stepsListFragment = StepsListFragment.newInstance(this.recipe);
      
      FragmentManager fragmentManager = this.getSupportFragmentManager();
      fragmentManager
          .beginTransaction()
          .add(R.id.container_recipeDetails_master, stepsListFragment)
          .commit();
    }
  }
  
  @Override
  public void onFragmentInteraction(String tag, String data) {
    if (OnFragmentInteractionListener.TAG_LAUNCH_INGREDIENTS.equals(tag)) {
      this.showIngredients(this.recipe);
    } else if (OnFragmentInteractionListener.TAG_LAUNCH_STEP.equals(tag)) {
      int stepNumber = Integer.parseInt(data);
      this.showStep(this.recipe, stepNumber);
    } else if (OnFragmentInteractionListener.TAG_LAUNCH_STEP_FROM_STEP.equals(tag)) {
      int stepNumber = Integer.parseInt(data);
      this.showStep(this.recipe, stepNumber, false);
    } else if (OnFragmentInteractionListener.TAG_SET_TITLE.equals(tag)) {
      this.setActionBarTitle(data);
    }
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      this.onBackPressed();
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }
  
  private void showIngredients(Recipe recipe) {
    IngredientsListFragment fragment = IngredientsListFragment.newInstance(recipe);
    
    if (Helper.isDualPane(this)) {
      this
          .getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container_recipeDetails_master, fragment) //ToDo: Change to detail container
          .commit();
    } else {
      this
          .getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container_recipeDetails_master, fragment)
          .addToBackStack(null)
          .commit();
    }
  }
  
  private void showStep(Recipe recipe, int stepNumber) {
    this.showStep(recipe, stepNumber, true);
  }
  
  private void showStep(Recipe recipe, int stepNumber, boolean addToBackStack) {
    StepDetailsFragment fragment = StepDetailsFragment.newInstance(recipe, stepNumber);
    
    if (Helper.isDualPane(this)) {
      this
          .getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container_recipeDetails_master, fragment) //ToDo: Change to detail container
          .commit();
    } else {
      if (!addToBackStack) {
        this
            .getSupportFragmentManager()
            .popBackStack();
      }
      
      this
          .getSupportFragmentManager()
          .beginTransaction()
          .replace(R.id.container_recipeDetails_master, fragment)
          .addToBackStack(null)
          .commit();
    }
  }
  
  private void setActionBarTitle(String title) {
    if (!Helper.isDualPane(this)) {
      if (this.getSupportActionBar() != null) {
        this
            .getSupportActionBar()
            .setTitle(title);
      }
    }
  }
}
