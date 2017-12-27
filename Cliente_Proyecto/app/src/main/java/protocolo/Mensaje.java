package protocolo;

import java.io.Serializable;
import java.sql.Date;

/**
 * La siguiente clase nos servirá para establecer un protocolo de comunicación
 * entre el cliente y el servidor.
 *
 * @author Youssef
 */

public class Mensaje implements Serializable{

    private static final long serialVersionUID = -5029030130391173279L;


    //Mensaje comodin de error, simplemente será ignorado
    public final static int ERROR = -1;

    /*
     * Cuando se conecte al servidor.
     * De momento lo haremos así, pero en el
     * futuro habrá que realizar un logueo más robusto.
     */
    public final static int PETICION_LOGIN = 0;

    //anuncia que el loguin no ha podido realizarse
    public final static int LOGIN_INCORRECTO = 1;

    // devuelve los msjs del buzon
    public final static int MENSAJES_BUZON = 2;

    // pide o devuelve, el tipo de usuario
    public final static int TIPO_USUARIO = 3;

    /*
     * En caso de ser necesario el servidor mandará las asignaturas a las
     * que pertenece el cliente.
     * Puede usarse en el sentido contrario para recuperar desde el cliente sus
     * asignaturas.
     */
    public final static int LISTA_ASIGNATURAS = 4;

    // Anuncia un mensaje de for
    public final static int MENSAJE_FORO = 5;

    // Anuncia un mensaje de difusion
    public final static int MENSAJE_DIFUSION = 6;

    // Mensaje para comprobar que el canal sigue abierto
    public final static int CANAL_PREGUNTA = 10;
    public final static int MENSAJE_RECIBIDO = 11;

    ///////////////////////////////////////////////////////

    //Estas constantes irán en el atributo "contenido", e identifican el tipo de usuario

    public final static int USUARIO_DESCONOCIDO = -1;
    public final static int USUARIO_PROFESOR = 0;
    public final static int USUARIO_ALUMNO = 1;


    private int tipo;
    private int id;
    private String asunto;
    private String emisor;
    private Object receptor;
    private Date fecha;
    private Object contenido;

    /**
     *
     * @param _tipo Identifica el tipo de mensaje
     * @param _id Identifica el mensaje en la bd
     * @param _asunto nos servirá para dar un contexto a los msjs
     * 			por ejemplo, para post de un foro
     * @param _emisor Contiene el nombre del emisor del mensaje
     * @param _receptor Destinatario del mensaje
     * @param _fecha con formato universal, del mensaje
     * @param _contenido El contenido del mensaje puede ser de diferentes tipos, de ahí su tipo
     *
     * @version: 22/04/2017 -> v.4
     */

    public Mensaje(int _tipo, int _id, String _asunto, String _emisor, Object _receptor, Date _fecha, Object _contenido){
        tipo = _tipo;
        id = _id;
        asunto = _asunto;
        emisor = _emisor;
        receptor = _receptor;
        fecha = _fecha;
        contenido = _contenido;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public Object getReceptor() {
        return receptor;
    }

    public void setReceptor(Object receptor) {
        this.receptor = receptor;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Object getContenido() {
        return contenido;
    }

    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
