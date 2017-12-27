package com.app.jose_youssef.cliente_proyecto.control.foro.post;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorConexion;
import com.app.jose_youssef.cliente_proyecto.conexion.ThConexion;
import com.app.jose_youssef.cliente_proyecto.control.asignatura.AsignaturasActivity;
import com.app.jose_youssef.cliente_proyecto.control.foro.ForoFragment;
import com.app.jose_youssef.cliente_proyecto.control.LoginActivity;
import com.app.jose_youssef.cliente_proyecto.observable.ManejadorMensajeOb;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorAlmacenDB;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import protocolo.Mensaje;

public class PostActivity extends AppCompatActivity implements Observer{

    public final static String SEPARADOR_MENSAJES = "</-/>";

    private String asignatura;
    private String usuario;
    private String post;
    private String usuarioMensajeMarcado = "";
    private String mensajeMarcado = "";

    private RecyclerView rvMensajes;
    private MensajePostAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MensajePost> lMensajes;
    private View marcado = null;

    private EditText etMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        asignatura = intent.getStringExtra(AsignaturasActivity.KEY_ASIGNATURA);
        usuario = intent.getStringExtra(LoginActivity.KEY_USUARIO);
        post = intent.getStringExtra(ForoFragment.KEY_POST);

        lMensajes = ManejadorAlmacenDB.getMensajesPost(post, asignatura);

        rvMensajes = (RecyclerView) findViewById(R.id.rvMensajes);
        adapter = new MensajePostAdapter(this, lMensajes);
        rvMensajes.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(layoutManager);
        rvMensajes.addItemDecoration(new DividerItemDecoration(rvMensajes.getContext(), DividerItemDecoration.VERTICAL));
        rvMensajes.setItemAnimator(new DefaultItemAnimator());
        rvMensajes.scrollToPosition(lMensajes.size()-1);

        etMensaje = (EditText) findViewById(R.id.etMensaje);
        ((TextView)findViewById(R.id.tvNombrePost)).setText(post);

        annadeOnClicks();
        annadeOnLongClicks();
        ManejadorMensajeOb.annadeObserver(this);
    }

    /**
     * Annade los eventos click a los mensajes
     */

    private void annadeOnClicks() {
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mensajeMarcado.equals("") && marcado != null){
                    usuarioMensajeMarcado = "";
                    mensajeMarcado = "";
                    marcado.findViewById(R.id.vMarcado).setVisibility(View.GONE);
                    marcado = null;
                }
            }
        });
    }

    /**
     * Annade los eventos onLongClick a los mensajes
     */

    private void annadeOnLongClicks() {
        adapter.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!mensajeMarcado.equals("")){
                    usuarioMensajeMarcado = "";
                    mensajeMarcado = "";
                    marcado.findViewById(R.id.vMarcado).setVisibility(View.GONE);
                    marcado = null;
                    return false;
                }
                mensajeMarcado = ((TextView)v.findViewById(R.id.tvMensaje)).getText().toString();
                usuarioMensajeMarcado = ((TextView)v.findViewById(R.id.tvNombre)).getText().toString();
                marcado = v;
                marcado.findViewById(R.id.vMarcado).setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    /**
     * Manda un msj al servidor.
     * Gestiona si se ha marcado otro mensaje o si se ha rellenado el campo
     * @param view
     */

    public void onClickMandaMsj(View view) {
        String contenido = etMensaje.getText().toString();
        if(contenido.equals("")){
            etMensaje.setHintTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
            return;
        }
        etMensaje.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));
        etMensaje.setText("");

        StringBuilder mensaje = new StringBuilder(contenido.replace(SEPARADOR_MENSAJES, ""));
        if(!usuarioMensajeMarcado.equals("") && !mensajeMarcado.equals("")){
            mensaje.append(SEPARADOR_MENSAJES+usuarioMensajeMarcado+SEPARADOR_MENSAJES+mensajeMarcado);
            usuarioMensajeMarcado = "";
            mensajeMarcado = "";
            marcado.findViewById(R.id.vMarcado).setVisibility(View.GONE);
            marcado = null;
        }

        try {
            ManejadorConexion.mandaMsj(new Mensaje(Mensaje.MENSAJE_FORO, -1, post, usuario, asignatura,
                    new Date(System.currentTimeMillis()), mensaje.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.error_post_creation, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Actualiza la vista cuando llega algun cambio
     * @param ob
     * @param o
     */

    @Override
    public void update(Observable ob, Object o) {
        Mensaje msj = (Mensaje) o;
        if(msj.getTipo() == Mensaje.MENSAJE_FORO && msj.getReceptor().equals(asignatura) && msj.getAsunto().equals(post))
        {
            mustraMensaje(mensajeToMensajePost(msj));
        }
    }

    private void mustraMensaje(MensajePost mensajePost) {
        lMensajes.add(mensajePost);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(lMensajes.size()-1);
                rvMensajes.scrollToPosition(lMensajes.size()-1);
            }
        });
    }

    /**
     * Convierte un objeto de tipo Mensaje a MensajePost
     * @param msj
     * @return
     */

    public static MensajePost mensajeToMensajePost(Mensaje msj){
        MensajePost msjP = new MensajePost(msj.getEmisor(), ThConexion.DF_MINUTE.format(msj.getFecha()));
        String []contenido = ((String)msj.getContenido()).split(SEPARADOR_MENSAJES);
        if(contenido.length == 3){
            msjP.setUsuarioContestado(contenido[1]);
            msjP.setMensajeContestado(contenido[2]);
        }
        msjP.setMensaje(contenido[0]);
        return msjP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManejadorMensajeOb.eliminaObserver(this);
    }
}
