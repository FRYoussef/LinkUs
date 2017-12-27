package com.app.jose_youssef.cliente_proyecto.conexion;

import android.os.StrictMode;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import protocolo.Mensaje;

/**
 * Usaremos este manejador para disponer de los recursos de la conexión.
 * Created by Youss on 29/03/2017.
 */

public class ManejadorConexion {

    private final static int PUERTO = 34123;
    private final static String DOMINIO = "192.168.1.92";
    private final static int TIMEOUT = 4000;

    private static Socket socket = null;
    private static ObjectInputStream entrada = null;
    private static ObjectOutputStream salida = null;
    public static boolean conectado = false;

    /**
     * Lo usaremos para iniciar la conexión con el servidor
     */

    public static void inicializaRecursos() throws IOException{
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        socket = new Socket();
        InetSocketAddress ia = new InetSocketAddress(DOMINIO, PUERTO);
        socket.connect(ia, TIMEOUT);
        salida = new ObjectOutputStream (socket.getOutputStream());
        entrada = new ObjectInputStream (socket.getInputStream());
        conectado = true;
    }


    /**
     * Manda un msj al servidor conectado
     * @param msj a enviar
     */

    public static void mandaMsj(Mensaje msj) throws IOException{
        if(salida != null)
            salida.writeObject(msj);
    }

    /**
     * Lee un msj que manda el servidor
     * @return un msj del servidor
     */

    public static Mensaje leeMsj() throws IOException, ClassNotFoundException{
            return (Mensaje) entrada.readObject();
    }

    /**
     * Cierra los recursos de la conexión
     */

    public static void cierraRecursos(){

        conectado = false;
        if(socket != null) try{socket.close();}catch(IOException e) {e.printStackTrace();}
        if(entrada != null) try{entrada.close();}catch(IOException e) {e.printStackTrace();}
        if(salida != null) try{salida.close();}catch(IOException e) {e.printStackTrace();}
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket _socket) {
        socket = _socket;
    }

    public static ObjectInputStream getEntrada() {
        return entrada;
    }

    public static void setEntrada(ObjectInputStream _entrada) {
        entrada = _entrada;
    }

    public static ObjectOutputStream getSalida() {
        return salida;
    }

    public static void setSalida(ObjectOutputStream _salida) {
        salida = _salida;
    }
}
