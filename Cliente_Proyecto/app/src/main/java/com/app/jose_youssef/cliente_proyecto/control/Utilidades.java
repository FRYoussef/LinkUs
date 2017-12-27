package com.app.jose_youssef.cliente_proyecto.control;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorConexion;

/**
 * Created by yelfaqir on 10/06/2017.
 * Usaremos esta clase, para utilidades generales que se usaran en mas
 * de un sitio de la app
 */

public class Utilidades {

    private final static int TIEMPO_REFRESCO = 500;
    private final static int COMPROBACIONES = 10;

    /**
     * Comprueba si se ha establecido la conexión con el servidor,
     * sino espera, y lo vuelve a comprobar, hasta un limite.
     * @return devuelve true si se ha establecido conexión, sino false
     */

    public static void compruebaConexion(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(!ManejadorConexion.conectado && i < COMPROBACIONES){
                    try {
                        Thread.sleep(TIEMPO_REFRESCO);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }

                if(i < COMPROBACIONES)
                    ((IUtilidades)context).estadoConectado();
                else
                    muestraAlerta(context);
            }
        }).start();
    }

    /**
     * Muestra una alerta de conexion
     * @param context necesario para crear un alert dialog
     */

    public static void muestraAlerta(final Context context){
        ((AppCompatActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle(R.string.conexion_error)
                        .setMessage(R.string.server_not_found)
                        .setCancelable(false)
                        .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ((IUtilidades)context).compruebaConexion();
                            }
                        })
                        .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((AppCompatActivity)context).finish();
                            }
                        });
                alertBuilder.show();
            }
        });
    }
}
