package com.apps.dsb.radioadaja;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentListaNoticias extends Fragment {

    private NonScrollGridView lista_noticias;
    private ImageView noticia_destacada_imagen;
    private TextView noticia_destacada_titular;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayout noticia_principal;
    private ImageButton twitter, facebook, mail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lista_noticias, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        lista_noticias = getView().findViewById(R.id.lista_noticias);
        noticia_destacada_imagen = getView().findViewById(R.id.noticia_destacada_imagen);
        noticia_destacada_titular = getView().findViewById(R.id.noticia_destacada_titular);
        noticia_principal = getView().findViewById(R.id.noticia_principal);

        mSwipeRefreshLayout = getView().findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new ObtenerNoticias().execute();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        twitter = getView().findViewById(R.id.twitter);
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.twitter.android");
                intent.putExtra(Intent.EXTRA_TEXT, "¡Mira esta noticia de @radioadaja!" + "\n\n" + Utilidades.noticiaPrincipal.getTituloNoticia() + "\n\n" + Utilidades.noticiaPrincipal.getEnlace());
                startActivity(intent);
            }
        });
        facebook = getView().findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.facebook.katana");
                intent.putExtra(Intent.EXTRA_TEXT, "¡Mira esta noticia de Radio Adaja!" + "\n\n" + Utilidades.noticiaPrincipal.getTituloNoticia() + "\n\n" + Utilidades.noticiaPrincipal.getEnlace());
                startActivity(intent);
            }
        });
        mail = getView().findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:"));
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "¡Mira esta noticia de Radio Adaja!" + "\n\n" + Utilidades.noticiaPrincipal.getTituloNoticia() + "\n\n" + Utilidades.noticiaPrincipal.getEnlace());
                startActivity(intent);
            }
        });


        if(Utilidades.arrayNoticias == null){
            new ObtenerNoticias().execute();
        } else {
            rellenarVista(Utilidades.arrayNoticias);
        }


    }

    private class ObtenerNoticias extends AsyncTask<ArrayList<Noticia>, Void, ArrayList<Noticia>> {

        @Override
        protected void onPreExecute() {
            // super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Recolectando noticias...");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected ArrayList<Noticia> doInBackground(ArrayList<Noticia>... params) {
            return Utilidades.recuperarNoticias(getResources());
        }

        @Override
        protected void onPostExecute(ArrayList<Noticia> s) {
            rellenarVista(s);
        }
    }

    public void rellenarVista(ArrayList<Noticia> s){
        if(s.size() != 0){
            AdapterNoticia adapter = new AdapterNoticia(getActivity(), s);
            lista_noticias = getView().findViewById(R.id.lista_noticias);
            lista_noticias.setAdapter(adapter);


            noticia_destacada_titular.setText(Utilidades.noticiaPrincipal.getTituloNoticia());
            noticia_destacada_imagen.setImageDrawable(Utilidades.noticiaPrincipal.getImagenNoticia());
            noticia_principal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Utilidades.noticiaPrincipal.getEnlace()));
                    v.getContext().startActivity(intent);
                }
            });

            if(progressDialog != null){
                progressDialog.dismiss();
            }

        }else {
            Snackbar.make(getView(), "No se pudieron obtener noticias. Revise su conexión e inténtelo de nuevo.", Snackbar.LENGTH_LONG)
                    .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                    .setAction("Reintentar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new ObtenerNoticias().execute();
                        }
                    })
                    .show();
            if(progressDialog != null){
                progressDialog.dismiss();
            }
        }
    }




}