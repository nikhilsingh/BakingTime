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
 * Service class which is called only once to fetch all the recipe data from the remote url and insert into the local DB.
 */

public class RecipeDataRemoteService extends IntentService {
    public static final String TAG = "RecipeDataRemoteService";

    public RecipeDataRemoteService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "Service starting in background to fetch data from remote URL.");
        String resp = NetworkUtils.getRecipeJSON();
        Log.i(TAG, "response is " + resp);
        ArrayList<ContentValues[]> contentValueArrayList = null;
        try {
            contentValueArrayList = getContentValues(resp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (contentValueArrayList != null && contentValueArrayList.size() == 3) {


            Log.i(TAG, "Starting to insert all the recipe data to DB.");
            getContentResolver().bulkInsert(BakingContract.FoodItem.CONTENT_URI, contentValueArrayList.get(0));
            getContentResolver().bulkInsert(BakingContract.Ingredient.CONTENT_URI, contentValueArrayList.get(1));
            getContentResolver().bulkInsert(BakingContract.Step.CONTENT_URI, contentValueArrayList.get(2));
            SharedPreferences mSharedPref = getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE);
            mSharedPref.edit().putBoolean(getString(R.string.pref_key_dataloaded), true).commit();

        } else {
            SharedPreferences mSharedPref = getSharedPreferences(getString(R.string.pref_file_name), MODE_PRIVATE);
            mSharedPref.edit().putBoolean(getString(R.string.pref_key_dataloaded), false).commit();
        }
        //Updating SharedPreference to confirm the load completion.
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(getString(R.string.broadcast_action_dataloaded)));

        Log.i(TAG, "Sending broadcast for data load completion");

        Log.i(TAG, "Service call ends. Data load completed from remote repository.");
    }
}
