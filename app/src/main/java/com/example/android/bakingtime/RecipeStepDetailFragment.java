package com.example.android.bakingtime;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingtime.utils.RecipeObject;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeStepActivity}
 * in two-pane mode (on tablets) or a {@link RecipeStepDetailActivity}
 * on handsets.
 */
public class RecipeStepDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static String PLAYBACK_POS_EXTRA = "position";
    public static String PLAY_WHEN_READY_EXTRA = "play";
    public static String WINDOW_EXTRA = "window";

    private RecipeObject.Step mItem;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView playerView;
    private ImageView errorImage;
    private Dialog fullScreenDialog;
    private boolean exoplayerFullscreen = false;
    private long playbackPosition;
    private int playbackWindow;
    private boolean playbackPlaying;

    public RecipeStepDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        ((TextView)rootView.findViewById(R.id.recipe_detail)).setText(mItem.getDescription());
        playerView = rootView.findViewById(R.id.video_view);
        errorImage = rootView.findViewById(R.id.error_image);

        initializePlayer(savedInstanceState);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = getArguments().getParcelable(ARG_ITEM_ID);
            Toolbar appBarLayout = getActivity().findViewById(R.id.detail_toolbar);
            if(appBarLayout != null){
                appBarLayout.setTitle(mItem.getShortDescription());
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            closeFullscreenDialog();
        }

        else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            openFullscreenDialog();
        }
    }

    private void initializePlayer(Bundle savedInstanceState){
        Uri uri;

        if(!TextUtils.isEmpty(mItem.getVideoURL())){
            uri = Uri.parse(mItem.getVideoURL());
        }
        else {
            playerView.setVisibility(View.GONE);
            errorImage.setVisibility(View.VISIBLE);
            return;
        }

        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
        String userAgent = Util.getUserAgent(getContext(), getActivity().getPackageName());
        MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(
                getContext(), userAgent)).createMediaSource(uri);

        playerView.setPlayer(mExoPlayer);
        if(!TextUtils.isEmpty(mItem.getThumbnailURL())){
            Picasso.with(getContext()).load(mItem.getThumbnailURL()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    playerView.setDefaultArtwork(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }

        mExoPlayer.prepare(mediaSource);

        if(savedInstanceState == null){
            mExoPlayer.setPlayWhenReady(true);
            playbackPlaying = true;
        }
        else if(savedInstanceState.containsKey(PLAYBACK_POS_EXTRA)){
            mExoPlayer.seekTo(savedInstanceState.getInt(WINDOW_EXTRA),savedInstanceState.getLong(PLAYBACK_POS_EXTRA));
            mExoPlayer.setPlayWhenReady(savedInstanceState.getBoolean(PLAY_WHEN_READY_EXTRA));
        }

        initFullscreenDialog();
    }

    @Override
    public void onStop() {
        super.onStop();

        //Release Player
        if(mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mExoPlayer != null && playerView.getPlayer() != null){
            playbackPosition = mExoPlayer.getCurrentPosition();
            playbackWindow = mExoPlayer.getCurrentWindowIndex();
            playbackPlaying = mExoPlayer.getPlayWhenReady();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(PLAYBACK_POS_EXTRA, playbackPosition);
        outState.putInt(WINDOW_EXTRA, playbackWindow);
        outState.putBoolean(PLAY_WHEN_READY_EXTRA, playbackPlaying);
    }

    private void initFullscreenDialog(){
        fullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen){
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                if(exoplayerFullscreen){
                    closeFullscreenDialog();
                }
            }
        };
    }

    private void openFullscreenDialog(){
        ((ViewGroup)playerView.getParent()).removeView(playerView);
        fullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        exoplayerFullscreen = true;
        fullScreenDialog.show();
    }

    private void closeFullscreenDialog(){
        ((ViewGroup)playerView.getParent()).removeView(playerView);
        ((FrameLayout)getView().findViewById(R.id.main_media_frame)).addView(playerView);
        exoplayerFullscreen = false;
        fullScreenDialog.dismiss();
    }


}
