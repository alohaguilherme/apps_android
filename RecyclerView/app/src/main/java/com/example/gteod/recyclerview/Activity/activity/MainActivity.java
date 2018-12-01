package com.example.gteod.recyclerview.Activity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gteod.recyclerview.Activity.RecyclerItemClickListener;
import com.example.gteod.recyclerview.Activity.adapter.Adapter;
import com.example.gteod.recyclerview.Activity.model.Filme;
import com.example.gteod.recyclerview.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Filme> listFilm = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcDados);

        //list Films
        this.criarFilmes();

        //configurar adapter
        Adapter adapter = new Adapter( listFilm );

        //configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration( new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setAdapter( adapter);

        //evento de click
        recyclerView.addOnItemTouchListener(
            new RecyclerItemClickListener(
                    getApplicationContext(),
                    recyclerView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Filme filme = listFilm.get(position);
                            Toast.makeText(
                                    getApplicationContext(),
                                    filme.getTituloFilme(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            Filme filme = listFilm.get(position);
                            Toast.makeText(
                                    getApplicationContext(),
                                    filme.getTituloFilme(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    }
            )
        );
    }

    public void criarFilmes(){
       Filme filme = new Filme("2018","Ficção","Homem Aranha de Volta ao Lar");
       this.listFilm.add(filme);

        filme = new Filme("2018","Aventura","Homem de Aço");
        this.listFilm.add(filme);

        filme = new Filme("2018","Comédia","Gente Grande");
        this.listFilm.add(filme);

        filme = new Filme("2018","Romance","Cartas para Guerra");
        this.listFilm.add(filme);

        filme = new Filme("2018","Aventura","Tomb Raider");
        this.listFilm.add(filme);

        filme = new Filme("2018","Ficção","Advengers");
        this.listFilm.add(filme);
    }
}
