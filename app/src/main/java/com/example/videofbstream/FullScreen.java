package com.example.videofbstream;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

import java.util.Collections;

import javax.sql.DataSource;

public class FullScreen extends AppCompatActivity {

    private ExoPlayer exoPlayer;
    private PlayerView playerView;
    TextView full_screen_title;
    private String url, title;
    private boolean playwhenready = false;
    private int currentWindow = 0;
    private long playbackposition = 0;


    boolean fullscreen = false;
    ImageView fullscreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Fullscreen");
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        full_screen_title = findViewById(R.id.tv_fullscreen);
        playerView = findViewById(R.id.exo_fullscreen);

        fullscreenButton = playerView.findViewById(R.id.exoplayer_fullscreen_icon);

        Intent intent = getIntent();
        title = intent.getExtras().getString("tat");
        url = intent.getExtras().getString("ur");

        full_screen_title.setText(title);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreen.this, R.drawable.ic_fullscreen));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) (200 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                    full_screen_title.setVisibility(View.VISIBLE);
                } else {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(FullScreen.this, R.drawable.ic_fullscreen_exit));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                    full_screen_title.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void initializePlayer() {
        exoPlayer = new ExoPlayer.Builder(this).build();
        Uri videoURI = Uri.parse(url);
        MediaItem mediaItem = MediaItem.fromUri(videoURI);
        playerView.setPlayer(exoPlayer);
        full_screen_title.setText(title);
        exoPlayer.addMediaItems(Collections.singletonList(mediaItem));
        exoPlayer.seekTo(currentWindow, playbackposition);
        exoPlayer.prepare();
        exoPlayer.setPlayWhenReady(playwhenready);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT >= 24) {
            initializePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT >= 24 || exoPlayer == null) {
//            initializePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT > 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playwhenready = exoPlayer.getPlayWhenReady();
            playbackposition = exoPlayer.getCurrentPosition();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            exoPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exoPlayer.stop();
        releasePlayer();

        final Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }
}