package com.varunbarad.bakingapp.recipedetails.recipesteps;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.databinding.FragmentRecipeDetailsStepsListBinding;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.util.Helper;
import com.varunbarad.bakingapp.util.eventlistener.ListItemClickListener;
import com.varunbarad.bakingapp.util.eventlistener.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepsListFragment extends Fragment implements ListItemClickListener {
  private static final String KEY_RECIPE = "recipe";
  
  private Recipe recipe;
  
  private OnFragmentInteractionListener fragmentInteractionListener;
  
  private FragmentRecipeDetailsStepsListBinding dataBinding;
  
  private RecipeStepsAdapter stepsAdapter;
  
  public StepsListFragment() {
    // Required empty public constructor
  }
  
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param recipe Recipe whose steps are to be shown.
   * @return A new instance of fragment StepsListFragment.
   */
  public static StepsListFragment newInstance(Recipe recipe) {
    StepsListFragment fragment = new StepsListFragment();
    Bundle args = new Bundle();
    args.putString(KEY_RECIPE, recipe.toString());
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    if ((this.getArguments() != null) && (this.getArguments().containsKey(KEY_RECIPE))) {
      this.recipe = Helper.getGsonInstance().fromJson(this.getArguments().getString(KEY_RECIPE), Recipe.class);
    }
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_details_steps_list, container, false);
    
    this.dataBinding.recyclerViewRecipeDetailsStepsListSteps.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));
  
    this.stepsAdapter = new RecipeStepsAdapter(this.recipe, this);
    this.dataBinding.recyclerViewRecipeDetailsStepsListSteps.setAdapter(this.stepsAdapter);
    this.dataBinding.recyclerViewRecipeDetailsStepsListSteps.addItemDecoration(new DividerItemDecoration(this.getContext(), LinearLayoutManager.VERTICAL));
    
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
  
  @Override
  public void onItemClick(int position) {
    if (this.stepsAdapter.getItemViewType(position) == RecipeStepsAdapter.VIEW_TYPE_INGREDIENT) {
      this.fragmentInteractionListener.onFragmentInteraction(
          OnFragmentInteractionListener.TAG_LAUNCH_INGREDIENTS,
          this.recipe.toString()
      );
    } else if (this.stepsAdapter.getItemViewType(position) == RecipeStepsAdapter.VIEW_TYPE_STEP) {
      this.fragmentInteractionListener
          .onFragmentInteraction(
              OnFragmentInteractionListener.TAG_LAUNCH_STEP,
              String.valueOf(position - 1)
          );
    }
  }
}
