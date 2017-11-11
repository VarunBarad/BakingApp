package com.varunbarad.bakingapp.recipelist;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.databinding.ActivityRecipeListBinding;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.recipedetails.RecipeDetailsActivity;
import com.varunbarad.bakingapp.util.Helper;
import com.varunbarad.bakingapp.util.RecipeApiHelper;
import com.varunbarad.bakingapp.util.eventlistener.ListItemClickListener;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeListActivity extends AppCompatActivity implements ListItemClickListener {
  @Nullable
  private volatile CountingIdlingResource idlingResource;
  
  private ActivityRecipeListBinding dataBinding;
  
  private RecipeListAdapter recipeListAdapter;
  
  public static void start(Context context) {
    Intent starter = new Intent(context, RecipeListActivity.class);
    context.startActivity(starter);
  }
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_list);
  
    if (this.idlingResource != null) {
      this.idlingResource.increment();
    }
    
    this.setSupportActionBar(this.dataBinding.toolbar);
    if (this.getSupportActionBar() != null) {
      this.getSupportActionBar().setTitle(R.string.app_name);
    }
  
    if (Helper.isRecipesUpdateNeeded(this)) {
      if (Helper.isConnectedToInternet(this)) {
        this.fetchRecipes();
      } else {
        this.showNetworkError();
      }
    } else {
      this.showRecipes(this.retrieveRecipes());
    }
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipeListRecipes.setHasFixedSize(true);
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipeListRecipes.setLayoutManager(new GridLayoutManager(this, this.getResources().getInteger(R.integer.column_count_recipe_list), LinearLayoutManager.VERTICAL, false));
  }
  
  private void fetchRecipes() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(RecipeApiHelper.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    
    RecipeApiHelper recipeApiHelper = retrofit.create(RecipeApiHelper.class);
    
    recipeApiHelper
        .getRecipes()
        .enqueue(new Callback<ArrayList<Recipe>>() {
          @Override
          public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
            RecipeListActivity.this.showRecipes(response.body());
            RecipeListActivity.this.saveRecipes(response.body());
  
            if (RecipeListActivity.this.idlingResource != null) {
              RecipeListActivity.this.idlingResource.decrement();
            }
          }
          
          @Override
          public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
            RecipeListActivity.this.showNetworkError();
  
            if (RecipeListActivity.this.idlingResource != null) {
              RecipeListActivity.this.idlingResource.decrement();
            }
          }
        });
  }
  
  private ArrayList<Recipe> retrieveRecipes() {
    Realm realm = Realm.getDefaultInstance();
    
    RealmResults<Recipe> recipeRealmResults = realm.where(Recipe.class).findAllSorted("id");
    
    return new ArrayList<>(realm.copyFromRealm(recipeRealmResults));
  }
  
  private void showRecipes(ArrayList<Recipe> recipes) {
    if (recipes == null || recipes.size() < 1) {
      this.showNoRecipes();
    } else {
      this.recipeListAdapter = new RecipeListAdapter(recipes, this);
      this.dataBinding.contentActivityRecipeList.recyclerViewRecipeListRecipes.setAdapter(this.recipeListAdapter);
      
      this.dataBinding.contentActivityRecipeList.placeholderProgress
          .setVisibility(View.GONE);
      this.dataBinding.contentActivityRecipeList.recyclerViewRecipeListRecipes
          .setVisibility(View.VISIBLE);
      this.dataBinding.contentActivityRecipeList.placeHolderError
          .setVisibility(View.GONE);
      this.dataBinding.contentActivityRecipeList.placeHolderNoRecipes
          .setVisibility(View.GONE);
    }
  }
  
  private void showNetworkError() {
    this.dataBinding.contentActivityRecipeList.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipeListRecipes
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.placeHolderError
        .setVisibility(View.VISIBLE);
    this.dataBinding.contentActivityRecipeList.placeHolderNoRecipes
        .setVisibility(View.GONE);
  }
  
  private void showNoRecipes() {
    this.dataBinding.contentActivityRecipeList.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipeListRecipes
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.placeHolderError
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.placeHolderNoRecipes
        .setVisibility(View.VISIBLE);
  }
  
  @Override
  public void onItemClick(int position) {
    Recipe recipe = this.recipeListAdapter.getRecipes().get(position);
    RecipeDetailsActivity.start(this, recipe);
  }
  
  private void saveRecipes(final ArrayList<Recipe> recipes) {
    Realm realm = Realm.getDefaultInstance();
    
    realm.executeTransaction(new Realm.Transaction() {
      @Override
      public void execute(Realm realm) {
        realm.insertOrUpdate(recipes);
        Helper.storeRecipesTimestamp(RecipeListActivity.this);
      }
    });
  }
  
  @VisibleForTesting
  @Nullable
  public IdlingResource getIdlingResource() {
    if (this.idlingResource == null) {
      this.idlingResource = new CountingIdlingResource("Network-Idling-Resource");
    }
    return this.idlingResource;
  }
}
