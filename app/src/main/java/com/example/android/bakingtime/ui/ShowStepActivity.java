package com.example.android.bakingtime.ui;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.IngredientListAdapter;
import com.example.android.bakingtime.model.BakingRecipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowStepActivity extends AppCompatActivity {

    @BindView(R.id.step_desc_tv)
    TextView mDescTV;
    @BindView(R.id.ingredientList)
    ListView mIngredientListView;

    @Nullable
    @BindView(R.id.nextbutton)
    Button mNextButton;
    @Nullable
    @BindView(R.id.previousbutton)
    Button mPreviousButton;
    @Nullable
    @BindView(R.id.novideo_image)
    ImageView mNoVideoImage;


    BakingRecipe mCurrentBakingRecipe;
    int mCurrentIndex,mStepIndex;
    boolean isIngredient;
    boolean isStep;
    boolean isNextClicked, isPreviousClicked;

    public static final String TAG="ShowStepActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_step);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            mCurrentBakingRecipe = getIntent().getExtras().getParcelable("bakingRecipeObject");
            int index = getIntent().getIntExtra("position", -1);
            setCurrentIndex(index);
        }

        setPreviousNextButtonListener();
    }

    public void setCurrentIndex(int index) {
        Log.i(TAG, "setCurrentIndex.. Index received is  " + index);
        if (index == 0) {
            mCurrentIndex = index;
            isIngredient = true;
            isStep = false;


        } else if (index > 0) {
            mCurrentIndex = index;
            mStepIndex = index - 1;
            isIngredient = false;
            isStep = true;

        }


    }

    void setPreviousNextButtonListener() {

        if (mPreviousButton != null) {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCurrentIndex(mCurrentIndex - 1);
                    mNextButton.setVisibility(View.VISIBLE);
                    if (mCurrentIndex == 0) {
                        mPreviousButton.setVisibility(View.INVISIBLE);

                    }
                    isPreviousClicked = true;
                    updateUI();
                    isPreviousClicked = false;

                }
            });
        }

        if (mNextButton != null) {
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCurrentIndex(mCurrentIndex + 1);

                    if (mCurrentIndex > 0) {
                        mPreviousButton.setVisibility(View.VISIBLE);
                    }
                    if (mCurrentIndex > 0 && mCurrentIndex + 1 > mCurrentBakingRecipe.getRecipeStepList().size()) {
                        mPreviousButton.setVisibility(View.VISIBLE);
                        mNextButton.setVisibility(View.INVISIBLE);
                    }
                    isNextClicked = true;
                    updateUI();
                    isNextClicked = false;
                }
            });


        }

    }

    public void updateUI() {
        Log.i(TAG, "updateUI starts");

        if (mCurrentIndex > mCurrentBakingRecipe.getRecipeStepList().size() | mCurrentIndex < 0) {
            return;
        }
        if (isIngredient) {
            setupIngredientView();
            IngredientListAdapter ingAdapter = new IngredientListAdapter(this, mCurrentBakingRecipe.getRecipeIngList());
            mIngredientListView.setAdapter(ingAdapter);

        } else if (isStep) {
            setupStepView();
         //   initializePlayer();
            mDescTV.setText(mCurrentBakingRecipe.getRecipeStepList().get(mStepIndex).getStepLongDesc());
        }
    }

    void setupIngredientView() {
        removeVideoContainerView();
        mDescTV.setVisibility(View.GONE);
        mIngredientListView.setVisibility(View.VISIBLE);
    }

    void setupStepView() {
   //     showExoplayerView();
        mDescTV.setVisibility(View.VISIBLE);
        mIngredientListView.setVisibility(View.GONE);
    }

    void removeVideoContainerView(){

    }

}
