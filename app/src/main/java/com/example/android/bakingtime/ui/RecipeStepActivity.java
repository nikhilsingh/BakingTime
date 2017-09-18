package com.example.android.bakingtime.ui;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.BakingRecipe;

import static com.example.android.bakingtime.utils.RecipeDataUtil.getBakingRecipeFromDB;

public class RecipeStepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);




    //
        BakingRecipe mBakingRecipe = getIntent().getExtras().getParcelable("bakingRecipeObject");
        int index = getIntent().getIntExtra("position",-1);

        Log.i("StepActivity","Baking Recipe "+mBakingRecipe.getBakeFoodItem().getFoodItemName());

   //     Log.i("StepActivity","ing list entre"+mBakingRecipe.getRecipeIngList().get(2).getIngredientDesc());
       // Log.i("StepActivity","ing step entre"+mBakingRecipe.getRecipeStepList().get(2).getStepLongDesc());

        Log.i("StepActiivty","inglist all "+mBakingRecipe.getRecipeIngList().toString());
        Log.i("StepActiivty","steplst all "+mBakingRecipe.getRecipeStepList().toString());
        Log.i("StepActiivty","fooditem "+mBakingRecipe.getBakeFoodItem().toString());


        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeStepFragment stepFragment = new RecipeStepFragment();
        stepFragment.setCurrentBakingRecipe(mBakingRecipe);
        stepFragment.setCurrentIndex(index);
        fragmentManager.beginTransaction().add(R.id.stepcontainer,stepFragment).commit();



      //  Log.i("StepAct",url);
    }
}
