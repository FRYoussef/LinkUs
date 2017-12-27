package com.app.jose_youssef.cliente_proyecto.control;

/**
 * Created by yelfaqir on 11/06/2017.
 */

public interface IUtilidades {

    /**
     * Comprueba si la conexión se ha establecido
     */

    void compruebaConexion();

    /**
     * Notifica a la vista que hay conexión
     */

    void estadoConectado();

    /**
     * Notifica a la vista que pase a estado desconectado
     */

    void estadoDesconectado();
}
