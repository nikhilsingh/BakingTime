package com.example.android.bakingtime.adapters;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.bakingtime.R;

import java.util.ArrayList;

/**
 * Adapter Class to handle details list of the recipe of a food item.
 * This includes ingredients and all the steps required for the recipe.
 */

public class RecipeDetailAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> mData;

    public RecipeDetailAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public void setDetailListData(ArrayList<String> data) {
        mData = data;
        notifyDataSetChanged();
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

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.listitem_recipe_details, parent, false);
        }
        TextView shortDescTV = listItemView.findViewById(R.id.tv_item_shortDesc);
        shortDescTV.setText(mData.get(position));
        return listItemView;
    }

}
