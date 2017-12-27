package com.app.jose_youssef.cliente_proyecto.control;

/**
 * Created by Youss on 10/04/2017.
 */

public class MensajeCorto {

    private String asunto;
    private String contenido;
    private String fecha;
    private String emisor;
    private String receptor;

    public MensajeCorto(String asunto, String contenido, String fecha,
                        String emisor, String receptor)
    {
        this.asunto = asunto;
        this.contenido = contenido;
        this.fecha = fecha;
        this.emisor = emisor;
        this.receptor = receptor;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }
}
