package com.example.guilherme.whatsapp.model;

import android.provider.ContactsContract;

import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.google.firebase.database.DatabaseReference;

import java.security.acl.Group;

public class Talk {
    private String idSender, idRecipient, lastMessage;
    private User user;
    private String isGroup;
    private GroupContact group;

    public Talk() {
        this.setIsGroup("false");
    }

    public void save(){
        DatabaseReference database = SettingsFirebase.getFirebaseDatabase();
        DatabaseReference talkRef = database.child("conversations");
        talkRef.child( this.getIdSender() )
               .child( this.getIdRecipient() )
               .setValue( this );

    }

    public String getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(String isGroup) {
        this.isGroup = isGroup;
    }

    public GroupContact getGroup() {
        return group;
    }

    public void setGroup(GroupContact group) {
        this.group = group;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdRecipient() {
        return idRecipient;
    }

    public void setIdRecipient(String idRecipient) {
        this.idRecipient = idRecipient;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
