package com.example.android.bakingtime.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.SelectRecipeAdapter;
import com.example.android.bakingtime.model.BakeFoodItem;
import com.example.android.bakingtime.sync.FoodItemCursorLoader;
import com.example.android.bakingtime.sync.RecipeDataRemoteService;
import com.example.android.bakingtime.utils.RecipeDataUtil;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SelectRecipeAdapter.SelectRecipeOnClickHandler {
    public static final String TAG = "MainActivity";

    public static final int LOADER_RECIPE_DB = 20;
    @BindView(R.id.gv_main_selectrecipe)
    GridView mGV_SelectRecipe;
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
    ArrayList<BakeFoodItem> mAllFoodItemList;

    SharedPreferences mSharedPref;
    RecipeLoadBroadcastReceiver mReceiver;
    SelectRecipeAdapter adapter;


    //TODO: RecipeSelect , RecipeDetail, RecipeStepDetail
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate starts");
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        ButterKnife.bind(this);
        mAllFoodItemList = new ArrayList<>();
        mReceiver = new RecipeLoadBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(mBroadcastActionDataLoad));

        mSharedPref = getSharedPreferences(mPrefFileName, Context.MODE_PRIVATE);

        if (!mSharedPref.getBoolean(isDataLoadedKey, false)) {
            Log.i("onCreate", "calling to start service");
            mProgressBar.setVisibility(View.VISIBLE);
            Intent service = new Intent(this, RecipeDataRemoteService.class);
            startService(service);
        } else {


            getSupportLoaderManager().initLoader(LOADER_RECIPE_DB, null, this);
        }
        Log.i(TAG,"OnCreate Ends");

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i(TAG,"OnCreateLoader Starts");
        mProgressBar.setVisibility(View.VISIBLE);

        return new FoodItemCursorLoader(this);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG,"onLoadFinished for Loader called. Cursor count is  "+data.getCount());
        Log.i(TAG,"onLoadFinished Setting adapter");
        mAllFoodItemList= RecipeDataUtil.getAllFoodItemList(data);
        adapter = new SelectRecipeAdapter(this, this, data);
        mGV_SelectRecipe.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onFoodSelected(int foodid) {
        Log.i(TAG,"On Food Selected on Main activity.Food Id Selected is "+foodid);
        mSharedPref.edit().putInt(mPrefKey_FoodId, foodid).commit();

        Intent i = new Intent(MainActivity.this, RecipeDetailActivity.class);
        i.putExtra(mIntentKey_FoodId, foodid);
        i.putParcelableArrayListExtra("allfooditemlist",mAllFoodItemList);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy Called");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
    }

    public class RecipeLoadBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"on Receive Broadcast RecipeLoadBroadcastReceiver");
            getSupportLoaderManager().restartLoader(LOADER_RECIPE_DB, null, MainActivity.this);
        }
    }
}



