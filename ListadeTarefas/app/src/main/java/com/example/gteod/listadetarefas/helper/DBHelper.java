package com.example.gteod.listadetarefas.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static int VERSION = 1;
    public static String NAME_DB = "DB_TAREFAS";
    public static String TABELA_TAREFAS = "tarefas";

    public DBHelper(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABELA_TAREFAS +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " nome TEXT NOT NULL); ";

        try {
            db.execSQL( sql );
            Log.i("INFO DB", "Tabela criada com sucesso!");
        }catch (Exception e ){
            Log.i("INFO DB", "Erro ao criar a tabela" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql = "DROP TABLE IF EXISTS " + TABELA_TAREFAS + " ;";

        try {
            db.execSQL( sql );
            onCreate(db);
            Log.i("INFO DB", "App atualizado com sucesso!");
        }catch (Exception e ){
            Log.i("INFO DB", "Erro ao Atualizar a tabela" + e.getMessage());
        }


    }
}
