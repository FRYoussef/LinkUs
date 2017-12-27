package com.app.jose_youssef.cliente_proyecto.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Youss on 13/04/2017.
 */

public class AlmacenSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String TABLE_TIPO_MENSAJE = "CREATE TABLE TipoMensaje(" +
                                                        "tipo TEXT PRIMARY KEY);";

    private final static String TABLE_MENSAJE = "CREATE TABLE Mensaje(" +
                                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                "asunto TEXT," +
                                                "contenido TEXT," +
                                                "fecha TEXT," +
                                                "emisor TEXT," +
                                                "receptor TEXT," +
                                                "tipoMensaje TEXT," +
                                                "idUsuario INTEGER," +
            "CONSTRAINT FK_Mensaje_tipoMensaje FOREIGN KEY (tipoMensaje) REFERENCES " +
            "TipoMensaje (tipo) ON DELETE CASCADE ON UPDATE CASCADE, " +
            "CONSTRAINT FK_Mensaje_idUsuario FOREIGN KEY (idUsuario) REFERENCES Usuario (id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE);";

    private final static String TABLE_USUARIO = "CREATE TABLE Usuario(" +
                                                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                "nombre TEXT);";

    private final static String TABLE_ASIGNATURA = "CREATE TABLE Asignatura(" +
                                                   "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                   "nombre TEXT);";

    private final static String TABLE_ASISTEA = "CREATE TABLE asisteA(" +
                                                "idUsuario INTEGER," +
                                                "idAsignatura INTEGER," +
            "CONSTRAINT PK_asisteA_idUsuario_idAsignatura PRIMARY KEY (idUsuario, idAsignatura), " +
            "CONSTRAINT FK_asisteA_idUsuario FOREIGN KEY (idUsuario) REFERENCES Usuario (id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE, " +
            "CONSTRAINT FK_asisteA_idAsignatura FOREIGN KEY (idAsignatura) REFERENCES Asignatura (id) " +
            "ON DELETE CASCADE ON UPDATE CASCADE);";

    public final static String MENSAJE_DIFUSION = "Difusion";
    public final static String MENSAJE_FORO = "Foro";
    private final static String INSERT_TIPO_MENSAJE = "insert into TipoMensaje values (";
    private final static String []SQL = {TABLE_TIPO_MENSAJE, TABLE_USUARIO,TABLE_MENSAJE, TABLE_ASIGNATURA, TABLE_ASISTEA,INSERT_TIPO_MENSAJE +
                   "'" + MENSAJE_DIFUSION + "');", INSERT_TIPO_MENSAJE + "'" + MENSAJE_FORO + "');"};

    public AlmacenSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(String sql : SQL){
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }
}
