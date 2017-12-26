package com.example.android.bakingtime.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeStep;
import com.example.android.bakingtime.utils.RecipeDataUtil;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
*Class to handle Steps (Ingredients,Video, Step description ) for single pane devices
*
 */
public class RecipeStepActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.nextbutton)
    Button mNextButton;
    @Nullable
    @BindView(R.id.previousbutton)
    Button mPreviousButton;

    @Nullable
    @BindView(R.id.tab_stepIngContainer)
    FrameLayout mIngContainer;
    @Nullable
    @BindView(R.id.stepVideoContainer)
    FrameLayout mVideoContainer;

    @Nullable
    @BindView(R.id.stepDescContainer)
    FrameLayout mDescContainer;


    @BindString(R.string.intent_bakingrecipeobj)
    String mIntentCurrentRecipeKey;
    @BindString(R.string.intent_recipedetail_position)
    String mIntentDetailPositionKey;
    int mCurrentIndex;

    @BindString(R.string.savedinstancekey_bakingrecipe)
    String mParcelableCurrentRecipeKey;

    @BindString(R.string.savedinstancekey_detailindex)
    String mParcelableDetailIndex;
    BakingRecipe mCurrentBakingRecipe;

    RecipeStep mCurrentStep;

    public static final String TAG = "RecipeStepActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);
        if (getResources().getBoolean(R.bool.isLandscape)) {
            getSupportActionBar().hide();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        if (savedInstanceState == null) {
            Log.i(TAG, "On Create saved instance is null");

            mCurrentBakingRecipe = getIntent().getExtras().getParcelable(mIntentCurrentRecipeKey);
            mCurrentIndex = getIntent().getIntExtra(mIntentDetailPositionKey, -1);


        } else if (savedInstanceState != null) {
            Log.i(TAG, "On Create saved instance is not null");
            mCurrentIndex = savedInstanceState.getInt(mParcelableDetailIndex);
            mCurrentBakingRecipe = savedInstanceState.getParcelable(mParcelableCurrentRecipeKey);
        }

        setPreviousNextButtonListener();


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    void updateStepState(int position) {

        if (position > 0) {
            mCurrentIndex = position;
            mCurrentStep = RecipeDataUtil.getStepForDetailIndex(mCurrentIndex, mCurrentBakingRecipe);
        }

    }

    void setupIngListFragment() {

        IngListFragment ingListFragment = (IngListFragment) getSupportFragmentManager().findFragmentById(R.id.tab_stepIngContainer);
        if (ingListFragment == null) {
            ingListFragment = new IngListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.tab_stepIngContainer, ingListFragment).commit();
        }
        if (mCurrentBakingRecipe != null) {
            ingListFragment.setIngArrayList(RecipeDataUtil.getIngListForDetailIndex(mCurrentBakingRecipe));
        }

    }

    void setupVideoFragment() {
        VideoFragment videoFragment = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.stepVideoContainer);


        if (videoFragment == null) {
            videoFragment = new VideoFragment();
            videoFragment.setMediaUrl(mCurrentStep.getStepVideoUrl());
            videoFragment.setThumbUrl(mCurrentStep.getStepThumbnailUrl());
            getSupportFragmentManager().beginTransaction().add(R.id.stepVideoContainer, videoFragment).commit();

        } else {
            videoFragment.setThumbUrl(mCurrentStep.getStepThumbnailUrl());
            videoFragment.setMediaUrl(mCurrentStep.getStepVideoUrl());
            videoFragment.playVideo();
        }

    }

    void removeVideoFragment() {
        VideoFragment videoFragment = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.stepVideoContainer);
        if (videoFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.stepVideoContainer)).commitNow();
        }
    }

    void setupDescFragment() {
        if (!getResources().getBoolean(R.bool.isLandscape)) {
            StepDescFragment stepDescFragment = (StepDescFragment) getSupportFragmentManager().findFragmentById(R.id.stepDescContainer);
            if (stepDescFragment == null) {
                stepDescFragment = new StepDescFragment();
                stepDescFragment.setStepDescData(mCurrentStep.getStepLongDesc());
                getSupportFragmentManager().beginTransaction().add(R.id.stepDescContainer, stepDescFragment).commit();
            } else {
                stepDescFragment.setStepDescData(mCurrentStep.getStepLongDesc());
            }
        }
    }


    void updateUI() {
        if (mCurrentIndex == 0) {
            setupIngListFragment();
            showIngContainer();
            mPreviousButton.setVisibility(View.INVISIBLE);
        } else {
            updateStepState(mCurrentIndex);
            showVideoContainer();
            setupDescFragment();
            setupVideoFragment();
            mPreviousButton.setVisibility(View.VISIBLE);
        }
    }


    void setPreviousNextButtonListener() {

        if (mPreviousButton != null) {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCurrentIndex = mCurrentIndex - 1;
                    updateStepState(mCurrentIndex);
                    mNextButton.setVisibility(View.VISIBLE);
                    if (mCurrentIndex == 0) {
                        mPreviousButton.setVisibility(View.INVISIBLE);
                    } else {
                        mPreviousButton.setVisibility(View.VISIBLE);
                    }
                    removeVideoFragment();
                    updateUI();


                }
            });
        }

        if (mNextButton != null) {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentIndex = mCurrentIndex + 1;

                    if (mCurrentIndex + 1 == mCurrentBakingRecipe.getRecipeStepList().size()) {
                        mPreviousButton.setVisibility(View.VISIBLE);
                        mNextButton.setVisibility(View.INVISIBLE);
                    } else {
                        mNextButton.setVisibility(View.VISIBLE);
                        mPreviousButton.setVisibility(View.VISIBLE);
                    }
                    removeVideoFragment();
                    updateUI();

                }
            });


        }

    }

    void showIngContainer() {
        mIngContainer.setVisibility(View.VISIBLE);
        mVideoContainer.setVisibility(View.GONE);
        mDescContainer.setVisibility(View.GONE);
    }

    void showVideoContainer() {
        mIngContainer.setVisibility(View.GONE);
        mVideoContainer.setVisibility(View.VISIBLE);
        mDescContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(mParcelableCurrentRecipeKey, mCurrentBakingRecipe);
        outState.putInt(mParcelableDetailIndex, mCurrentIndex);
        super.onSaveInstanceState(outState);
    }

}
