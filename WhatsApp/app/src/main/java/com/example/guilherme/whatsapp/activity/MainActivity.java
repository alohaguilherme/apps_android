package com.example.guilherme.whatsapp.activity;

import android.Manifest;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.guilherme.whatsapp.R;
import com.example.guilherme.whatsapp.config.SettingsFirebase;
import com.example.guilherme.whatsapp.fragment.ChatsFragment;
import com.example.guilherme.whatsapp.fragment.ContactsFragment;
import com.example.guilherme.whatsapp.helper.Permissions;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private String[] permissions = new String[]{
            Manifest.permission.RECORD_AUDIO
    };
     private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
     private FirebaseAuth auth;
     private MaterialSearchView materialSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = SettingsFirebase.getFirebaseAuth();

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Whatsapp");
        setSupportActionBar( toolbar );

        Permissions.validPermissions(permissions, this, 1);

         final FragmentPagerAdapter adapter = new FragmentPagerItemAdapter(
            getSupportFragmentManager(),
            FragmentPagerItems.with(this)
            .add(R.string.name_chats, ChatsFragment.class)
            .add(R.string.name_contacts, ContactsFragment.class)

            .create()
        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter( adapter );

        SmartTabLayout smartTabLayout = findViewById(R.id.viewPagerTab);
        smartTabLayout.setViewPager( viewPager );

        materialSearchView = findViewById(R.id.search_view);

        materialSearchView.setVoiceSearch(true); //or false
        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {

                ChatsFragment chatsFragment = (ChatsFragment) ((FragmentPagerItemAdapter) adapter).getPage(0);
                chatsFragment.reloadConversations();

            }
        });

        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //acessa o fragment
                ChatsFragment chatsFragment = (ChatsFragment) ((FragmentPagerItemAdapter) adapter).getPage(0);

                if (newText != null && !newText.isEmpty()){
                    chatsFragment.searchConversation( newText.toLowerCase() );
                }

                return true;
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0){
                String teste = matches.get( 0 );
                Log.i("DADOS: ", "TESTES DE VOZ");
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem( R.id.searchMenu );
        materialSearchView.setMenuItem( item );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId()){
            case R.id.closeMenu:
                signOut();
                finish();
                break;

            case R.id.configMenu:
                openSettingUser();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void openSettingUser(){
        startActivity( new Intent( MainActivity.this, UserSettingsActivity.class));
    }

    public void signOut(){

        try{
            auth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
