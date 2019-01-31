package com.example.guilherme.whatsapp.helper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseUser {

    public static String getIdUser(){
        FirebaseAuth user = SettingsFirebase.getFirebaseAuth();
        String mail = user.getCurrentUser().getEmail();
        String idUser = Base64Custom.encodeBase64(mail);

        return idUser;
    }

    public static com.google.firebase.auth.FirebaseUser getCurrentUser(){
        FirebaseAuth currentUser = SettingsFirebase.getFirebaseAuth();
        return currentUser.getCurrentUser();
    }

    public static boolean updateUserPhoto(Uri url){

        try {
            com.google.firebase.auth.FirebaseUser user = getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri( url )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( !task.isSuccessful() ){
                        Log.d("PERFIL", "Erro ao atualizar foto de perfil");
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }

    public static boolean updateUserName(String name){

        try {
            com.google.firebase.auth.FirebaseUser user = getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName( name )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if ( !task.isSuccessful() ){
                        Log.d("PERFIL", "Erro ao atualizar nome de perfil");
                    }
                }
            });
        }catch (Exception e) {
            e.printStackTrace();
        }

        return true;

    }

    public static User getDataLogInUser(){

        com.google.firebase.auth.FirebaseUser fireBaseUser = getCurrentUser();

        User user = new User();
        user.setEmail( fireBaseUser.getEmail());
        user.setName(fireBaseUser.getDisplayName());
        if ( fireBaseUser.getPhotoUrl() == null){
            user.setPhoto("");
        }else {
            user.setPhoto(fireBaseUser.getPhotoUrl().toString());
        }

        return user;


    }
}
