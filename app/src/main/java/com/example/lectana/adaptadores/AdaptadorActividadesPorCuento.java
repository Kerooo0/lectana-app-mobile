package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloActividadDetallada;
import com.example.lectana.modelos.ModeloCuentoConActividades;

import java.util.List;

public class AdaptadorActividadesPorCuento extends RecyclerView.Adapter<AdaptadorActividadesPorCuento.ViewHolderCuentoConActividades> {

    private List<ModeloCuentoConActividades> listaCuentosConActividades;
    private OnActividadClickListener onActividadClickListener;

    public interface OnActividadClickListener {
        void onActividadClick(String actividadId);
    }

    public AdaptadorActividadesPorCuento(List<ModeloCuentoConActividades> listaCuentosConActividades) {
        this.listaCuentosConActividades = listaCuentosConActividades;
    }

    public void setOnActividadClickListener(OnActividadClickListener listener) {
        this.onActividadClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolderCuentoConActividades onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_contenedor_cuento, parent, false);
        return new ViewHolderCuentoConActividades(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCuentoConActividades holder, int position) {
        ModeloCuentoConActividades cuentoConActividades = listaCuentosConActividades.get(position);
        
        // Configurar header del cuento
        holder.textoNombreCuento.setText(cuentoConActividades.getNombreCuento());
        holder.textoTotalActividades.setText(cuentoConActividades.getTotalActividades() + " actividades");
        
        // Configurar actividades
        if (!cuentoConActividades.getActividades().isEmpty()) {
            ModeloActividadDetallada actividadDetallada = cuentoConActividades.getActividades().get(0);
            
            // Actividad 1
            holder.textoNombreActividad1.setText(actividadDetallada.getNombreActividad1());
            holder.textoTipoActividad1.setText(actividadDetallada.getTipoActividad1());
            holder.textoEstudiantesActividad1.setText(actividadDetallada.getEstudiantesActividad1() + " estudiantes");
            holder.textoEstadoActividad1.setText(actividadDetallada.getEstadoActividad1());
            
            // Configurar click listener para actividad 1
            if (actividadDetallada.getIdActividad1() != null && !actividadDetallada.getIdActividad1().isEmpty()) {
                holder.contenedorActividad1.setVisibility(View.VISIBLE);
                holder.contenedorActividad1.setOnClickListener(v -> {
                    if (onActividadClickListener != null) {
                        onActividadClickListener.onActividadClick(actividadDetallada.getIdActividad1());
                    }
                });
            } else {
                holder.contenedorActividad1.setVisibility(View.GONE);
            }
            
            // Actividad 2
            if (actividadDetallada.getNombreActividad2() != null && !actividadDetallada.getNombreActividad2().isEmpty()) {
                holder.textoNombreActividad2.setText(actividadDetallada.getNombreActividad2());
                holder.textoTipoActividad2.setText(actividadDetallada.getTipoActividad2());
                holder.textoEstudiantesActividad2.setText(actividadDetallada.getEstudiantesActividad2() + " estudiantes");
                holder.textoEstadoActividad2.setText(actividadDetallada.getEstadoActividad2());
                holder.contenedorActividad2.setVisibility(View.VISIBLE);
                
                // Configurar click listener para actividad 2
                if (actividadDetallada.getIdActividad2() != null && !actividadDetallada.getIdActividad2().isEmpty()) {
                    holder.contenedorActividad2.setOnClickListener(v -> {
                        if (onActividadClickListener != null) {
                            onActividadClickListener.onActividadClick(actividadDetallada.getIdActividad2());
                        }
                    });
                }
            } else {
                holder.contenedorActividad2.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaCuentosConActividades.size();
    }

    public static class ViewHolderCuentoConActividades extends RecyclerView.ViewHolder {
        TextView textoNombreCuento;
        TextView textoTotalActividades;
        LinearLayout contenedorActividad1;
        TextView textoNombreActividad1;
        TextView textoTipoActividad1;
        TextView textoEstudiantesActividad1;
        TextView textoEstadoActividad1;
        LinearLayout contenedorActividad2;
        TextView textoNombreActividad2;
        TextView textoTipoActividad2;
        TextView textoEstudiantesActividad2;
        TextView textoEstadoActividad2;

        public ViewHolderCuentoConActividades(@NonNull View itemView) {
            super(itemView);
            textoNombreCuento = itemView.findViewById(R.id.texto_nombre_cuento);
            textoTotalActividades = itemView.findViewById(R.id.texto_total_actividades);
            contenedorActividad1 = itemView.findViewById(R.id.contenedor_actividad_1);
            textoNombreActividad1 = itemView.findViewById(R.id.texto_nombre_actividad_1);
            textoTipoActividad1 = itemView.findViewById(R.id.texto_tipo_actividad_1);
            textoEstudiantesActividad1 = itemView.findViewById(R.id.texto_estudiantes_actividad_1);
            textoEstadoActividad1 = itemView.findViewById(R.id.texto_estado_actividad_1);
            contenedorActividad2 = itemView.findViewById(R.id.contenedor_actividad_2);
            textoNombreActividad2 = itemView.findViewById(R.id.texto_nombre_actividad_2);
            textoTipoActividad2 = itemView.findViewById(R.id.texto_tipo_actividad_2);
            textoEstudiantesActividad2 = itemView.findViewById(R.id.texto_estudiantes_actividad_2);
            textoEstadoActividad2 = itemView.findViewById(R.id.texto_estado_actividad_2);
        }
    }
}
