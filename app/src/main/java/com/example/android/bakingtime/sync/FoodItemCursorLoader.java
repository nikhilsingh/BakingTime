package com.example.android.bakingtime.sync;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.example.android.bakingtime.data.BakingContract;

/**
 * Loader to fetch all the food items available. This doesnot read the recipe details.
 */

public class FoodItemCursorLoader extends CursorLoader {
    public static final String TAG = "FoodItemCursorLoader";
    Context mContext;

    public FoodItemCursorLoader(Context context) {
        super(context);
        mContext = context;
    }


    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        Log.i(TAG, "Starting to fetch all the food items recipe from DB");
        Cursor cursor = mContext.getContentResolver().query(BakingContract.FoodItem.CONTENT_URI, null, null, null, null);
        return cursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        super.deliverResult(cursor);
    }


}
