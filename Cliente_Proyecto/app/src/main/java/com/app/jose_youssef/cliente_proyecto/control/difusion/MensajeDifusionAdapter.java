package com.app.jose_youssef.cliente_proyecto.control.difusion;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jose_youssef.cliente_proyecto.R;
import com.app.jose_youssef.cliente_proyecto.control.MensajeCorto;
import com.app.jose_youssef.cliente_proyecto.customView.ExpandableTextView;

import java.util.ArrayList;

/**
 * Adaptador que necesitaremos para añadir elementos al RecyclerView
 * Created by Youss on 10/04/2017.
 */

public class MensajeDifusionAdapter extends RecyclerView.Adapter<MensajeDifusionAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<MensajeCorto> mDs;

    public MensajeDifusionAdapter(Context _context, ArrayList<MensajeCorto> _mDs){
        context = _context;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDs = _mDs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_mensaje_difusion, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MensajeCorto mD = mDs.get(position);
        if(!mD.getAsunto().equals(""))
            holder.tvAsunto.setText(mD.getAsunto());
        if(!mD.getContenido().equals(""))
            holder.tvContenido.setText(mD.getContenido());
        if(!mD.getEmisor().equals(""))
            holder.tvEmisor.setText(mD.getEmisor());
        if(!mD.getFecha().equals(""))
            holder.tvFecha.setText(mD.getFecha());
    }

    @Override
    public int getItemCount() {
        return mDs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvAsunto;
        public ExpandableTextView tvContenido;
        public TextView tvEmisor;
        public TextView tvFecha;


        public ViewHolder(View itemView) {
            super(itemView);

            tvAsunto = (TextView) itemView.findViewById(R.id.tvAsunto);
            tvContenido = (ExpandableTextView) itemView.findViewById(R.id.tvContenido);
            tvEmisor = (TextView) itemView.findViewById(R.id.tvEmisor);
            tvFecha = (TextView) itemView.findViewById(R.id.tvFecha);
        }
    }

}
