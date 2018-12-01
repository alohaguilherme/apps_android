package com.example.gteod.calculadoragorjeta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    private EditText etValor;
    private TextView tvPorcentagem , tvGorjeta , tvTotal;
    private SeekBar sbGorjeta;

    private double porcentagem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etValor = findViewById(R.id.edValor);
        tvPorcentagem = findViewById(R.id.tvPorcentagem);
        tvGorjeta = findViewById(R.id.tvGorjeta);
        tvTotal = findViewById(R.id.tvTotal);
        sbGorjeta = findViewById(R.id.sbGorjeta);

        sbGorjeta.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                porcentagem = sbGorjeta.getProgress();
                tvPorcentagem.setText( Math.round(porcentagem)+"%" );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                System.out.print("teste");
                calcular();
            }
        });
    }

    public void calcular(){

        if (etValor == null || etValor.equals("")){
            Toast.makeText(getApplicationContext(),"Informe um valor para calcular a gorjeta!",Toast.LENGTH_LONG);
        } else {
            double valorDigitado = Double.parseDouble(etValor.getText().toString());
            double gorjeta = valorDigitado * (porcentagem / 100);
            double total = gorjeta + valorDigitado;
            tvGorjeta.setText("R$ " + Math.round(gorjeta));
            tvTotal.setText("R$ " + total);
        }
    }
}
