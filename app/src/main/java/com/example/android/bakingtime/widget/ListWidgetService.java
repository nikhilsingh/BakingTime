package com.example.android.bakingtime.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.data.BakingContract;

/**
 * Class to handle widget related service to load data
 */

public class ListWidgetService extends RemoteViewsService {

    public static final String TAG = "ListWidgetService";

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(getApplicationContext());
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;
    int mFoodId;

    public ListRemoteViewFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

        SharedPreferences mSharedPref = mContext.getSharedPreferences("bakingprefs", Context.MODE_PRIVATE);
        mFoodId = mSharedPref.getInt("foodid", -1);
        if (mFoodId > 0) {
            String sFoodId = String.valueOf(mFoodId);
            String mSelection = BakingContract.COMMON_COLUMN_FOODID + "=?";
            String[] mSelectionArgs = new String[]{sFoodId};
            mCursor = mContext.getContentResolver().query(BakingContract.Ingredient.CONTENT_URI, null, mSelection, mSelectionArgs, null);
        }


    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        mCursor.moveToPosition(position);

        String desc = mCursor.getString(mCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_DESC));
        String quant = mCursor.getString(mCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_QUANTITY));
        String measure = mCursor.getString(mCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_MEASURE));
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.listitem_ingredients_widget);

        views.setTextViewText(R.id.item_ingdesc, desc);
        views.setTextViewText(R.id.item_ing_quantity, quant + " " + measure);

        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.intent_foodid), mFoodId);
        views.setOnClickFillInIntent(R.id.widget_listitem_parent, intent);


        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
