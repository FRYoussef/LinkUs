package com.app.jose_youssef.cliente_proyecto.control.foro;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.conexion.ThConexion;
import com.app.jose_youssef.cliente_proyecto.control.asignatura.AsignaturasActivity;
import com.app.jose_youssef.cliente_proyecto.control.foro.post.PostActivity;
import com.app.jose_youssef.cliente_proyecto.control.LoginActivity;
import com.app.jose_youssef.cliente_proyecto.observable.ManejadorMensajeOb;
import com.app.jose_youssef.cliente_proyecto.persistencia.ManejadorAlmacenDB;

import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import protocolo.Mensaje;

public class ForoFragment extends Fragment implements Observer{

    public final static String KEY_POST = "post";

    private String asignatura;
    private String usuario;
    private int tipoUsuario;
    private String post = "";

    private RecyclerView rvPosts;
    private PostAdapter postAdapter;
    private List<Post> lPosts;
    private RecyclerView.LayoutManager layoutManager;
    private Animation animTrasladar;
    private View vSeleccionada;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_foro, container, false);
        rvPosts = (RecyclerView) vista.findViewById(R.id.rvPosts);

        vista.findViewById(R.id.fbNuevoPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNuevoPost(v);
            }
        });

        return vista;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        asignatura = args.getString(AsignaturasActivity.KEY_ASIGNATURA);
        usuario = args.getString(LoginActivity.KEY_USUARIO);
        tipoUsuario = args.getInt(LoginActivity.KEY_TIPO_USUARIO, -1);

        lPosts = ManejadorAlmacenDB.getPosts(asignatura);


        postAdapter = new PostAdapter(getContext(), lPosts);
        rvPosts.setAdapter(postAdapter);
        layoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(layoutManager);

        cargaAnimaciones();

        annadeOnClicks();
        ManejadorMensajeOb.annadeObserver(this);
    }

    private void cargaAnimaciones() {
        animTrasladar = AnimationUtils.loadAnimation(getContext(), R.anim.anim_trasladar_post);

        animTrasladar.setAnimationListener(new Animation.AnimationListener() {
            private Intent intent;
            @Override
            public void onAnimationStart(Animation animation) {
                intent = new Intent(getContext(), PostActivity.class);
                intent.putExtra(LoginActivity.KEY_USUARIO, usuario);
                intent.putExtra(AsignaturasActivity.KEY_ASIGNATURA, asignatura);
                intent.putExtra(KEY_POST, post);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                vSeleccionada.setVisibility(View.INVISIBLE);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }

    /**
     * Annade un evento a las views que se carguen
     */

    private void annadeOnClicks() {
        postAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vSeleccionada = v;
                post = (String)vSeleccionada.findViewById(R.id.tvNombre).getTag();
                vSeleccionada.startAnimation(animTrasladar);
            }
        });
    }

    /**
     * Inicia un activity, para un nuevo post
     * @param view
     */

    public void onClickNuevoPost(View view) {
        Pair<View, String> pair = Pair.create(view, getResources().getString(R.string.new_post_transition));

        Activity act = getActivity();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(act, pair);

        Intent intent = new Intent(act, NuevoPostActivity.class);
        intent.putExtra(LoginActivity.KEY_USUARIO, usuario);
        intent.putExtra(AsignaturasActivity.KEY_ASIGNATURA, asignatura);
        startActivityForResult(intent, 4, options.toBundle());
    }

    /**
     * Cuando llega un nuevo msj lo muestra
     * @param observable
     * @param o
     */

    @Override
    public void update(Observable observable, Object o) {
        Mensaje msj = (Mensaje) o;
        if(msj.getTipo() == Mensaje.MENSAJE_FORO && msj.getReceptor().equals(asignatura))
        {
            mustraMensaje(new Post(msj.getAsunto(), ThConexion.DF_MINUTE.format(msj.getFecha()), msj.getEmisor(), 1));
        }
    }

    /**
     * Busca si esta el post, si lo esta lo actualiza, sino annade uno nuevo
     * @param post
     */
    private void mustraMensaje(Post post) {

        if(lPosts.contains(post)){
            Post p = lPosts.get(lPosts.indexOf(post));
            p.annadeUnaRespuesta();
            p.setFechaUltimoMensaje(post.getFechaUltimoMensaje());
            p.setUltimoEnContestar(post.getUltimoEnContestar());
        } else{
          lPosts.add(post);
        }
        Collections.sort(lPosts);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(vSeleccionada != null){
            vSeleccionada.setVisibility(View.VISIBLE);
            vSeleccionada = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ManejadorMensajeOb.eliminaObserver(this);
    }
}
