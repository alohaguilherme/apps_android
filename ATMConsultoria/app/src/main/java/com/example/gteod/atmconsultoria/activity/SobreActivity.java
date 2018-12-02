package com.example.gteod.atmconsultoria.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.gteod.atmconsultoria.R;

import mehdi.sakout.aboutpage.AboutPage;

public class SobreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_sobre);
        String descricao = "Atender o mercado de software para a gestão pública, garantindo a satisfação dos clientes, colaboradores, sócios, parceiros e melhorando a interface entre sociedade e servidor público.\n\n"+
                "Nosso futuro é ser líder no Brasil em soluções WEB para administração pública, destacando-se na qualidade dos produtos e serviços.\n";
        View sobre = new AboutPage(this)
                .setImage(R.drawable.logo)
                .setDescription(descricao)
                .addGroup("Fale Conosco")
                .addEmail("atmconsultoria@gmail.com")
                .addWebsite("HTTP://google.com.br", "Acesse nosso site")
                .addGroup("Acesse nossas redes sociais")
                .addFacebook("google")
                .addTwitter("google")
                .addYoutube("google")
                .addPlayStore("google")
                .addInstagram("google")
                .create();

        setContentView( sobre );
    }
}
