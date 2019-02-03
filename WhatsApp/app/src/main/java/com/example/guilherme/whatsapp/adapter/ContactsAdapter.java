package com.example.guilherme.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.security.keystore.UserPresenceUnavailableException;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.model.User;
import com.google.firebase.storage.ControllableTask;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>{

    private List<User> contacts;
    private Context context;

    public ContactsAdapter(List<User> contactList, Context c) {
        this.contacts = contactList;
        this.context = c;
    }

    public List<User> getContacts(){
        return this.contacts;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from( viewGroup.getContext() ).inflate(R.layout.contact_adapter, viewGroup, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {

        User user = contacts.get( i );
        boolean hold = user.getEmail().isEmpty();

        myViewHolder.name.setText( user.getName() );
        myViewHolder.mail.setText( user.getEmail() );


        if (user.getPhoto() != null ) {
            Uri uri = Uri.parse( user.getPhoto() );
            Glide.with( context ).load( uri ).into( myViewHolder.photo);

        }else {
            if (hold) {
                myViewHolder.photo.setImageResource(R.drawable.icone_grupo);
                myViewHolder.mail.setVisibility(View.GONE);
            } else {
                myViewHolder.photo.setImageResource(R.drawable.padrao);
            }
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView photo;
        TextView name, mail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.imageViewContactPhoto);
            name  = itemView.findViewById(R.id.textContactName);
            mail  = itemView.findViewById(R.id.textContactEmail);

        }
    }
}
