package com.varunbarad.bakingapp.recipedetails;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.model.Recipe;

/**
 * Creator: Varun Barad
 * Date: 03-11-2017
 * Project: BakingApp
 */
public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {
  private static final int VIEW_TYPE_INGREDIENT = 1;
  private static final int VIEW_TYPE_STEP = 2;
  
  private Recipe recipe;
  
  public RecipeStepsAdapter(Recipe recipe) {
    this.recipe = recipe;
  }
  
  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = null;
    
    if (viewType == VIEW_TYPE_INGREDIENT) {
      itemView =
          LayoutInflater
              .from(parent.getContext())
              .inflate(R.layout.list_item_recycler_view_recipe_details_steps_ingredient, parent, false);
    } else if (viewType == VIEW_TYPE_STEP) {
      itemView =
          LayoutInflater
              .from(parent.getContext())
              .inflate(R.layout.list_item_recycler_view_recipe_details_steps_step, parent, false);
    }
    
    return new ViewHolder(itemView, viewType);
  }
  
  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    int viewType = this.getItemViewType(position);
    
    if (viewType == VIEW_TYPE_STEP) {
      holder.textViewIndex.setText(String.valueOf(this.recipe.getSteps().get(position - 1).getId() + 1));
      holder.textViewName.setText(this.recipe.getSteps().get(position - 1).getShortDescription());
      
      String thumbnailUrl = this.recipe.getSteps().get(position - 1).getThumbnailUrl();
      if ((thumbnailUrl != null) && (!thumbnailUrl.isEmpty())) {
        Picasso
            .with(holder.imageViewThumbnail.getContext())
            .load(thumbnailUrl)
            .into(holder.imageViewThumbnail);
        
        holder.imageViewThumbnail.setVisibility(View.VISIBLE);
      } else {
        holder.imageViewThumbnail.setVisibility(View.GONE);
      }
    }
  }
  
  @Override
  public int getItemViewType(int position) {
    if (position == 0) {
      return VIEW_TYPE_INGREDIENT;
    } else {
      return VIEW_TYPE_STEP;
    }
  }
  
  @Override
  public int getItemCount() {
    if ((this.recipe != null) && (this.recipe.getSteps() != null)) {
      return this.recipe.getSteps().size();
    } else {
      return 0;
    }
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView textViewIndex;
    private AppCompatTextView textViewName;
    private AppCompatImageView imageViewThumbnail;
    
    public ViewHolder(View itemView, int viewType) {
      super(itemView);
      
      if (viewType == VIEW_TYPE_INGREDIENT) {
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //ToDo: Launch ingredients fragment
            Toast.makeText(v.getContext(), "Launch ingredients", Toast.LENGTH_SHORT).show();
          }
        });
      } else if (viewType == VIEW_TYPE_STEP) {
        textViewIndex = itemView.findViewById(R.id.textView_listItemRecipeDetailsStepsStep_index);
        textViewName = itemView.findViewById(R.id.textView_listItemRecipeDetailsStepsStep_name);
        imageViewThumbnail = itemView.findViewById(R.id.imageView_listItemRecipeDetailsStepsStep_thumbnail);
        
        itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //ToDo: Open step details fragment
            Toast.makeText(v.getContext(), "Launch step details", Toast.LENGTH_SHORT).show();
          }
        });
      }
    }
  }
}
