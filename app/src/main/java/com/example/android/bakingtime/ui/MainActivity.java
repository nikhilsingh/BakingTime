package com.example.android.bakingtime.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.SelectRecipeAdapter;
import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.utils.NetworkUtils;
import com.example.android.bakingtime.widget.IngredientWidgetProvider;
import com.example.android.bakingtime.widget.MyIntentService;
import com.facebook.stetho.Stetho;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.android.bakingtime.utils.RecipeDataUtil.getBakingRecipeObjFromJSON;
import static com.example.android.bakingtime.utils.RecipeDataUtil.getContentValues;

public class MainActivity extends AppCompatActivity {

    GridView mGV_SelectRecipe;
    SharedPreferences mSharedPref;


    //TODO: RecipeSelect , RecipeDetail, RecipeStepDetail
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);




        mSharedPref = getSharedPreferences("bakingprefs", Context.MODE_PRIVATE);

        if(!mSharedPref.getBoolean("dataloaded",false)){
            new GetJSONDATA().execute();
        }

        mGV_SelectRecipe = (GridView) findViewById(R.id.gv_selectrecipe);

        final Cursor cursor = getContentResolver().query(BakingContract.FoodItem.CONTENT_URI, null, null, null, null);

        SelectRecipeAdapter adapter = new SelectRecipeAdapter(this, cursor);
        mGV_SelectRecipe.setAdapter(adapter);

        mGV_SelectRecipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                Intent i = new Intent(MainActivity.this, RecipeDetailActivity.class);
                cursor.moveToPosition(position);

                int mFoodId=cursor.getInt(cursor.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_ID));
                mSharedPref.edit().putInt("foodid",mFoodId).commit();

                i.putExtra("foodid",mFoodId);
                i.putExtra("foodname",cursor.getString(cursor.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_NAME)));






                startActivity(i);
            }
        });


    }





  public class GetJSONDATA extends AsyncTask<String, String, String>

    {

        @Override
        protected String doInBackground(String... params) {
            String resp = NetworkUtils.getRecipeJSON();
            Log.i("MainAc", "network call done");
            try {
                ArrayList<ContentValues[]> contentValueArrayList = getContentValues(resp);


                getContentResolver().bulkInsert(BakingContract.FoodItem.CONTENT_URI, contentValueArrayList.get(0));
                getContentResolver().bulkInsert(BakingContract.Ingredient.CONTENT_URI, contentValueArrayList.get(1));
                getContentResolver().bulkInsert(BakingContract.Step.CONTENT_URI, contentValueArrayList.get(2));

                //getContentResolver().insert(BakingContract.FoodItem.CONTENT_URI,)
                mSharedPref.edit().putBoolean("dataloaded",true).commit();
            } catch (JSONException e) {
                //  Log.i("Nik",e.toString());
                e.printStackTrace();
            }
            return resp;
        }
    }
}



