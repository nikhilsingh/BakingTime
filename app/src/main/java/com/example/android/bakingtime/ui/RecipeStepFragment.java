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

/**
 * Created by nikhil on 12/8/17.
 */

public class RecipeStepFragment extends Fragment  {
    BakingRecipe mCurrentBakingRecipe;
    SimpleExoPlayerView mPlayerView;
    SimpleExoPlayer mPlayer;
    TextView mDescTV;
    boolean isIngredient;
    boolean isStep;
    int mIndex,mStepIndex;
    ListView mIngredientListView;
    boolean isNextClicked;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.myexo_playerview);
        mDescTV = (TextView) rootView.findViewById(R.id.step_desc_tv);
        mIngredientListView = (ListView) rootView.findViewById(R.id.ingredientList);
        Button mButton =(Button) rootView.findViewById(R.id.nextbutton);




        updateUI();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               setCurrentIndex(mIndex+1);
                isNextClicked=true;
                updateUI();
                isNextClicked=false;
            }
        });


        //


        return rootView;
    }

    @Override
    public void onDestroy() {
        Log.i("NikFragment", "onDestroy");
        super.onDestroy();
        releasePlayer();

    }

    @Override
    public void onPause() {
        Log.i("NikFragment", "OnPause");
        super.onPause();

    }

    @Override
    public void onResume() {
        Log.i("NikFragment", "onResume");
        super.onResume();
        updateUI();
    }

    @Override
    public void onStop() {
        Log.i("NikFragment", "onStop");
        super.onStop();
        releasePlayer();
    }


    void initializePlayer() {

        Uri mediaUri = getMediaUri();
        if (mediaUri == null | Uri.EMPTY.equals(mediaUri)) {
            if(mPlayer!=null){
                mPlayer.setPlayWhenReady(false);
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(),R.drawable.exo_controls_play));
            }
            Log.i("initPlayer","url is null"+mediaUri+"finish");
            return;
        }
        if (mPlayer == null) {
            Log.i("initPlayer","initializing player"+mediaUri+"finish2");
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            //initialize tand create the player

            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mPlayer);

            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultHttpDataSourceFactory("ua"), new DefaultExtractorsFactory(), null, null);

            AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(new OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {

                }
            },AudioManager.STREAM_MUSIC,AudioManager.AUDIOFOCUS_GAIN);



            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);


        }else if(mPlayer!=null && !Uri.EMPTY.equals(mediaUri)&& isNextClicked){
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultHttpDataSourceFactory("ua"), new DefaultExtractorsFactory(), null, null);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
        }
    }

    void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    Uri getMediaUri() {
        String url = mCurrentBakingRecipe.getRecipeStepList().get(mStepIndex).getStepVideoUrl();
        Log.i("FragmentUrl", url);
        if(url==null){
            return null;
        }
        Uri uri = Uri.parse(url);
        return uri;
    }

    public void setCurrentBakingRecipe(BakingRecipe recipe){
        mCurrentBakingRecipe=recipe;
    }
    public void setCurrentIndex(int index){
        Log.i("StepFragment","Index is "+index);
        if(index==0) {
            mIndex = index;
            isIngredient=true;
            isStep=false;
        }else if(index>0){
            mIndex = index;
            mStepIndex = index-1;
            isIngredient=false;
            isStep =true;
        }
    }

    public void updateCurrentIndexOnNext(){

    }

    public void updateUI(){

        if(mIndex>mCurrentBakingRecipe.getRecipeStepList().size()){
            return;
        }
        if(isIngredient){
            mPlayerView.setVisibility(View.GONE);
            mDescTV.setVisibility(View.GONE);
            mIngredientListView.setVisibility(View.VISIBLE);

            IngredientListAdapter ingAdapter =  new IngredientListAdapter(getContext(), mCurrentBakingRecipe.getRecipeIngList());
            mIngredientListView.setAdapter(ingAdapter);

        }else if (isStep){
            mPlayerView.setVisibility(View.VISIBLE);
            mDescTV.setVisibility(View.VISIBLE);
            mIngredientListView.setVisibility(View.GONE);
            initializePlayer();
            mDescTV.setText(mCurrentBakingRecipe.getRecipeStepList().get(mStepIndex).getStepLongDesc());
        }
    }

}
