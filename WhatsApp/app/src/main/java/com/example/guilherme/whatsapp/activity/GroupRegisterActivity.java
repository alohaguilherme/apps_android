package com.example.guilherme.whatsapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.adapter.SelectedGroupAdapter;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.example.guilherme.whatsapp.model.GroupContact;
import com.example.guilherme.whatsapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GroupRegisterActivity extends AppCompatActivity {

    private ArrayList<User> userSelectedList = new ArrayList<>();
    private SelectedGroupAdapter selectedGroupAdapter;
    private TextView totalParticipantes;
    private RecyclerView recyclerSelectedMembers;
    private StorageReference storageReference;
    private ImageView imageGroup;
    private String idUser;
    private User userLogON;
    private GroupContact group;
    private static final int SELECTION_GALLERY = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);

        idUser = FirebaseUser.getIdUser();

        totalParticipantes = findViewById(R.id.textTotalParticipantes);
        recyclerSelectedMembers = findViewById(R.id.recyclerGroupMembers);
        imageGroup = findViewById(R.id.circlePhotoGrupo);
        storageReference = SettingsFirebase.getFirebaseStorage();
        group = new GroupContact();

        imageGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECTION_GALLERY);
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recover List Members
        if ( getIntent().getExtras() != null ){
            List<User> members = (List<User>) getIntent().getExtras().getSerializable("members");
            userSelectedList.addAll( members );

            totalParticipantes.setText( "Participantes: " + userSelectedList.size());

        }

        //Recycler view
        selectedGroupAdapter = new SelectedGroupAdapter(userSelectedList, getApplicationContext());

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        recyclerSelectedMembers.setLayoutManager( layoutManagerHorizontal );
        recyclerSelectedMembers.setHasFixedSize( true );
        recyclerSelectedMembers.setAdapter( selectedGroupAdapter );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap image = null;
        Uri imageSelectedLocation = data.getData();

        try {
            image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageSelectedLocation);
            Log.i("IMAGEM:" , String.valueOf(image));
            if (image != null ){
                imageGroup.setImageBitmap( image );

                //Recover date image firebase
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                byte[] dataImage = baos.toByteArray();

                //gravar no firebase
                Log.i("IMAGEM:" , "Iniciou");
                final StorageReference imageRef = storageReference
                        .child("imagens")
                        .child("group")
                        .child(group.getId() + ".jpeg");

                Log.i("IMAGEM:" , "Passou");

                UploadTask uploadTask = imageRef.putBytes( dataImage );
                Log.i("IMAGEM:" , String.valueOf( uploadTask ));
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        Log.i("DADOS:" , String.valueOf(task.isSuccessful()));
                        if (!task.isSuccessful() ){
                            throw task.getException();
                        }
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Log.i("DADOS:" , String.valueOf(task.isSuccessful()));
                        if (task.isSuccessful()) {
                           String url = task.getResult().toString();
                            Log.i("DADOS:" , url);
                           group.setPhoto( url );

                        }
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
