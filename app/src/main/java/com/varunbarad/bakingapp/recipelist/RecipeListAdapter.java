package com.varunbarad.bakingapp.recipelist;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;
import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.util.eventlistener.ListItemClickListener;

import java.util.ArrayList;

/**
 * Creator: Varun Barad
 * Date: 02-11-2017
 * Project: BakingApp
 */
public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> implements ListItemClickListener {
  private ArrayList<Recipe> recipes;
  private ListItemClickListener itemClickListener;
  
  public RecipeListAdapter(ArrayList<Recipe> recipes, ListItemClickListener itemClickListener) {
    this.recipes = recipes;
    this.itemClickListener = itemClickListener;
  }
  
  public ArrayList<Recipe> getRecipes() {
    return recipes;
  }
  
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.list_item_recycler_view_recipe_list_recipe, parent, false);
    return new ViewHolder(itemView);
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    Context context = holder.imageView.getContext();
    
    String imageUrl = this.recipes.get(position).getImage();
    
    if ((imageUrl != null) && (!imageUrl.isEmpty())) {
      Picasso
          .with(context)
          .load(imageUrl)
          .error(R.drawable.ic_cloud_off)
          .placeholder(R.drawable.bg_rounded)
          .into(holder.imageView);
    } else {
      holder.imageView.setImageResource(R.drawable.bg_rounded);
      //ToDo: Tint the drawable to a random color
    }
    
    holder.textViewTitle.setText(this.recipes.get(position).getName());
    holder.textViewServings.setText(context.getString(R.string.serving_size, this.recipes.get(position).getServings()));
  }
  
  @Override
  public int getItemCount() {
    if (this.recipes != null) {
      return this.recipes.size();
    } else {
      return 0;
    }
  }
  
  @Override
  public void onItemClick(int position) {
    this.itemClickListener.onItemClick(position);
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View itemView;
    private AppCompatImageView imageView;
    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewServings;
    
    public ViewHolder(View itemView) {
      super(itemView);
      this.itemView = itemView;
      
      this.imageView = this.itemView.findViewById(R.id.imageView_listItemRecipeListRecipe_thumbnail);
      this.textViewTitle = this.itemView.findViewById(R.id.textView_listItemRecipeListRecipe_title);
      this.textViewServings = this.itemView.findViewById(R.id.textView_listItemRecipeListRecipe_serving);
      
      this.itemView.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
      RecipeListAdapter.this.itemClickListener.onItemClick(this.getAdapterPosition());
    }
  }
}
