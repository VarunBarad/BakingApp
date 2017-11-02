package com.varunbarad.bakingapp.recipelist;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.databinding.ActivityRecipeListBinding;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.util.RecipeApiHelper;
import com.varunbarad.bakingapp.util.eventlistener.ListItemClickListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeListActivity extends AppCompatActivity implements ListItemClickListener {
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
    
    this.setSupportActionBar(this.dataBinding.toolbar);
    if (this.getSupportActionBar() != null) {
      this.getSupportActionBar().setTitle(R.string.app_name);
    }
    
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
          }
          
          @Override
          public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
            RecipeListActivity.this.showNetworkError();
          }
        });
  }
  
  @Override
  protected void onStart() {
    super.onStart();
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipes.setHasFixedSize(true);
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipes.setLayoutManager(new GridLayoutManager(this, this.getResources().getInteger(R.integer.column_count_recipe_list), LinearLayoutManager.VERTICAL, false));
  }
  
  private void showRecipes(ArrayList<Recipe> recipes) {
    if (recipes == null || recipes.size() < 1) {
      this.showNoRecipes();
    } else {
      this.recipeListAdapter = new RecipeListAdapter(recipes, this);
      this.dataBinding.contentActivityRecipeList.recyclerViewRecipes.setAdapter(this.recipeListAdapter);
      
      this.dataBinding.contentActivityRecipeList.placeholderProgress
          .setVisibility(View.GONE);
      this.dataBinding.contentActivityRecipeList.recyclerViewRecipes
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
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipes
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.placeHolderError
        .setVisibility(View.VISIBLE);
    this.dataBinding.contentActivityRecipeList.placeHolderNoRecipes
        .setVisibility(View.GONE);
  }
  
  private void showNoRecipes() {
    this.dataBinding.contentActivityRecipeList.placeholderProgress
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.recyclerViewRecipes
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.placeHolderError
        .setVisibility(View.GONE);
    this.dataBinding.contentActivityRecipeList.placeHolderNoRecipes
        .setVisibility(View.VISIBLE);
  }
  
  @Override
  public void onItemClick(int position) {
    Toast.makeText(this, this.recipeListAdapter.getRecipes().get(position).getName(), Toast.LENGTH_SHORT).show();
  }
}
