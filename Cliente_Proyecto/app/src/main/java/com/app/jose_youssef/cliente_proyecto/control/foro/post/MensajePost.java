package com.app.jose_youssef.cliente_proyecto.control.foro.post;

import android.support.annotation.NonNull;

/**
 * Created by yelfaqir on 28/05/2017.
 */

public class MensajePost implements Comparable{

    private String nombre = "";
    private String fecha = "";
    private String mensaje = "";
    private String usuarioContestado = "";
    private String mensajeContestado = "";

    public MensajePost() {
    }

    public MensajePost(String nombre, String fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public MensajePost(String nombre, String fecha, String mensaje, String usuarioContestado, String mensajeContestado) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.mensaje = mensaje;
        this.usuarioContestado = usuarioContestado;
        this.mensajeContestado = mensajeContestado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUsuarioContestado() {
        return usuarioContestado;
    }

    public void setUsuarioContestado(String usuarioContestado) {
        this.usuarioContestado = usuarioContestado;
    }

    public String getMensajeContestado() {
        return mensajeContestado;
    }

    public void setMensajeContestado(String mensajeContestado) {
        this.mensajeContestado = mensajeContestado;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return fecha.compareTo(((MensajePost)o).getFecha());
    }

    @Override
    public boolean equals(Object obj) {
        return nombre.equals(((MensajePost)obj).getNombre());
    }
}
