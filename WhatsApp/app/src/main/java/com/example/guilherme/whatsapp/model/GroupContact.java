package com.example.guilherme.whatsapp.model;

import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class GroupContact implements Serializable {

    private String id;
    private String name;
    private String photo;
    private List<User> members;

    public GroupContact() {

        DatabaseReference database = SettingsFirebase.getFirebaseDatabase();
        DatabaseReference groupref = database.child("groups");

        String idGroupFirebase = groupref.push().getKey();
        setId( idGroupFirebase  );

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
