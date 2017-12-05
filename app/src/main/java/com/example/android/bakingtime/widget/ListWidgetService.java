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
 * Created by nikhil on 14/9/17.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i("WidgetService", "onGetViewFactory called");
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
        Log.i("RemoteViewFactory", "onCreate called");

    }

    @Override
    public void onDataSetChanged() {
        Log.i("ListWidgetService", "onDataSetChanged called");
        SharedPreferences mSharedPref = mContext.getSharedPreferences("bakingprefs", Context.MODE_PRIVATE);
        mFoodId = mSharedPref.getInt("foodid", -1);
        Log.i("widgetservice", "food id is" + mFoodId);
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
        Log.i("RemoteViewFactory", "getViewAt called. Position " + position);
        mCursor.moveToPosition(position);

        String msg = mCursor.getString(mCursor.getColumnIndex(BakingContract.Ingredient.COLUMN_DESC));
        Log.i("WidgetService", "ing text " + msg);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.listitem_ingredients);
        views.setTextViewText(R.id.item_ingdesc, msg);
        Intent intent = new Intent();
        intent.putExtra(mContext.getString(R.string.intent_foodid),mFoodId);
        views.setOnClickFillInIntent(R.id.widget_listitem_parent,intent);



        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.i("RemoteViewFactory", "getLoadingView called");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.i("RemoteViewFactory", "getViewTypeCount called");
        return 1;
    }

    @Override
    public long getItemId(int position) {

        Log.i("RemoteViewFactory", "getItemId called. position"+position);
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        Log.i("RemoteViewFactory", "hasStableIds called");
        return false;
    }
}
