package com.example.gteod.executandovideos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayerActivity extends AppCompatActivity {

    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        videoView = findViewById(R.id.videoView);

        //colocar em full scream
        View decorView = getWindow().getDecorView();

        int uiOpcoes = View.SYSTEM_UI_FLAG_FULLSCREEN ;

        decorView.setSystemUiVisibility( uiOpcoes );

        //esconder action bar
        getSupportActionBar().hide();

        //Executa video
        videoView.setMediaController( new MediaController(this));
        videoView.setVideoPath("android.resource://"+ getPackageName() + "/" + R.raw.video);
        videoView.start();
    }
}
