package com.varunbarad.bakingapp.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Creator: Varun Barad
 * Date: 02-11-2017
 * Project: BakingApp
 */
public final class Recipe {
  @Expose
  @SerializedName("id")
  private int id;
  @Expose
  @SerializedName("name")
  private String name;
  @Expose
  @SerializedName("servings")
  private int servings;
  @Expose
  @SerializedName("image")
  private String image;
  @Expose
  @SerializedName("ingredients")
  private ArrayList<Ingredient> ingredients;
  @Expose
  @SerializedName("steps")
  private ArrayList<RecipeStep> steps;
  
  public Recipe() {
  }
  
  public Recipe(int id, String name, int servings, String image, ArrayList<Ingredient> ingredients, ArrayList<RecipeStep> steps) {
    this.id = id;
    this.name = name;
    this.servings = servings;
    this.image = image;
    this.ingredients = ingredients;
    this.steps = steps;
  }
  
  public int getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public int getServings() {
    return servings;
  }
  
  public String getImage() {
    return image;
  }
  
  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }
  
  public ArrayList<RecipeStep> getSteps() {
    return steps;
  }
  
  @Override
  public String toString() {
    return (new Gson().toJson(this));
  }
}
