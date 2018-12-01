package com.example.gteod.cardview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gteod.cardview.R;
import com.example.gteod.cardview.model.Postagem;

import java.util.List;

public class PostagemAdaptter extends RecyclerView.Adapter<PostagemAdaptter.MyViewHolder> {

    private List<Postagem> postagens;
    public PostagemAdaptter(List<Postagem> listaPostagens) {
        this.postagens = listaPostagens;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemLista = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.postagem_detalhe,viewGroup,false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Postagem postagem = postagens.get(i);
        myViewHolder.textNome.setText(postagem.getNome());
        myViewHolder.textPostagem.setText(postagem.getPostagem());
        myViewHolder.imagemPostagem.setImageResource( postagem.getImagem() );
    }

    @Override
    public int getItemCount() {
        return postagens.size() ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textNome, textPostagem;
        private ImageView imagemPostagem;

        public MyViewHolder(View itemView) {
            super(itemView);

            textNome = itemView.findViewById(R.id.textNome);
            textPostagem = itemView.findViewById(R.id.textPostagem);
            imagemPostagem = itemView.findViewById(R.id.imagePostagem);

        }
    }
}
