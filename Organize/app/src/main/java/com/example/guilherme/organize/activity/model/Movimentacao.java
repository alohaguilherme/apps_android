package com.example.guilherme.organize.activity.model;

import android.util.Log;
import android.widget.Toast;

import com.example.guilherme.organize.activity.activity.DespesasActivity;
import com.example.guilherme.organize.activity.config.ConfiguracaoFirebase;
import com.example.guilherme.organize.activity.helper.Base64Custom;
import com.example.guilherme.organize.activity.helper.DateCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data, categoria, descricao, tipo, key;
    private double valor;

    public Movimentacao() {
    }

    public void save(String dateSelect){

        FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
        String idUsuario = Base64Custom.encodeBase64(auth.getCurrentUser().getEmail());
        String dateFinal = DateCustom.mesAnoDateSelect( dateSelect );
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDataBase();
        firebase.child("movimentacao")
                .child(idUsuario)
                .child(dateFinal)
                .push()
                .setValue(this);
    }

    public String getData() {
        return data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
