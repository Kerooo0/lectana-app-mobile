package com.example.lectana.docente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.Actividad;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActividadesAdapter extends RecyclerView.Adapter<ActividadesAdapter.ActividadViewHolder> {
    private List<Actividad> actividades;
    private OnActividadClickListener listener;

    public interface OnActividadClickListener {
        void onActividadClick(Actividad actividad);
        void onEditarClick(Actividad actividad);
        void onEliminarClick(Actividad actividad);
        void onAsignarAulasClick(Actividad actividad);
    }

    public ActividadesAdapter(List<Actividad> actividades, OnActividadClickListener listener) {
        this.actividades = actividades;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            android.util.Log.d("ActividadesAdapter", "=== ONCREATEVIEWHOLDER ===");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad, parent, false);
            android.util.Log.d("ActividadesAdapter", "Layout inflado correctamente");
            ActividadViewHolder holder = new ActividadViewHolder(view);
            android.util.Log.d("ActividadesAdapter", "ViewHolder creado correctamente");
            return holder;
        } catch (Exception e) {
            android.util.Log.e("ActividadesAdapter", "Error en onCreateViewHolder", e);
            throw e;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        try {
            android.util.Log.d("ActividadesAdapter", "=== ONBINDVIEWHOLDER ===");
            android.util.Log.d("ActividadesAdapter", "Position: " + position);
            android.util.Log.d("ActividadesAdapter", "Total actividades: " + actividades.size());
            
            if (position >= actividades.size()) {
                android.util.Log.e("ActividadesAdapter", "Position fuera de rango: " + position);
                return;
            }
            
            Actividad actividad = actividades.get(position);
            android.util.Log.d("ActividadesAdapter", "Actividad obtenida: " + actividad.getId_actividad());
            
            holder.bind(actividad, listener);
            android.util.Log.d("ActividadesAdapter", "Bind completado");
        } catch (Exception e) {
            android.util.Log.e("ActividadesAdapter", "Error en onBindViewHolder", e);
        }
    }

    @Override
    public int getItemCount() {
        return actividades.size();
    }

    static class ActividadViewHolder extends RecyclerView.ViewHolder {
        private TextView textDescripcion;
        private TextView textTipo;
        private TextView textFechaCreacion;
        private TextView textTotalPreguntas;
        private TextView textTotalAulas;
        private ImageButton btnEditar;
        private ImageButton btnEliminar;
        private ImageButton btnAsignarAulas;

        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                android.util.Log.d("ActividadesAdapter", "=== CONSTRUCTOR VIEWHOLDER ===");
                
                textDescripcion = itemView.findViewById(R.id.textDescripcion);
                android.util.Log.d("ActividadesAdapter", "textDescripcion: " + (textDescripcion != null ? "OK" : "NULL"));
                
                textTipo = itemView.findViewById(R.id.textTipo);
                android.util.Log.d("ActividadesAdapter", "textTipo: " + (textTipo != null ? "OK" : "NULL"));
                
                textFechaCreacion = itemView.findViewById(R.id.textFechaCreacion);
                android.util.Log.d("ActividadesAdapter", "textFechaCreacion: " + (textFechaCreacion != null ? "OK" : "NULL"));
                
                textTotalPreguntas = itemView.findViewById(R.id.textTotalPreguntas);
                android.util.Log.d("ActividadesAdapter", "textTotalPreguntas: " + (textTotalPreguntas != null ? "OK" : "NULL"));
                
                textTotalAulas = itemView.findViewById(R.id.textTotalAulas);
                android.util.Log.d("ActividadesAdapter", "textTotalAulas: " + (textTotalAulas != null ? "OK" : "NULL"));
                
                btnEditar = itemView.findViewById(R.id.btnEditar);
                android.util.Log.d("ActividadesAdapter", "btnEditar: " + (btnEditar != null ? "OK" : "NULL"));
                
                btnEliminar = itemView.findViewById(R.id.btnEliminar);
                android.util.Log.d("ActividadesAdapter", "btnEliminar: " + (btnEliminar != null ? "OK" : "NULL"));
                
                btnAsignarAulas = itemView.findViewById(R.id.btnAsignarAulas);
                android.util.Log.d("ActividadesAdapter", "btnAsignarAulas: " + (btnAsignarAulas != null ? "OK" : "NULL"));
                
                android.util.Log.d("ActividadesAdapter", "Constructor ViewHolder completado");
            } catch (Exception e) {
                android.util.Log.e("ActividadesAdapter", "Error en constructor ViewHolder", e);
                throw e;
            }
        }

        public void bind(Actividad actividad, OnActividadClickListener listener) {
            try {
                android.util.Log.d("ActividadesAdapter", "=== BIND ACTIVIDAD ===");
                android.util.Log.d("ActividadesAdapter", "ID: " + actividad.getId_actividad());
                android.util.Log.d("ActividadesAdapter", "Descripción: " + actividad.getDescripcion());
                android.util.Log.d("ActividadesAdapter", "Tipo: " + actividad.getTipo());
                
                textDescripcion.setText(actividad.getDescripcion());
                android.util.Log.d("ActividadesAdapter", "Descripción seteada");
                
                textTipo.setText(actividad.getTipoDisplay());
                android.util.Log.d("ActividadesAdapter", "Tipo display seteado");
                
                textFechaCreacion.setText(formatearFecha(actividad.getFecha_creacion()));
                android.util.Log.d("ActividadesAdapter", "Fecha seteada");
                
                textTotalPreguntas.setText("Preguntas: " + actividad.getTotalPreguntas());
                android.util.Log.d("ActividadesAdapter", "Total preguntas seteado");
                
                textTotalAulas.setText("Aulas: " + actividad.getTotalAulas());
                android.util.Log.d("ActividadesAdapter", "Total aulas seteado");

                // Configurar listeners
                itemView.setOnClickListener(v -> {
                    android.util.Log.d("ActividadesAdapter", "Click en actividad: " + actividad.getId_actividad());
                    listener.onActividadClick(actividad);
                });
                btnEditar.setOnClickListener(v -> {
                    android.util.Log.d("ActividadesAdapter", "Click en editar: " + actividad.getId_actividad());
                    listener.onEditarClick(actividad);
                });
                btnEliminar.setOnClickListener(v -> {
                    android.util.Log.d("ActividadesAdapter", "Click en eliminar: " + actividad.getId_actividad());
                    listener.onEliminarClick(actividad);
                });
                btnAsignarAulas.setOnClickListener(v -> {
                    android.util.Log.d("ActividadesAdapter", "Click en asignar aulas: " + actividad.getId_actividad());
                    listener.onAsignarAulasClick(actividad);
                });
                
                android.util.Log.d("ActividadesAdapter", "Bind completado exitosamente");
            } catch (Exception e) {
                android.util.Log.e("ActividadesAdapter", "Error en bind", e);
                // Mostrar datos básicos en caso de error
                textDescripcion.setText("Error cargando actividad");
                textTipo.setText("Error");
                textFechaCreacion.setText("Error");
                textTotalPreguntas.setText("Error");
                textTotalAulas.setText("Error");
            }
        }

        private String formatearFecha(String fecha) {
            try {
                SimpleDateFormat formatoEntrada = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat formatoSalida = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date date = formatoEntrada.parse(fecha);
                return formatoSalida.format(date);
            } catch (Exception e) {
                return fecha;
            }
        }
    }
}

