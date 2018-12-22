package com.apps.dsb.radioadaja;

import android.graphics.drawable.Drawable;

public class Noticia {
    private String tituloNoticia;
    private Drawable imagenNoticia;
    private String enlace;

    public Noticia() {
        super();
    }

    public Noticia(String titular, Drawable imagenNoticia, String enlace) {
        super();
        this.tituloNoticia = titular;
        this.imagenNoticia = imagenNoticia;
        this.enlace = enlace;
    }


    public String getTituloNoticia() {
        return tituloNoticia;
    }

    public void setTituloNoticia(String title) {
        this.tituloNoticia = title;
    }

    public Drawable getImagenNoticia() {
        return imagenNoticia;
    }

    public void setImagenNoticia(Drawable imagenNoticia) {
        this.imagenNoticia = imagenNoticia;
    }

    public String getEnlace() {
        return enlace;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }
}
