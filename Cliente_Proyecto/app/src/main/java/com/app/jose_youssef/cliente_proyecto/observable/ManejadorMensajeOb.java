package com.app.jose_youssef.cliente_proyecto.observable;

import java.util.Observer;

/**
 * Singelton, para que se puedan añadir observadores ellos mismos.
 * Created by Youss on 09/04/2017.
 */

public class ManejadorMensajeOb {

    private static OMensajeRecibido oMsj;

    public static void inicializaMensajeOb(){
        if(oMsj == null)
            oMsj = new OMensajeRecibido();
    }

    public static void borraObservadores() {
        if (oMsj != null){
            oMsj.deleteObservers();
            oMsj = null;
        }
    }

    public static void annadeObserver(Observer o){
        oMsj.addObserver(o);
    }

    public static void eliminaObserver(Observer o){
        oMsj.deleteObserver(o);
    }

    public static OMensajeRecibido getoMsj() {
        return oMsj;
    }

    public static void setoMsj(OMensajeRecibido _oMsj) {
        oMsj = _oMsj;
    }
}
