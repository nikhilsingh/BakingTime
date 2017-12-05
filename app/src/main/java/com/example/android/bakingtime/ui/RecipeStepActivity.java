package com.example.android.bakingtime.ui;

import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.model.BakingRecipe;
import com.example.android.bakingtime.model.RecipeStep;
import com.example.android.bakingtime.utils.RecipeDataUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingtime.utils.RecipeDataUtil.getBakingRecipeFromDB;

public class RecipeStepActivity extends AppCompatActivity {
    @Nullable
    @BindView(R.id.nextbutton)
    Button mNextButton;
    @Nullable
    @BindView(R.id.previousbutton)
    Button mPreviousButton;

    int mCurrentIndex;
    BakingRecipe mCurrentBakingRecipe;

    RecipeStep mCurrentStep;

    public static final String TAG = "RecipeStepActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
        ButterKnife.bind(this);
        if (getResources().getBoolean(R.bool.isLandscape)) {
            getSupportActionBar().hide();
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        if (savedInstanceState == null) {


            mCurrentBakingRecipe = getIntent().getExtras().getParcelable("bakingRecipeObject");
            mCurrentIndex = getIntent().getIntExtra("position", -1);
            if (mCurrentIndex > 0) {
                //    mCurrentStep = RecipeDataUtil.getStepForDetailIndex(mCurrentIndex, mCurrentBakingRecipe);
            }
            Log.i("StepActivity", "Baking Recipe " + mCurrentBakingRecipe.getBakeFoodItem().getFoodItemName());

            //     Log.i("StepActivity","ing list entre"+mBakingRecipe.getRecipeIngList().get(2).getIngredientDesc());
            // Log.i("StepActivity","ing step entre"+mBakingRecipe.getRecipeStepList().get(2).getStepLongDesc());

            Log.i("StepActiivty", "inglist all " + mCurrentBakingRecipe.getRecipeIngList().toString());
            Log.i("StepActiivty", "steplst all " + mCurrentBakingRecipe.getRecipeStepList().toString());
            Log.i("StepActiivty", "fooditem " + mCurrentBakingRecipe.getBakeFoodItem().toString());


            updateUI();
            setPreviousNextButtonListener();
        } else if (savedInstanceState != null) {

            mCurrentIndex = savedInstanceState.getInt("currentindex");
            mCurrentBakingRecipe = savedInstanceState.getParcelable("currentrecipe");
            Log.i(TAG, "in on create saved instance not null Current Index" + mCurrentIndex);
            updateUI();
        }



            /*



            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepFragment stepFragment = new RecipeStepFragment();
            stepFragment.setCurrentBakingRecipe(mBakingRecipe);
            stepFragment.setCurrentIndex(index);
            fragmentManager.beginTransaction().add(R.id.stepcontainer, stepFragment).commit();
*/

    }

    void updateStepState(int position) {

        if (position > 0) {
            mCurrentIndex = position;
            mCurrentStep = RecipeDataUtil.getStepForDetailIndex(mCurrentIndex, mCurrentBakingRecipe);
        }

    }

    void setupIngListFragment() {

        IngListFragment ingListFragment = (IngListFragment) getSupportFragmentManager().findFragmentById(R.id.tab_stepIngContainer);
        if (ingListFragment == null) {
            ingListFragment = new IngListFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.tab_stepIngContainer, ingListFragment).commit();
        }
        if (mCurrentBakingRecipe != null) {
            ingListFragment.setIngArrayList(RecipeDataUtil.getIngListForDetailIndex(mCurrentBakingRecipe));
            ingListFragment.updateListView();
        }

    }

    void setupVideoFragment() {
        VideoFragment videoFragment = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.stepVideoContainer);
        if (videoFragment == null) {
            Log.i(TAG, "New Video Fragment Creating");
            videoFragment = new VideoFragment();
            videoFragment.setMediaUrl(mCurrentStep.getStepVideoUrl());
            getSupportFragmentManager().beginTransaction().add(R.id.stepVideoContainer, videoFragment).commit();
        } else {
            Log.i(TAG, "Video Fragment Found");
          //  videoFragment.setMediaUrl(mCurrentStep.getStepVideoUrl());
           // videoFragment.playVideo();
        }

    }

    void setupDescFragment() {
        if (!getResources().getBoolean(R.bool.isLandscape)) {
            StepDescFragment stepDescFragment = (StepDescFragment) getSupportFragmentManager().findFragmentById(R.id.stepDescContainer);
            if (stepDescFragment == null) {
                stepDescFragment = new StepDescFragment();
                stepDescFragment.setStepDescData(mCurrentStep.getStepLongDesc());
                getSupportFragmentManager().beginTransaction().add(R.id.stepDescContainer, stepDescFragment).commit();
            } else {
                stepDescFragment.setStepDescData(mCurrentStep.getStepLongDesc());
            }
        }
    }


    void updateVideoUI() {

        //  String videoUrl = mCurrentBakingRecipe.getRecipeStepList().get(mCurrentIndex - 1).getStepVideoUrl();


    }

    void updateUI() {
        if (mCurrentIndex == 0) {
            setupIngListFragment();
        } else {
            updateStepState(mCurrentIndex);
            setupDescFragment();
            setupVideoFragment();
            //  updateVideoUI();
        }
    }


    void setPreviousNextButtonListener() {

        if (mPreviousButton != null) {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCurrentIndex = mCurrentIndex - 1;
                    updateStepState(mCurrentIndex);
                    mNextButton.setVisibility(View.VISIBLE);
                    if (mCurrentIndex == 0) {
                        mPreviousButton.setVisibility(View.INVISIBLE);
                    } else {
                        mPreviousButton.setVisibility(View.VISIBLE);
                    }

                    updateUI();


                }
            });
        }

        if (mNextButton != null) {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentIndex = mCurrentIndex + 1;


                    if (mCurrentIndex + 1 == mCurrentBakingRecipe.getRecipeStepList().size()) {
                        mPreviousButton.setVisibility(View.VISIBLE);
                        mNextButton.setVisibility(View.INVISIBLE);
                    } else {
                        mNextButton.setVisibility(View.VISIBLE);
                        mPreviousButton.setVisibility(View.VISIBLE);
                    }
                    updateUI();

                }
            });


        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Log.i(TAG, "Setting current index on saveinstance" + mCurrentIndex);
        outState.putParcelable("currentrecipe", mCurrentBakingRecipe);

        outState.putInt("currentindex", mCurrentIndex);
        super.onSaveInstanceState(outState);

    }

    //

    //  Log.i("StepAct",url);

}
