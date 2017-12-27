package com.app.jose_youssef.cliente_proyecto.control;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorConexion;
import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.NotificacionesService;
import com.app.jose_youssef.cliente_proyecto.control.asignatura.AsignaturasActivity;
import com.app.jose_youssef.cliente_proyecto.observable.ManejadorMensajeOb;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorArchivoConfig;

import protocolo.Mensaje;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends AppCompatActivity implements Observer, IUtilidades{

    private EditText etUsuario;
    private EditText etContrasenna;
    private TextView tvNotificacion;
    private CheckBox cbSesion;
    private Toolbar toolbar;
    private CardView cvLogIn;
    private ProgressBar pbConectando;

    public static final String KEY_USUARIO = "usuario";
    public static final String KEY_TIPO_USUARIO = "tipo de usuario";
    private String usuario = null;
    private String contrasena = null;
    private int tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        pbConectando = (ProgressBar) findViewById(R.id.pbConectando);
        cvLogIn = (CardView) findViewById(R.id.cvLogIn);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        etContrasenna = (EditText) findViewById(R.id.etContrasenna);
        tvNotificacion = (TextView) findViewById(R.id.tvNotificacion);
        cbSesion = (CheckBox) findViewById(R.id.cbSesion);

        ManejadorMensajeOb.inicializaMensajeOb();
    }

    @Override
    protected void onStart() {
        super.onStart();
        estadoDesconectado();
        compruebaConexion();
    }

    @Override
    public void compruebaConexion(){
        startService(new Intent(getApplicationContext(), NotificacionesService.class));
        Utilidades.compruebaConexion(LoginActivity.this);
    }

    @Override
    public void estadoConectado() {
        ManejadorMensajeOb.annadeObserver(this);
        cargaUsuario();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pbConectando.setVisibility(View.GONE);
                cvLogIn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void estadoDesconectado() {
        ManejadorConexion.cierraRecursos();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cvLogIn.setVisibility(View.GONE);
                pbConectando.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * Manda al servidor una peticion de logueo y comprueba que los campos
     * han sido rellenados.
     * @param view
     */

    public void onClickEnviar(View view) {
        usuario = etUsuario.getText().toString();
        contrasena = etContrasenna.getText().toString();

        //compruebo que haya algo en los campos
        if(usuario.equals("")){
            etUsuario.setHintTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
        }else if(contrasena.equals("")){
            etContrasenna.setHintTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
        }else{
            try {
                ManejadorConexion.mandaMsj(new Mensaje(Mensaje.PETICION_LOGIN, -1, null, usuario,
                                                    null, null,contrasena));
            } catch (IOException e) {
                e.printStackTrace();
            }

            etUsuario.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));
            etContrasenna.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));
            etUsuario.setText("");
            etContrasenna.setText("");
        }
    }

    /**
     * Me va ha servir para cargar el usuario en memoria,
     * que sera si esta activada la opcion previa para autologueo
     */

    private void cargaUsuario(){
        usuario = ManejadorArchivoConfig.dameUsuario(getApplicationContext());
        tipoUsuario = ManejadorArchivoConfig.dameTipoUsuario(getApplicationContext());
        if(tipoUsuario != -1 && !usuario.equals(""))
            siguientePantalla();
    }

    /**
     * Prepara el fin de esta actividad para pasar a la siguiente
     */

    private void siguientePantalla(){
        ManejadorMensajeOb.eliminaObserver(this);

        Activity act = LoginActivity.this;
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext()
                , R.anim.anim_activity_asignatura_in, 0);

        Intent intent = new Intent(act, AsignaturasActivity.class);
        intent.putExtra(KEY_USUARIO, usuario);
        intent.putExtra(KEY_TIPO_USUARIO, tipoUsuario);
        startActivity(intent, options.toBundle());
        finish();
    }

    /**
     * En funcion de si el login se ha completado satisfactoriamente o no
     * mostraremos un msj de error, o pasaremos a la siguiente pantalla.
     * @param observable
     * @param o tipo del msj recibido
     */

    @Override
    public void update(Observable observable, Object o) {
        Mensaje msj = (Mensaje) o;

        if(msj.getTipo() == Mensaje.LOGIN_INCORRECTO){
            muestraError("Usuario o contraseña incorrectos");
        }
        else if(msj.getTipo() == Mensaje.MENSAJES_BUZON){
            try {
                ManejadorConexion.mandaMsj(new Mensaje(Mensaje.TIPO_USUARIO, -1, null, usuario,
                        null, null, null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(msj.getTipo() == Mensaje.TIPO_USUARIO){
            tipoUsuario = (Integer) msj.getContenido();
            //el usuario quiere autologueo
            if(cbSesion.isChecked()){
                ManejadorArchivoConfig.guardaSesion(usuario, contrasena, getApplicationContext());
                ManejadorArchivoConfig.guardaTipoUsuario(tipoUsuario, getApplicationContext());
            }
            siguientePantalla();
        }
    }

    /**
     * Muestra en pantalla unn error para notificar al usuario.
     * @param error
     */

    private void muestraError(final String error){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvNotificacion.setTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
                tvNotificacion.setText(error);
            }
        });
    }
}
