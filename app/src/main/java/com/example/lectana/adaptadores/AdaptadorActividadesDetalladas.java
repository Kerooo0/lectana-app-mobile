package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloActividadDetallada;

import java.util.List;

public class AdaptadorActividadesDetalladas extends RecyclerView.Adapter<AdaptadorActividadesDetalladas.ViewHolderActividadDetallada> {

    private List<ModeloActividadDetallada> listaActividadesDetalladas;

    public AdaptadorActividadesDetalladas(List<ModeloActividadDetallada> listaActividadesDetalladas) {
        this.listaActividadesDetalladas = listaActividadesDetalladas;
    }

    @NonNull
    @Override
    public ViewHolderActividadDetallada onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_detallada, parent, false);
        return new ViewHolderActividadDetallada(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderActividadDetallada holder, int position) {
        ModeloActividadDetallada actividad = listaActividadesDetalladas.get(position);
        
        holder.textoTituloHistoria.setText(actividad.getTituloHistoria());
        
        holder.textoNombreActividad1.setText(actividad.getNombreActividad1());
        holder.textoTipoActividad1.setText("Tipo: " + actividad.getTipoActividad1());
        holder.textoEstudiantesActividad1.setText(actividad.getEstudiantesActividad1() + " estudiantes");
        holder.textoEstadoActividad1.setText(actividad.getEstadoActividad1());
        
        holder.textoNombreActividad2.setText(actividad.getNombreActividad2());
        holder.textoTipoActividad2.setText("Tipo: " + actividad.getTipoActividad2());
        holder.textoEstudiantesActividad2.setText(actividad.getEstudiantesActividad2() + " estudiantes");
        holder.textoEstadoActividad2.setText(actividad.getEstadoActividad2());
    }

    @Override
    public int getItemCount() {
        return listaActividadesDetalladas.size();
    }

    public static class ViewHolderActividadDetallada extends RecyclerView.ViewHolder {
        TextView textoTituloHistoria;
        TextView textoNombreActividad1;
        TextView textoTipoActividad1;
        TextView textoEstudiantesActividad1;
        TextView textoEstadoActividad1;
        TextView textoNombreActividad2;
        TextView textoTipoActividad2;
        TextView textoEstudiantesActividad2;
        TextView textoEstadoActividad2;

        public ViewHolderActividadDetallada(@NonNull View itemView) {
            super(itemView);
            textoTituloHistoria = itemView.findViewById(R.id.texto_titulo_historia);
            textoNombreActividad1 = itemView.findViewById(R.id.texto_nombre_actividad_1);
            textoTipoActividad1 = itemView.findViewById(R.id.texto_tipo_actividad_1);
            textoEstudiantesActividad1 = itemView.findViewById(R.id.texto_estudiantes_actividad_1);
            textoEstadoActividad1 = itemView.findViewById(R.id.texto_estado_actividad_1);
            textoNombreActividad2 = itemView.findViewById(R.id.texto_nombre_actividad_2);
            textoTipoActividad2 = itemView.findViewById(R.id.texto_tipo_actividad_2);
            textoEstudiantesActividad2 = itemView.findViewById(R.id.texto_estudiantes_actividad_2);
            textoEstadoActividad2 = itemView.findViewById(R.id.texto_estado_actividad_2);
        }
    }
}
