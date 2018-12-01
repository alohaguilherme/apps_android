package com.example.gteod.componentesdeinterface2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private TextView resultado;
    private Switch sEstado;
    private ToggleButton tbEstado;
    private CheckBox cbEstado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sEstado = findViewById(R.id.sEstado);
        tbEstado = findViewById(R.id.tbEstado);
        cbEstado  = findViewById(R.id.cbEstado);
        resultado = findViewById(R.id.tvResultado);
    }

    public  void enviar(View view){
        if ( tbEstado.isChecked()) {
            resultado.setText("Toggle Ligado!");
        } else {
            resultado.setText("Toggle Desligado");
        }
    }
}
