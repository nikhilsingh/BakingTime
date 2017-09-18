package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.model.RecipeIngredient;

import java.util.ArrayList;

/**
 * Created by nikhil on 12/9/17.
 */

public class IngredientListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<RecipeIngredient> ingList;
    public IngredientListAdapter(Context context, ArrayList<RecipeIngredient> data) {
        mContext =context;
        ingList=data;

    }

    @Override
    public int getCount() {
        return ingList.size();
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


        txtView.setText(ingList.get(position).getIngredientDesc());
        txtView.setBackgroundColor(0xfff0000);


        return gridItemView;
    }
}
