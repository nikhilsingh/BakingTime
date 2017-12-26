package com.example.android.bakingtime.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.SelectRecipeAdapter;
import com.example.android.bakingtime.model.BakeFoodItem;
import com.example.android.bakingtime.sync.FoodItemCursorLoader;
import com.example.android.bakingtime.sync.RecipeDataRemoteService;
import com.example.android.bakingtime.utils.RecipeDataUtil;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SelectRecipeAdapter.SelectRecipeOnClickHandler {
    public static final String TAG = "MainActivity";

    public static final int LOADER_RECIPE_DB = 20;
    @BindView(R.id.gv_main_selectrecipe)
    RecyclerView mRV_SelectRecipe;
    @BindView(R.id.pb_main_inprogress)
    ProgressBar mProgressBar;
    @BindString(R.string.broadcast_action_dataloaded)
    String mBroadcastActionDataLoad;
    @BindString(R.string.pref_file_name)
    String mPrefFileName;
    @BindString(R.string.pref_key_dataloaded)
    String isDataLoadedKey;
    @BindString(R.string.intent_foodid)
    String mIntentKey_FoodId;
    @BindString(R.string.pref_key_foodid)
    String mPrefKey_FoodId;
    @BindString(R.string.key_allfoodids_set)
    String mAllFoodIdSetKey;
    @BindView(R.id.tv_main_error)
    TextView mErrorView;

    ArrayList<BakeFoodItem> mAllFoodItemList;

    SharedPreferences mSharedPref;
    RecipeLoadBroadcastReceiver mReceiver;
    SelectRecipeAdapter adapter;


    //TODO: RecipeSelect , RecipeDetail, RecipeStepDetail
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        mAllFoodItemList = new ArrayList<>();
        mReceiver = new RecipeLoadBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(mBroadcastActionDataLoad));
        mSharedPref = getSharedPreferences(mPrefFileName, Context.MODE_PRIVATE);
        mErrorView.setVisibility(View.GONE);

        mRV_SelectRecipe.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.selectrecipe_grid_colmn)));


        if (!mSharedPref.getBoolean(isDataLoadedKey, false)) {
            Log.i(TAG, "Calling Service to save remote data .");
            if (isInternetActive()) {
                mProgressBar.setVisibility(View.VISIBLE);
                Intent service = new Intent(this, RecipeDataRemoteService.class);
                startService(service);
            } else {
                showErrorMessage();
            }
        } else {
            Log.i(TAG, "Load data from Database");
            getSupportLoaderManager().initLoader(LOADER_RECIPE_DB, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);
        return new FoodItemCursorLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG, "Data Load from DB finished. Total Count is " + data.getCount());

        mAllFoodItemList = RecipeDataUtil.getAllFoodItemList(data);

        adapter = new SelectRecipeAdapter(this, this, data);
        mRV_SelectRecipe.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);
        addAllFoodItemPref();
    }

    public void addAllFoodItemPref() {
        Set<String> foodIdStrings = new HashSet<>();
        for (int i = 0; i < mAllFoodItemList.size(); i++) {
            foodIdStrings.add(String.valueOf(mAllFoodItemList.get(i).getFoodItemId()));
        }
        mSharedPref.edit().putStringSet(mAllFoodIdSetKey, foodIdStrings).commit();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onFoodSelected(int foodid) {
        Log.i(TAG, "On Food Clicked.Food Id Selected is " + foodid);
        mSharedPref.edit().putInt(mPrefKey_FoodId, foodid).commit();
        Intent i = new Intent(MainActivity.this, RecipeDetailActivity.class);
        i.putExtra(mIntentKey_FoodId, foodid);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    public class RecipeLoadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Broadcast received from Service completion. This is the confirmation that remote data has been loaded");

            if (!mSharedPref.getBoolean(isDataLoadedKey, false)) {
                showErrorMessage();
            } else {

                getSupportLoaderManager().restartLoader(LOADER_RECIPE_DB, null, MainActivity.this);

            }
        }
    }

    private boolean isInternetActive() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&

                activeNetwork.isConnected();
        Log.i(TAG, "Internet is active?" + isConnected);
        return isConnected;

    }

    private void showErrorMessage() {
        mErrorView.setText(getString(R.string.nointernet_errormsg));
        mErrorView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }
}



