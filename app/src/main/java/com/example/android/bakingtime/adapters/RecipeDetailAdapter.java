package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nikhil on 11/8/17.
 */

public class RecipeDetailAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> mData;

    public RecipeDetailAdapter(Context context, ArrayList<String> detailShortDesc) {
        mContext =context;
        mData=detailShortDesc;
    }

    @Override
    public int getCount() {
        return mData.size();
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
        View listItemView=convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1,parent,false);
        }
        TextView tv = (TextView) listItemView;
        tv.setText(mData.get(position));


        return listItemView;
    }
}
