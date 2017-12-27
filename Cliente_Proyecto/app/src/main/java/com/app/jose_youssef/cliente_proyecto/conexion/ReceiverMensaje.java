package com.app.jose_youssef.cliente_proyecto.conexion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.jose_youssef.cliente_proyecto.observable.ManejadorMensajeOb;

import protocolo.Mensaje;

/**
 * Intermediario entre el service y la app.
 * Lo usare para notificar a la app que ha llegado un nuevo msj.
 * Created by Youss on 17/04/2017.
 */

public class ReceiverMensaje extends BroadcastReceiver {

    public static final String KEY_MENSAJE = "mensaje";
    public static final String NUEVO_MENSAJE = "NOTIFICA_MENSAJE";

    @Override
    public void onReceive(Context context, Intent intent) {
        try{
            Mensaje msj = (Mensaje) intent.getSerializableExtra(KEY_MENSAJE);
            ManejadorMensajeOb.getoMsj().avisaMensaje(msj);
        }catch (Exception e){}
    }
}
