package com.varunbarad.bakingapp.widget.configure;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.databinding.WidgetConfigureIngredientsListBinding;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.widget.IngredientsListWidget;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * The configuration screen for the {@link IngredientsListWidget IngredientsListWidget} AppWidget.
 */
public class IngredientsListWidgetConfigureActivity extends AppCompatActivity {
  
  private static final String PREFS_NAME = "com.varunbarad.bakingapp.widget.IngredientsListWidget";
  private static final String PREF_PREFIX_KEY = "appwidget_";
  
  private int appWidgetId;
  
  private WidgetConfigureIngredientsListBinding dataBinding;
  
  private RecipeAdapter recipeAdapter;
  
  public IngredientsListWidgetConfigureActivity() {
    super();
  }
  
  // Write the prefix to the SharedPreferences object for this widget
  private static void saveRecipeIdToPref(Context context, int appWidgetId, int recipeId) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
    prefs.apply();
  }
  
  // Read the prefix from the SharedPreferences object for this widget.
  // If there is no preference saved, get the default from a resource
  public static Recipe loadRecipeFromPref(Context context, int appWidgetId) {
    SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
    int recipeId = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, -1);
    
    if (recipeId < 0) {
      return null;
    } else {
      Realm realm = Realm.getDefaultInstance();
      
      RealmResults<Recipe> recipeRealmResults =
          realm
              .where(Recipe.class)
              .equalTo("id", recipeId)
              .findAll();
      
      Recipe recipe;
      if (recipeRealmResults.size() > 0) {
        recipe = realm.copyFromRealm(recipeRealmResults.get(0));
      } else {
        recipe = null;
      }
      
      realm.close();
      
      return recipe;
    }
  }
  
  public static void deleteTitlePref(Context context, int appWidgetId) {
    SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
    prefs.remove(PREF_PREFIX_KEY + appWidgetId);
    prefs.apply();
  }
  
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    
    // Set the result to CANCELED.  This will cause the widget host to cancel
    // out of the widget placement if the user presses the back button.
    setResult(RESULT_CANCELED);
    
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.widget_configure_ingredients_list);
    
    this.setSupportActionBar(this.dataBinding.toolbar);
    if (this.getSupportActionBar() != null) {
      this.getSupportActionBar().setTitle(R.string.app_name);
    }
    
    this.dataBinding.recyclerViewWidgetConfigureRecipeList.setLayoutManager(new LinearLayoutManager(
        this,
        LinearLayoutManager.VERTICAL,
        false
    ));
    this.recipeAdapter = new RecipeAdapter(this.retrieveRecipes());
    this.dataBinding.recyclerViewWidgetConfigureRecipeList.setAdapter(this.recipeAdapter);
    
    this.dataBinding.buttonWidgetConfigureAddWidget.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        final Context context = IngredientsListWidgetConfigureActivity.this;
        
        // When the button is clicked, store the recipe-id locally
        int recipeId = IngredientsListWidgetConfigureActivity.this.recipeAdapter.getSelectedRecipe().getId();
        IngredientsListWidgetConfigureActivity.saveRecipeIdToPref(
            context,
            appWidgetId,
            recipeId
        );
        
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        IngredientsListWidget.updateAppWidget(context, appWidgetManager, appWidgetId);
        
        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
      }
    });
    
    // Find the widget id from the intent.
    if (this.getIntent().getExtras() != null) {
      appWidgetId = this.getIntent().getExtras().getInt(
          AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID
      );
    } else {
      this.appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    }
    
    // If this activity was started with an intent without an app widget ID, finish with an error.
    if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
      finish();
    }
  }
  
  private ArrayList<Recipe> retrieveRecipes() {
    Realm realm = Realm.getDefaultInstance();
    
    RealmResults<Recipe> recipeRealmResults = realm.where(Recipe.class).findAllSorted("id");
    
    return new ArrayList<>(realm.copyFromRealm(recipeRealmResults));
  }
}

