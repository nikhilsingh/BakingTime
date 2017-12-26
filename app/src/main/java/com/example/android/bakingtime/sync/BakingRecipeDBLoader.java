package com.example.android.bakingtime.sync;

import android.content.Context;

import android.support.v4.content.AsyncTaskLoader;

import android.util.Log;

import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.utils.RecipeDataUtil;

/**
 * Loader class to get all the recipe details of a particular food item based on its foodid.
 */

public class BakingRecipeDBLoader extends AsyncTaskLoader<BakingRecipe> {
    public static final String TAG = "BakingRecipeDBLoader";
    int mFoodId;
    Context mContext;

    public BakingRecipeDBLoader(Context context, int foodid) {
        super(context);
        mContext = context;
        mFoodId = foodid;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public BakingRecipe loadInBackground() {
        Log.i(TAG, "Starting to load all the recipe details for food id  " + mFoodId);
        return RecipeDataUtil.getBakingRecipeFromDB(mFoodId, mContext);
    }

    @Override
    public void deliverResult(BakingRecipe data) {
        super.deliverResult(data);
    }

}
