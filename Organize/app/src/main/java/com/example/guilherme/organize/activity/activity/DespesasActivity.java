package com.example.guilherme.organize.activity.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guilherme.organize.R;
import com.example.guilherme.organize.activity.config.ConfiguracaoFirebase;
import com.example.guilherme.organize.activity.helper.Base64Custom;
import com.example.guilherme.organize.activity.helper.DateCustom;
import com.example.guilherme.organize.activity.model.Movimentacao;
import com.example.guilherme.organize.activity.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DespesasActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private FloatingActionButton fabSalvar;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);
        fabSalvar = findViewById(R.id.fabSalvar);

        //preenche o campo data com a data atual
        campoData.setText( DateCustom.dateNow() );
        getDespesaTotal();
        fabSalvar.setOnClickListener(this);
   }


    @Override
    public void onClick(View view){
        if ( validDataDespesa() ) {
            movimentacao = new Movimentacao();
            String date = campoData.getText().toString();
            Double valueRecuperado = Double.parseDouble(campoValor.getText().toString());
            movimentacao.setValor(valueRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(date);
            movimentacao.setTipo("D");

            Double despesaAtualizada = despesaTotal + valueRecuperado;
            updateDespesa( despesaAtualizada );

            movimentacao.save(date);
            finish();
        }

    }

    public Boolean validDataDespesa(){

        String textDate =  campoData.getText().toString();
        String textValue = campoValor.getText().toString();
        String textCategory = campoCategoria.getText().toString();
        String textDescription = campoDescricao.getText().toString();

        if ( !textDescription.isEmpty() ){
            if ( !textCategory.isEmpty() ){
                if ( !textDate.isEmpty() ){
                    if ( !textValue.isEmpty() ){
                        return true;
                    }else {
                        Toast.makeText(this, "Valor nã foi preenchido!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }else {
                    Toast.makeText(this, "Data nã foi preenchido!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else {
                Toast.makeText(this, "Categoria nã foi preenchido!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else {
            Toast.makeText(this, "Descricão nã foi preenchida!", Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public void updateDespesa(Double despesa){
        String emailUsuario  = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.encodeBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child( idUsuario );
        usuarioRef.child("despesaTotal").setValue( despesa );
    }

    public void getDespesaTotal(){
        String emailUsuario  = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.encodeBase64( emailUsuario );
        DatabaseReference usuarioRef = firebaseref.child("usuarios").child( idUsuario );

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue( Usuario.class );
                despesaTotal = usuario.getDespesaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
