package com.example.android.bakingtime.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.data.BakingContract;

/**
 * Recycler View Adapter to handle list of food items available.
 */

public class SelectRecipeAdapter extends RecyclerView.Adapter<SelectRecipeAdapter.SelectRecipeViewHolder> {
    Context mContext;
    Cursor mData;
    SelectRecipeOnClickHandler mClickHandler;

    public SelectRecipeAdapter(Context context, SelectRecipeOnClickHandler handler, Cursor data) {
        mContext = context;
        mData = data;
        mClickHandler = handler;
    }


    @Override
    public SelectRecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int LayoutIdForGridItem = R.layout.griditem_selectrecipe;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(LayoutIdForGridItem, parent, shouldAttachToParentImmediately);

        return new SelectRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectRecipeViewHolder holder, int position) {
        mData.moveToPosition(position);
        final int mFoodId = mData.getInt(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_ID));


        holder.foodNameTV.setText(mData.getString(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_FOOD_NAME)));
        holder.servingsTV.setText("Servings : " + mData.getString(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_SERVINGS)));
        Glide.with(mContext)
                .load(mData.getString(mData.getColumnIndex(BakingContract.FoodItem.COLUMN_FOODIMAGE)))
                .placeholder(R.drawable.ic_local_dining_black_72dp)
                .into(holder.foodImgView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onFoodSelected(mFoodId);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.getCount();
    }

    public class SelectRecipeViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImgView;
        TextView foodNameTV;
        TextView servingsTV;

        public SelectRecipeViewHolder(View itemView) {
            super(itemView);
            foodNameTV = itemView.findViewById(R.id.tv_select_foodname);
            servingsTV = itemView.findViewById(R.id.tv_select_serving);
            foodImgView = itemView.findViewById(R.id.iv_foodimg);

        }
    }


    public interface SelectRecipeOnClickHandler {
        void onFoodSelected(int foodid);
    }
}
