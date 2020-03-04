package com.example.baking.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.baking.Constants;
import com.example.baking.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class InstructionDetailVideoFrag extends Fragment  {
    public InstructionDetailVideoFrag() {
    }

    private String videoUrl;
    private View rootView;
    
    private SimpleExoPlayer mExoPlayer = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        if (getArguments().containsKey( Constants.VIDEO_URL )) {
            videoUrl = getArguments().getString( Constants.VIDEO_URL );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate( R.layout.instruction_detail_video_frag, container, false );

        if ((mExoPlayer == null) && !videoUrl.equals( "000" )) {
            Uri mediaUri = Uri.parse( videoUrl );
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance( getContext(), trackSelector, loadControl );

            ((PlayerView) rootView.findViewById( R.id.playerView )).setPlayer( mExoPlayer );
            String userAgent = Util.getUserAgent( getActivity(), "Baking" );
            MediaSource mediaSource = new ExtractorMediaSource( mediaUri, new DefaultDataSourceFactory( getActivity(), userAgent ), new DefaultExtractorsFactory(), null, null );
            mExoPlayer.prepare( mediaSource );
            mExoPlayer.setPlayWhenReady( true );
        } else {
            rootView.findViewById( R.id.playerView ).setVisibility( View.GONE );
        }
        return rootView;
    }

    private void pausePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady( false );
            mExoPlayer.getPlaybackState();
        }
    }

    private void startPlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady( true );
            mExoPlayer.getPlaybackState();
        }
    }

    @Override
    public void onDestroy() {
        if (mExoPlayer != null) {
            rootView.findViewById( R.id.playerView ).setVisibility( View.GONE );
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        pausePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        startPlayer();
    }
}
