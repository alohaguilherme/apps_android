package com.example.guilherme.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.activity.ChatActivity;
import com.example.guilherme.whatsapp.adapter.ContactsAdapter;
import com.example.guilherme.whatsapp.adapter.ConversationsAdapter;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.example.guilherme.whatsapp.helper.RecyclerItemClickListener;
import com.example.guilherme.whatsapp.model.Talk;
import com.example.guilherme.whatsapp.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ConversationsAdapter adapter;
    private ArrayList<Talk> conversationList = new ArrayList<>();
    private DatabaseReference databaseRef;
    private DatabaseReference converRef;
    private ChildEventListener childEventListenerConversation;

    public ChatsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recyclerConversation);

        adapter = new ConversationsAdapter(conversationList, getActivity());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adapter );

        recyclerView.addOnItemTouchListener( new RecyclerItemClickListener(
                getActivity(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        List<Talk> listUpdatedConversation = adapter.getConversantion();
                        Talk selectedConversion = listUpdatedConversation.get( position );


                        if (selectedConversion.getIsGroup().equals("true")) {

                            Intent i =   new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatGroup", selectedConversion.getGroup());
                            startActivity( i );

                        }else {

                            Intent i =   new Intent(getActivity(), ChatActivity.class);
                            i.putExtra("chatContact", selectedConversion.getUser());
                            startActivity( i );

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));

        String idUser = FirebaseUser.getIdUser();
        databaseRef = SettingsFirebase.getFirebaseDatabase();
        converRef = databaseRef.child("conversations").child( idUser );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recoverConversation();
    }

    @Override
    public void onStop() {
        super.onStop();
        converRef.removeEventListener(childEventListenerConversation);
    }

    public void searchConversation( String texto ){
        List<Talk> listSerchConversation = new ArrayList<>();

        for (Talk talk : conversationList) {
            if (talk.getUser() != null){
                String name = talk.getUser().getName().toLowerCase();
                String lastMessage = talk.getLastMessage().toLowerCase();

                if (name.contains(texto) || lastMessage.contains(texto)) {
                    listSerchConversation.add(talk);
                }
            }else {
                String name = talk.getGroup().getName().toLowerCase();
                String lastMessage = talk.getLastMessage().toLowerCase();

                if (name.contains(texto) || lastMessage.contains(texto)) {
                    listSerchConversation.add(talk);
                }
            }
        }

        adapter = new ConversationsAdapter( listSerchConversation, getActivity());
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();
    }

    public void reloadConversations(){

        adapter = new ConversationsAdapter( conversationList, getActivity());
        recyclerView.setAdapter( adapter );
        adapter.notifyDataSetChanged();

    }

    public void recoverConversation(){

        conversationList.clear();

        childEventListenerConversation = converRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Talk talk = dataSnapshot.getValue( Talk.class );
                conversationList.add( talk );
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
