package com.example.gteod.componentesbasicos;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText campoProduto;
    private TextView campoResultado;
    private CheckBox cbBranco, cbVerde, cbVermelho;
    List<String> check = new ArrayList<String>();
    private RadioGroup rgEstoque;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoProduto = (TextInputEditText) findViewById(R.id.etNomeProduto);
        campoResultado = (TextView) findViewById(R.id.tvResultado);
        cbBranco = findViewById(R.id.cbBranco);
        cbVerde = findViewById(R.id.cbVerde);
        cbVermelho = findViewById(R.id.cbVermelho);
        rgEstoque = findViewById(R.id.rgEstoque);

        verificaRadioButton();
    }

    public void verificaCheck(){

        if (cbBranco.isChecked()) check.add( cbBranco.getText().toString());
        if (cbVerde.isChecked()) check.add( cbVerde.getText().toString());
        if (cbVermelho.isChecked()) check.add( cbVermelho.getText().toString());

        campoResultado.setText(check.toString());

    }

    public void verificaRadioButton(){
        rgEstoque.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                if ((i == R.id.rbSim)) {
                    campoResultado.setText("Sim");
                } else {
                    campoResultado.setText("Não");
                }

            }
        });
    }

    public void btEnviar(View view) {


        //check.clear();
        /*String nomeProduto = campoProduto.getText().toString();
        campoResultado.setText(nomeProduto);*/
        //verificaCheck();

    }
}
