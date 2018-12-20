package com.example.guilherme.organize.activity.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guilherme.organize.R;
import com.example.guilherme.organize.activity.adapter.AdapterMovimentacao;
import com.example.guilherme.organize.activity.config.ConfiguracaoFirebase;
import com.example.guilherme.organize.activity.helper.Base64Custom;
import com.example.guilherme.organize.activity.model.Movimentacao;
import com.example.guilherme.organize.activity.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class IndexActivity extends AppCompatActivity {

    private String monthYear;
    private MaterialCalendarView calendarView;
    private TextView textUser, textSaldo;
    private Double receitaTotal, despesaTotal, resumoTotal;
    private RecyclerView recyclerView;
    private FirebaseAuth auth = ConfiguracaoFirebase.getFirebaseAuth();
    private DatabaseReference firebaseref = ConfiguracaoFirebase.getFirebaseDataBase();
    private DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebaseDataBase();
    private DatabaseReference movtoRef ;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacoes;
    private AdapterMovimentacao adapter;
    private Movimentacao movimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organizze");
        setSupportActionBar(toolbar);


        textUser = findViewById(R.id.textUser);
        textSaldo = findViewById(R.id.textSaldo);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerMovimento);
        configurationCalendar();
        swipe();

        //Configuar adapter
        adapter = new AdapterMovimentacao( movimentacoes, this);


        //Config RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adapter );
    }

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove( RecyclerView recyclerView,  RecyclerView.ViewHolder viewHolder,  RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int i) {
                removeMovimentaion( viewHolder );
            }

        };

        new ItemTouchHelper( itemTouch ).attachToRecyclerView( recyclerView );
    }

    public void removeMovimentaion(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Movimento da Conta");
        alertDialog.setTitle("Você tem certeza que deseja excluir a movimentacão da sua conta?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                movimentacao = movimentacoes.get(position);
                String emailUsuario  = auth.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.encodeBase64( emailUsuario );
                movtoRef = firebaseref.child("movimentacao")
                        .child(idUsuario)
                        .child( monthYear );

                movtoRef.child( movimentacao.getKey() ).removeValue();
                adapter.notifyItemRemoved( position );
                updateValue();

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(IndexActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void updateValue(){
        String emailUsuario  = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.encodeBase64( emailUsuario );
        usuarioRef = firebaseref.child("usuarios").child( idUsuario );

        if (movimentacao.getTipo().equals("R")){

            receitaTotal = receitaTotal - movimentacao.getValor();
            usuarioRef.child("receitaTotal").setValue(receitaTotal);
        }

        if (movimentacao.getTipo().equals("D")){
            despesaTotal = despesaTotal - movimentacao.getValor();
            usuarioRef.child("despesaTotal").setValue(despesaTotal);
        }
    }

    public void recoverMovimentation(){

        String emailUsuario  = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.encodeBase64( emailUsuario );
        movtoRef = firebaseref.child("movimentacao")
                              .child(idUsuario)
                              .child( monthYear );

        valueEventListenerMovimentacoes = movtoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movimentacoes.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){

                    Movimentacao movimentacao = dados.getValue( Movimentacao.class);
                    movimentacao.setKey( dados.getKey() );
                    movimentacoes.add( movimentacao );
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void recoverResume(){
        String emailUsuario  = auth.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.encodeBase64( emailUsuario );
        usuarioRef = firebaseref.child("usuarios").child( idUsuario );

       valueEventListenerUsuario =  usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue( Usuario.class );
                receitaTotal = usuario.getReceitaTotal();
                despesaTotal = usuario.getDespesaTotal();
                resumoTotal = receitaTotal - despesaTotal;

                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String resultadoFormatado = decimalFormat.format( resumoTotal);

                textUser.setText("Olá, " + usuario.getNome() );
                textSaldo.setText("R$" + resultadoFormatado);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recoverResume();
        recoverMovimentation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener( valueEventListenerUsuario );
        movtoRef.removeEventListener( valueEventListenerMovimentacoes );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuClose:
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void configurationCalendar(){
        calendarView.state().edit()
                .setMinimumDate(CalendarDay.from(2015,1,1))
                .setMaximumDate(CalendarDay.from(2018,3,1));

        CharSequence meses[] = {"Jan","Fev","Mar","Abr","Mai","Jun","Jul","Ago","Set","Out","Nov","Dez"};
        calendarView.setTitleMonths(meses);
        CalendarDay dataAtual = calendarView.getCurrentDate();
        String monthSelected = String.format("%02d", dataAtual.getMonth() + 1) ;
        monthYear = monthSelected + String.valueOf(dataAtual.getYear());

        CharSequence semanas[] = {"Seg","Ter","Qua","Qui","Sex","Sab","Dom"};
        calendarView.setWeekDayLabels( semanas );

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String monthSelected = String.format("%02d", date.getMonth() + 1) ;
                monthYear = monthSelected + String.valueOf(date.getYear());

                //movtoRef.removeEventListener(valueEventListenerMovimentacoes);
                recoverMovimentation();

            }
        });
    }

    public void addDespesa(View view){
        startActivity( new Intent(this,DespesasActivity.class));
    }

    public void addReceita(View view){
        startActivity( new Intent(this,ReceitasActivity.class));
    }

}
