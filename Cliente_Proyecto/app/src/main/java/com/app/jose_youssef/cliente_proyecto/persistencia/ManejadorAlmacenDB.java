package com.app.jose_youssef.cliente_proyecto.persistencia;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.app.jose_youssef.cliente_proyecto.conexion.ThConexion;
import com.app.jose_youssef.cliente_proyecto.control.foro.Post;
import com.app.jose_youssef.cliente_proyecto.control.foro.post.MensajePost;
import com.app.jose_youssef.cliente_proyecto.control.foro.post.PostActivity;
import com.app.jose_youssef.cliente_proyecto.control.MensajeCorto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


import protocolo.Mensaje;

/**
 * Servirá de intermediario para el crud con la bd
 * Created by Youss on 13/04/2017.
 */

public class ManejadorAlmacenDB {

    private static int idUsuario = -1;

    private static final String INSERT_MENSAJE = "insert into Mensaje (asunto, contenido, " +
            "fecha, emisor, receptor, tipoMensaje, idUsuario) values (?, ?, ?, ?, ?, ?, ?);";

    private static final String SELECT_MENSAJE = "select asunto, contenido, fecha, " +
            "emisor from Mensaje where tipoMensaje = '";

    private static final String FROM_MENSAJE_FORO = "from Mensaje where tipoMensaje = '" +
            AlmacenSQLiteOpenHelper.MENSAJE_FORO + "' AND receptor = '";

    private static final String SELECT_USUARIO = "select id from Usuario where nombre = ?;";
    private static final String INSERT_USUARIO = "insert into Usuario (nombre) values (?);";

    private static final String SELECT_ASIGNATURA_IDUSUARIO = "select nombre from Asignatura where id = ? order by nombre;";
    private static final String SELECT_ASIGNATURA_NOMBRE = "select id from Asignatura where nombre = ?;";
    private static final String INSERT_ASIGNATURA = "insert into Asignatura (nombre) values (?);";

    private static final String SELECT_ASISTEA = "select idAsignatura from asisteA where idUsuario = ";
    private static final String INSERT_ASISTEA = "insert into asisteA (idUsuario, idAsignatura) values (?, ?);";

    private static Context context;
    private static SQLiteDatabase db;

    /**
     * Inicializa los atributos, como si fuera el constructor
     * @param _context de la aplicacion
     */

    public static void inicializaDBAlmacen(Context _context, String usuario){
        if(db == null || !db.isOpen()){
            context = _context;
            SQLiteOpenHelper sqLiteOpenHelper = new AlmacenSQLiteOpenHelper(context, "almacen.db", null, 1);
            db = sqLiteOpenHelper.getReadableDatabase();
            cargaUsuario(usuario);
        }
    }

    /**
     * guardo el id del usuario conectado, o lo creo si no esta
     * @param usuario
     */
    private static void cargaUsuario(String usuario) {
        SQLiteStatement sentencia;
        sentencia = db.compileStatement(SELECT_USUARIO);
        sentencia.bindString(1, usuario);
        try{
            idUsuario = (int) sentencia.simpleQueryForLong();
        }catch (SQLiteDoneException e){
            sentencia = db.compileStatement(INSERT_USUARIO);
            sentencia.bindString(1, usuario);
            sentencia.execute();
            cargaUsuario(usuario);
        }
    }

    /**
     * Devuelve la lista de asignaturas de un usuario
     * @return la lista de asignaturas, sino hay, null
     */

    public static List<String> getAsignaturas(){
        Cursor c = db.rawQuery(SELECT_ASISTEA + idUsuario + ";", null);
        List<String> listAsig = new ArrayList<>();
        while(c.moveToNext()){
            SQLiteStatement sentencia;
            sentencia = db.compileStatement(SELECT_ASIGNATURA_IDUSUARIO);
            sentencia.bindString(1, c.getString(0));
            listAsig.add(sentencia.simpleQueryForString());
        }
        Collections.sort(listAsig);
        List<String> listaOrdenada = new ArrayList<>();
        for (int i = listAsig.size()-1; i >= 0; i--) {
            listaOrdenada.add(listAsig.get(i));
        }
        return (listaOrdenada.size() == 0) ? null : listaOrdenada;
    }

    /**
     * Devuelve el id del nombre de una asignatura
     * @param asignatura
     * @return el id de la asignatura
     */

    private static int getIdAsignatura(String asignatura){
        SQLiteStatement sentencia;
        sentencia = db.compileStatement(SELECT_ASIGNATURA_NOMBRE);
        sentencia.bindString(1, asignatura);
        int id = -1;
        try{
            id = (int) sentencia.simpleQueryForLong();
        }catch (SQLiteDoneException e){
            sentencia = db.compileStatement(INSERT_ASIGNATURA);
            sentencia.bindString(1, asignatura);
            sentencia.execute();
            id = getIdAsignatura(asignatura);
        }
        return id;
    }

    /**
     * Mete en la bd las asignaturas
     * @param asignaturas
     */

    public static void setAsignaturas(HashSet<String> asignaturas){
        List<Integer> listId = new ArrayList<>();
        for(String asig : asignaturas){
            listId.add(getIdAsignatura(asig));
        }
        SQLiteStatement sentencia;
        for(int id : listId){
            sentencia = db.compileStatement(INSERT_ASISTEA);
            sentencia.bindLong(1, idUsuario);
            sentencia.bindLong(2, id);
            sentencia.execute();
        }
    }

    /**
     * Devuelve los post de una item_asignatura
     * @param asignatura
     * @return
     */

    public static ArrayList<Post> getPosts(String asignatura){
        String postsAsignatura = FROM_MENSAJE_FORO + asignatura + "' AND idUsuario = " + idUsuario + " ";

        String sql = "select asunto " + postsAsignatura + "group by asunto";

        Cursor c = db.rawQuery(sql, null);
        ArrayList<Post> alPost = new ArrayList<>();
        sql = "select emisor, max(fecha) " + postsAsignatura + " AND asunto = '";
        while(c.moveToNext()){
            String sql2 = sql + c.getString(0) + "';";
            Cursor c1 = db.rawQuery(sql2, null);
            if(c1.moveToFirst()){
                Cursor c2 = db.rawQuery("select count(id) " + postsAsignatura + " AND asunto = '" + c.getString(0) + "';", null);
                if(c2.moveToFirst()){
                    alPost.add(new Post(c.getString(0), c1.getString(1), c1.getString(0), c2.getInt(0)));
                }
            }
        }
        Collections.sort(alPost);
        return alPost;
    }

    /**
     * Devuelve un array con todos los msjs de difusion
     * de una item_asignatura determinada.
     * @param asignatura, de la que sacaremos los mensajes
     * @return ArrayList<MensajeCorto>
     */

    public static ArrayList<MensajeCorto> getDifusiones(String asignatura){
        String sql = SELECT_MENSAJE + AlmacenSQLiteOpenHelper.MENSAJE_DIFUSION +
                "' AND receptor = '" + asignatura + "' AND idUsuario = " + idUsuario + " order by id;";

        return rellenaArray(asignatura, sql);
    }

    /**
     * Devuelve un array con los mensajes de un post determinado
     * @param post nombre del post
     * @param asignatura normalmente la item_asignatura a la que esta dirigido
     * @return
     */

    public static ArrayList<MensajePost> getMensajesPost(String post, String asignatura){
        String sql = SELECT_MENSAJE + AlmacenSQLiteOpenHelper.MENSAJE_FORO +
                "' AND receptor = '" + asignatura + "' AND asunto = '" + post + "' AND idUsuario = " + idUsuario + " order by id;";

        Cursor c = db.rawQuery(sql, null);
        ArrayList<MensajePost> alMensajes = new ArrayList<>();
        MensajePost msj;
        while(c.moveToNext()){
            msj = new MensajePost(c.getString(3), c.getString(2));
            String []contenido = c.getString(1).split(PostActivity.SEPARADOR_MENSAJES);
            if(contenido.length == 3){
                msj.setUsuarioContestado(contenido[1]);
                msj.setMensajeContestado(contenido[2]);
            }
            msj.setMensaje(contenido[0]);

            alMensajes.add(msj);
        }
        Collections.sort(alMensajes);
        return alMensajes;
    }

    @NonNull
    private static ArrayList<MensajeCorto> rellenaArray(String asignatura, String sql) {
        Cursor c = db.rawQuery(sql, null);
        ArrayList<MensajeCorto> alMensajes = new ArrayList<>();
        while(c.moveToNext()){
            MensajeCorto msjC = new MensajeCorto(c.getString(0), c.getString(1), c.getString(2)
                    , c.getString(3), asignatura);
            alMensajes.add(msjC);
        }
        return alMensajes;
    }

    /**
     * Añade un msj a la db.
     * Uso el statement para evitar inyecciones sql;
     * @param msj mensaje que vamos a añadir.
     * @param tipo del mensaje
     */

    public static void insertaMensaje(MensajeCorto msj, String tipo){
        SQLiteStatement sentencia;
        sentencia = db.compileStatement(INSERT_MENSAJE);
        sentencia.bindString(1, msj.getAsunto());
        sentencia.bindString(2, msj.getContenido());
        sentencia.bindString(3, msj.getFecha());
        sentencia.bindString(4, msj.getEmisor());
        sentencia.bindString(5, msj.getReceptor());
        sentencia.bindString(6, tipo);
        sentencia.bindLong(7, idUsuario);

        sentencia.execute();
    }

    /**
     * Sera usado para añadir mensajes del buzon del servidor pendientes
     * a local
     * @param msjs
     */

    public static void annadeMensajesBuzon(HashSet<Mensaje> msjs){
        if(msjs != null){
            //TODO es una chapuza hay que mandarlos ordenados desde el servidor
            ArrayList<Mensaje> listaMensajes = new ArrayList<>(msjs);
            Collections.sort(listaMensajes, new Comparator<Mensaje>() {
                @Override
                public int compare(Mensaje m1, Mensaje m2) {
                    return m1.getFecha().compareTo(m2.getFecha());
                }
            });

            for(Mensaje msj : listaMensajes){
                MensajeCorto mC = new MensajeCorto(msj.getAsunto(), (String) msj.getContenido(),
                        ThConexion.DF_MINUTE.format(msj.getFecha()), msj.getEmisor(), (String)msj.getReceptor());
                if(msj.getTipo() == Mensaje.MENSAJE_DIFUSION){
                    insertaMensaje(mC, AlmacenSQLiteOpenHelper.MENSAJE_DIFUSION);
                }else if(msj.getTipo() == Mensaje.MENSAJE_FORO){
                    insertaMensaje(mC, AlmacenSQLiteOpenHelper.MENSAJE_FORO);
                }
            }
        }
    }

    /**
     * Cierra la db.
     */

    public static void cierraDB(){
        if(db != null && db.isOpen()){
            db.close();
        }
    }
}
