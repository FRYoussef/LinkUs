package com.app.jose_youssef.cliente_proyecto.conexion;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.control.ComunicacionActivity;
import com.app.jose_youssef.cliente_proyecto.control.MensajeCorto;
import com.app.jose_youssef.cliente_proyecto.control.LoginActivity;
import com.app.jose_youssef.cliente_proyecto.control.asignatura.AsignaturasActivity;
import com.app.jose_youssef.cliente_proyecto.control.foro.ForoFragment;
import com.app.jose_youssef.cliente_proyecto.control.foro.post.PostActivity;
import com.app.jose_youssef.cliente_proyecto.observable.ManejadorMensajeOb;
import com.app.jose_youssef.cliente_proyecto.persistencia.AlmacenSQLiteOpenHelper;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorAlmacenDB;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorArchivoConfig;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;

import protocolo.Mensaje;

/**
 * Esta clase nos servirá para mantener un hilo escuchando msj que
 * puedan llegar del servidor, para notificar.
 *
 * Created by Youss on 09/04/2017.
 */

public class ThConexion extends Thread{

    private Context context;
    private boolean conexion = false;
    private String usuario = null;
    private NotificationManager notificationManager;
    public static final DateFormat DF_MINUTE = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private final static int NOTIFICACION_FORO = 1;
    private final static int NOTIFICACION_DIFUSION = 2;

    /**
     * Inicializa recursos, y comienza el proceso de autoLogueo
     * @param _context
     */
    public ThConexion(Context _context){
        context = _context;
        try {
            ManejadorConexion.inicializaRecursos();
            conexion = true;
            ManejadorReceiverMensaje.inicializa();
            ManejadorMensajeOb.inicializaMensajeOb();
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            //Auto logueo, si lo tiene activado el usuario
            usuario = ManejadorArchivoConfig.dameUsuario(context);
            if(usuario != null && !usuario.equals("")) {
                ManejadorConexion.mandaMsj(new Mensaje(Mensaje.PETICION_LOGIN, -1, null, usuario,
                        null, null, ManejadorArchivoConfig.dameContrasena(context)));
            }
        } catch (IOException e) {
            Log.e("Conexion", context.getString(R.string.server_not_found));
        }
    }

    /**
     * Escuchará mensajes que lleguen del servidor, y notificará de ello
     * tanto al usuario como al resto de la app.
     */

    @Override
    public void run(){
        if(conexion){
            //hilo para comprobar que el canal esta abierto
            ThComprobadorCanal thC = new ThComprobadorCanal();
            thC.setDaemon(true);
            thC.start();
            try{
                Mensaje msj;
                while(true){
                    msj = ManejadorConexion.leeMsj();
                    Intent intent = new Intent(ReceiverMensaje.NUEVO_MENSAJE);
                    intent.putExtra(ReceiverMensaje.KEY_MENSAJE, msj);
                    context.sendBroadcast(intent);
                    gestionaMensaje(msj);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                cierraRecursos();
            }
        }
    }

    /**
     * Cierra los recursos de los que hace uso
     */

    public void cierraRecursos(){
        notificationManager.cancel(NOTIFICACION_FORO);
        notificationManager.cancel(NOTIFICACION_DIFUSION);
        ManejadorMensajeOb.borraObservadores();
        ManejadorConexion.cierraRecursos();
        ManejadorAlmacenDB.cierraDB();
    }

    /**
     * Gestiona un msj recibido, para ser notificado y añadido a la db
     */

    private void gestionaMensaje(Mensaje msj) throws IOException{

        if(msj.getTipo() == Mensaje.CANAL_PREGUNTA){

        }
        else if(msj.getTipo() == Mensaje.MENSAJES_BUZON){
            usuario = (String)msj.getReceptor();
            ManejadorAlmacenDB.inicializaDBAlmacen(context, usuario);
            ManejadorAlmacenDB.annadeMensajesBuzon((HashSet<Mensaje>) msj.getContenido());
        }
        else if(msj.getTipo() == Mensaje.MENSAJE_DIFUSION) {

            muestraNotificacion(msj);
            MensajeCorto md = new MensajeCorto(msj.getAsunto(), (String) msj.getContenido(),
                    DF_MINUTE.format(msj.getFecha()), msj.getEmisor(), (String) msj.getReceptor());
            ManejadorAlmacenDB.insertaMensaje(md, AlmacenSQLiteOpenHelper.MENSAJE_DIFUSION);
            ManejadorConexion.mandaMsj(new Mensaje(Mensaje.MENSAJE_RECIBIDO, msj.getId(), null,
                    null, null, null, null));
        }
        else if(msj.getTipo() == Mensaje.MENSAJE_FORO) {

            muestraNotificacion(msj);
            MensajeCorto md = new MensajeCorto(msj.getAsunto(), (String) msj.getContenido(),
                    DF_MINUTE.format(msj.getFecha()), msj.getEmisor(), (String) msj.getReceptor());

            ManejadorAlmacenDB.insertaMensaje(md, AlmacenSQLiteOpenHelper.MENSAJE_FORO);
            ManejadorConexion.mandaMsj(new Mensaje(Mensaje.MENSAJE_RECIBIDO, msj.getId(), null,
                    null, null, null, null));
        }
        else if(msj.getTipo() == Mensaje.LISTA_ASIGNATURAS){
            ManejadorAlmacenDB.setAsignaturas((HashSet<String>) msj.getContenido());
        }
    }

    /**
     * Muestra una notificacion, del mensaje recibido
     */

    private void muestraNotificacion(Mensaje msj){
        if(!msj.getEmisor().equals(usuario)){
            Intent intent;
            Builder builder = new Builder(context);
            String usuarioGuardado = ManejadorArchivoConfig.dameUsuario(context);
            int tipoNotificacion;
            if(usuarioGuardado.equals(""))
                usuarioGuardado = usuario;

            if(Mensaje.MENSAJE_FORO == msj.getTipo()){
                tipoNotificacion = NOTIFICACION_FORO;
                intent = new Intent(context, PostActivity.class)
                        .putExtra(ForoFragment.KEY_POST, msj.getAsunto());
                builder.setContentTitle(msj.getAsunto())
                        .setContentText(msj.getEmisor() + ": " + ((String)msj.getContenido()).split(PostActivity.SEPARADOR_MENSAJES)[0]);
            }
            else{
                tipoNotificacion = NOTIFICACION_DIFUSION;
                intent = new Intent(context, ComunicacionActivity.class)
                        .putExtra(ComunicacionActivity.KEY_TAB, ComunicacionActivity.TAB_DIFUSIONES);
                builder.setContentTitle(AlmacenSQLiteOpenHelper.MENSAJE_DIFUSION)
                        .setContentText(msj.getEmisor() + ": " + msj.getAsunto());
            }

            intent.putExtra(LoginActivity.KEY_USUARIO, usuarioGuardado)
                    .putExtra(AsignaturasActivity.KEY_ASIGNATURA, (String) msj.getReceptor())
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            builder.setAutoCancel(true)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setLights(Notification.DEFAULT_LIGHTS, 200, 3000)
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    .setSmallIcon(R.drawable.ic_action_user)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(tipoNotificacion, builder.build());
        }
    }

    /**
     * Este hilo servira para ver si el canal sigue abierto
     */

    private class ThComprobadorCanal extends Thread{
        @Override
        public void run() {
            try{
                while(true){
                    try {
                        Thread.sleep(300000);
                        ManejadorConexion.mandaMsj(new Mensaje(Mensaje.CANAL_PREGUNTA
                                , -1, null, null, null, null, null));

                    } catch (InterruptedException e) {e.printStackTrace();}
                }
            }catch(Exception e){
                Log.e(context.getResources().getString(R.string.conexion_error), context.getResources().getString(R.string.serverDisconnected));
                cierraRecursos();
            }
        }
    }
}
