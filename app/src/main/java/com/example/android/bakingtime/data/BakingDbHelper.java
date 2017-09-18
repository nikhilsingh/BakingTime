package com.example.android.bakingtime.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nikhil on 9/8/17.
 */

public class BakingDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "BakingDbHelper";
    private static final String DATABASE_NAME = "baking.db";
    private static final int VERSION = 2;

    private static final String CREATE_TABLE_FOOD = "CREATE TABLE " + BakingContract.FoodItem.TABLE_NAME + " ( " +
            BakingContract.FoodItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.FoodItem.COLUMN_FOOD_ID + " INTEGER," +
            BakingContract.FoodItem.COLUMN_FOOD_NAME + " TEXT," +
            BakingContract.FoodItem.COLUMN_SERVINGS + " INTEGER);";

    private static final String CREATE_TABLE_INGREDIENT = "CREATE TABLE " + BakingContract.Ingredient.TABLE_NAME + " ( " +
            BakingContract.Ingredient._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.Ingredient.COLUMN_FOOD_ID + " INTEGER," +
            BakingContract.Ingredient.COLUMN_ING_ID + " INTEGER," +
            BakingContract.Ingredient.COLUMN_DESC + " TEXT," +
            BakingContract.Ingredient.COLUMN_MEASURE + " TEXT," +
            BakingContract.Ingredient.COLUMN_QUANTITY + " REAL);";

    private static final String CREATE_TABLE_STEPS = "CREATE TABLE " + BakingContract.Step.TABLE_NAME + " ( " +
            BakingContract.Step._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.Step.COLUMN_FOOD_ID + " INTEGER," +
            BakingContract.Step.COLUMN_STEP_ID + " INTEGER," +
            BakingContract.Step.COLUMN_SHORTDESC + " TEXT," +
            BakingContract.Step.COLUMN_LONGDESC + " TEXT," +
            BakingContract.Step.COLUMN_VIDEOURL + " TEXT," +
            BakingContract.Step.COLUMN_THUMBNAILURL + " TEXT);";


    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,"onCreate starts");
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_INGREDIENT);
        db.execSQL(CREATE_TABLE_STEPS);
        Log.i(TAG,"onCreate ends");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
