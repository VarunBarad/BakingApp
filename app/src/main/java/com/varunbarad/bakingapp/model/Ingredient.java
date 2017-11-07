package com.varunbarad.bakingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.varunbarad.bakingapp.util.Helper;

import io.realm.RealmObject;

/**
 * Creator: Varun Barad
 * Date: 02-11-2017
 * Project: BakingApp
 */
public class Ingredient extends RealmObject {
  @Expose
  @SerializedName("quantity")
  private double quantity;
  @Expose
  @SerializedName("measure")
  private String measure;
  @Expose
  @SerializedName("ingredient")
  private String ingredient;
  
  /**
   * No args constructor for serialization
   */
  public Ingredient() {
  }
  
  /**
   * @param quantity
   * @param measure
   * @param ingredient
   */
  public Ingredient(int quantity, String measure, String ingredient) {
    this.quantity = quantity;
    this.measure = measure;
    this.ingredient = ingredient;
  }
  
  public final double getQuantity() {
    return quantity;
  }
  
  public final String getMeasure() {
    return measure;
  }
  
  public final String getIngredient() {
    return ingredient;
  }
  
  @Override
  public String toString() {
    return Helper.getGsonInstance().toJson(this);
  }
}
