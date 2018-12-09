package com.example.gteod.aprendendoingles.Fragment;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.gteod.aprendendoingles.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BichosFragment extends Fragment implements View.OnClickListener{

    private ImageButton imageButtonCao,imageButtonGato,imageButtonOvelha,
                        imageButtonMacaco,imageButtonVaca,imageButtonLeao;

    private MediaPlayer mediaPlayer;

    public BichosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.imageCao :
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.dog);
                playSong();
                break;
            case R.id.imageGato :
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.cat);
                playSong();
                break;
            case R.id.imageLeao :
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.lion);
                playSong();
                break;
            case R.id.imageMacaco :
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.monkey);
                playSong();
                break;
            case R.id.imageOvelha :
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.sheep);
                playSong();
                break;
            case R.id.imageVaca :
                mediaPlayer = MediaPlayer.create(getActivity(),R.raw.cow);
                playSong();
                break;
        }
    }

    public void playSong(){
        if (mediaPlayer != null) {
            mediaPlayer.start();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                }
            });
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bichos, container, false);

        imageButtonCao = view.findViewById(R.id.imageCao);
        imageButtonGato = view.findViewById(R.id.imageGato);
        imageButtonLeao = view.findViewById(R.id.imageLeao);
        imageButtonMacaco = view.findViewById(R.id.imageMacaco);
        imageButtonOvelha = view.findViewById(R.id.imageOvelha);
        imageButtonVaca = view.findViewById(R.id.imageVaca);

        imageButtonCao.setOnClickListener(this);
        imageButtonGato.setOnClickListener(this);
        imageButtonLeao.setOnClickListener(this);
        imageButtonMacaco.setOnClickListener(this);
        imageButtonOvelha.setOnClickListener(this);
        imageButtonVaca.setOnClickListener(this);



        return view;
    }

}
