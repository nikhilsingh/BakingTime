package com.example.android.bakingtime.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.RecipeDetailAdapter;
import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeStep;

import java.util.ArrayList;

import static com.example.android.bakingtime.utils.RecipeDataUtil.getBakingRecipeFromDB;

/**
 * Created by nikhil on 11/8/17.
 */

public class RecipeDetailFragment extends Fragment {
    ListView recipeDetailLV;
    ArrayList<String> mDataList;
    BakingRecipe mCurrentBakingRecipe;

    OnDetailClickListener mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_details,container,false);

        mDataList = new ArrayList<>();
        int foodId=getActivity().getIntent().getIntExtra("foodid",0);
        String foodName=getActivity().getIntent().getStringExtra("foodname");

        Log.i("RecipeDetail","Food id "+foodId+" name="+foodName);

        recipeDetailLV = (ListView) rootView.findViewById(R.id.lv_recipedetail);
   //     mCurrentBakingRecipe=getBakingRecipeFromDB(foodId,getContext());

        mDataList.add("Ingredients");

        for( RecipeStep step : mCurrentBakingRecipe.getRecipeStepList() ){
            mDataList.add(step.getStepShortDesc());
        }


        Log.i("DetailFragment","BakingRecipe FoodItem"+mCurrentBakingRecipe.getBakeFoodItem());
        Log.i("DetailFragment","BakingRecipe Ingredients"+mCurrentBakingRecipe.getRecipeIngList());
        Log.i("DetailFragment","BakingRecipe Steps"+mCurrentBakingRecipe.getRecipeStepList());



        RecipeDetailAdapter adapter = new RecipeDetailAdapter(getContext(),mDataList);
        recipeDetailLV.setAdapter(adapter);

        recipeDetailLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onDetailClicked(position);
            }
        });
       // Log.i("DetailFragment","Total cursor count"+cursor.getCount()+"");
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback=(OnDetailClickListener) context;


    }

    public interface OnDetailClickListener{
        void onDetailClicked(int position);
    }

    public void setBakingRecipeData(BakingRecipe bakingRecipe){
        mCurrentBakingRecipe=bakingRecipe;
    }

}
