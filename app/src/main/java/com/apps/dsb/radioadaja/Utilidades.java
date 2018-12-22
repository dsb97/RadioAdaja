package com.apps.dsb.radioadaja;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class Utilidades {

    public static ArrayList<Noticia> arrayNoticias;
    public static Noticia noticiaPrincipal;


    public static Drawable LoadImageFromWebOperations(Resources r, String url, float reductionFactor) {
        Log.println(Log.VERBOSE, "debug", url);
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap b = ((BitmapDrawable)Drawable.createFromStream(is, "src name")).getBitmap();
            Bitmap bitmapResized =
                    Bitmap.createScaledBitmap(b,
                            (int) (b.getWidth()*reductionFactor),
                            (int) (b.getHeight()*reductionFactor),
                            false);
            return new BitmapDrawable(r, bitmapResized);
        } catch (Exception e) {
            return null;
        }
    }


    public static ArrayList<Noticia> recuperarNoticias(Resources r){

        arrayNoticias = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(r.getString(R.string.URLWeb)).get();
            Elements enlaces = doc.select("div[class=list_news fleft]");
            Elements enlacesNoticiasArriba = doc.select("div[id=featured]");

            String[] noticiasIzquierda = doc.select("div[class=content_noticias_home]").get(0)
                    .select("div[class=list_news fleft]")
                    .select("a").text().substring(3)
                    .replace("(0)", "separator").split("separator");


            Noticia add;
            for(int i = 0; i < 4; i++){
                try{
                    add = new Noticia();
                    add.setTituloNoticia(enlacesNoticiasArriba.select("div[id=fragment-"+(i+1)+"]").select("div[class=info]").select("p").text().replace("leer más", ""));
                    add.setEnlace(r.getString(R.string.URLWeb) + enlacesNoticiasArriba.select("div[id=fragment-"+(i+1)+"]").select("div[class=info]").select("p").select("a").attr("href"));
                    add.setImagenNoticia(LoadImageFromWebOperations(
                            r,
                            r.getString(R.string.URLWeb) + enlacesNoticiasArriba.select("div[id=fragment-"+(i+1)+"]").select("img").attr("src"),
                            (i==0 ? 0.5F : 0.2F)));
                    arrayNoticias.add(add);
                }catch (Exception ex){
                    Log.println(Log.VERBOSE, "INFO", ex.getMessage());
                }
            }


            for (int i = 0; i < (noticiasIzquierda.length > enlaces.size() ? enlaces.size() : noticiasIzquierda.length); i++) {
                //String title, String description, Drawable imagenNoticia, String enlace
                try {
                    add = new Noticia();
                    add.setEnlace(r.getString(R.string.URLWeb)+enlaces.get(i)
                            .select("a")
                            .attr("href")
                            .replace("#comentarios", ""));
                    add.setImagenNoticia(LoadImageFromWebOperations(r,r.getString(R.string.URLWeb) + enlaces.get(i)
                            .select("img").not("img[src=support/img/ico_escucha.gif]").not("img[src=support/img/ico_coment.gif]")
                            .attr("src")
                            .replace("image.axd?src=", "")
                            .replace("&h=82&w=122", "").replace("peq", ""), (0.2F)));
                    add.setTituloNoticia(noticiasIzquierda[i].trim());

                    arrayNoticias.add(add);
                }catch (Exception ex){
                    Log.println(Log.ERROR, "dev", ex.getMessage());
                }
            }

        } catch (IOException e) {
            Log.println(Log.ERROR, "dev", e.getMessage());
        }


        noticiaPrincipal = arrayNoticias.remove(0);

        return arrayNoticias;
    }
}
