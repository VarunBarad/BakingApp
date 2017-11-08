package com.varunbarad.bakingapp.widget.configure;

import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.model.Recipe;

import java.util.ArrayList;

/**
 * Creator: Varun Barad
 * Date: 08-11-2017
 * Project: BakingApp
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
  private ArrayList<Recipe> recipes;
  private int selectedIndex;
  
  public RecipeAdapter(ArrayList<Recipe> recipes) {
    this.recipes = recipes;
    this.selectedIndex = -1;
  }
  
  @Override
  public RecipeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.list_item_recycler_view_widget_configre_recipe, parent, false);
    return new ViewHolder(itemView);
  }
  
  @Override
  public void onBindViewHolder(RecipeAdapter.ViewHolder holder, int position) {
    holder
        .textViewName
        .setText(this.recipes.get(position).getName());
    
    holder
        .radioButtonSelector
        .setChecked(position == this.selectedIndex);
  }
  
  @Override
  public int getItemCount() {
    if (this.recipes != null) {
      return this.recipes.size();
    } else {
      return 0;
    }
  }
  
  private void selectRecipe(int position) {
    int oldSelectedIndex = this.selectedIndex;
    this.selectedIndex = position;
    
    this.notifyItemChanged(oldSelectedIndex);
    this.notifyItemChanged(this.selectedIndex);
  }
  
  public Recipe getSelectedRecipe() {
    if (this.selectedIndex > -1) {
      return this.recipes.get(this.selectedIndex);
    } else {
      return null;
    }
  }
  
  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View itemView;
    
    private AppCompatTextView textViewName;
    private AppCompatRadioButton radioButtonSelector;
    
    public ViewHolder(View itemView) {
      super(itemView);
      this.itemView = itemView;
      
      this.textViewName = this.itemView.findViewById(R.id.textView_listItemWidgetConfigureRecipe_recipeName);
      this.radioButtonSelector = this.itemView.findViewById(R.id.radioButton_listItemWidgetConfigureRecipe_selector);
      
      this.itemView.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View v) {
      RecipeAdapter.this.selectRecipe(this.getAdapterPosition());
    }
  }
}
