package com.apps.dsb.radioadaja;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterNoticia extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Noticia> items;


    public AdapterNoticia (Activity activity, ArrayList<Noticia> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Noticia> Noticia) {
        for (int i = 0 ; i < Noticia.size(); i++) {
            items.add(Noticia.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        //Se infla el ítem
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_noticias, null);
        }

        final Noticia dir = items.get(position);

        //Se carga el titular de la noticia
        TextView title = v.findViewById(R.id.titular_noticia);
        title.setText(dir.getTituloNoticia());


        //Se carga la imagen de la noticia
        ImageView imagen = v.findViewById(R.id.imagen_noticia);
        imagen.setImageDrawable(dir.getImagenNoticia());

        LinearLayout panel_noticias = v.findViewById(R.id.panel_noticia);
        panel_noticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(dir.getEnlace()));
                v.getContext().startActivity(intent);
            }
        });

        ImageButton share = v.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View k) {
                Noticia n = Utilidades.arrayNoticias.get(position);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "¡Mira esta noticia de Radio Adaja!" + "\n\n" + n.getTituloNoticia() + "\n\n" + n.getEnlace());
                k.getContext().startActivity(Intent.createChooser(intent, "Compartir con..."));
            }
        });
        return v;
    }
}
