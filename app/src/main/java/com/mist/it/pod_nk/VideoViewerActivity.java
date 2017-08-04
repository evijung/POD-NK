package com.mist.it.pod_nk;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoViewerActivity extends AppCompatActivity {

    private String TAG;
    private int stopPosition;
    @BindView(R.id.videoView)
    VideoView videoView;
    MediaPlayer mp;
    private MediaController mediaController;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_viewer);
        ButterKnife.bind(this);

        // Set the media controller buttons
        if (mediaController == null) {
            mediaController = new MediaController(VideoViewerActivity.this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);


            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);
        }

        Uri videoLink = Uri.parse(MyConstant.urlVideo);
        videoView.setVideoURI(videoLink);

        //set size screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoView.getLayoutParams();
        params.width = (int) (960*metrics.density);
        params.height = (int) (560*metrics.density);
        params.leftMargin = 0;
        videoView.setLayoutParams(params);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                videoView.start();
                mediaController.setAnchorView(videoView);
            }


        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            mp.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mp != null) {
            mp.start();
        }
    }
}