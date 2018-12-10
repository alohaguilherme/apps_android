package com.example.gteod.listadetarefas.activity;

import android.content.ContentValues;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gteod.listadetarefas.R;
import com.example.gteod.listadetarefas.helper.DBHelper;
import com.example.gteod.listadetarefas.helper.TarefaDAO;
import com.example.gteod.listadetarefas.model.Tarefa;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        editTarefa = findViewById(R.id.textTarefa);

        //recupera dados quando for edição
            tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelect");

            //configura caixa de texto

        if  (tarefaAtual != null){
            editTarefa.setText(tarefaAtual.getNomeTarefa());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemSalvar :
                TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                if (tarefaAtual != null){
                    //EDIÇÃO
                    String nomeTarefa = editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(nomeTarefa);
                        tarefa.setId(tarefaAtual.getId());

                        //atualizar
                        if(tarefaDAO.update( tarefa )){
                            finish();
                            Toast.makeText(getApplicationContext(), "Sucesso ao atualizar!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Erro ao atualizar!", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Informe uma tarefa!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //INSERÇÃO
                    String nomeTarefa = editTarefa.getText().toString();
                    if (!nomeTarefa.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(editTarefa.getText().toString());
                        if(tarefaDAO.save( tarefa )){
                            finish();
                            Toast.makeText(getApplicationContext(), "Sucesso ao salvar tarefa!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), "Erro ao salvar tarefa!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Informe uma tarefa!", Toast.LENGTH_SHORT).show();
                    }
                }
        }

        return super.onOptionsItemSelected(item);
    }
}
