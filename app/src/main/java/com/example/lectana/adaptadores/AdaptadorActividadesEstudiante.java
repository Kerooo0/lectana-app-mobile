package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloActividadEstudiante;

import java.util.List;

public class AdaptadorActividadesEstudiante extends RecyclerView.Adapter<AdaptadorActividadesEstudiante.ViewHolderActividad> {

    private List<ModeloActividadEstudiante> listaActividades;

    public AdaptadorActividadesEstudiante(List<ModeloActividadEstudiante> listaActividades) {
        this.listaActividades = listaActividades;
    }

    @NonNull
    @Override
    public ViewHolderActividad onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_actividad_estudiante, parent, false);
        return new ViewHolderActividad(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderActividad holder, int position) {
        ModeloActividadEstudiante actividad = listaActividades.get(position);
        
        holder.textoTituloActividad.setText(actividad.getTitulo());
        holder.textoEstado.setText(actividad.getEstado());
        holder.textoFecha.setText(actividad.getFecha());
        holder.textoProgreso.setText(actividad.getProgreso() + "%");
        
        // Cambiar color según el estado
        if (actividad.isCompletada()) {
            holder.textoEstado.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.verde));
            holder.indicadorEstado.setBackgroundResource(R.drawable.circulo_verde);
        } else {
            holder.textoEstado.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.naranja));
            holder.indicadorEstado.setBackgroundResource(R.drawable.circulo_naranja);
        }
    }

    @Override
    public int getItemCount() {
        return listaActividades.size();
    }

    public static class ViewHolderActividad extends RecyclerView.ViewHolder {
        TextView textoTituloActividad;
        TextView textoEstado;
        TextView textoFecha;
        TextView textoProgreso;
        View indicadorEstado;

        public ViewHolderActividad(@NonNull View itemView) {
            super(itemView);
            // Usar los IDs del nuevo layout
            textoTituloActividad = itemView.findViewById(R.id.textCuentoTitulo);
            textoEstado = itemView.findViewById(R.id.textEstado);
            textoFecha = itemView.findViewById(R.id.textFechaEntrega);
            // Estos IDs no existen en el nuevo layout, usar valores por defecto
            textoProgreso = new TextView(itemView.getContext());
            indicadorEstado = itemView.findViewById(R.id.iconoEstado);
        }
    }
}
