package com.app.jose_youssef.cliente_proyecto.persistencia;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Usare esta clase para leer y escribir en un archivo la configuracion
 * de la aplicacion
 * Created by Youss on 14/04/2017.
 */

public class ManejadorArchivoConfig {

    private static final String KEY_USUARIO = "usuario";
    private static final String KEY_CONTRASENA = "contrasena";
    private static final String KEY_TIPO_USUARIO = "tipo usuario";
    private static final String ARCHIVO = "configuracion";

    /**
     * Guarda el usuario y contraseña, para mantener la sesion.
     * @param usuario
     * @param contrasena
     * @param context
     */

    public static void guardaSesion(String usuario, String contrasena, Context context){
        SharedPreferences sP = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();

        editor.putString(KEY_USUARIO, usuario);
        editor.putString(KEY_CONTRASENA, contrasena);

        editor.commit();
    }

    /**
     * Guarda el tipo de usuario, para mantener la sesion.
     * @param tipoUsuario
     * @param context
     */

    public static void guardaTipoUsuario(int tipoUsuario, Context context){
        SharedPreferences sP = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sP.edit();

        editor.putInt(KEY_TIPO_USUARIO, tipoUsuario);

        editor.commit();
    }

    /**
     * Devuelve el usuario guardado en el archivo, sino ""
     * @param context
     * @return usuario
     */

    public static String dameUsuario(Context context){
        SharedPreferences sP = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        return sP.getString(KEY_USUARIO, "");
    }

    /**
     * Devuelve la contraseña del usuario, sino ""
     * @param context
     * @return contrasena
     */

    public static String dameContrasena(Context context){
        SharedPreferences sP = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        return sP.getString(KEY_CONTRASENA, "");
    }

    /**
     * Devuelve el tipo de usuario, sino -1
     * @param context
     * @return tipoUsuario
     */
    public static int dameTipoUsuario(Context context){
        SharedPreferences sP = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        return sP.getInt(KEY_TIPO_USUARIO, -1);
    }

    /**
     * Elimina todos los datos del archivo
     * @param context
     */

    public static void eliminaDatos(Context context){
        SharedPreferences sP = context.getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
        sP.edit().clear().commit();
    }
}
