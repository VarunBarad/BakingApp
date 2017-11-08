package com.varunbarad.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.model.Recipe;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Creator: Varun Barad
 * Date: 09-11-2017
 * Project: BakingApp
 */
public class IngredientsListWidgetService extends RemoteViewsService {
  
  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    int recipeId;
    if (intent.hasExtra(Intent.EXTRA_TEXT)) {
      recipeId = intent.getIntExtra(Intent.EXTRA_TEXT, -1);
    } else {
      throw new IllegalArgumentException("A recipe-id must be specified to dislpay ingredients");
    }
    
    return new IngredientsListRemoteViewsFactory(this.getApplicationContext(), recipeId);
  }
}

class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
  private Context context;
  
  private int recipeId;
  private Recipe recipe;
  
  public IngredientsListRemoteViewsFactory(Context context, int recipeId) {
    this.context = context;
    this.recipeId = recipeId;
  }
  
  @Override
  public void onCreate() {
  
  }
  
  @Override
  public void onDataSetChanged() {
    this.retrieveRecipeDetails();
  }
  
  @Override
  public void onDestroy() {
  
  }
  
  @Override
  public int getCount() {
    if ((this.recipe != null) && (this.recipe.getIngredients() != null)) {
      return this.recipe.getIngredients().size();
    } else {
      return 0;
    }
  }
  
  @Override
  public RemoteViews getViewAt(int position) {
    RemoteViews views = new RemoteViews(this.context.getPackageName(), R.layout.list_item_list_view_widget_ingredient);
    
    views.setTextViewText(
        R.id.textView_listViewWidget_ingredientName,
        this.recipe.getIngredients().get(position).getIngredient()
    );
    views.setTextViewText(
        R.id.textView_listViewWidget_ingredientQuantity,
        context.getString(
            R.string.ingredient_quantity,
            this.recipe.getIngredients().get(position).getQuantity(),
            this.recipe.getIngredients().get(position).getMeasure()
        )
    );
    
    return views;
  }
  
  @Override
  public RemoteViews getLoadingView() {
    return null;
  }
  
  @Override
  public int getViewTypeCount() {
    return 1; // Treat all items in the ListView the same
  }
  
  @Override
  public long getItemId(int position) {
    return position;
  }
  
  @Override
  public boolean hasStableIds() {
    return true;
  }
  
  private void retrieveRecipeDetails() {
    Realm realm = Realm.getDefaultInstance();
    
    RealmResults<Recipe> realmResults =
        realm
            .where(Recipe.class)
            .equalTo("id", this.recipeId)
            .findAll();
    
    if (realmResults.size() > 0) {
      this.recipe = realm.copyFromRealm(realmResults.first());
    } else {
      this.recipe = null;
    }
    
    realm.close();
  }
}
