package com.example.guilherme.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    private TextView tvCadastro;
    private TextInputEditText editMailL, editPasswordL;
    private Button buttonLogon;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = SettingsFirebase.getFirebaseAuth();
        editMailL = findViewById(R.id.editMailLogin);
        editPasswordL = findViewById(R.id.editPasswordLogin);
        buttonLogon = findViewById(R.id.buttonLogon);
        tvCadastro = findViewById(R.id.tvCadastro);
        buttonLogon.setOnClickListener(this);
        tvCadastro.setOnClickListener(this);
    }

    public void singIn(){
        startActivity( new Intent(this, MainActivity.class));
    }

    public void openCad(){
        startActivity( new Intent(this, CadastroActivity.class));
    }

    public void validLogin(User user){
        auth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){
                    singIn();
                }else {

                    String exception = "";

                    try{
                        throw  task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        exception = "Usuário não cadastrado";
                        Log.i("ERROR : ",e.getMessage());
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "E-mail ou senha inválido";
                        Log.i("ERROR : ",e.getMessage());
                    } catch ( Exception e) {
                        exception = "Erro ao fazer login:" +e.getMessage();
                        e.printStackTrace();
                        Log.i("ERROR : ",e.getMessage());
                    }
                    Toast.makeText(LoginActivity.this
                            ,exception
                            ,Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void validDate(){
        String textMail = "";
        String textPassword = "";

        Log.i("INFO:", textMail + " " + textPassword);

        textMail = editMailL.getText().toString();
        textPassword = editPasswordL.getText().toString();

        Log.i("INFO:", textMail + " " + textPassword);

        if( !textMail.isEmpty() ){
            if ( !textPassword.isEmpty() ){
                User user = new User();
                user.setEmail( textMail );
                user.setPassword( textPassword );
                validLogin( user );
            }else {
                Toast.makeText(LoginActivity.this, "Informe a senha", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(LoginActivity.this, "Informe o e-mail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser userNow = auth.getCurrentUser();
        if ( userNow != null){
            singIn();
        }
    }

    public void onClick(View view){
        switch ( view.getId() ){

            case (R.id.tvCadastro):
                openCad();
                break;
            case (R.id.buttonLogon):
                validDate();
                break;
        }
    }
}
