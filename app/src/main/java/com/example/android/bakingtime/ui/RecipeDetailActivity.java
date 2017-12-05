package com.example.android.bakingtime.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.BakeFoodItem;
import com.example.android.bakingtime.model.BakingRecipe;

import com.example.android.bakingtime.model.RecipeStep;
import com.example.android.bakingtime.sync.BakingRecipeDBLoader;
import com.example.android.bakingtime.utils.RecipeDataUtil;
import com.example.android.bakingtime.widget.UpdateWidgetService;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnDetailActionListener {
    public static final String TAG = "RecipeDetailActivity";

    BakingRecipe mCurrentBakingRecipe;
    ArrayList<BakeFoodItem> mAllFoodItemList;
    boolean mTwoPane = false;
    int mFoodId;
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

    int mDetailIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);


        Log.i(TAG, "On Create starts");
        Log.i(TAG, "Calling service to update widget data as food is already selected");

        if (savedInstanceState == null) {
            Log.i(TAG, "savedInstanceState is null");
            Intent service = new Intent(this, UpdateWidgetService.class);
            startService(service);
            //   mProgressBar.setVisibility(View.VISIBLE);
            mTwoPane = getResources().getBoolean(R.bool.isTab);
            mFoodId = getIntent().getIntExtra(getString(R.string.intent_foodid), -1);

            mAllFoodItemList = getIntent().getParcelableArrayListExtra("allfooditemlist");

            Log.i("NikTestList", mAllFoodItemList.size() + " size is : " + mAllFoodItemList.toString());
            //    getCurrentRecipeFromDB(mFoodId);

            addDetailsFragment();
            setupIngListFragment();


        }
        if (savedInstanceState != null) {
            if (mCurrentBakingRecipe == null) {
                mCurrentBakingRecipe = savedInstanceState.getParcelable("bakingrecipe");

            }
            mFoodId = savedInstanceState.getInt("foodid");
            mAllFoodItemList = savedInstanceState.getParcelableArrayList("allfooditem");
        }
    }


    @Override
    public void onDetailClicked(int position) {

        Log.i(TAG, "onDetailClicked - User clicked detail list at position" + position);
        if (mTwoPane) {

            if (position == 0) {
                showIngredientList();

            } else {
                showVideoDesc(position);
            }


        } else {
            Intent i = new Intent(RecipeDetailActivity.this, RecipeStepActivity.class);
            i.putExtra(getResources().getString(R.string.intent_bakingrecipeobj), mCurrentBakingRecipe);
            i.putExtra(getResources().getString(R.string.intent_recipedetail_position), position);
            startActivity(i);
        }

    }

    void showVideoDesc(int position) {
        setupStepsFragments(position);
    }

    @Override
    public ArrayList<BakeFoodItem> getAllFoodItemList() {
        return mAllFoodItemList;
    }

    @Override
    public int getCurrentFoodId() {
        return mFoodId;
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

    void addIngListFragment() {
        if (mTwoPane) {
            IngListFragment ingListFragment = new IngListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.tab_stepIngContainer, ingListFragment).commit();
        }
    }


    @Override
    public void updateNewCurrentRecipe(int foodid, BakingRecipe newCurrentBakingRecipe) {
        mFoodId = foodid;
        mCurrentBakingRecipe = newCurrentBakingRecipe;
        showIngredientList();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCurrentBakingRecipe != null) {
            outState.putParcelable("bakingrecipe", mCurrentBakingRecipe);


        }
        outState.putParcelableArrayList("allfooditem", mAllFoodItemList);
        outState.putInt("foodid", mFoodId);
        super.onSaveInstanceState(outState);
    }

    void showIngredientList() {
        setupIngListFragment();
        if (mVideoContainer != null) {
            mVideoContainer.setVisibility(View.GONE);
        }
        if (mIngContainer != null) {
            mIngContainer.setVisibility(View.VISIBLE);
        }
        if (mDescContainer != null) {
            mDescContainer.setVisibility(View.GONE);
        }
        setupIngListFragment();

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
                ingListFragment.updateListView();
            }
        }

    }

    void setupStepsFragments(int position) {
        RecipeStep newRecipeStep = RecipeDataUtil.getStepForDetailIndex(position, mCurrentBakingRecipe);

        setupVideoFragment(newRecipeStep);
        setupStepDescFragment(newRecipeStep);

        mVideoContainer.setVisibility(View.VISIBLE);
        mDescContainer.setVisibility(View.VISIBLE);
        mIngContainer.setVisibility(View.GONE);
    }

    void setupVideoFragment(RecipeStep recipeStep) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        VideoFragment videoFragment = (VideoFragment) fragmentManager.findFragmentById(R.id.tab_stepVideoContainer);
        if (videoFragment == null) {
            videoFragment = new VideoFragment();
            videoFragment.setMediaUrl(recipeStep.getStepVideoUrl());
            fragmentManager.beginTransaction().add(R.id.tab_stepVideoContainer, videoFragment).commit();
        } else {
            videoFragment.setMediaUrl(recipeStep.getStepVideoUrl());
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
}
