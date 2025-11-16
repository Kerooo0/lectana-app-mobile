package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloEstudianteAula;

import java.util.List;

public class AdaptadorEstudiantesAula extends RecyclerView.Adapter<AdaptadorEstudiantesAula.ViewHolderEstudiante> {

    private List<ModeloEstudianteAula> listaEstudiantes;
    private OnClickListenerEstudiante listenerEstudiante;

    public interface OnClickListenerEstudiante {
        void onClicEstudiante(ModeloEstudianteAula estudiante);
        void onClicQuitarEstudiante(ModeloEstudianteAula estudiante);
    }

    public AdaptadorEstudiantesAula(List<ModeloEstudianteAula> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setOnClickListenerEstudiante(OnClickListenerEstudiante listener) {
        this.listenerEstudiante = listener;
    }

    @NonNull
    @Override
    public ViewHolderEstudiante onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estudiante_aula, parent, false);
        return new ViewHolderEstudiante(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderEstudiante holder, int position) {
        ModeloEstudianteAula estudiante = listaEstudiantes.get(position);
        
        holder.textoNombreEstudiante.setText(estudiante.getNombre());
        holder.textoUltimaActividad.setText(estudiante.getUltimaActividad());
        holder.textoProgreso.setText(estudiante.getProgreso() + "%");
        
        // Cambiar color del indicador según el estado
        if (estudiante.isActivo()) {
            holder.indicadorEstado.setBackgroundResource(R.drawable.circulo_verde);
        } else {
            holder.indicadorEstado.setBackgroundResource(R.drawable.circulo_gris);
        }
        
        // Configurar click listeners
        holder.itemView.setOnClickListener(v -> {
            if (listenerEstudiante != null) {
                listenerEstudiante.onClicEstudiante(estudiante);
            }
        });
        
        holder.iconoQuitarEstudiante.setOnClickListener(v -> {
            if (listenerEstudiante != null) {
                listenerEstudiante.onClicQuitarEstudiante(estudiante);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEstudiantes.size();
    }

    public static class ViewHolderEstudiante extends RecyclerView.ViewHolder {
        TextView textoNombreEstudiante;
        TextView textoUltimaActividad;
        TextView textoProgreso;
        View indicadorEstado;
        ImageView iconoQuitarEstudiante;

        public ViewHolderEstudiante(@NonNull View itemView) {
            super(itemView);
            textoNombreEstudiante = itemView.findViewById(R.id.texto_nombre_estudiante);
            textoUltimaActividad = itemView.findViewById(R.id.texto_ultima_actividad);
            textoProgreso = itemView.findViewById(R.id.texto_progreso);
            indicadorEstado = itemView.findViewById(R.id.indicador_estado);
            iconoQuitarEstudiante = itemView.findViewById(R.id.icono_quitar_estudiante);
        }
    }
}
