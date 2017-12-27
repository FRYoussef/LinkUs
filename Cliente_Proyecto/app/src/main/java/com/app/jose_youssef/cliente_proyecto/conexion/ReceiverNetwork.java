package com.app.jose_youssef.cliente_proyecto.conexion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Nos servira para detectar cambios en la conexion a internet
 * Created by Youss on 17/04/2017.
 */

public class ReceiverNetwork extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(hayConexion(context))
            context.startService(new Intent(context, NotificacionesService.class));

    }

    private boolean hayConexion(Context context){
        NetworkInfo nI = ((ConnectivityManager) context.getSystemService
                (Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return (nI != null && nI.isConnected());
    }
}
