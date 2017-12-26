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

import com.bumptech.glide.Glide;
import com.example.android.bakingtime.R;

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



import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *Fragment class to handle video related operations.
 */
public class VideoFragment extends Fragment {

    @BindView(R.id.myexo_playerview)
    SimpleExoPlayerView mPlayerView;
    @Nullable
    @BindView(R.id.novideo_image)
    ImageView mNoVideoImage;

    @BindString(R.string.video_resumeindexkey)
    String mResumeIndexKey;

    @BindString(R.string.video_resumewindowindexkey)
    String mResumeWindowIndexKey;

    @BindString(R.string.video_urlkey)
    String mUrlKey;
    SimpleExoPlayer mPlayer;

    String mediaUriString;
    Uri mediaUri;
    int resumeWindowIndex;
    long resumePositionIndex;
    @Nullable
    String mThumbUrl;

    public static final String TAG = "VideoFragment";

    public VideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            clearResumePosition();
            if (mediaUriString != null && !mediaUriString.isEmpty()) {
                if (validateMediaUri()) {
                    playVideo();
                }
            }
        } else {
            resumePositionIndex = savedInstanceState.getLong(mResumeIndexKey);
            resumeWindowIndex = savedInstanceState.getInt(mResumeWindowIndexKey);

            mediaUriString = savedInstanceState.getString(mUrlKey);
            setMediaUrl(mediaUriString);
        }


        return rootView;
    }

    public void setThumbUrl(String thumburl){
        mThumbUrl=thumburl;
    }

    public void setMediaUrl(String uriString) {
        mediaUriString = uriString;
        if (mediaUriString != null && !mediaUriString.isEmpty()) {
            mediaUri = Uri.parse(mediaUriString);
        } else {
            mediaUri = null;
            mediaUriString = null;
        }
    }

    void initializePlayerInstance() {
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

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
        requestAudioFocus();
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultHttpDataSourceFactory("ua"), new DefaultExtractorsFactory(), null, null);
        boolean shouldResume = false;
        if (resumeWindowIndex != -1) {
            shouldResume = true;
            mPlayer.seekTo(resumeWindowIndex, resumePositionIndex);
        }

        mPlayer.prepare(mediaSource, !shouldResume, false);
        mPlayer.setPlayWhenReady(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(mResumeWindowIndexKey, resumeWindowIndex);
        outState.putLong(mResumeIndexKey, resumePositionIndex);
        outState.putString(mUrlKey, mediaUriString);
        super.onSaveInstanceState(outState);
    }

    void releasePlayer() {

        if (mPlayer != null) {
            updateResumePosition();
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }


    private void updateResumePosition() {
        resumeWindowIndex = mPlayer.getCurrentWindowIndex();
        resumePositionIndex = Math.max(0, mPlayer.getCurrentPosition());
    }

    public void clearResumePosition() {
        resumeWindowIndex = -1;
        resumePositionIndex = -1;
    }

    void playVideo() {
        if (validateMediaUri()) {

            if (mPlayer == null) {
                initializePlayerInstance();
            }
            mNoVideoImage.setVisibility(View.GONE);
            mPlayerView.setVisibility(View.VISIBLE);

            preparePlayerToPlay(mediaUri);
        } else {
            releasePlayer();
            showNoVideoImage();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        playVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    boolean validateMediaUri() {

        if (mediaUri == null | Uri.EMPTY.equals(mediaUri)) {
            return false;
        } else {
            return true;
        }
    }

    void showNoVideoImage(){
        Log.i(TAG,"Thumbs Url is "+mThumbUrl);
        Glide.with(this)
                .load(mThumbUrl)
                .placeholder(R.drawable.novideo)
                .into(mNoVideoImage);
        mNoVideoImage.setVisibility(View.VISIBLE);
        mPlayerView.setVisibility(View.GONE);

    }
}
