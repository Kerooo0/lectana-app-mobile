package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloActividadAula;

import java.util.List;

public class AdaptadorActividadesAula extends RecyclerView.Adapter<AdaptadorActividadesAula.ViewHolderActividad> {

    private List<ModeloActividadAula> listaActividades;

    public AdaptadorActividadesAula(List<ModeloActividadAula> listaActividades) {
        this.listaActividades = listaActividades;
    }

    @NonNull
    @Override
    public ViewHolderActividad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_aula, parent, false);
        return new ViewHolderActividad(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderActividad holder, int position) {
        ModeloActividadAula actividad = listaActividades.get(position);
        
        holder.textoNombreActividad.setText(actividad.getNombre());
        holder.textoFechaActividad.setText(actividad.getFecha());
        holder.textoProgresoActividad.setText(actividad.getProgreso() + "%");
        
        // Cambiar color del indicador según el estado
        if (actividad.isCompletada()) {
            holder.indicadorEstadoActividad.setBackgroundResource(R.drawable.circulo_verde);
        } else {
            holder.indicadorEstadoActividad.setBackgroundResource(R.drawable.circulo_gris);
        }
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    public static class ViewHolderActividad extends RecyclerView.ViewHolder {
        TextView textoNombreActividad;
        TextView textoFechaActividad;
        TextView textoProgresoActividad;
        View indicadorEstadoActividad;

        public ViewHolderActividad(@NonNull View itemView) {
            super(itemView);
            textoNombreActividad = itemView.findViewById(R.id.texto_nombre_actividad);
            textoFechaActividad = itemView.findViewById(R.id.texto_fecha_actividad);
            textoProgresoActividad = itemView.findViewById(R.id.texto_progreso_actividad);
            indicadorEstadoActividad = itemView.findViewById(R.id.indicador_estado_actividad);
        }
    }
}
