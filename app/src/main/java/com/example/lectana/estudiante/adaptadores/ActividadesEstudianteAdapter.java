package com.example.lectana.estudiante.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.Actividad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActividadesEstudianteAdapter extends RecyclerView.Adapter<ActividadesEstudianteAdapter.ActividadViewHolder> {
    private List<Actividad> actividades;
    private OnActividadClickListener listener;

    public interface OnActividadClickListener {
        void onActividadClick(Actividad actividad);
    }

    public ActividadesEstudianteAdapter(List<Actividad> actividades, OnActividadClickListener listener) {
        this.actividades = actividades;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_estudiante, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        Actividad actividad = actividades.get(position);
        holder.bind(actividad, listener);
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    static class ActividadViewHolder extends RecyclerView.ViewHolder {
        private CardView cardActividad;
        private ImageView iconoEstado;
        private TextView textCuentoTitulo;
        private TextView textDescripcion;
        private TextView textTipo;
        private TextView textFechaEntrega;
        private TextView textEstado;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cardActividad = (CardView) itemView;
            iconoEstado = itemView.findViewById(R.id.iconoEstado);
            textCuentoTitulo = itemView.findViewById(R.id.textCuentoTitulo);
            textDescripcion = itemView.findViewById(R.id.textDescripcion);
            textTipo = itemView.findViewById(R.id.textTipo);
            textFechaEntrega = itemView.findViewById(R.id.textFechaEntrega);
            textEstado = itemView.findViewById(R.id.textEstado);
        }

        public void bind(Actividad actividad, OnActividadClickListener listener) {
            // Título del cuento
            if (actividad.getCuento() != null) {
                textCuentoTitulo.setText(actividad.getCuento().getTitulo());
            } else {
                textCuentoTitulo.setText("Cuento sin título");
            }
            
            // Descripción
            textDescripcion.setText(actividad.getDescripcion());
            
            // Tipo de actividad
            textTipo.setText(actividad.getTipoDisplay());
            
            // Fecha de entrega
            if (actividad.getFecha_entrega() != null && !actividad.getFecha_entrega().isEmpty()) {
                textFechaEntrega.setText("Vence: " + formatearFecha(actividad.getFecha_entrega()));
            } else {
                textFechaEntrega.setText("Sin fecha de entrega");
            }
            
            // Estado (completada o pendiente)
            if (actividad.isCompletada()) {
                iconoEstado.setImageResource(R.drawable.ic_activity_completed);
                textEstado.setText("Completada");
                textEstado.setTextColor(itemView.getContext().getResources().getColor(R.color.verde_exito));
                cardActividad.setCardBackgroundColor(itemView.getContext().getResources().getColor(R.color.fondo_claro));
            } else {
                iconoEstado.setImageResource(R.drawable.ic_activity_pending);
                textEstado.setText("Pendiente");
                textEstado.setTextColor(itemView.getContext().getResources().getColor(R.color.naranja_advertencia));
                cardActividad.setCardBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.white));
            }
            
            // Click listener (solo para actividades pendientes)
            if (!actividad.isCompletada()) {
                itemView.setOnClickListener(v -> listener.onActividadClick(actividad));
                itemView.setClickable(true);
                itemView.setFocusable(true);
            } else {
                itemView.setOnClickListener(null);
                itemView.setClickable(false);
                itemView.setFocusable(false);
            }
        }

        private String formatearFecha(String fechaISO) {
            try {
                SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
                Date fecha = isoFormat.parse(fechaISO);
                return displayFormat.format(fecha);
            } catch (ParseException e) {
                // Si falla el parseo, intentar formato simple
                try {
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    SimpleDateFormat displayFormat = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
                    Date fecha = simpleFormat.parse(fechaISO);
                    return displayFormat.format(fecha);
                } catch (ParseException ex) {
                    return fechaISO;
                }
            }
        }
    }
}
