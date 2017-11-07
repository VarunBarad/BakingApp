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
public class RecipeStep extends RealmObject {
  @Expose
  @SerializedName("id")
  private int id;
  @Expose
  @SerializedName("shortDescription")
  private String shortDescription;
  @Expose
  @SerializedName("description")
  private String description;
  @Expose
  @SerializedName("videoURL")
  private String videoUrl;
  @Expose
  @SerializedName("thumbnailURL")
  private String thumbnailUrl;
  
  public RecipeStep() {
  }
  
  public RecipeStep(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
    this.id = id;
    this.shortDescription = shortDescription;
    this.description = description;
    this.videoUrl = videoUrl;
    this.thumbnailUrl = thumbnailUrl;
  }
  
  public final int getId() {
    return id;
  }
  
  public final String getShortDescription() {
    return shortDescription;
  }
  
  public final String getDescription() {
    return description;
  }
  
  public final String getVideoUrl() {
    return videoUrl;
  }
  
  public final String getThumbnailUrl() {
    return thumbnailUrl;
  }
  
  @Override
  public String toString() {
    return Helper.getGsonInstance().toJson(this);
  }
}
