package com.example.android.bakingtime.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeStep;
import com.example.android.bakingtime.utils.RecipeDataUtil;
import com.example.android.bakingtime.widget.UpdateWidgetService;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnDetailActionListener {
    public static final String TAG = "RecipeDetailActivity";

    BakingRecipe mCurrentBakingRecipe;
    boolean mTwoPane = false;
    int mFoodId;
    int mCurrentDetailIndex = -1;
    @Nullable
    @BindView(R.id.tab_stepVideoContainer)
    FrameLayout mVideoContainer;
    @Nullable
    @BindView(R.id.tab_stepDescContainer)
    FrameLayout mDescContainer;

    @Nullable
    @BindView(R.id.tab_stepIngContainer)
    FrameLayout mIngContainer;

    @BindView(R.id.pb_detail_inprogress)
    ProgressBar mProgressBar;
    SharedPreferences mSharedPref;
    int mDetailIndex = 0;
    @BindString(R.string.pref_key_foodid)
    String mPrefKey_FoodId;

    @BindString(R.string.intent_foodid)
    String mIntentFoodIdKey;
    @BindString(R.string.savedinstancekey_detailindex)
    String mDetailIndexKey;

    @BindString(R.string.savedinstancekey_foodid)
    String mFoodIdKey;

    @BindString(R.string.savedinstancekey_bakingrecipe)
    String mCurrentRecipeKey;

    @BindString(R.string.intent_bakingrecipeobj)
    String mIntentCurrentRecipeKey;

    @BindString(R.string.pref_file_name)
    String mPrefFileName;
    boolean mIsNewDetailPosition = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);

        mTwoPane = getResources().getBoolean(R.bool.isTab);
        if (savedInstanceState == null) {

            startWidgetDataService();           //   mProgressBar.setVisibility(View.VISIBLE);
            mFoodId = getIntent().getIntExtra(mIntentFoodIdKey, -1);

        } else {
            Log.i(TAG, "Saved Instance is not null.");
            if (mCurrentBakingRecipe == null) {
                mCurrentBakingRecipe = savedInstanceState.getParcelable(mCurrentRecipeKey);
            }
            mCurrentDetailIndex = savedInstanceState.getInt(mDetailIndexKey, -1);
            mFoodId = savedInstanceState.getInt(mFoodIdKey);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addDetailsFragment();
        if (mCurrentDetailIndex > -1) {
            setupUIWithPosition(mCurrentDetailIndex);
        } else {
            setupIngListFragment();
        }

    }

    void startWidgetDataService() {
        Intent service = new Intent(this, UpdateWidgetService.class);
        startService(service);
    }

    @Override
    public void onDetailClicked(int position) {
        mIsNewDetailPosition = true;
        Log.i(TAG, "onDetailClicked - User clicked detail list at position" + position);
        setupUIWithPosition(position);
    }

    void setupUIWithPosition(int position) {
        if (mTwoPane) {

            mCurrentDetailIndex = position;
            if (position == 0) {
                showIngredientList();

            } else {
                showVideoDesc(position);
            }


        } else {
            Intent i = new Intent(RecipeDetailActivity.this, RecipeStepActivity.class);
            i.putExtra(mIntentCurrentRecipeKey, mCurrentBakingRecipe);
            i.putExtra(getResources().getString(R.string.intent_recipedetail_position), position);
            startActivity(i);
        }

        mIsNewDetailPosition = false;
    }

    void showIngredientList() {
        setupIngListFragment();
        showIngViews();
    }

    void showVideoViews() {
        if (mVideoContainer != null) {
            mVideoContainer.setVisibility(View.VISIBLE);
        }
        if (mDescContainer != null) {
            mDescContainer.setVisibility(View.VISIBLE);
        }
        if (mIngContainer != null) {
            mIngContainer.setVisibility(View.GONE);
        }
    }

    void showIngViews() {
        if (mVideoContainer != null) {
            mVideoContainer.setVisibility(View.GONE);
        }
        if (mDescContainer != null) {
            mDescContainer.setVisibility(View.GONE);
        }
        if (mIngContainer != null) {
            mIngContainer.setVisibility(View.VISIBLE);
        }
    }

    void showVideoDesc(int position) {
        setupStepsFragments(position);
    }

    void setupIngListFragment() {
        if (mTwoPane) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            IngListFragment ingListFragment = (IngListFragment) fragmentManager.findFragmentById(R.id.tab_stepIngContainer);
            if (ingListFragment == null) {
                ingListFragment = new IngListFragment();
                fragmentManager.beginTransaction().add(R.id.tab_stepIngContainer, ingListFragment).commit();
            }
            if (mCurrentBakingRecipe != null) {
                ingListFragment.setIngArrayList(RecipeDataUtil.getIngListForDetailIndex(mCurrentBakingRecipe));
            }
        }

    }

    void setupStepsFragments(int position) {
        RecipeStep newRecipeStep = RecipeDataUtil.getStepForDetailIndex(position, mCurrentBakingRecipe);

        setupVideoFragment(newRecipeStep);
        setupStepDescFragment(newRecipeStep);
        showVideoViews();
    }

    void setupVideoFragment(RecipeStep recipeStep) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        VideoFragment videoFragment = (VideoFragment) fragmentManager.findFragmentById(R.id.tab_stepVideoContainer);
        if (videoFragment == null) {
            videoFragment = new VideoFragment();
            Log.i(TAG,"Thumbnail Url is "+recipeStep.getStepThumbnailUrl());
            videoFragment.setMediaUrl(recipeStep.getStepVideoUrl());
            videoFragment.setThumbUrl(recipeStep.getStepThumbnailUrl());
            fragmentManager.beginTransaction().add(R.id.tab_stepVideoContainer, videoFragment).commit();
        } else {
            if (mIsNewDetailPosition) {
                videoFragment.clearResumePosition();
            }

            videoFragment.setMediaUrl(recipeStep.getStepVideoUrl());
            videoFragment.setThumbUrl(recipeStep.getStepThumbnailUrl());
            videoFragment.playVideo();
        }

    }

    void setupStepDescFragment(RecipeStep recipeStep) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        StepDescFragment stepDescFragment = (StepDescFragment) fragmentManager.findFragmentById(R.id.tab_stepDescContainer);
        if (stepDescFragment == null) {
            stepDescFragment = new StepDescFragment();
            fragmentManager.beginTransaction().add(R.id.tab_stepDescContainer, stepDescFragment).commit();
        }
        stepDescFragment.setStepDescData(recipeStep.getStepLongDesc());


    }


    void addDetailsFragment() {
        Log.i(TAG, "Adding Detail Fragment");
        RecipeDetailFragment mDetailFragment = (RecipeDetailFragment) getSupportFragmentManager().findFragmentById(R.id.recipedetail_container);
        if (mDetailFragment == null) {
            Log.i(TAG, "Detail Fragment is null . Creating a new one");
            mDetailFragment = new RecipeDetailFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.recipedetail_container, mDetailFragment).commit();
        }

    }


    @Override
    public int getCurrentFoodId() {
        return mFoodId;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCurrentBakingRecipe != null) {
            outState.putParcelable(mCurrentRecipeKey, mCurrentBakingRecipe);


        }
        if (mCurrentDetailIndex > -1) {
            outState.putInt(mDetailIndexKey, mCurrentDetailIndex);
        }
        outState.putInt(mFoodIdKey, mFoodId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateNewCurrentRecipe(int foodid, BakingRecipe newCurrentBakingRecipe) {
        mFoodId = foodid;
        mCurrentDetailIndex = -1;
        mCurrentBakingRecipe = newCurrentBakingRecipe;
        showIngredientList();
        updateFoodIdPref();

    }

    void updateFoodIdPref() {
        mSharedPref = getSharedPreferences(mPrefFileName, Context.MODE_PRIVATE);
        mSharedPref.edit().putInt(mPrefKey_FoodId, mFoodId).commit();
        startWidgetDataService();

    }

}
