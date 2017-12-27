package com.example.android.bakingtime.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingtime.R;

import java.util.ArrayList;

/**
 * Adapter Class to handle details list of the recipe of a food item.
 * This includes ingredients and all the steps required for the recipe.
 */

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.RecipeDetailViewHolder> {
public static final String TAG ="RecipeDetailAdapter";
    ArrayList<String> mData;
    RecipeDetailOnClickHandler mDetailClickHandler;
    int mClickedPosition=-1;
    public RecipeDetailAdapter(RecipeDetailOnClickHandler handler) {
        mDetailClickHandler = handler;
        mData=new ArrayList<>();
    }

    @Override
    public RecipeDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.listitem_recipe_details, parent, false);

        return new RecipeDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeDetailViewHolder holder, final int position) {
        holder.shortDescTV.setText(mData.get(position));

        holder.shortDescTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickedPosition=position;
                notifyDataSetChanged();
                mDetailClickHandler.onDetailSelected(position);
            }
        });

        if(position==mClickedPosition){
            holder.shortDescTV.setBackgroundResource(R.drawable.shape_btn_filledrectangle);
        }else {
            holder.shortDescTV.setBackgroundResource(R.drawable.shape_roundedrectangleborder);
        }

    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public void setDetailListData(ArrayList<String> data) {
       Log.i(TAG,"New Data is received"+data);
        mData = data;
        mClickedPosition=-1;
        notifyDataSetChanged();
    }

    public class RecipeDetailViewHolder extends RecyclerView.ViewHolder {
        TextView shortDescTV;

        public RecipeDetailViewHolder(View itemView) {
            super(itemView);
            shortDescTV = itemView.findViewById(R.id.tv_item_shortDesc);
        }
    }


    public interface RecipeDetailOnClickHandler {
        void onDetailSelected(int position);
    }
}
