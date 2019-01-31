package com.example.guilherme.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedGroupAdapter extends RecyclerView.Adapter<SelectedGroupAdapter.MyViewHolder> {

    private List<User> contacts;
    private Context context;

    public SelectedGroupAdapter(List<User> contactList, Context c) {
        this.contacts = contactList;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from( viewGroup.getContext() ).inflate(R.layout.adapter_selected_group, viewGroup, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        User user = contacts.get( i );

        myViewHolder.name.setText( user.getName() );
        //myViewHolder.mail.setText( user.getEmail() );


        if (user.getPhoto() != null ) {
            Uri uri = Uri.parse( user.getPhoto() );
            Glide.with( context ).load( uri ).into( myViewHolder.photo);

        }else {
            myViewHolder.photo.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView photo;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo_user_selected);
            name  = itemView.findViewById(R.id.textContactNameSelected);
            name  = itemView.findViewById(R.id.textContactNameSelected);

        }
    }
}
