package com.varunbarad.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.widget.configure.IngredientsListWidgetConfigureActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientsListWidgetConfigureActivity IngredientsListWidgetConfigureActivity}
 */
public class IngredientsListWidget extends AppWidgetProvider {
  
  public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    
    Recipe recipe = IngredientsListWidgetConfigureActivity.loadRecipeFromPref(context, appWidgetId);
    if (recipe == null) {
      throw new IllegalStateException("Recipe must not be null");
    }
    
    // Construct the RemoteViews object
    RemoteViews views = IngredientsListWidget.getIngredientsListRemoteView(context, recipe.getId());
    
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }
  
  private static RemoteViews getIngredientsListRemoteView(Context context, int recipeId) {
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);
    
    Intent intent = new Intent(context, IngredientsListWidgetService.class);
    intent.putExtra(Intent.EXTRA_TEXT, recipeId);
    views.setRemoteAdapter(R.id.listView_widget_ingredients, intent);
    
    return views;
  }
  
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    // There may be multiple widgets active, so update all of them
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
  }
  
  @Override
  public void onDeleted(Context context, int[] appWidgetIds) {
    // When the user deletes the widget, delete the preference associated with it.
    for (int appWidgetId : appWidgetIds) {
      IngredientsListWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
    }
  }
  
  @Override
  public void onEnabled(Context context) {
    // Enter relevant functionality for when the first widget is created
  }
  
  @Override
  public void onDisabled(Context context) {
    // Enter relevant functionality for when the last widget is disabled
  }
}

