package com.example.gteod.mediaplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekVolume;
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teste);
        initSeekVolume();
    }

    private void initSeekVolume(){
        seekVolume = findViewById(R.id.seekVolume);

        //configurar audio mananger
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //recupera valor do volume
        int volMaximo = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int volAtual = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        //configurar o volume maximo da seekbar
        seekVolume.setMax( volMaximo );

        //configurar o progresso atual
        seekVolume.setProgress( volAtual );

        seekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    public void playAudio(View view){
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void pauseAudio(View view){
        setPauseMusic();
    }

    public void setPauseMusic(){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopAudio(View view){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.teste);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        setPauseMusic();
    }
}
