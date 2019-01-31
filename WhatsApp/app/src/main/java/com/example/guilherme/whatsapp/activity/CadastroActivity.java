package com.example.guilherme.whatsapp.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.helper.Base64Custom;
import com.example.guilherme.whatsapp.helper.FirebaseUser;
import com.example.guilherme.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText editName, editMail, editPassword;
    private Button buttonCadastrar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        editName = findViewById(R.id.editName);
        editMail = findViewById(R.id.editMail);
        editPassword = findViewById(R.id.editPassword);
        buttonCadastrar = findViewById(R.id.buttonCadastrar);

        buttonCadastrar.setOnClickListener(this);
    }

    public void registerUser(final User user){
        auth = SettingsFirebase.getFirebaseAuth();

        auth.createUserWithEmailAndPassword(
            user.getEmail(), user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ) {
                    try {

                        String idUser = Base64Custom.encodeBase64( user.getEmail() );
                        user.setIdUser( idUser );
                        user.saveUser();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this
                            ,"Usuário cadastrado"
                            ,Toast.LENGTH_SHORT);
                    FirebaseUser.updateUserName( user.getName() );
                    finish();
                }else {
                    String exception = "";

                   try{
                       throw  task.getException();
                   }catch ( FirebaseAuthWeakPasswordException e){
                       exception = "Digite uma senha mais forte!";
                   }catch ( FirebaseAuthInvalidCredentialsException e ){
                       exception = "Por favor, digite um e-mail válido";
                   }catch ( FirebaseAuthUserCollisionException e ){
                       exception = "Esta conta já foi cadastrada";
                   }catch (Exception e){
                       exception = "Erro ao cadastrar usuário: "+ e.getMessage();
                       e.printStackTrace();
                   }

                   Toast.makeText(CadastroActivity.this,
                           exception,
                           Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void validateUser(){
        String textName = editName.getText().toString();
        String textMail = editMail.getText().toString();
        String textPassword = editPassword.getText().toString();

        if (!textName.isEmpty()){
            if (!textMail.isEmpty()){
                if (!textPassword.isEmpty()){

                    User user = new User();
                    user.setName( textName );
                    user.setEmail( textMail );
                    user.setPassword( textPassword );
                    registerUser( user );

                }else {
                    Toast.makeText(this, "Preencha a senha", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Preencha o e-mail", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Preencha o none", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view){
        validateUser();
    }
}
