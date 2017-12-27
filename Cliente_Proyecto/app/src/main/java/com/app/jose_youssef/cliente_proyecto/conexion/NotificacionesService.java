package com.app.jose_youssef.cliente_proyecto.conexion;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import protocolo.Mensaje;

/**
 * Servicio, que nos permitirá mantener el escucha permanenete,
 * para recibir notificaciones
 * Created by Youss on 13/04/2017.
 */

public class NotificacionesService extends Service {

    private ThConexion thEscucha = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if((thEscucha == null || !thEscucha.isAlive())){
            thEscucha = new ThConexion(getApplicationContext());
            thEscucha.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thEscucha.cierraRecursos();
        thEscucha.interrupt();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
