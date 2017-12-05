package com.example.android.bakingtime.ui;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.RecipeDetailAdapter;
import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.model.BakeFoodItem;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeStep;
import com.example.android.bakingtime.sync.BakingRecipeDBLoader;

import java.util.ArrayList;

import static com.example.android.bakingtime.utils.RecipeDataUtil.getBakingRecipeFromDB;

/**
 * Created by nikhil on 11/8/17.
 */

public class RecipeDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<BakingRecipe> {

    public static final String TAG = "RecipeDetailFragment";
    ListView recipeDetailLV;
    ArrayList<String> mDataList;
    BakingRecipe mCurrentBakingRecipe;
    TextView recipeNameTV;
    OnDetailActionListener mCallback;
    RecipeDetailAdapter mRecipeAdapter;
    Button mPreviousBtn, mNextButton;


    ArrayList<BakeFoodItem> mAllFoodItemList;
    int mCurrentFoodId;
    int mDetailIndex=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.i(TAG, "On Create starts");
        mCurrentFoodId = mCallback.getCurrentFoodId();
        mAllFoodItemList = mCallback.getAllFoodItemList();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView starts");
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        recipeDetailLV = (ListView) rootView.findViewById(R.id.lv_recipedetail);
        recipeNameTV = (TextView) rootView.findViewById(R.id.tv_recipedetail_name);
        mPreviousBtn = (Button) rootView.findViewById(R.id.btn_previousitem);
        mNextButton = (Button) rootView.findViewById(R.id.btn_nextitem);
        mRecipeAdapter = new RecipeDetailAdapter(getContext());
        recipeDetailLV.setAdapter(mRecipeAdapter);
        getCurrentRecipeFromDB(mCurrentFoodId);
        recipeDetailLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCallback.onDetailClicked(position);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToNextFoodId();

            }
        });

        mPreviousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateToPreviousFoodId();
            }
        });

        if (savedInstanceState != null) {
            mCurrentBakingRecipe = savedInstanceState.getParcelable("bakingrecipe");
            setBakingRecipeData(mCurrentBakingRecipe);
        }

        return rootView;
    }

    void onDetailItemClick(int position){

        if(position==0){

            //show Ingredients List in Step View
        }else{
            //show Video/Desc Fragments
        }

    }
    @Override
    public void onAttach(Context context) {
        Log.i(TAG, "onAttach starts");
        super.onAttach(context);
        mCallback = (OnDetailActionListener) context;

    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "On Activity Created"+mCurrentFoodId);

    }

    public interface OnDetailActionListener {
        void onDetailClicked(int position);
        ArrayList<BakeFoodItem> getAllFoodItemList();
        int getCurrentFoodId();

        void updateNewCurrentRecipe(int foodid,BakingRecipe newCurrentBakingRecipe);
    }

    void getCurrentRecipeFromDB(int foodId) {
        Log.i(TAG,"Start fetching recipe from DB.Current Food Id "+foodId);
        if (foodId> 0 ) {
            mCurrentFoodId=foodId;
            //Show ProgressBar
            //hide list
            getLoaderManager().restartLoader(22, null,this);
        }
    }
    public void updateToPreviousFoodId() {

        int currentIndex = -1;
        int i;
        for (i = 0; i < mAllFoodItemList.size(); i++) {
            if (mCurrentFoodId == mAllFoodItemList.get(i).getFoodItemId()) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex == 1) {
            hidePreviousItemButton();
        } else {
            showPreviousItemButton();
        }
        int previousFoodId = mAllFoodItemList.get(currentIndex - 1).getFoodItemId();
        getCurrentRecipeFromDB(previousFoodId);
    }

    public void updateToNextFoodId() {

        int mAllFoodListSize = mAllFoodItemList.size();
        int lastIndex = mAllFoodListSize - 1;

        int currentIndex = -1;
        int i;
        for (i = 0; i < mAllFoodItemList.size(); i++) {
            if (mCurrentFoodId == mAllFoodItemList.get(i).getFoodItemId()) {
                currentIndex = i;
                break;
            }
        }


        if (currentIndex + 1 == lastIndex) {
            hideNextItemButton();
        } else {
            showNextItemButton();
        }

        int nextFoodId = mAllFoodItemList.get(currentIndex + 1).getFoodItemId();

        getCurrentRecipeFromDB(nextFoodId);


    }

    public void setBakingRecipeData(BakingRecipe bakingRecipe) {
        Log.i(TAG, "setBakingRecipeData starts");
        mCurrentBakingRecipe = bakingRecipe;
        recipeNameTV.setText(mCurrentBakingRecipe.getBakeFoodItem().getFoodItemName());
        mRecipeAdapter.setDetailListData(prepareDetailList());

    }


    ArrayList<String> prepareDetailList() {
        Log.i(TAG, "prepareDetailList starts");
        mDataList = null;
        mDataList = new ArrayList<>();
        mDataList.add("Ingredients");

        mCurrentBakingRecipe.getRecipeStepList().toString();
        for (RecipeStep step : mCurrentBakingRecipe.getRecipeStepList()) {
            mDataList.add(step.getStepShortDesc());
        }
        Log.i(TAG, "mDataList  is " + mDataList.toString());
        return mDataList;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.i(TAG, "OnSaveInstance State starts");
        outState.putParcelable("bakingrecipe", mCurrentBakingRecipe);
        //outState.putInt("foodid", mFoodId);

        super.onSaveInstanceState(outState);
    }

    public void hideNextItemButton() {
        mNextButton.setVisibility(View.INVISIBLE);
        mPreviousBtn.setVisibility(View.VISIBLE);
    }

    public void showNextItemButton() {
        mNextButton.setVisibility(View.VISIBLE);
        mPreviousBtn.setVisibility(View.VISIBLE);
    }

    public void hidePreviousItemButton() {
        mPreviousBtn.setVisibility(View.INVISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    public void showPreviousItemButton() {
        mPreviousBtn.setVisibility(View.VISIBLE);
        mNextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<BakingRecipe> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "Loader onCreateLoader called. Food Id is " + mCurrentFoodId);
        return new BakingRecipeDBLoader(getContext(), mCurrentFoodId);
    }

    @Override
    public void onLoadFinished(Loader<BakingRecipe> loader, BakingRecipe data) {
        Log.i(TAG, "onLoadFinished starts");
        if (data != null) {
            mCurrentBakingRecipe = null;
        }

     //   mProgressBar.setVisibility(View.GONE);
        mCurrentBakingRecipe = data;
        mDetailIndex = 0;
        newCurrentRecipeDataAvailable();

    }

    void newCurrentRecipeDataAvailable(){
        setBakingRecipeData(mCurrentBakingRecipe);
        mCallback.updateNewCurrentRecipe(mCurrentFoodId,mCurrentBakingRecipe);
    }

    @Override
    public void onLoaderReset(Loader<BakingRecipe> loader) {

    }
}
