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
    SelectRecipeOnClickHandler mClickHandler;

    public SelectRecipeAdapter(Context context, SelectRecipeOnClickHandler handler, Cursor data) {
        mContext=context;
        mData=data;
        mClickHandler=handler;
    }

    public interface SelectRecipeOnClickHandler{
        void onFoodSelected(int foodid);
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
            gridItemView = LayoutInflater.from(mContext).inflate(R.layout.griditem_selectrecipe,parent,false);
        }

        TextView foodNameTV =(TextView) gridItemView.findViewById(R.id.tv_select_foodname);
        TextView servingsTV=(TextView) gridItemView.findViewById(R.id.tv_select_serving);


        mData.moveToPosition(position);
        final int mFoodId=mData.getInt(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_ID));

        foodNameTV.setText(mData.getString(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_NAME)));
        servingsTV.setText("Servings : "+mData.getString(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_SERVINGS)));
        //gridItemView.setBackgroundColor(0xfff0000);

        gridItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onFoodSelected(mFoodId);
            }
        });

        return gridItemView;
    }
}
