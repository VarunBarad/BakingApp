package com.varunbarad.bakingapp.recipedetails.recipeingredients;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.databinding.FragmentRecipeDetailsIngredientsListBinding;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.util.Helper;
import com.varunbarad.bakingapp.util.eventlistener.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IngredientsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IngredientsListFragment extends Fragment {
  private static final String KEY_RECIPE = "recipe";
  
  private Recipe recipe;
  
  private OnFragmentInteractionListener fragmentInteractionListener;
  
  private FragmentRecipeDetailsIngredientsListBinding dataBinding;
  
  private RecipeIngredientsAdapter ingredientsAdapter;
  
  public IngredientsListFragment() {
    // Required empty public constructor
  }
  
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param recipe Recipe whose ingredients are to be shown.
   * @return A new instance of fragment IngredientsListFragment.
   */
  public static IngredientsListFragment newInstance(Recipe recipe) {
    IngredientsListFragment fragment = new IngredientsListFragment();
    Bundle args = new Bundle();
    args.putString(KEY_RECIPE, recipe.toString());
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    if ((this.getArguments() != null) && (this.getArguments().containsKey(KEY_RECIPE))) {
      String recipeJson = getArguments().getString(KEY_RECIPE);
      this.recipe = Helper.getGsonInstance().fromJson(recipeJson, Recipe.class);
    }
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_details_ingredients_list, container, false);
    
    int numberOfColumns = this.getContext().getResources().getInteger(R.integer.column_count_ingredients_list);
    this.dataBinding.recyclerViewRecipeDetailsIngredientsListIngredients.setLayoutManager(new GridLayoutManager(this.getContext(), numberOfColumns, LinearLayoutManager.VERTICAL, false));
  
    this.ingredientsAdapter = new RecipeIngredientsAdapter(this.recipe);
    this.dataBinding.recyclerViewRecipeDetailsIngredientsListIngredients.setAdapter(this.ingredientsAdapter);
    this.dataBinding.recyclerViewRecipeDetailsIngredientsListIngredients.addItemDecoration(new DividerItemDecoration(this.getContext(), GridLayoutManager.HORIZONTAL));
    
    return this.dataBinding.getRoot();
  }
  
  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      fragmentInteractionListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    fragmentInteractionListener = null;
  }
}
