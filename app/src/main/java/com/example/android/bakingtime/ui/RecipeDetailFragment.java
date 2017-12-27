package com.example.android.bakingtime.ui;

import android.content.Context;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;

import com.example.android.bakingtime.R;

import com.example.android.bakingtime.adapters.RecipeDetailAdapter;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeStep;
import com.example.android.bakingtime.sync.BakingRecipeDBLoader;
import com.example.android.bakingtime.utils.RecipeDataUtil;


import java.util.ArrayList;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Fragment class which handles the details of a recipe.
 */

public class RecipeDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<BakingRecipe>, RecipeDetailAdapter.RecipeDetailOnClickHandler {

    public static final String TAG = "RecipeDetailFragment";
    @BindView(R.id.lv_recipedetail)
    RecyclerView recipeDetailRV;

    ArrayList<String> mDataList;

    BakingRecipe mCurrentBakingRecipe;

    @BindView(R.id.tv_recipedetail_name)
    TextView recipeNameTV;
    OnDetailActionListener mCallback;
    RecipeDetailAdapter mRecipeAdapter;
    @BindView(R.id.btn_previousitem)
    ImageButton mPreviousBtn;
    @BindView(R.id.btn_nextitem)
    ImageButton mNextButton;

    @BindString(R.string.savedinstancekey_bakingrecipe)
    String mCurrentRecipeKey;

    @BindString(R.string.pref_file_name)
    String mPrefKey_FileName;
    @BindString(R.string.key_allfoodids_set)
    String mAllFoodIdSetKey;

    ArrayList<Integer> mAllFoodIdList;
    int mCurrentFoodId;
    int mDetailIndex = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mCurrentFoodId = mCallback.getCurrentFoodId();
        mPrefKey_FileName = getString(R.string.pref_file_name);
        SharedPreferences mSharedPref = getActivity().getSharedPreferences(mPrefKey_FileName, Context.MODE_PRIVATE);
        mAllFoodIdSetKey = getString(R.string.key_allfoodids_set);
        Set<String> strSet = mSharedPref.getStringSet(mAllFoodIdSetKey, new HashSet<String>());
        mAllFoodIdList = RecipeDataUtil.getAllFoodIdList(strSet);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);



        mRecipeAdapter = new RecipeDetailAdapter(this );
        recipeDetailRV.setAdapter(mRecipeAdapter);
        recipeDetailRV.setLayoutManager(new LinearLayoutManager(getContext()));
        if (savedInstanceState == null) {
            getCurrentRecipeFromDB(mCurrentFoodId);
        } else {
            mCurrentBakingRecipe = savedInstanceState.getParcelable(mCurrentRecipeKey);
            setBakingRecipeData(mCurrentBakingRecipe);
        }


      /*  recipeDetailLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               view.setSelected(true);
                mCallback.onDetailClicked(position);
            }
        });
*/
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToNextFoodId();

            }
        });
        mPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToPreviousFoodId();
            }
        });

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (OnDetailActionListener) context;
    }


    void getCurrentRecipeFromDB(int foodId) {
        Log.i(TAG, "Start fetching recipe from DB.Current Food Id " + foodId);
        if (foodId > 0) {
            mCurrentFoodId = foodId;
            //Todo: Show ProgressBar
            //Todo: hide list
            getLoaderManager().restartLoader(22, null, this);
        }
    }

    public void updateToPreviousFoodId() {

        int currentFoodItemIndex = getFoodItemIndex();

        if (currentFoodItemIndex == 1) {
            hidePreviousItemButton();
        } else {
            showPreviousItemButton();
        }
        int previousFoodId = mAllFoodIdList.get(currentFoodItemIndex - 1);
        getCurrentRecipeFromDB(previousFoodId);
    }

    int getFoodItemIndex() {
        int currentIndex = -1;
        int i;
        for (i = 0; i < mAllFoodIdList.size(); i++) {
            if (mCurrentFoodId == mAllFoodIdList.get(i)) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    public void updateToNextFoodId() {

        int mAllFoodListSize = mAllFoodIdList.size();
        int lastIndex = mAllFoodListSize - 1;

        int currentFoodItemIndex = getFoodItemIndex();


        if (currentFoodItemIndex + 1 == lastIndex) {
            hideNextItemButton();
        } else {
            showNextItemButton();
        }

        int nextFoodId = mAllFoodIdList.get(currentFoodItemIndex + 1);
        getCurrentRecipeFromDB(nextFoodId);

    }

    public void setBakingRecipeData(BakingRecipe bakingRecipe) {
        mCurrentBakingRecipe = bakingRecipe;
        recipeNameTV.setText(mCurrentBakingRecipe.getBakeFoodItem().getFoodItemName());
        mRecipeAdapter.setDetailListData(prepareDetailList());
        int foodItemIndex = getFoodItemIndex();
        if (foodItemIndex == 0) {
            hidePreviousItemButton();
        }
        if (foodItemIndex == mAllFoodIdList.size() - 1) {
            hideNextItemButton();
        }

    }


    ArrayList<String> prepareDetailList() {
        mDataList = null;
        mDataList = new ArrayList<>();
        mDataList.add("Ingredients");
        mCurrentBakingRecipe.getRecipeStepList().toString();
        for (RecipeStep step : mCurrentBakingRecipe.getRecipeStepList()) {
            mDataList.add(step.getStepShortDesc());
        }
        return mDataList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(mCurrentRecipeKey, mCurrentBakingRecipe);
        super.onSaveInstanceState(outState);
    }

    public void hideNextItemButton() {
        mNextButton.setVisibility(View.INVISIBLE);
        mPreviousBtn.setVisibility(View.VISIBLE);
    }

    public void showNextItemButton() {
        mNextButton.setVisibility(View.VISIBLE);
        mPreviousBtn.setVisibility(View.VISIBLE);
    }

    public void hidePreviousItemButton() {
        mPreviousBtn.setVisibility(View.INVISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    public void showPreviousItemButton() {
        mPreviousBtn.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<BakingRecipe> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Loader onCreateLoader called. Food Id is " + mCurrentFoodId);
        return new BakingRecipeDBLoader(getContext(), mCurrentFoodId);
    }

    @Override
    public void onLoadFinished(Loader<BakingRecipe> loader, BakingRecipe data) {
        Log.i(TAG, "onLoadFinished starts");

        if (data != null) {
            mCurrentBakingRecipe = null;
        }


        mCurrentBakingRecipe = data;
        mDetailIndex = 0;
        newCurrentRecipeDataAvailable();

    }

    void newCurrentRecipeDataAvailable() {
        setBakingRecipeData(mCurrentBakingRecipe);
        mCallback.updateNewCurrentRecipe(mCurrentFoodId, mCurrentBakingRecipe);
    }

    @Override
    public void onLoaderReset(Loader<BakingRecipe> loader) {

    }

    @Override
    public void onDetailSelected(int position) {
        mCallback.onDetailClicked(position);
    }

    public interface OnDetailActionListener {
        void onDetailClicked(int position);

        int getCurrentFoodId();

        void updateNewCurrentRecipe(int foodid, BakingRecipe newCurrentBakingRecipe);
    }
}
