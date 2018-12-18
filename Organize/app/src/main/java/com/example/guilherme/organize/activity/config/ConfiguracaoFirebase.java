package com.example.guilherme.organize.activity.config;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth auth;
    private static DatabaseReference firebase;

    //Retorna instancia
    public static FirebaseAuth getFirebaseAuth(){

        if (auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    //Retorna instancia do firebasedatabase
    public static DatabaseReference getFirebaseDataBase(){

        if (firebase == null) {
            firebase = FirebaseDatabase.getInstance().getReference();
        }
        return firebase;
    }

}
