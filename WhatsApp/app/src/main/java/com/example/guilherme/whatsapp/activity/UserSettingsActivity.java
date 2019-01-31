package com.example.guilherme.whatsapp.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.Base64Custom;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.example.guilherme.whatsapp.helper.Permissions;
import com.example.guilherme.whatsapp.model.GroupContact;
import com.example.guilherme.whatsapp.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera;
    private static final int SELECTION_CAMERA = 100;
    private static final int SELECTION_GALLERY = 200;
    private EditText editNameUserProfile;
    private ImageView imageNameNew;
    private CircleImageView imageProfile;
    private StorageReference storageReference;
    private String idUser;
    private User userLogON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        storageReference = SettingsFirebase.getFirebaseStorage();
        idUser = FirebaseUser.getIdUser();

        Permissions.validPermissions(permissions, this, 1);

        imageButtonCamera = findViewById(R.id.imageButtonCamGal);
        imageProfile = findViewById(R.id.photo_user_profile);
        editNameUserProfile = findViewById(R.id.editNameUserProfile);
        imageNameNew = findViewById(R.id.imageNameNew);


        userLogON = FirebaseUser.getDataLogInUser();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recover datauser
        com.google.firebase.auth.FirebaseUser user = FirebaseUser.getCurrentUser();
        Uri url = user.getPhotoUrl();

        if ( url != null) {
            Glide.with(UserSettingsActivity.this)
                    .load(url)
                    .into( imageProfile );
        }else {
            imageProfile.setImageResource(R.drawable.padrao);
        }

        editNameUserProfile.setText(user.getDisplayName());
        imageButtonCamera.setOnClickListener(this);
        imageNameNew.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK){
            Bitmap image = null;

            try {
                switch ( requestCode ){
                    case SELECTION_CAMERA:
                        image = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECTION_GALLERY:
                        Uri imageSelectedLocation = data.getData();
                        image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageSelectedLocation);
                        break;
                }

                if (image != null ){
                    imageProfile.setImageBitmap( image );

                    //Recover date image firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    byte[] dataImage = baos.toByteArray();

                    //gravar no firebase
                    final StorageReference imageRef = storageReference
                            .child("imagens")
                            .child("profile")
                            .child(idUser)
                            .child("profile.jpeg");

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
                                updateUserPhoto( url );

                            }
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int resultPermissions : grantResults) {
            if (resultPermissions == PackageManager.PERMISSION_DENIED) {
                validPermissionAlert();
            }
        }
    }


    private void validPermissionAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissõs Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validOption() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Escolha");
        builder.setMessage("Você deseja usar a câmera ou a galeria?");
        builder.setPositiveButton("Câmera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECTION_CAMERA);
                }
            }
        });
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (i.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(i, SELECTION_GALLERY);
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void updateNameUser(){
        String name = editNameUserProfile.getText().toString();
        boolean returnf = FirebaseUser.updateUserName( name );
        if (returnf ) {

            userLogON.setName( name );
            userLogON.updateUser();

            Toast.makeText(UserSettingsActivity.this,
                    "Nome alterado com sucesso",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUserPhoto(Uri url){
        boolean returnf = FirebaseUser.updateUserPhoto( url );
        if (returnf) {
            userLogON.setPhoto(url.toString());
            userLogON.updateUser();
            Toast.makeText(UserSettingsActivity.this,
                    "Sua foto foi atualizada",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageButtonCamGal:
                validOption();
                break;
            case R.id.imageNameNew:
                    updateNameUser();
                break;
        }
    }
}
