package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloCuentoConActividades;

import java.util.List;

public class AdaptadorActividadesPorCuento extends RecyclerView.Adapter<AdaptadorActividadesPorCuento.ViewHolderCuentoConActividades> {

    private List<ModeloCuentoConActividades> listaCuentosConActividades;

    public AdaptadorActividadesPorCuento(List<ModeloCuentoConActividades> listaCuentosConActividades) {
        this.listaCuentosConActividades = listaCuentosConActividades;
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
        
        // Configurar actividades (asumiendo que siempre hay 2 actividades por cuento)
        if (cuentoConActividades.getActividades().size() >= 2) {
            // Actividad 1
            holder.textoNombreActividad1.setText(cuentoConActividades.getActividades().get(0).getNombreActividad1());
            holder.textoTipoActividad1.setText(cuentoConActividades.getActividades().get(0).getTipoActividad1());
            holder.textoEstudiantesActividad1.setText(cuentoConActividades.getActividades().get(0).getEstudiantesActividad1() + " estudiantes");
            holder.textoEstadoActividad1.setText(cuentoConActividades.getActividades().get(0).getEstadoActividad1());
            
            // Actividad 2
            holder.textoNombreActividad2.setText(cuentoConActividades.getActividades().get(0).getNombreActividad2());
            holder.textoTipoActividad2.setText(cuentoConActividades.getActividades().get(0).getTipoActividad2());
            holder.textoEstudiantesActividad2.setText(cuentoConActividades.getActividades().get(0).getEstudiantesActividad2() + " estudiantes");
            holder.textoEstadoActividad2.setText(cuentoConActividades.getActividades().get(0).getEstadoActividad2());
        }
    }

    @Override
    public int getItemCount() {
        return listaCuentosConActividades.size();
    }

    public static class ViewHolderCuentoConActividades extends RecyclerView.ViewHolder {
        TextView textoNombreCuento;
        TextView textoTotalActividades;
        TextView textoNombreActividad1;
        TextView textoTipoActividad1;
        TextView textoEstudiantesActividad1;
        TextView textoEstadoActividad1;
        TextView textoNombreActividad2;
        TextView textoTipoActividad2;
        TextView textoEstudiantesActividad2;
        TextView textoEstadoActividad2;

        public ViewHolderCuentoConActividades(@NonNull View itemView) {
            super(itemView);
            textoNombreCuento = itemView.findViewById(R.id.texto_nombre_cuento);
            textoTotalActividades = itemView.findViewById(R.id.texto_total_actividades);
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
