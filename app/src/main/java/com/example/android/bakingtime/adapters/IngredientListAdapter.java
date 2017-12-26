package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.RecipeIngredient;

import java.util.ArrayList;

/**
 * Adapter Class to handle ListView adapter for Ingredient List
 */

public class IngredientListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<RecipeIngredient> ingList;//List of all the Ingredients for a Food Item
    public static final String TAG = "IngredientListAdapter";

    public IngredientListAdapter(Context context, ArrayList<RecipeIngredient> data) {
        mContext = context;
        ingList = data;
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
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.listitem_ingredients, parent, false);
        }

        TextView descTV = listItemView.findViewById(R.id.item_ingdesc);
        TextView quantityTV = listItemView.findViewById(R.id.item_ing_quantity);

        String desc=ingList.get(position).getIngredientDesc();
        String quantMeasure=ingList.get(position).getIngredientQuantity() + " " + ingList.get(position).getIngredientMeasure();

        descTV.setText(desc);
        quantityTV.setText(quantMeasure);

        return listItemView;
    }

    public void updateListData(ArrayList<RecipeIngredient> list) {
        ingList = list;
        notifyDataSetChanged();
    }
}
