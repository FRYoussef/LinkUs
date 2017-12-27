package com.app.jose_youssef.cliente_proyecto.control.difusion;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.ManejadorConexion;
import com.app.jose_youssef.cliente_proyecto.conexion.ThConexion;
import com.app.jose_youssef.cliente_proyecto.control.LoginActivity;
import com.app.jose_youssef.cliente_proyecto.control.MensajeCorto;
import com.app.jose_youssef.cliente_proyecto.control.asignatura.AsignaturasActivity;
import com.app.jose_youssef.cliente_proyecto.observable.ManejadorMensajeOb;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorAlmacenDB;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import protocolo.Mensaje;

public class DifusionFragment extends Fragment implements Observer{

    private String asignatura;
    private String usuario;
    private int tipoUsuario;

    private RecyclerView rvDifusiones;
    private RecyclerView.LayoutManager layoutManager;
    private EditText etContenido;
    private EditText etAsunto;
    private ArrayList<MensajeCorto> mDs;
    private MensajeDifusionAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_difusion, container, false);

        etContenido = (EditText) vista.findViewById(R.id.etContenidoDifusion);
        etAsunto = (EditText) vista.findViewById(R.id.etAsuntoDifusion);
        rvDifusiones = (RecyclerView) vista.findViewById(R.id.rvDifusiones);
        vista.findViewById(R.id.fbEnviarDifusion).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEnviarDifusion(v);
            }
        });

        return vista;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        asignatura = args.getString(AsignaturasActivity.KEY_ASIGNATURA);
        usuario = args.getString(LoginActivity.KEY_USUARIO);
        tipoUsuario = args.getInt(LoginActivity.KEY_TIPO_USUARIO, -1);

        mDs = ManejadorAlmacenDB.getDifusiones(asignatura);

        adapter = new MensajeDifusionAdapter(getContext(), mDs);
        rvDifusiones.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        rvDifusiones.setLayoutManager(layoutManager);
        rvDifusiones.addItemDecoration(new DividerItemDecoration(rvDifusiones.getContext(), DividerItemDecoration.VERTICAL));
        rvDifusiones.setItemAnimator(new DefaultItemAnimator());
        rvDifusiones.scrollToPosition(mDs.size()-1);

        ManejadorMensajeOb.annadeObserver(this);
    }

    /**
     * Manda al servidor un mensaje de tipo difusion
     * @param view el boton
     */

    public void onClickEnviarDifusion(View view) {

        etAsunto.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));
        etContenido.setHintTextColor(Color.parseColor(getResources().getString(R.string.grayHint)));

        if(etAsunto.getText().toString().equals("")){
            etAsunto.setHintTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
        }else if(etContenido.getText().toString().equals("")){
            etContenido.setHintTextColor(Color.parseColor(getResources().getString(R.string.redHint)));
        }
        else{
            try {
                ManejadorConexion.mandaMsj(new Mensaje(Mensaje.MENSAJE_DIFUSION, -1,
                        etAsunto.getText().toString(), usuario, asignatura,
                        new Date(System.currentTimeMillis()), etContenido.getText().toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            etContenido.setText("");
            etAsunto.setText("");
        }
    }

    /**
     * Actualiza la pantalla con el mensaje recibido
     * @param mD que va ha mostrar
     */

    private void muestraMsj(final MensajeCorto mD){
        mDs.add(mD);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(mDs.size()-1);
                rvDifusiones.scrollToPosition(mDs.size()-1);
            }
        });
    }

    /**
     * Espera a que le notifiquen para actualizar el layout
     * @param observable
     * @param o mensaje recibido del servidor
     */

    @Override
    public void update(Observable observable, Object o) {
        Mensaje msj = (Mensaje) o;
        if (msj.getTipo() == Mensaje.MENSAJE_DIFUSION &&
                asignatura.equals(msj.getReceptor()))
        {
            MensajeCorto mD = new MensajeCorto(msj.getAsunto(), (String) msj.getContenido(),
                    ThConexion.DF_MINUTE.format(msj.getFecha()), msj.getEmisor(), (String)msj.getReceptor());

            muestraMsj(mD);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManejadorMensajeOb.eliminaObserver(this);
    }
}
