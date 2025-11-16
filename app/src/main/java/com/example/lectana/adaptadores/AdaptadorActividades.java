package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ActividadAula;

import java.util.List;

public class AdaptadorActividades extends RecyclerView.Adapter<AdaptadorActividades.ViewHolder> {

    private List<ActividadAula> actividades;
    private OnActividadClickListener listener;

    public interface OnActividadClickListener {
        void onActividadClick(ActividadAula actividad);
    }

    public AdaptadorActividades(List<ActividadAula> actividades, OnActividadClickListener listener) {
        this.actividades = actividades;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_actividad, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActividadAula actividad = actividades.get(position);
        holder.bind(actividad, listener);
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitulo;
        private TextView tvDescripcion;
        private TextView tvEstado;
        private Button btnRealizar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = new TextView(itemView.getContext());
            tvDescripcion = new TextView(itemView.getContext());
            tvEstado = new TextView(itemView.getContext());
            btnRealizar = new Button(itemView.getContext());
        }

        public void bind(ActividadAula actividad, OnActividadClickListener listener) {
            if (actividad == null || actividad.getActividad() == null) return;
            
            tvTitulo.setText(actividad.getActividad().getDescripcion());
            tvDescripcion.setText(actividad.getActividad().getDescripcion());
            tvEstado.setText("Pendiente");

            btnRealizar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onActividadClick(actividad);
                }
            });
        }
    }
}
