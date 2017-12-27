package com.app.jose_youssef.cliente_proyecto.control.foro.post;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jose_youssef.cliente_proyecto.R;

import java.util.List;

/**
 * Created by yelfaqir on 28/05/2017.
 */

public class MensajePostAdapter extends RecyclerView.Adapter<MensajePostAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    private List<MensajePost> lMsj;
    protected View.OnClickListener onClickListener;
    protected View.OnLongClickListener onLongClickListener;

    public MensajePostAdapter(Context context, List<MensajePost> lMsj) {
        this.context = context;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.lMsj = lMsj;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_post_mensaje, parent, false);
        itemView.setOnClickListener(onClickListener);
        itemView.setOnLongClickListener(onLongClickListener);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MensajePost mP = lMsj.get(position);
        if(!mP.getNombre().equals(""))
            holder.tvNombre.setText(mP.getNombre());
        if(!mP.getFecha().equals(""))
            holder.tvFecha.setText(mP.getFecha());
        if(!mP.getMensaje().equals(""))
            holder.tvMensaje.setText(mP.getMensaje());
        if(!mP.getUsuarioContestado().equals("") && !mP.getMensajeContestado().equals("")){
            holder.tvUsuarioContestado.setText(mP.getUsuarioContestado());
            holder.tvMensajeContestado.setText(mP.getMensajeContestado());
            holder.cvRespuesta.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return lMsj.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvNombre;
        public TextView tvFecha;
        public TextView tvUsuarioContestado;
        public TextView tvMensajeContestado;
        public TextView tvMensaje;
        public CardView cvRespuesta;

        public ViewHolder(View item) {
            super(item);
            tvNombre = (TextView) item.findViewById(R.id.tvNombre);
            tvFecha = (TextView) item.findViewById(R.id.tvFecha);
            tvUsuarioContestado = (TextView) item.findViewById(R.id.tvUsuarioContestado);
            tvMensajeContestado = (TextView) item.findViewById(R.id.tvMensajeContestado);
            tvMensaje = (TextView) item.findViewById(R.id.tvMensaje);
            cvRespuesta = (CardView) item.findViewById(R.id.cvRespuesta);
        }
    }
}
