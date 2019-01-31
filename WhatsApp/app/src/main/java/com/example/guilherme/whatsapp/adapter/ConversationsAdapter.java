package com.example.guilherme.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.model.Talk;
import com.example.guilherme.whatsapp.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.MyViewHolder>{

    private List<Talk> listTalks;
    private Context context;

    public ConversationsAdapter(List<Talk> talks, Context c) {
        this.listTalks = talks;
        this.context = c;
    }


    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View listItem = LayoutInflater.from( viewGroup.getContext() ).inflate(R.layout.contact_adapter, viewGroup, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder( MyViewHolder myViewHolder, int i) {

        Talk conversation = listTalks.get( i );


        myViewHolder.lastMessage.setText( conversation.getLastMessage() );

        User user = conversation.getUser();

        myViewHolder.name.setText( user.getName() );

        if (user.getPhoto() != null ) {
            Uri uri = Uri.parse( user.getPhoto() );
            Glide.with( context ).load( uri ).into( myViewHolder.photo);

        }else  {
            myViewHolder.photo.setImageResource( R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return listTalks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView photo;
        TextView name, lastMessage;

        public MyViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.imageViewContactPhoto);
            name  = itemView.findViewById(R.id.textContactName);
            lastMessage  = itemView.findViewById(R.id.textContactEmail);

        }
    }
}
