package com.example.guilherme.whatsapp.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.example.guilherme.whatsapp.model.Message;

import org.w3c.dom.Text;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    private List<Message> messages;
    private Context context;
    private static final int TYPE_SENDER = 0;
    private static final int TYPE_RECIPIENT = 1;

    public MessageAdapter(List<Message> list, Context c) {
        this.messages = list;
        this.context = c;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View item = null;

        if ( i == TYPE_SENDER) {

           item  = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.message_adapter_sender, viewGroup, false);

        }else if ( i == TYPE_RECIPIENT ){
            item = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.message_adapter_recipient, viewGroup, false);
        }


        return new MyViewHolder( item ) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Message message = messages.get( i );
        String msg = message.getMessage(),
               image = message.getImage(),
               name = message.getName();

        if (image != null) {
            Uri url = Uri.parse( image );
            Glide.with( context ).load(url).into( myViewHolder.image );

            if ( !name.isEmpty() ){
                myViewHolder.name.setText( name );
            }else {
                myViewHolder.name.setVisibility(View.GONE);
            }
            myViewHolder.msg.setVisibility(View.GONE);
        }else {
            myViewHolder.msg.setText( msg );
            if ( !name.isEmpty() ){
                myViewHolder.name.setText( name );
            }else {
                myViewHolder.name.setVisibility(View.GONE);
            }
            myViewHolder.image.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {

        Message msg = messages.get( position );

        String idUser = FirebaseUser.getIdUser();

        if (idUser.equals(msg.getIdUser()))        return TYPE_SENDER;

        return TYPE_RECIPIENT;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView msg , name;
        ImageView image;

        public MyViewHolder( View itemView) {
            super(itemView);

            msg = itemView.findViewById(R.id.textMessageString);
            name = itemView.findViewById(R.id.textExibitionName);
            image = itemView.findViewById(R.id.imageMessagePhoto);
        }
    }
}
