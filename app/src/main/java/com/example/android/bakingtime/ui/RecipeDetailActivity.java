package com.example.android.bakingtime.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.data.BakingContract;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.widget.IngredientWidgetProvider;
import com.example.android.bakingtime.widget.MyIntentService;

import static com.example.android.bakingtime.utils.RecipeDataUtil.getBakingRecipeFromDB;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnDetailClickListener {
    BakingRecipe mCurrentBakingRecipe;
    boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent service = new Intent(this,MyIntentService.class);
        startService(service);


        if(findViewById(R.id.tab_linearlayout) !=null){
            mTwoPane=true;

        }else {
            mTwoPane=false;
        }

        int foodId = getIntent().getIntExtra("foodid", -1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeDetailFragment detailFragment = new RecipeDetailFragment();
        mCurrentBakingRecipe = getBakingRecipeFromDB(foodId, this);
        detailFragment.setBakingRecipeData(mCurrentBakingRecipe);
        fragmentManager.beginTransaction().add(R.id.recipedetail_container, detailFragment).commit();

        if(mTwoPane){


            RecipeStepFragment stepFragment = new RecipeStepFragment();
            stepFragment.setCurrentBakingRecipe(mCurrentBakingRecipe);
            stepFragment.setCurrentIndex(0);
            fragmentManager.beginTransaction().add(R.id.tab_stepcontainer,stepFragment).commit();
        }

    }

    @Override
    public void onDetailClicked(int position) {

        if(mTwoPane) {

            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepFragment stepFragment = new RecipeStepFragment();
            stepFragment.setCurrentBakingRecipe(mCurrentBakingRecipe);
            stepFragment.setCurrentIndex(position);
            fragmentManager.beginTransaction().replace(R.id.tab_stepcontainer,stepFragment).commit();
        }else{
            Intent i = new Intent(RecipeDetailActivity.this, RecipeStepActivity.class);


            i.putExtra("bakingRecipeObject", mCurrentBakingRecipe);

            i.putExtra("position",position);

            startActivity(i);
        }

    }
}
