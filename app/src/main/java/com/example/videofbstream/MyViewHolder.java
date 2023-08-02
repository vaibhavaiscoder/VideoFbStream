package com.example.videofbstream;

import android.app.Application;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;
import java.util.Collections;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView vTitleView;
    PlayerView playerView;
    ExoPlayer exoPlayer;

    public MyViewHolder(@NonNull View itemView) {

        super(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mClickListener.onItemClick(v, getAdapterPosition());

            }
        });
//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mClickListener.onItemLongClick(v, getAdapterPosition());
//                return false;
//            }
//        });

        vTitleView = itemView.findViewById(R.id.vtitle);
        playerView = itemView.findViewById(R.id.exoplayerview);
    }

    public void setVideo(Application application,String videoTitle, String videoUrl) {
        try {
            exoPlayer = new ExoPlayer.Builder(application).build();
            Uri videoURI = Uri.parse(videoUrl);
            MediaItem mediaItem = MediaItem.fromUri(videoURI);
            playerView.setPlayer(exoPlayer);
            vTitleView.setText(videoTitle);
            exoPlayer.addMediaItems(Collections.singletonList(mediaItem));
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(false);

        } catch (Exception e) {
            Toast.makeText(application, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private MyViewHolder.ClickListener mClickListener;
    public interface ClickListener{
        void onItemClick(View view, int position);
//        void onItemLongClick(View view , int position);
    }
    public void setOnClickListener(MyViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}