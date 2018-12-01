package com.example.gteod.cardview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.gteod.cardview.R;
import com.example.gteod.cardview.adapter.PostagemAdaptter;
import com.example.gteod.cardview.model.Postagem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerPostagem;
    private List<Postagem> postagens = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerPostagem = findViewById(R.id.recyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerPostagem.setLayoutManager(layoutManager);

        //Define adapter
        this.prepararPostagem();
        PostagemAdaptter adapter = new PostagemAdaptter(postagens);
        recyclerPostagem.setAdapter( adapter );
    }

    public void prepararPostagem(){

        Postagem p = new Postagem("Guilherme Santos", "Viagem top!!!", R.drawable.imagem1);
        this.postagens.add( p );

        p = new Postagem("José", "", R.drawable.imagem2);
        this.postagens.add( p );

        p = new Postagem("João", "Viagem top!!!", R.drawable.imagem3);
        this.postagens.add( p );

        p = new Postagem("Maria", "Que Dia!!", R.drawable.imagem4);
        this.postagens.add( p );


    }
}
