package com.app.jose_youssef.cliente_proyecto.conexion;

import android.content.Context;
import android.content.IntentFilter;

/**
 * Necesito poder acceder a los recursos en toda la app
 * Created by Youss on 17/04/2017.
 */

public class ManejadorReceiverMensaje {

    private static ReceiverMensaje receiverMensaje = null;
    private static IntentFilter intentFilter = null;

    public static void inicializa(){
        receiverMensaje = new ReceiverMensaje();
        intentFilter = new IntentFilter(ReceiverMensaje.NUEVO_MENSAJE);
    }

    public static void registra(Context context){
        context.registerReceiver(receiverMensaje, intentFilter);
    }

    public static void borra(Context context){
        context.unregisterReceiver(receiverMensaje);
    }
}
