package com.example.gteod.aprendendoingles.Fragment;


import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;

import com.example.gteod.aprendendoingles.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumerosFragment extends Fragment implements  View.OnClickListener{

    private ImageButton imageButtonNum1,imageButtonNum2,imageButtonNum3,
                        imageButtonNum4,imageButtonNum5,imageButtonNum6;

    private MediaPlayer mediaPlayer;

    @Override
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.imageNum1:
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.one);
                playSong();
                break;
            case R.id.imageNum2:
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.two);
                playSong();
                break;
            case R.id.imageNum3:
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.three);
                playSong();
                break;
            case R.id.imageNum4:
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.four);
                playSong();
                break;
            case R.id.imageNum5:
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.five);
                playSong();
                break;
            case R.id.imageNum6:
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.six);
                playSong();
                break;
        }
    }

    public void playSong(){
        if (mediaPlayer != null){
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
        }
    }

    public NumerosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_numeros, container, false);

        imageButtonNum1 = view.findViewById(R.id.imageNum1);
        imageButtonNum2 = view.findViewById(R.id.imageNum2);
        imageButtonNum3 = view.findViewById(R.id.imageNum3);
        imageButtonNum4 = view.findViewById(R.id.imageNum4);
        imageButtonNum5 = view.findViewById(R.id.imageNum5);
        imageButtonNum6 = view.findViewById(R.id.imageNum6);

        imageButtonNum1.setOnClickListener(this);
        imageButtonNum2.setOnClickListener(this);
        imageButtonNum3.setOnClickListener(this);
        imageButtonNum4.setOnClickListener(this);
        imageButtonNum5.setOnClickListener(this);
        imageButtonNum6.setOnClickListener(this);

        return view;
    }

}
