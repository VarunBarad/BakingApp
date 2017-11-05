package com.varunbarad.bakingapp.recipedetails.recipeingredients;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.model.Recipe;

/**
 * Creator: Varun Barad
 * Date: 03-11-2017
 * Project: BakingApp
 */
public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {
  private Recipe recipe;
  
  public RecipeIngredientsAdapter(Recipe recipe) {
    this.recipe = recipe;
  }
  
  @Override
  public RecipeIngredientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.list_item_recycler_view_recipe_details_ingredients_list_ingredient, parent, false);
    
    return new ViewHolder(itemView);
  }
  
  @Override
  public void onBindViewHolder(RecipeIngredientsAdapter.ViewHolder holder, int position) {
    String quantityString = holder.textViewQuantity.getContext().getString(
        R.string.ingredient_quantity,
        this.recipe.getIngredients().get(position).getQuantity(),
        this.recipe.getIngredients().get(position).getMeasure()
    );
    
    holder.textViewName.setText(this.recipe.getIngredients().get(position).getIngredient());
    holder.textViewQuantity.setText(quantityString);
  }
  
  @Override
  public int getItemCount() {
    if ((this.recipe != null) && (this.recipe.getIngredients() != null)) {
      return this.recipe.getIngredients().size();
    } else {
      return 0;
    }
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView textViewName;
    private AppCompatTextView textViewQuantity;
    
    public ViewHolder(View itemView) {
      super(itemView);
      
      this.textViewName = itemView.findViewById(R.id.textView_listItemRecipeDetailsStepsIngredient_ingredientTitle);
      this.textViewQuantity = itemView.findViewById(R.id.textView_listItemRecipeDetailsStepsIngredient_ingredientQuantity);
    }
  }
}
