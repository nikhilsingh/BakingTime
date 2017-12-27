package com.example.android.bakingtime.adapters;

/**
 * Adapter Class to handle ListView adapter for Ingredient List
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.RecipeIngredient;

import java.util.ArrayList;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientListViewHolder> {
    Context mContext;
    ArrayList<RecipeIngredient> ingList;//List of all the Ingredients for a Food Item
    public static final String TAG = "IngredientListAdapter";

    public IngredientListAdapter(Context context, ArrayList<RecipeIngredient> data) {
        mContext = context;
        ingList = data;
    }

    @Override
    public IngredientListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.listitem_ingredients, parent, false);

        return new IngredientListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientListViewHolder holder, int position) {
        String desc = ingList.get(position).getIngredientDesc();
        String quantMeasure = ingList.get(position).getIngredientQuantity() + " " + ingList.get(position).getIngredientMeasure();

        holder.descTV.setText(desc);
        holder.quantityTV.setText(quantMeasure);
    }


    @Override
    public int getItemCount() {
        return ingList.size();
    }

    public class IngredientListViewHolder extends RecyclerView.ViewHolder {
        TextView descTV;
        TextView quantityTV;

        public IngredientListViewHolder(View itemView) {
            super(itemView);
            descTV = itemView.findViewById(R.id.item_ingdesc);
            quantityTV = itemView.findViewById(R.id.item_ing_quantity);

        }
    }

    public void updateListData(ArrayList<RecipeIngredient> list) {
        ingList = list;
        notifyDataSetChanged();
    }
}
