package com.example.guilherme.whatsapp.model;

import android.provider.Telephony;

import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.Base64Custom;
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

    public void save(){
        DatabaseReference database = SettingsFirebase.getFirebaseDatabase();
        DatabaseReference groupref = database.child("groups");

        groupref.child( getId() ).setValue( this );

        //salvar cinversa
        for ( User member: getMembers() ) {

            String idSender = Base64Custom.encodeBase64( member.getEmail() );
            String idRecipient = getId();

            Talk talk = new Talk();

            talk.setIdSender( idSender );
            talk.setIdRecipient( idRecipient );
            talk.setLastMessage("");
            talk.setIsGroup("true");
            talk.setGroup( this );

            talk.save();
        }


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
