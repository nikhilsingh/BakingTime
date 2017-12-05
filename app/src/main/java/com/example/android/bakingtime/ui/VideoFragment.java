package com.example.android.bakingtime.ui;


import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.bakingtime.R;
import com.google.android.exoplayer2.C;
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
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.net.URI;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends Fragment {

    @BindView(R.id.myexo_playerview)
    SimpleExoPlayerView mPlayerView;
    @Nullable
    @BindView(R.id.novideo_image)
    ImageView mNoVideoImage;

    SimpleExoPlayer mPlayer;

    String mediaUriString;
    Uri mediaUri;
    int resumeWindowIndex;
    long resumePositionIndex;

    public static final String TAG = "VideoFragment";

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

   //     setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);

        Log.i(TAG,"On CreateView starts");
        if (savedInstanceState == null) {
            Log.i(TAG,"saved instance is null");
            if (mediaUriString!=null&&!mediaUriString.isEmpty()) {
                if (validateMediaUri()) {
                    playVideo();
                }
            }
        }else {
            mediaUriString=savedInstanceState.getString("url");
            setMediaUrl(mediaUriString);
        }


        return rootView;
    }

    public void setMediaUrl(String uriString) {
        mediaUriString = uriString;
        Log.i(TAG,"mediaUriString is "+mediaUriString);
        if (!mediaUriString.isEmpty()) {
            mediaUri = Uri.parse(mediaUriString);
        }else{
            mediaUri=null;
            mediaUriString=null;
        }
    }

    void initializePlayerInstance() {
        Log.i(TAG,"Initializing Player Instance");
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();    //initialize tand create the player

        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mPlayerView.setPlayer(mPlayer);
    }

    void requestAudioFocus() {
        AudioManager am = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {

            }
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

    }

    void preparePlayerToPlay(Uri mediaUri) {
        Log.i(TAG,"PreparingPlayer to Play");
        requestAudioFocus();
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultHttpDataSourceFactory("ua"), new DefaultExtractorsFactory(), null, null);
        boolean shouldResume=false;
        if(resumeWindowIndex != -1){
            shouldResume=true;
            mPlayer.seekTo(resumeWindowIndex, resumePositionIndex);
        }

        mPlayer.prepare(mediaSource,!shouldResume,false);
        mPlayer.setPlayWhenReady(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("url",mediaUriString);
        super.onSaveInstanceState(outState);


    }

    void releasePlayer() {
        Log.i(TAG, "releasePlayer starts");
        if (mPlayer != null) {
            Log.i(TAG,"Player is not null so stopping and releasing");
            updateResumePosition();
            mPlayer.stop();
            mPlayer.release();

            mPlayer = null;
        }
    }


    private void updateResumePosition() {
        resumeWindowIndex = mPlayer.getCurrentWindowIndex();
        resumePositionIndex = Math.max(0,mPlayer.getCurrentPosition());
    }

    private void clearResumePosition() {
        resumeWindowIndex = -1;
        resumePositionIndex = -1;
    }
    void playVideo() {
        Log.i(TAG,"PlayVideo starts");
        if (validateMediaUri()) {


            if (mPlayer == null) {
                Log.i(TAG,"Player is null. Creating another");
                initializePlayerInstance();
            }
            mNoVideoImage.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);

            preparePlayerToPlay(mediaUri);
        } else {
            releasePlayer();
            mNoVideoImage.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
        }
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
        releasePlayer();

    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();

            playVideo();

    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
        releasePlayer();
    }

    boolean validateMediaUri() {
        Log.i(TAG,"Validate Uri Starts");
        if (mediaUri == null | Uri.EMPTY.equals(mediaUri)) {
            Log.i(TAG,"uri validation is false"+mediaUri);
            return false;

        } else {
            Log.i(TAG,"uri validation is true"+mediaUri);
            return true;
        }
    }

}
