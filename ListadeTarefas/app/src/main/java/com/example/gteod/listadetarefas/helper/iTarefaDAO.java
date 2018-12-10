package com.example.gteod.listadetarefas.helper;

import com.example.gteod.listadetarefas.model.Tarefa;

import java.util.List;

public interface iTarefaDAO {

    public boolean save(Tarefa tarefa);
    public boolean update(Tarefa tarefa);
    public boolean delete(Tarefa tarefa);
    public List<Tarefa> listar();
}
