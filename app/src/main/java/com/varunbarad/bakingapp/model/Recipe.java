package com.varunbarad.bakingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.varunbarad.bakingapp.util.Helper;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Creator: Varun Barad
 * Date: 02-11-2017
 * Project: BakingApp
 */
public class Recipe extends RealmObject {
  @Expose
  @SerializedName("id")
  @PrimaryKey
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
  private RealmList<Ingredient> ingredients;
  @Expose
  @SerializedName("steps")
  private RealmList<RecipeStep> steps;
  
  public Recipe() {
  }
  
  public Recipe(int id, String name, int servings, String image, RealmList<Ingredient> ingredients, RealmList<RecipeStep> steps) {
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
  
  public RealmList<Ingredient> getIngredients() {
    return ingredients;
  }
  
  public RealmList<RecipeStep> getSteps() {
    return steps;
  }
  
  @Override
  public String toString() {
    return Helper.getGsonInstance().toJson(this);
  }
}
