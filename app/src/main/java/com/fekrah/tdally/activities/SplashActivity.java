package com.fekrah.tdally.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.widget.VideoView;

import com.fekrah.tdally.MainActivity;
import com.fekrah.tdally.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.splash_video)
    SurfaceView splashVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        VideoView videoview = (VideoView) findViewById(R.id.splash_video);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.splash);
        videoview.setVideoURI(uri);
        videoview.start();

        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });



    }
}
