package com.example.android.bakingtime.data;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by nikhil on 9/8/17.
 */

public class BakingProvider extends ContentProvider {
    public static final String TAG = "BakingProvider";

    private BakingDbHelper mDBHelper;

    private UriMatcher mUriMatcher = buildUriMatcher();

    private static final int CODE_FOODITEMS = 100;
    private static final int CODE_ING = 200;
    private static final int CODE_ING_FOODID = 201;
    private static final int CODE_STEPS = 300;
    private static final int CODE_STEPS_FOODID = 301;


    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate Starts");
        mDBHelper = new BakingDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor;
        String tableName;
        switch (mUriMatcher.match(uri)) {
            case CODE_FOODITEMS:
                tableName = BakingContract.FoodItem.TABLE_NAME;
                break;
            case CODE_ING:
                tableName = BakingContract.Ingredient.TABLE_NAME;
                break;
            case CODE_STEPS:
                tableName = BakingContract.Step.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri" + uri);


        }


        cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        Uri returnUri = null;
        switch (mUriMatcher.match(uri)) {
            case CODE_FOODITEMS:
                break;
            case CODE_ING:
                break;
            case CODE_STEPS:
                break;
        }


        return returnUri;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String tableName = null;
        int rowsInserted = 0;

        switch (mUriMatcher.match(uri)) {
            case CODE_FOODITEMS:
                tableName = BakingContract.FoodItem.TABLE_NAME;
                break;
            case CODE_ING:
                tableName = BakingContract.Ingredient.TABLE_NAME;
                break;
            case CODE_STEPS:
                tableName = BakingContract.Step.TABLE_NAME;
                break;
            default:
                return super.bulkInsert(uri, values);

        }
        try {


            db.beginTransaction();
            for (ContentValues value : values) {
                long id = db.insert(tableName, null, value);

                if (id != -1) {
                    rowsInserted++;
                }
            }
            db.setTransactionSuccessful();


        } finally {
            db.endTransaction();
        }
        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsInserted;
    }

    //TODO:Remove this method  if not used
    @NonNull
    @Override
    public ContentProviderResult[] applyBatch(@NonNull ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            int operationsSize = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[operationsSize];
            for (int i = 0; i < operationsSize; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;

        } finally {
            db.endTransaction();
        }


    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {



        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_FOODITEMS, CODE_FOODITEMS);
        matcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_FOODITEMS +"/#", CODE_FOODITEMS);
        matcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_INGREDIENT, CODE_ING);
        matcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_INGREDIENT + "/#", CODE_ING_FOODID);
        matcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_STEPS, CODE_STEPS);
        matcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_STEPS + "/#", CODE_STEPS_FOODID);

        return matcher;
    }
}
