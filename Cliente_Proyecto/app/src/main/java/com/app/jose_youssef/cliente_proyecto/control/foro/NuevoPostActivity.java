package com.app.jose_youssef.cliente_proyecto.control.foro;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorConexion;
import com.app.jose_youssef.cliente_proyecto.control.asignatura.AsignaturasActivity;
import com.app.jose_youssef.cliente_proyecto.control.LoginActivity;

import java.io.IOException;
import java.sql.Date;

import protocolo.Mensaje;

public class NuevoPostActivity extends AppCompatActivity {

    private String asignatura;
    private String usuario;

    private EditText etAsunto;
    private EditText etMensaje;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_post);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.new_post));

        etAsunto = (EditText) findViewById(R.id.etAsunto);
        etMensaje = (EditText) findViewById(R.id.etMensaje);

        Intent intent = getIntent();
        asignatura = intent.getStringExtra(AsignaturasActivity.KEY_ASIGNATURA);
        usuario = intent.getStringExtra(LoginActivity.KEY_USUARIO);
    }

    /**
     * Comprueba si los campos han sido rellenados, si es asi manda un msj de foro
     * @param view
     */

    public void onClickGuardaPost(View view) {
        String asunto = etAsunto.getText().toString();
        String mensaje = etMensaje.getText().toString();

        if(asunto.equals("")){
            etAsunto.setHintTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
            return;
        }
        else if(mensaje.equals("")){
            etAsunto.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));
            etMensaje.setHintTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
            return;
        }
        etAsunto.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));
        etMensaje.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));

        try {
            ManejadorConexion.mandaMsj(new Mensaje(Mensaje.MENSAJE_FORO, -1, asunto, usuario, asignatura,
                        new Date(System.currentTimeMillis()), mensaje));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.error_post_creation, Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}
