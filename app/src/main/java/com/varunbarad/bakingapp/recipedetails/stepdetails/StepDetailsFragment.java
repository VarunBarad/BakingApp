package com.varunbarad.bakingapp.recipedetails.stepdetails;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.varunbarad.bakingapp.R;
import com.varunbarad.bakingapp.databinding.FragmentRecipeDetailsStepDetailsBinding;
import com.varunbarad.bakingapp.model.Recipe;
import com.varunbarad.bakingapp.model.RecipeStep;
import com.varunbarad.bakingapp.util.Helper;
import com.varunbarad.bakingapp.util.eventlistener.OnFragmentInteractionListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailsFragment extends Fragment {
  private static final String KEY_RECIPE = "recipe";
  private static final String KEY_STEP_NUMBER = "step_number";
  
  private Recipe recipe;
  private int stepNumber;
  private RecipeStep step;
  
  private OnFragmentInteractionListener fragmentInteractionListener;
  
  private FragmentRecipeDetailsStepDetailsBinding dataBinding;
  
  private SimpleExoPlayer player;
  
  public StepDetailsFragment() {
    // Required empty public constructor
  }
  
  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param recipe     Parameter 1.
   * @param stepNumber Parameter 2.
   * @return A new instance of fragment StepDetailsFragment.
   */
  public static StepDetailsFragment newInstance(Recipe recipe, int stepNumber) {
    if ((stepNumber < 0) || (stepNumber >= recipe.getSteps().size())) {
      throw new IllegalArgumentException("Illegal step-number specified. Step-number: " + stepNumber + ", Available steps: " + recipe.getSteps().size());
    }
    
    StepDetailsFragment fragment = new StepDetailsFragment();
    Bundle args = new Bundle();
    args.putString(KEY_RECIPE, recipe.toString());
    args.putInt(KEY_STEP_NUMBER, stepNumber);
    fragment.setArguments(args);
    return fragment;
  }
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      String recipeJson = getArguments().getString(KEY_RECIPE);
      this.recipe = Helper.getGsonInstance().fromJson(recipeJson, Recipe.class);
      this.stepNumber = getArguments().getInt(KEY_STEP_NUMBER);
      this.step = this.recipe.getSteps().get(stepNumber);
    }
  }
  
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    this.dataBinding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_recipe_details_step_details,
        container,
        false
    );
    
    if ((this.step.getVideoUrl() != null) && (!this.step.getVideoUrl().isEmpty())) {
      this.initializePlayer();
    } else {
      this.dataBinding
          .playerViewStepDetailsVideo
          .setVisibility(View.GONE);
    }
    
    this.dataBinding
        .textViewStepDetailsDescription
        .setText(this.step.getDescription());
    
    if (this.stepNumber == 0) {
      this.dataBinding
          .buttonStepDetailsPreviousStep
          .setVisibility(View.GONE);
    }
    
    if (this.stepNumber == (this.recipe.getSteps().size() - 1)) {
      this.dataBinding
          .buttonStepDetailsNextStep
          .setVisibility(View.GONE);
    }
    
    this.dataBinding.buttonStepDetailsPreviousStep.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        StepDetailsFragment
            .this
            .fragmentInteractionListener
            .onFragmentInteraction(
                OnFragmentInteractionListener.TAG_LAUNCH_STEP_FROM_STEP,
                String.valueOf(StepDetailsFragment.this.stepNumber - 1)
            );
      }
    });
    
    this.dataBinding.buttonStepDetailsNextStep.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        StepDetailsFragment
            .this
            .fragmentInteractionListener
            .onFragmentInteraction(
                OnFragmentInteractionListener.TAG_LAUNCH_STEP_FROM_STEP,
                String.valueOf(StepDetailsFragment.this.stepNumber + 1)
            );
      }
    });
  
    this.setTitle(this.step.getShortDescription());
    
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
  public void onPause() {
    super.onPause();
    this.releasePlayer();
  }
  
  @Override
  public void onDetach() {
    super.onDetach();
    fragmentInteractionListener = null;
  }
  
  private void initializePlayer() {
    this.player = ExoPlayerFactory.newSimpleInstance(
        new DefaultRenderersFactory(this.getContext()),
        new DefaultTrackSelector(),
        new DefaultLoadControl()
    );
    
    this
        .dataBinding
        .playerViewStepDetailsVideo
        .setPlayer(this.player);
    
    Uri videoUri = Uri.parse(this.step.getVideoUrl());
    
    MediaSource mediaSource = new ExtractorMediaSource(
        videoUri,
        new DefaultHttpDataSourceFactory("ua"),
        new DefaultExtractorsFactory(),
        null,
        null
    );
    
    this.player.prepare(mediaSource);
    this.player.setPlayWhenReady(false);
  }
  
  private void releasePlayer() {
    if (this.player != null) {
      this.player.stop();
      this.player.release();
      this.player = null;
    }
  }
  
  private void setTitle(String title) {
    this
        .fragmentInteractionListener
        .onFragmentInteraction(
            OnFragmentInteractionListener.TAG_SET_TITLE,
            title
        );
  }
  
  public int getStepNumber() {
    return this.stepNumber;
  }
}
