package com.example.gteod.recyclerview.Activity.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gteod.recyclerview.Activity.model.Filme;
import com.example.gteod.recyclerview.R;

import org.w3c.dom.Text;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private List<Filme> listaFilmes;

    public Adapter(List<Filme> lista) {
        this.listaFilmes = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_list, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        Filme filme = listaFilmes.get( i );

        myViewHolder.ano.setText(filme.getAno());
        myViewHolder.genero.setText(filme.getGenero());
        myViewHolder.titulo.setText(filme.getTituloFilme());

    }

    @Override
    public int getItemCount() {
        return listaFilmes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ano;
        TextView genero;
        TextView titulo;

        public MyViewHolder(View itemView) {
            super(itemView);

            ano = itemView.findViewById(R.id.textAno);
            genero = itemView.findViewById(R.id.textGenero);
            titulo = itemView.findViewById(R.id.textTitulo);
        }
    }

}
