package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.data.BakingContract;

/**
 * Created by nikhil on 11/8/17.
 */

public class SelectRecipeAdapter extends BaseAdapter {
    Context mContext;
    Cursor mData;

    public SelectRecipeAdapter(Context context, Cursor data) {
        mContext=context;
        mData=data;
    }

    @Override
    public int getCount() {
        return mData.getCount();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridItemView = convertView;

        if(gridItemView == null){
            gridItemView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,parent,false);
        }
        TextView txtView = (TextView) gridItemView;
        mData.moveToPosition(position);

        txtView.setText(mData.getString(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_NAME)));
        txtView.setBackgroundColor(0xfff0000);


        return gridItemView;
    }
}
