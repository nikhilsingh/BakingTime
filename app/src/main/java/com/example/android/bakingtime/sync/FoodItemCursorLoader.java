package com.example.android.bakingtime.sync;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.example.android.bakingtime.data.BakingContract;

/**
 * Created by nikhil on 19/9/17.
 */

public class FoodItemCursorLoader extends CursorLoader {
    public static final String TAG="FoodItemCursorLoader";
    Context mContext;

    public FoodItemCursorLoader(Context context) {
        super(context);
        Log.i(TAG,"FoodItemCursorLoader instance created");
        mContext=context;
    }



    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.i(TAG,"onStartLoading");
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        Log.i(TAG,"loadInBackground starts");
             Cursor cursor = mContext.getContentResolver().query(BakingContract.FoodItem.CONTENT_URI, null, null, null, null);
        return cursor;
    }

    @Override
    public void deliverResult(Cursor cursor) {
        Log.i(TAG,"deliverResult starts");
        super.deliverResult(cursor);
    }


}
