package com.example.guilhermesantos.alcoolgasolina;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText alcool;
    private EditText gasolina;
    private TextView resultado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alcool = findViewById(R.id.etAlcool);
        gasolina = findViewById(R.id.etGasol);
        resultado = findViewById(R.id.tvResultado);
    }

    public void btVerificar(View view){
        Double gasolinavalor = Double.parseDouble(gasolina.getText().toString());
        Double alcoolvalor = Double.parseDouble(alcool.getText().toString());
        gasolinavalor = (gasolinavalor * 0.7);

        if (gasolinavalor > alcoolvalor) {
            resultado.setText("Usar Álcool");
        } else {
            resultado.setText("Usar Gasolina");
        }


    }

}
