package com.example.guilherme.whatsapp.config;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SettingsFirebase {

    private static DatabaseReference databaseReference;
    private static FirebaseAuth auth;
    private static StorageReference storage;

    //Retorna instacia do firebase database
    public static DatabaseReference getFirebaseDatabase(){
        if (databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }

    //Retorna a instancia do firebase auth
    public static FirebaseAuth getFirebaseAuth(){
        if ( auth == null ){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    public static StorageReference getFirebaseStorage(){
        if ( storage == null ){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}
