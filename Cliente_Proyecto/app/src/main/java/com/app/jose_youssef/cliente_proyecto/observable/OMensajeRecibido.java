package com.app.jose_youssef.cliente_proyecto.observable;

import android.util.Log;

import java.util.Observable;

import protocolo.Mensaje;

/**
 * Clase para nuestro patrón de diseño, que permitira avisos entre objetos
 * cuando haya un cambio de estado.
 * Created by Youss on 09/04/2017.
 */

public class OMensajeRecibido extends Observable{

    public void avisaMensaje(Mensaje msj){
        setChanged();
        notifyObservers(msj);
    }
}
