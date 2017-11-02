package com.varunbarad.bakingapp.util;

import com.varunbarad.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Creator: Varun Barad
 * Date: 02-11-2017
 * Project: BakingApp
 */
public interface RecipeApiHelper {
  String baseUrl = "https://go.udacity.com/";
  
  @GET("/android-baking-app-json")
  Call<ArrayList<Recipe>> getRecipes();
}
