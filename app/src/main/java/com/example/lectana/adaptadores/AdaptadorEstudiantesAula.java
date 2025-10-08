package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloEstudianteAula;

import java.util.List;

public class AdaptadorEstudiantesAula extends RecyclerView.Adapter<AdaptadorEstudiantesAula.ViewHolderEstudiante> {

    private List<ModeloEstudianteAula> listaEstudiantes;

    public AdaptadorEstudiantesAula(List<ModeloEstudianteAula> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
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

        public ViewHolderEstudiante(@NonNull View itemView) {
            super(itemView);
            textoNombreEstudiante = itemView.findViewById(R.id.texto_nombre_estudiante);
            textoUltimaActividad = itemView.findViewById(R.id.texto_ultima_actividad);
            textoProgreso = itemView.findViewById(R.id.texto_progreso);
            indicadorEstado = itemView.findViewById(R.id.indicador_estado);
        }
    }
}
