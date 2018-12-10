package com.example.gteod.listadetarefas.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.gteod.listadetarefas.R;
import com.example.gteod.listadetarefas.adapter.TarefaAdapter;
import com.example.gteod.listadetarefas.helper.DBHelper;
import com.example.gteod.listadetarefas.helper.RecyclerItemClickListener;
import com.example.gteod.listadetarefas.helper.TarefaDAO;
import com.example.gteod.listadetarefas.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        //Evento de clique
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Tarefa tarefaSelect = listaTarefas.get(position);

                                Intent intent = new Intent(MainActivity.this, AdicionarTarefaActivity.class);
                                intent.putExtra("tarefaSelect", tarefaSelect);

                                startActivity( intent );
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                tarefaSelecionada = listaTarefas.get( position );

                                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                                dialog.setTitle("Confirmar exclusão");
                                dialog.setMessage("Deseja excluir  a tarefa: "+ tarefaSelecionada.getNomeTarefa() + "?");

                                dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                                        if(tarefaDAO.delete(tarefaSelecionada)){
                                            carregarListaTarefas();
                                            Toast.makeText(getApplicationContext(), "Sucesso ao excluir!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "Erro ao excluir!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog.setNegativeButton("Não", null);

                                dialog.create();
                                dialog.show();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }

                )
        );

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdicionarTarefaActivity.class);
                startActivity( intent );
            }
        });
    }

    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }

    public void carregarListaTarefas(){

        //Listar
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();
        //adapter
        tarefaAdapter = new TarefaAdapter( listaTarefas );

        //reyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext());
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration( new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
        recyclerView.setAdapter( tarefaAdapter );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
