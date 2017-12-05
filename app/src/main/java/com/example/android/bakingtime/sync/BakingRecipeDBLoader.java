package com.example.android.bakingtime.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;

import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.utils.RecipeDataUtil;

/**
 * Created by nikhil on 20/9/17.
 */

public class BakingRecipeDBLoader extends AsyncTaskLoader<BakingRecipe> {
    public static final String TAG="BakingRecipeDBLoader";
    int mFoodId;
    Context mContext;

    public BakingRecipeDBLoader(Context context,int foodid) {
        super(context);
        mContext=context;
        mFoodId=foodid;
        Log.i(TAG,"constructor . Food Id is  "+mFoodId);
    }



    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public BakingRecipe loadInBackground() {
        Log.i(TAG,"loadInBackground starts. Food Id is  "+mFoodId);
         return RecipeDataUtil.getBakingRecipeFromDB(mFoodId,mContext);
    }

    @Override
    public void deliverResult(BakingRecipe data) {
        super.deliverResult(data);
    }


}
