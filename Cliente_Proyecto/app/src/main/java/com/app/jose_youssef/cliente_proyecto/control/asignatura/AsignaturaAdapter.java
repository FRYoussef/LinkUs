package com.app.jose_youssef.cliente_proyecto.control.asignatura;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jose_youssef.cliente_proyecto.R;

import java.util.List;

/**
 * Created by yelfaqir on 03/06/2017.
 */

public class AsignaturaAdapter extends RecyclerView.Adapter<AsignaturaAdapter.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private List<String> listAsig;
    protected View.OnClickListener onClickListener;

    public AsignaturaAdapter(Context context, List<String> listAsig) {
        this.context = context;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listAsig = listAsig;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_asignatura, parent, false);
        itemView.setOnClickListener(onClickListener);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String asignatura = listAsig.get(position);
        holder.tvAsignatura.setText(asignatura);
        holder.tvAsignatura.setTag(asignatura);
    }

    @Override
    public int getItemCount() {
        return listAsig.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvAsignatura;

        public ViewHolder(View itemView) {
            super(itemView);
            tvAsignatura = (TextView) itemView.findViewById(R.id.tvAsignatura);
        }
    }
}
