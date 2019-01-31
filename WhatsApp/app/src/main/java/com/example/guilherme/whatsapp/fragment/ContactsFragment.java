package com.example.guilherme.whatsapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.activity.ChatActivity;
import com.example.guilherme.whatsapp.activity.GroupActivity;
import com.example.guilherme.whatsapp.adapter.ContactsAdapter;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.example.guilherme.whatsapp.helper.RecyclerItemClickListener;
import com.example.guilherme.whatsapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private ArrayList<User> contactList = new ArrayList<>();
    private DatabaseReference userRef;
    private ValueEventListener valueEventListenerContact;
    private com.google.firebase.auth.FirebaseUser currentUser;

    public ContactsFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);

        recyclerView = view.findViewById(R.id.recyclerContact);
        userRef = SettingsFirebase.getFirebaseDatabase().child("usuarios");
        currentUser = FirebaseUser.getCurrentUser();

        //adapter
        adapter = new ContactsAdapter(contactList, getActivity());



        //Layout  setting
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize( true );
        recyclerView.setAdapter( adapter );


        recyclerView.addOnItemTouchListener(

                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                User selectedUser = contactList.get( position );
                                boolean hold = selectedUser.getEmail().isEmpty();

                                if( hold ){
                                    startActivity( new Intent(getActivity(), GroupActivity.class));
                                }else {

                                    Intent i = new Intent(getActivity(), ChatActivity.class);
                                    i.putExtra("chatContact", selectedUser);
                                    startActivity(i);
                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );



        return view;
    }

    public void addHeader(){
        //cria opção novo grupo
        User itemGroup = new User();
        itemGroup.setName("Novo Grupo");
        itemGroup.setEmail("");
        contactList.add( itemGroup );
    }
    @Override
    public void onStart() {
        super.onStart();
        contactRecover();
    }

    @Override
    public void onStop() {
        super.onStop();
        userRef.removeEventListener( valueEventListenerContact );
    }

    public void contactRecover(){

        valueEventListenerContact = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               contactList.clear();
               addHeader();
               adapter.notifyDataSetChanged();

                for (DataSnapshot dates: dataSnapshot.getChildren() ){

                    User user = dates.getValue( User.class );
                    String mailCurrentUser =  currentUser.getEmail();

                    if ( !mailCurrentUser.equals(user.getEmail()) ) {
                        contactList.add(user);
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
