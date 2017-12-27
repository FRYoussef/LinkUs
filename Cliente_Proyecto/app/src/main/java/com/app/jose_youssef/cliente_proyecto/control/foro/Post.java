package com.app.jose_youssef.cliente_proyecto.control.foro;

import android.support.annotation.NonNull;

/**
 * Created by youss on 21/05/2017.
 */

public class Post implements Comparable{
    private String nombre = "";
    private String fechaUltimoMensaje = "";
    private String ultimoEnContestar = "";
    private int respuestas;

    public Post() {}

    public Post(String nombre, String fechaUltimoMensaje, String ultimoEnContestar, int respuestas) {
        this.nombre = nombre;
        this.fechaUltimoMensaje = fechaUltimoMensaje;
        this.ultimoEnContestar = ultimoEnContestar;
        this.respuestas = respuestas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(int respuestas) {
        this.respuestas = respuestas;
    }

    public String getFechaUltimoMensaje() {
        return fechaUltimoMensaje;
    }

    public void setFechaUltimoMensaje(String fechaUltimoMensaje) {
        this.fechaUltimoMensaje = fechaUltimoMensaje;
    }

    public String getUltimoEnContestar() {
        return ultimoEnContestar;
    }

    public void setUltimoEnContestar(String ultimoEnContestar) {
        this.ultimoEnContestar = ultimoEnContestar;
    }

    public void annadeUnaRespuesta(){
        respuestas++;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        int comp = fechaUltimoMensaje.compareTo(((Post)o).getFechaUltimoMensaje());
        if(comp < 0)
            return 1;
        else if(comp > 0)
            return -1;

        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return nombre.equals(((Post)obj).getNombre());
    }
}
