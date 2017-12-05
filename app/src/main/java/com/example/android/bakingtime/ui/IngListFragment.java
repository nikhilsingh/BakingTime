package com.example.android.bakingtime.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.IngredientListAdapter;
import com.example.android.bakingtime.model.RecipeIngredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngListFragment extends Fragment {
    @BindView(R.id.ingredientList)
    ListView mIngredientListView;
    IngredientListAdapter mIngAdapter;
    ArrayList<RecipeIngredient> mRecipeIngList;

    public IngListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_ing_list, container, false);
        ButterKnife.bind(this,rootView);
        mRecipeIngList = new ArrayList<>();
         mIngAdapter = new IngredientListAdapter(getContext(), mRecipeIngList);
        mIngredientListView.setAdapter(mIngAdapter);
        return rootView;

    }

    public void setIngArrayList(ArrayList<RecipeIngredient> list){
        mRecipeIngList= list;
    }


    void updateListView(){
        mIngAdapter.updateListData(mRecipeIngList);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mRecipeIngList!=null){
            updateListView();
        }
    }
}
