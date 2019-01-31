package com.example.guilherme.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.adapter.MessageAdapter;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.Base64Custom;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.example.guilherme.whatsapp.model.Message;
import com.example.guilherme.whatsapp.model.Talk;
import com.example.guilherme.whatsapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewName;
    private CircleImageView imagePhotoUser;
    private User userRecipient;
    private EditText editMessage;
    private ImageView imageCam;
    private List<Message> messagesList = new ArrayList<>();
    private RecyclerView recyclerMessage;
    private MessageAdapter adapter;
    private DatabaseReference database;
    private DatabaseReference msgRef;
    private StorageReference storage;
    private ChildEventListener childEventListenerMsg;
    private static final int SELECTION_CAMERA = 100;

    private String idSenderUser, idRecipientUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewName = findViewById(R.id.textChatName);
        imagePhotoUser = findViewById(R.id.circleImagePhoto);
        editMessage = findViewById(R.id.editMessage);
        imageCam = findViewById(R.id.imageCam);

        recyclerMessage = findViewById(R.id.recyclerMessage);

        //Usuario remetente
        idSenderUser = FirebaseUser.getIdUser();

        Bundle bundle = getIntent().getExtras();

        if ( bundle != null) {

            userRecipient = (User) bundle.getSerializable( "chatContact");
            textViewName.setText( userRecipient.getName() );

            String photo = userRecipient.getPhoto();
            if ( photo != null ){

                Uri url = Uri.parse(userRecipient.getPhoto());
                Glide.with(ChatActivity.this).load(url).into( imagePhotoUser );

            }else {
                imagePhotoUser.setImageResource(R.drawable.padrao);
            }

            //Usuario destinatario
            idRecipientUser = Base64Custom.encodeBase64( userRecipient.getEmail() );
        }

        //Adapter
        adapter = new MessageAdapter(messagesList, getApplicationContext());


        //recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerMessage.setLayoutManager( layoutManager );
        recyclerMessage.setHasFixedSize( true );
        recyclerMessage.setAdapter( adapter );

        database = SettingsFirebase.getFirebaseDatabase();
        storage = SettingsFirebase.getFirebaseStorage();

        msgRef = database.child("messages")
                .child(idSenderUser)
                .child(idRecipientUser);

        imageCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECTION_CAMERA);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK) {
            Bitmap image = null;

            try{
                switch ( requestCode ){
                    case SELECTION_CAMERA:
                        image = (Bitmap) data.getExtras().get("data");
                        break;
                }
                if (image != null ){

                    //Recover date image firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] dataImage = baos.toByteArray();

                     final String nameimage = UUID.randomUUID().toString();
                    //gravar no firebase
                    final StorageReference imageRef = storage
                            .child("imagens")
                            .child("messages")
                            .child("photos")
                            .child(idSenderUser)
                            .child(nameimage);

                    UploadTask uploadTask = imageRef.putBytes( dataImage );
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful() ){
                                throw task.getException();
                            }
                            return imageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri url = task.getResult();
                                Message msg = new Message();
                                msg.setIdUser( idSenderUser );
                                msg.setMessage("imagem.jpeg");
                                msg.setImage(url.toString());
                                //remetente
                                saveMessage(idSenderUser, idRecipientUser, msg);

                                //destinatario
                                saveMessage(idRecipientUser, idSenderUser, msg);
                            }
                        }
                    });
                }
            }catch (Exception e){

            }
        }
    }

    public void submitMessage(View view){

        String textMessage = editMessage.getText().toString();

        if ( textMessage != null && !textMessage.equals("") ) {
            Message message = new Message();

            message.setIdUser(idSenderUser);
            message.setMessage(textMessage);

            //remetente
            saveMessage(idSenderUser, idRecipientUser, message);

            //detinatario
            saveMessage(idRecipientUser, idSenderUser, message);

            saveConversation(message);

            editMessage.setText("");

        }
    }

    private void saveConversation(Message msg) {
        Talk talkSender = new Talk();
        talkSender.setIdSender(idSenderUser);
        talkSender.setIdRecipient(idRecipientUser);
        talkSender.setLastMessage( msg.getMessage());
        talkSender.setUser( userRecipient );

        talkSender.save();
    }


    private void saveMessage(String idSender, String idRecipient,  Message msg){

        DatabaseReference database = SettingsFirebase.getFirebaseDatabase();
        DatabaseReference msgRef = database.child("messages");

        msgRef.child(idSender)
                .child(idRecipient)
                .push()
                .setValue(msg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recoverMessages();
    }

    @Override
    protected void onStop() {
        super.onStop();
        msgRef.removeEventListener( childEventListenerMsg );
    }

    private void recoverMessages(){
        childEventListenerMsg = msgRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue( Message.class );
                messagesList.add( message );
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
