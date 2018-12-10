package com.example.gteod.listadetarefas.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gteod.listadetarefas.R;
import com.example.gteod.listadetarefas.model.Tarefa;

import org.w3c.dom.Text;

import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.MyViewHolder> {

    private List<Tarefa> listaTarefas;

    public TarefaAdapter(List<Tarefa> lista) {
        this.listaTarefas = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.lista_tarefa_adapter, viewGroup, false);
        return new MyViewHolder( itemLista );
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Tarefa tarefa = listaTarefas.get(i);
        myViewHolder.tarefa.setText(tarefa.getNomeTarefa());
    }

    @Override
    public int getItemCount() {
        return this.listaTarefas.size();
    }

    public  class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView tarefa;

        public MyViewHolder(View itemView) {
            super(itemView);

            tarefa = itemView.findViewById(R.id.textTarefa);
        }
    }

}
