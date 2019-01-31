package com.example.guilherme.whatsapp.model;

import android.util.Log;

import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    private String name, email, password, idUser, photo;

    public User() {
    }

    public void saveUser(){
        DatabaseReference firebaseRef = SettingsFirebase.getFirebaseDatabase();
        DatabaseReference userReg = firebaseRef.child("usuarios").child( getIdUser() );

        userReg.setValue( this );

    }

    public void updateUser(){
        String idUser = FirebaseUser.getIdUser();
        DatabaseReference dataBase = SettingsFirebase.getFirebaseDatabase();
        DatabaseReference userRef = dataBase.child("usuarios").child(idUser);

        Map<String, Object>valueUsers = convertForMap();

        userRef.updateChildren( valueUsers );

    }

    @Exclude
    public Map<String, Object> convertForMap(){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", getEmail());
        userMap.put("name", getName());
        userMap.put("photo",getPhoto());

        return userMap;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Exclude
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
