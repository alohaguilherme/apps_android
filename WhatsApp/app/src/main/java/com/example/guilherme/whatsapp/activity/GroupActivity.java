package com.example.guilherme.whatsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.adapter.ContactsAdapter;
import com.example.guilherme.whatsapp.adapter.SelectedGroupAdapter;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.RecyclerItemClickListener;
import com.example.guilherme.whatsapp.model.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GroupActivity extends AppCompatActivity {

    private RecyclerView recyclerSelectedMembers, recyclerMembers;
    private ContactsAdapter contactsAdapter;
    private SelectedGroupAdapter selectedGroupAdapter;
    private ArrayList<User> userList = new ArrayList<>();
    private ArrayList<User> userSelectedList = new ArrayList<>();
    private DatabaseReference membersRef;
    private com.google.firebase.auth.FirebaseUser currentUser;
    private ValueEventListener valueEventListenerMembers;
    private FloatingActionButton fabGoGroupReg;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        setSupportActionBar(toolbar);


        membersRef = SettingsFirebase.getFirebaseDatabase().child("usuarios");
        currentUser = com.example.guilherme.whatsapp.helper.FirebaseUser.getCurrentUser();

        recyclerMembers = findViewById(R.id.recyclerMembers);
        recyclerSelectedMembers = findViewById(R.id.recyclerSelectdsMembers);

        //Configura Adapter
        contactsAdapter = new ContactsAdapter( userList, getApplicationContext());

        //Configura RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerMembers.setLayoutManager( layoutManager );
        recyclerMembers.setHasFixedSize( true );
        recyclerMembers.setAdapter( contactsAdapter );

        recyclerMembers.addOnItemTouchListener(
                new RecyclerItemClickListener(
                    getApplicationContext(),
                    recyclerMembers,
                     new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            User selectedUser = userList.get( position );


                            //remove selecionado da lista
                            userList.remove( selectedUser );
                            contactsAdapter.notifyDataSetChanged();

                            //adiciona user list
                            userSelectedList.add( selectedUser );
                            selectedGroupAdapter.notifyDataSetChanged();

                            updateMembersToolbar();

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


        //recycler view dos selecionados
        selectedGroupAdapter = new SelectedGroupAdapter(userSelectedList, getApplicationContext());

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        recyclerSelectedMembers.setLayoutManager( layoutManagerHorizontal );
        recyclerSelectedMembers.setHasFixedSize( true );
        recyclerSelectedMembers.setAdapter( selectedGroupAdapter );

        recyclerSelectedMembers.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerSelectedMembers,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                User selectedUser = userSelectedList.get( position );
                                //remove selecionado da lista
                                userSelectedList.remove( selectedUser );
                                selectedGroupAdapter.notifyDataSetChanged();

                                //adiciona user list
                                userList.add( selectedUser );
                                contactsAdapter.notifyDataSetChanged();

                                updateMembersToolbar();


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

        fabGoGroupReg = findViewById(R.id.fabGoGroupReg);

        fabGoGroupReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GroupActivity.this, GroupRegisterActivity.class);
                i.putExtra("members", userSelectedList);
                startActivity( i );
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        contactRecover();
    }

    @Override
    protected void onStop() {
        super.onStop();
        membersRef.removeEventListener(valueEventListenerMembers);
    }

    public void updateMembersToolbar(){
        int totalSelected = userSelectedList.size();
        int total = userList.size() + totalSelected;
        toolbar.setSubtitle( totalSelected +" de "+ total + " selecionados");
    }

    public void contactRecover(){

        valueEventListenerMembers = membersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {

                for (DataSnapshot dates: dataSnapshot.getChildren() ){

                    User user = dates.getValue( User.class );
                    String mailCurrentUser =  currentUser.getEmail();

                    if ( !mailCurrentUser.equals(user.getEmail()) ) {
                        userList.add(user);
                    }

                }

                contactsAdapter.notifyDataSetChanged();
                updateMembersToolbar();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
