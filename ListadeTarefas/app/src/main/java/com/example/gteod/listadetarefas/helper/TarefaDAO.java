package com.example.gteod.listadetarefas.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.gteod.listadetarefas.activity.MainActivity;
import com.example.gteod.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class TarefaDAO implements iTarefaDAO {

    private SQLiteDatabase write;
    private SQLiteDatabase read;

    public TarefaDAO(Context context) {
        DBHelper db = new DBHelper(context);
        write = db.getWritableDatabase();
        read = db.getReadableDatabase();
    }

    @Override
    public boolean save(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put("nome",tarefa.getNomeTarefa());

        try {
            write.insert(DBHelper.TABELA_TAREFAS,null, cv);
            Log.i("INFO","Dados salvos com sucesso!");
        }catch (Exception e){
            Log.i("INFO","Erro ao salvar tarefa "+ e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public boolean update(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());

        try {
            String[] args = {tarefa.getId().toString()};
            write.update(DBHelper.TABELA_TAREFAS, cv, "id = ?", args);
            Log.i("INFO","Dados atualizados com sucesso!");
        }catch (Exception e){
            Log.i("INFO","Erro ao atualizar os dados "+e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean delete(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());

        try {
            String[] args = {tarefa.getId().toString()};
            write.delete(DBHelper.TABELA_TAREFAS,"id = ?", args);
            Log.i("INFO","Dados atualizados com sucesso!");
        }catch (Exception e){
            Log.i("INFO","Erro ao atualizar os dados "+e.getMessage());
            return false;
        }


        return true;
    }

    @Override
    public List<Tarefa> listar() {
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM "+DBHelper.TABELA_TAREFAS + " ;";

        Cursor c = read.rawQuery(sql, null);

        while (c.moveToNext()) {
            Tarefa tarefa = new Tarefa();

            Long id = c.getLong( c.getColumnIndex("id"));
            String nome = c.getString(c.getColumnIndex("nome"));

            tarefa.setId(id);
            tarefa.setNomeTarefa(nome);

            tarefas.add( tarefa );
        }

        return tarefas;
    }
}
