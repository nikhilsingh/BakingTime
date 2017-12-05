package com.example.android.bakingtime.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.bakingtime.R;
import com.example.android.bakingtime.adapters.IngredientListAdapter;
import com.example.android.bakingtime.model.BakingRecipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by nikhil on 12/8/17.
 */

public class RecipeStepFragment extends Fragment {

    public static final String TAG = "RecipeStepFragment";

    BakingRecipe mCurrentBakingRecipe;
    @BindView(R.id.myexo_playerview)
    SimpleExoPlayerView mPlayerView;
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

    @Nullable
    @BindView(R.id.frame_layout_video)
    FrameLayout mFrameLayoutVideo;

    SimpleExoPlayer mPlayer;
    boolean isIngredient;
    boolean isStep;
    int mIndex, mStepIndex;

    boolean isNextClicked, isPreviousClicked;


    public RecipeStepFragment() {
        Log.i(TAG,"constructor called");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate called");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);
        Log.i(TAG, "onCreateView starts");
        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState is not null. setting required values");
            mCurrentBakingRecipe = savedInstanceState.getParcelable("bakingrecipe");
            mIndex = savedInstanceState.getInt("index");
            setCurrentBakingRecipe(mCurrentBakingRecipe);
            setCurrentIndex(mIndex);
        }

        if (mCurrentBakingRecipe != null) {
            // Log.i(TAG,"baking recipe is not null. calling updateUI");

        }

        setPreviousNextButtonListener();


        //


        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        releasePlayer();

    }

    @Override
    public void onPause() {
        Log.i(TAG, "OnPause");
        super.onPause();

    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
        if (mCurrentBakingRecipe != null) {
            updateUI();
        }
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
        releasePlayer();
    }

    boolean validateMediaUri() {
        Uri mediaUri = getMediaUri();
        if (mediaUri == null | Uri.EMPTY.equals(mediaUri)) {
            return false;

        } else {
            return true;
        }
    }

    void initializePlayerInstance() {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();    //initialize tand create the player

        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mPlayerView.setPlayer(mPlayer);
    }


    void requestAudioFocus() {
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(new OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {

            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

    }


    void preparePlayerToPlay(Uri mediaUri) {
        requestAudioFocus();
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultHttpDataSourceFactory("ua"), new DefaultExtractorsFactory(), null, null);
        mPlayer.prepare(mediaSource);
        mPlayer.setPlayWhenReady(true);
    }

    void initializePlayer() {
        Log.i(TAG, "initializePlayer starts");

        boolean validMediaUri = validateMediaUri();

        Uri mediaUri = getMediaUri();
        if (!validMediaUri) {
            showNoVideoImage();
            hideExoplayerView();
            setPlayerPlayWhenReadyState(false);

            Log.i(TAG, "initializePlayer . url is null " + mediaUri);
            return;
        } else {
            hideNoVideoImage();
            showExoplayerView();

        }


        if (mPlayer == null) {
            initializePlayerInstance();
            preparePlayerToPlay(mediaUri);
        } else {
            preparePlayerToPlay(mediaUri);
        }


        /*else if (mPlayer != null && !Uri.EMPTY.equals(mediaUri) && (isNextClicked | isPreviousClicked)) {
            Log.i(TAG, "Player is not null. Next Button is clicked or pevious button is clicked");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultHttpDataSourceFactory("ua"), new DefaultExtractorsFactory(), null, null);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
        }*/
    }

    void releasePlayer() {
        Log.i(TAG, "releasePlayer starts");
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    Uri getMediaUri() {
        String url = mCurrentBakingRecipe.getRecipeStepList().get(mStepIndex).getStepVideoUrl();
        Log.i(TAG, "Video Url is " + url);
        if (url == null) {
            return null;
        }
        Uri uri = Uri.parse(url);
        return uri;
    }

    public void setCurrentBakingRecipe(BakingRecipe recipe) {
        Log.i(TAG, "settingCurrentBakingRecipe starts");
        mCurrentBakingRecipe = recipe;
    }

    public void setCurrentIndex(int index) {
        Log.i(TAG, "setCurrentIndex.. Index received is  " + index);
        if (index == 0) {
            mIndex = index;
            isIngredient = true;
            isStep = false;


        } else if (index > 0) {
            mIndex = index;
            mStepIndex = index - 1;
            isIngredient = false;
            isStep = true;

        }


    }

    public void updateCurrentIndexOnNext() {

    }

    public void updateUI() {
        Log.i(TAG, "updateUI starts");

        if (mIndex > mCurrentBakingRecipe.getRecipeStepList().size() | mIndex < 0) {
            return;
        }
        if (isIngredient) {
            setupIngredientView();
            IngredientListAdapter ingAdapter = new IngredientListAdapter(getContext(), mCurrentBakingRecipe.getRecipeIngList());
            mIngredientListView.setAdapter(ingAdapter);

        } else if (isStep) {
            setupStepView();
            initializePlayer();
            mDescTV.setText(mCurrentBakingRecipe.getRecipeStepList().get(mStepIndex).getStepLongDesc());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState called.");
        outState.putParcelable("bakingrecipe", mCurrentBakingRecipe);
        outState.putInt("index", mIndex);

        super.onSaveInstanceState(outState);
    }

    void setPreviousNextButtonListener() {

        if (mPreviousButton != null) {
            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setCurrentIndex(mIndex - 1);
                    mNextButton.setVisibility(View.VISIBLE);
                    if (mIndex - 1 == 0) {
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
                    setCurrentIndex(mIndex + 1);

                    if (mIndex + 1 > 0) {
                        mPreviousButton.setVisibility(View.VISIBLE);
                    }
                    if (mIndex + 1 > 0 && mIndex + 1 > mCurrentBakingRecipe.getRecipeStepList().size()) {
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

    void showNextButton() {
        if (mNextButton != null) {
            mNextButton.setVisibility(View.VISIBLE);
        }
    }

    void hideNextButton() {
        if (mNextButton != null) {
            mNextButton.setVisibility(View.INVISIBLE);
        }
    }

    void showPreviousButton() {

        if (mPreviousButton != null) {
            mPreviousButton.setVisibility(View.VISIBLE);
        }
    }

    void hidePreviousButton() {
        if (mPreviousButton != null) {
            mPreviousButton.setVisibility(View.INVISIBLE);
        }
    }

    void showNoVideoImage() {
        if (mNoVideoImage != null) {

            mNoVideoImage.setVisibility(View.VISIBLE);

           /* if (getResources().getBoolean(R.bool.isLandscape)) {
                Log.i("StepFrag", "landscape i s true");
                if (mFrameLayoutVideo != null) {
                    mFrameLayoutVideo.setVisibility(View.GONE);
                }

            }*/
        }
    }

    void hideNoVideoImage() {
        if (mNoVideoImage != null) {
            mNoVideoImage.setVisibility(View.INVISIBLE);

        }
    }

    void showExoplayerView() {
        if (mPlayerView != null) {
            mPlayerView.setVisibility(View.VISIBLE);
        }
    }

    void hideExoplayerView() {
        if (mPlayerView != null) {
            mPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    void removeExoplayerView() {
        if (mPlayerView != null) {
            mPlayerView.setVisibility(View.GONE);
        }
    }

    void setupIngredientView() {
        removeExoplayerView();
        mDescTV.setVisibility(View.GONE);
        mIngredientListView.setVisibility(View.VISIBLE);
    }

    void setupStepView() {
        showExoplayerView();
        mDescTV.setVisibility(View.VISIBLE);
        mIngredientListView.setVisibility(View.GONE);
    }

    void setupStepNoVideoView() {

    }

    void setupStepVideoView() {

    }

    void setupStepVideoFullView() {

    }

    void setupStepNoVideoFullView() {

    }

    void setPlayerPlayWhenReadyState(boolean state) {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(state);
        }
    }
}
