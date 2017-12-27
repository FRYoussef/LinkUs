package com.app.jose_youssef.cliente_proyecto.control.foro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jose_youssef.cliente_proyecto.R;

import java.util.List;

/**
 * Created by yelfaqir on 27/05/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<Post> lPosts;
    protected View.OnClickListener onClickListener;

    public PostAdapter(Context context, List<Post> lPosts) {
        this.context = context;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lPosts = lPosts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_post, parent, false);
        itemView.setOnClickListener(onClickListener);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = lPosts.get(position);
        if(!post.getNombre().equals(""))
            holder.tvNombrePost.setText(post.getNombre());
        if(!post.getNombre().equals(""))
            holder.tvNombrePost.setTag(post.getNombre());
        if(!post.getUltimoEnContestar().equals(""))
            holder.tvUltimoUsuario.setText(post.getUltimoEnContestar());
        if(!post.getFechaUltimoMensaje().equals(""))
            holder.tvFecha.setText(post.getFechaUltimoMensaje());
        holder.tvRespuestas.setText(post.getRespuestas() + "");
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getItemCount() {
        return lPosts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNombrePost;
        public TextView tvUltimoUsuario;
        public TextView tvFecha;
        public TextView tvRespuestas;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombrePost = (TextView) itemView.findViewById(R.id.tvNombre);
            tvUltimoUsuario = (TextView) itemView.findViewById(R.id.tvUltimoUsuario);
            tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
            tvRespuestas = (TextView) itemView.findViewById(R.id.tvRespuestas);
        }
    }
}
