package com.apps.dsb.radioadaja;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISO_INTERNET = 3;
    private MediaPlayer mediaPlayer;
    private ScrollView scroll;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Inicio");


        //Comprobamos los permisos a Internet
        comprobarPermisosInternet();


        //UI
        //  Barra de navegación inferior
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //  ScrollView
        scroll = (ScrollView) findViewById(R.id.scroll);
        getSupportFragmentManager().beginTransaction().replace(R.id.principal, new FragmentListaNoticias()).commit();



    }
    //----------------------------------
    //--------Métodos propios-----------
    //----------------------------------




    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                try{
                    Fragment fragmentInFrame = getSupportFragmentManager()
                            .findFragmentById(R.id.principal);
                    if(fragmentInFrame==null || !(fragmentInFrame instanceof FragmentListaNoticias)){
                        getSupportFragmentManager().beginTransaction().replace(R.id.principal, new FragmentListaNoticias()).commit();
                    }
                }catch (Exception e){
                    getSupportFragmentManager().beginTransaction().replace(R.id.principal, new FragmentListaNoticias()).commit();
                }
                    setTitle("Inicio");
                    return true;
                case R.id.navigation_podcast:
                    //mTextMessage.setText(R.string.title_dashboard);
                    //Toast.makeText(getApplicationContext(), R.string.title_podcast, Toast.LENGTH_SHORT).show();
                    setTitle("Podcast");
                    return true;
                case R.id.navigation_radio:
                    try{
                        Fragment fragmentInFrame = getSupportFragmentManager()
                                .findFragmentById(R.id.principal);
                        if(fragmentInFrame==null || !(fragmentInFrame instanceof FragmentReproductor)){
                            getSupportFragmentManager().beginTransaction().replace(R.id.principal, new FragmentReproductor()).commit();
                        }
                    }catch (Exception e){
                        getSupportFragmentManager().beginTransaction().replace(R.id.principal, new FragmentReproductor()).commit();
                    }
                    setTitle("Radio en directo");
                    return true;
                case R.id.navigation_info:
                    //mTextMessage.setText(R.string.title_notifications);
                    //Toast.makeText(getApplicationContext(), R.string.title_info, Toast.LENGTH_SHORT).show();
                    setTitle("Radio Adaja");
                    return true;

            }
            return false;
        }
    };

    private void permissionRejected() {
        Toast.makeText(MainActivity.this, "Debes dar a la aplicación los permisos necesarios para conexión a internet", Toast.LENGTH_SHORT).show();
    }

    private void comprobarPermisosInternet(){
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            //Si el permiso no se encuentra concedido se solicita
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISO_INTERNET);
        } else {
            //Si el permiso está concedido prosigue con el flujo normal
            //ACTIONS
            //radio();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Se obtiene el resultado de los permisos con base en la clave
        switch (requestCode) {
            case PERMISO_INTERNET:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso está concedido prosigue con el flujo normal
                    //ACTIONS
                    radio();
                } else {
                    //Si el permiso no fue concedido no se puede continuar
                    permissionRejected();
                }
                break;
        }
    }

    public void radio(){
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource("http://173.236.61.226/stream");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Toast.makeText(MainActivity.this, "La URL de stream es incorrecta o no está disponible. \nPor favor, inténtalo de nuevo más tarde o comprueba si existe alguna actualización de la app.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (SecurityException | IllegalStateException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e){
            Toast.makeText(MainActivity.this, "Imposible conectar con la radio en directo.\nComprueba tu conexión a internet e inténtalo de nuevo", Toast.LENGTH_LONG);
        }

    }

}
