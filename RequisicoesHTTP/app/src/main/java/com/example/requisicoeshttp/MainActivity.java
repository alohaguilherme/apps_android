package com.example.requisicoeshttp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.requisicoeshttp.api.CEPService;
import com.example.requisicoeshttp.api.DataService;
import com.example.requisicoeshttp.model.CEP;
import com.example.requisicoeshttp.model.Foto;
import com.example.requisicoeshttp.model.Postagens;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    private Button btRecuperar;
    private TextView tvRecuperar;
    private Retrofit retrofit;
    private DataService service ;
    private List<Foto> listaFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btRecuperar = findViewById(R.id.button);
        tvRecuperar = findViewById(R.id.textView);

        retrofit = new Retrofit.Builder()
                //.baseUrl("https://viacep.com.br/ws/")
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DataService.class);


        btRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recuperarCEPRetrofit();
               // recuperarListaRetrofit();
               // savePostagem();
               // updatePostagem();
                deletePostagem();

               /* MyTask task = new MyTask();
                String url = "https://blockchain.info/ticker";
                String url1 =  "https://viacep.com.br/ws/88810360/json/";
                task.execute( url, url1 );*/
            }
        });
    }

    private void deletePostagem(){

        Call<Void> call = service.deletePostagem(2);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()){
                    tvRecuperar.setText(
                            "Status: "+ response.code()
                    );
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });

    }

    private void updatePostagem(){

        Postagens postagem = new Postagens("1234", null, "Corpo post");

        Call<Postagens> call = service.updatePostPath(2, postagem);

        call.enqueue(new Callback<Postagens>() {
            @Override
            public void onResponse(Call<Postagens> call, Response<Postagens> response) {

                if (response.isSuccessful()){
                    Postagens postagensResposta = response.body();
                    tvRecuperar.setText(
                            "Status: "+ response.code() +
                                    " id: "+ postagensResposta.getId() +
                                    " userId: "+ postagensResposta.getUserid()+
                                    " titulo: "+ postagensResposta.getTitle()+
                                    " body: "+ postagensResposta.getBody()
                    );
                }
            }

            @Override
            public void onFailure(Call<Postagens> call, Throwable t) {

            }
        });

    };

    private void savePostagem(){

        //Configurar o objeto
       // final Postagens postagem = new Postagens("1234", "Titulo post", "Corpo post");
        DataService service = retrofit.create(DataService.class);


        Call<Postagens> call = service.savePostagem("1234", "Titulo post", "Corpo post");

        call.enqueue(new Callback<Postagens>() {
            @Override
            public void onResponse(Call<Postagens> call, Response<Postagens> response) {

                if (response.isSuccessful()){
                    Postagens postagensResposta = response.body();
                    tvRecuperar.setText(
                            "Código: "+ response.code() +
                            " id: "+ postagensResposta.getId() +
                            " titulo: "+ postagensResposta.getTitle()
                    );
                }

            }

            @Override
            public void onFailure(Call<Postagens> call, Throwable t) {

            }
        });
    }

    private void recuperarListaRetrofit(){
        DataService service = retrofit.create( DataService.class );
        Call<List<Foto>> call = service.recuperarFotos();

        call.enqueue(new Callback<List<Foto>>() {
            @Override
            public void onResponse(Call<List<Foto>> call, Response<List<Foto>> response) {
                if ( response.isSuccessful() ){

                    listaFotos = response.body();

                    for (int i =0;i< listaFotos.size(); i++){
                        Foto foto = listaFotos.get( i );
                        Log.d("RESULTADOS: ", "Resultado: "+ foto.getId() + " / "+ foto.getTitle());
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Foto>> call, Throwable t) {

            }
        });

    }

    private void recuperarCEPRetrofit(){
        CEPService service = retrofit.create( CEPService.class );
        Call<CEP> call = service.recuperarCEP("88810360");

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {

                if ( response.isSuccessful() ){

                    CEP cep = response.body();
                    tvRecuperar.setText("Cidade: "+cep.getLocalidade()+"\nUF: "+cep.getUf()+"\nBairro: "+cep.getBairro()+
                            "\nLogradouro: "+cep.getLogradouro()+"\nCEP: "+cep.getCep());
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {

            }
        });

    }


    class MyTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {

            String stringUrl = strings[1];
            StringBuffer buffer = null;
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;

            try{
                URL url = new URL( stringUrl );
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //recupera como bytes tem que converter
                inputStream = conexao.getInputStream();
                inputStreamReader = new InputStreamReader( inputStream );

                BufferedReader reader = new BufferedReader( inputStreamReader );
                buffer = new StringBuffer();
                String linha = "";


                while((linha = reader.readLine()) != null){
                    buffer.append( linha );
                }

            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return String.valueOf(buffer);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String logradouro = null,
                   cep = null,
                   complemento = null,
                   bairro = null,
                   uf = null,
                   cidade = null ;

            JSONObject json;
            try {
                json = new JSONObject( s );

                cidade = json.getString("localidade");
                cep = json.getString("cep");
                bairro = json.getString("bairro");
                uf = json.getString("uf");
                logradouro = json.getString("logradouro");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            tvRecuperar.setText("Cidade: "+cidade+"\nUF: "+uf+"\nBairro: "+bairro+
                    "\nLogradouro: "+logradouro+"\nCEP: "+cep);
        }


    }
}
