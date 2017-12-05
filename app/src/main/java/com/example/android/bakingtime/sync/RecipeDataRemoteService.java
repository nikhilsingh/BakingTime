package com.example.android.bakingtime.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.utils.NetworkUtils;

import org.json.JSONException;

import java.util.ArrayList;

import static com.example.android.bakingtime.utils.RecipeDataUtil.getContentValues;

/**
 * Created by nikhil on 19/9/17.
 */

public class RecipeDataRemoteService extends IntentService {
    public static final String TAG="RecipeDataRemoteService";

    public RecipeDataRemoteService() {
        super(TAG);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG,"onHandleIntent Starts");
        String resp = NetworkUtils.getRecipeJSON();

        ArrayList<ContentValues[]> contentValueArrayList = null;
        try {
            contentValueArrayList = getContentValues(resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        getContentResolver().bulkInsert(BakingContract.FoodItem.CONTENT_URI, contentValueArrayList.get(0));
        getContentResolver().bulkInsert(BakingContract.Ingredient.CONTENT_URI, contentValueArrayList.get(1));
        getContentResolver().bulkInsert(BakingContract.Step.CONTENT_URI, contentValueArrayList.get(2));

        SharedPreferences mSharedPref = getSharedPreferences(getString(R.string.pref_file_name),MODE_PRIVATE);
        //getContentResolver().insert(BakingContract.FoodItem.CONTENT_URI,)
        mSharedPref.edit().putBoolean(getString(R.string.pref_key_dataloaded), true).commit();

        Log.i(TAG,"sending broadcast for data load completion");
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(getString(R.string.broadcast_action_dataloaded)));

        Log.i(TAG,"onHandle Intent Ends");

    }
}
