package com.app.jose_youssef.cliente_proyecto.control.asignatura;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorReceiverMensaje;
import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorConexion;
import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.ThConexion;
import com.app.jose_youssef.cliente_proyecto.control.ComunicacionActivity;
import com.app.jose_youssef.cliente_proyecto.control.LoginActivity;
import com.app.jose_youssef.cliente_proyecto.observable.ManejadorMensajeOb;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorAlmacenDB;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorArchivoConfig;

import protocolo.Mensaje;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AsignaturasActivity extends AppCompatActivity implements Observer {
    public static final String KEY_ASIGNATURA = "item_asignatura";

    private String asignatura = "";
    private String usuario = null;
    private int tipoUsuario;
    private List<String> listaAsignaturas = null;
    private RecyclerView rvAsignaturas;
    private AsignaturaAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Animation anim;
    private Toolbar toolbar;

    @Override

    /**
     * Recojo los elementos gráficos, y los valores del intent.
     */

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignaturas);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.subjects);

        Intent intent = getIntent();
        usuario = intent.getStringExtra(LoginActivity.KEY_USUARIO);
        tipoUsuario = intent.getIntExtra(LoginActivity.KEY_TIPO_USUARIO, -1);

        ManejadorAlmacenDB.inicializaDBAlmacen(getApplicationContext(), usuario);
        listaAsignaturas = ManejadorAlmacenDB.getAsignaturas();
        if(listaAsignaturas == null)
            listaAsignaturas = new ArrayList<>();

        rvAsignaturas = (RecyclerView) findViewById(R.id.rvAsignaturas);
        adapter = new AsignaturaAdapter(this, listaAsignaturas);
        rvAsignaturas.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        rvAsignaturas.setLayoutManager(layoutManager);
        rvAsignaturas.setItemAnimator(new DefaultItemAnimator());
        rvAsignaturas.scrollToPosition(listaAsignaturas.size()-1);
        annadeClicks();

        cargaAnimacion();

        ManejadorMensajeOb.annadeObserver(this);

        //vemos si ya teniamos asignaturas guardadas, sino las pedimos al servidor
        if(listaAsignaturas.size() == 0){
            try {
                ManejadorConexion.mandaMsj(new Mensaje(Mensaje.LISTA_ASIGNATURAS, -1, null,
                                usuario, null, null, null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga la animacion, y annade su manejador
     */

    private void cargaAnimacion(){
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_asignatura);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                siguientePantalla();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    /**
     * Actualiza la vista con las asignaturas
     */

    private void muestraAsignaturas(ArrayList<String>asignaturas){
        for(int i = asignaturas.size()-1; i >= 0; i--)
            listaAsignaturas.add(asignaturas.get(i));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                rvAsignaturas.scrollToPosition(AsignaturasActivity.this.listaAsignaturas.size()-1);
            }
        });
    }

    /**
     * Annade al manejador de eventos la funcion de click para iniciar
     * la animacion
     */

    private void annadeClicks(){
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asignatura = (String) v.findViewById(R.id.tvAsignatura).getTag();
                v.findViewById(R.id.cvAsignatura).startAnimation(anim);
            }
        });
    }

    /**
     * Pasa a la siguiente pantalla
     */

    private void siguientePantalla(){
        Activity act = AsignaturasActivity.this;
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(getApplicationContext()
                , R.anim.anim_activity_comunicacion_in, R.anim.anim_activity_comunicacion_out);

        Intent intent2 = new Intent(act, ComunicacionActivity.class);
        intent2.putExtra(KEY_ASIGNATURA, asignatura);
        intent2.putExtra(LoginActivity.KEY_USUARIO, usuario);
        intent2.putExtra(LoginActivity.KEY_TIPO_USUARIO, tipoUsuario);
        startActivityForResult(intent2, 2, options.toBundle());
    }

    /**
     * Añade a la vista las asignaturas asociadas
     * @param observable
     * @param o mensaje recibido del servidor
     */

    @Override
    public void update(Observable observable, Object o) {
        Mensaje msj = (Mensaje) o;
        //TODO es una chapuza hay que mandarlos ordenados desde el servidor
        //TODO hay que ver porque se llama dos veces a este metodo
        if(msj.getTipo() == Mensaje.LISTA_ASIGNATURAS && listaAsignaturas.size() == 0){
            ArrayList<String> asignaturas = new ArrayList<>((HashSet<String>) msj.getContenido());
            Collections.sort(asignaturas);
            muestraAsignaturas(asignaturas);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                ManejadorConexion.cierraRecursos();
                ManejadorArchivoConfig.eliminaDatos(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        ManejadorReceiverMensaje.registra(getApplicationContext());
    }

    @Override
    public void onPause(){
        super.onPause();
        ManejadorReceiverMensaje.borra(getApplicationContext());
    }
}
