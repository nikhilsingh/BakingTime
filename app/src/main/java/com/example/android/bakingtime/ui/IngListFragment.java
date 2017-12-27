package com.example.android.bakingtime.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.IngredientListAdapter;
import com.example.android.bakingtime.model.RecipeIngredient;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Fragment to handle the display of all the ingredients.
 */
public class IngListFragment extends Fragment {
    @BindView(R.id.ingredientList)
    RecyclerView mIngredientRV;

    @BindString(R.string.key_ingredient_list)
    String mKey_IngList;

    IngredientListAdapter mIngAdapter;
    ArrayList<RecipeIngredient> mRecipeIngList;

    public static String TAG = "IngListFragment";

    public IngListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ing_list, container, false);
        ButterKnife.bind(this, rootView);
        mIngredientRV.setLayoutManager(new LinearLayoutManager(getContext()));

        mIngAdapter = new IngredientListAdapter(getContext(), new ArrayList<RecipeIngredient>());
        mIngredientRV.setAdapter(mIngAdapter);

        if (savedInstanceState != null) {
            mRecipeIngList = savedInstanceState.getParcelableArrayList(mKey_IngList);
        }
        return rootView;

    }

    public void setIngArrayList(ArrayList<RecipeIngredient> list) {
        mRecipeIngList = list;
        updateListView();
    }

    void updateListView() {
        if (mIngAdapter != null) {
            Log.i(TAG, "Calling adapter to update Ingredient List data");
            mIngAdapter.updateListData(mRecipeIngList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRecipeIngList != null) {
            updateListView();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mRecipeIngList != null && mRecipeIngList.size() > 0) {
            outState.putParcelableArrayList(mKey_IngList, mRecipeIngList);
        }
        super.onSaveInstanceState(outState);
    }
}
