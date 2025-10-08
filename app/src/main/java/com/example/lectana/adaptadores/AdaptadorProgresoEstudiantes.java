package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloProgresoEstudiante;

import java.util.List;

public class AdaptadorProgresoEstudiantes extends RecyclerView.Adapter<AdaptadorProgresoEstudiantes.ViewHolderProgreso> {

    private List<ModeloProgresoEstudiante> listaProgresoEstudiantes;

    public AdaptadorProgresoEstudiantes(List<ModeloProgresoEstudiante> listaProgresoEstudiantes) {
        this.listaProgresoEstudiantes = listaProgresoEstudiantes;
    }

    @NonNull
    @Override
    public ViewHolderProgreso onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_progreso_estudiante, parent, false);
        return new ViewHolderProgreso(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderProgreso holder, int position) {
        ModeloProgresoEstudiante progreso = listaProgresoEstudiantes.get(position);
        holder.configurarProgreso(progreso);
    }

    @Override
    public int getItemCount() {
        return listaProgresoEstudiantes.size();
    }

    public class ViewHolderProgreso extends RecyclerView.ViewHolder {
        private ImageView avatarEstudiante;
        private TextView nombreEstudianteProgreso;
        private TextView tiempoUltimaActividad;
        private TextView puntuacionEstudiante;
        private TextView porcentajeProgreso;
        private View barraProgresoRellena;

        public ViewHolderProgreso(@NonNull View itemView) {
            super(itemView);
            avatarEstudiante = itemView.findViewById(R.id.avatar_estudiante);
            nombreEstudianteProgreso = itemView.findViewById(R.id.nombre_estudiante_progreso);
            tiempoUltimaActividad = itemView.findViewById(R.id.tiempo_ultima_actividad);
            puntuacionEstudiante = itemView.findViewById(R.id.puntuacion_estudiante);
            porcentajeProgreso = itemView.findViewById(R.id.porcentaje_progreso);
            barraProgresoRellena = itemView.findViewById(R.id.barra_progreso_rellena);
        }

        public void configurarProgreso(ModeloProgresoEstudiante progreso) {
            nombreEstudianteProgreso.setText(progreso.getNombreEstudiante());
            tiempoUltimaActividad.setText(progreso.getTiempoUltimaActividad());
            puntuacionEstudiante.setText(progreso.getPuntuacionFormateada());
            porcentajeProgreso.setText(progreso.getPorcentajeFormateado());

            // Configurar color del porcentaje y barra según el progreso
            int porcentaje = progreso.getPorcentajeProgreso();
            int colorPorcentaje;
            int colorBarra;

            if (porcentaje >= 80) {
                colorPorcentaje = itemView.getContext().getResources().getColor(R.color.verde);
                colorBarra = R.drawable.barra_progreso_verde;
            } else if (porcentaje >= 60) {
                colorPorcentaje = itemView.getContext().getResources().getColor(R.color.naranjaPastel);
                colorBarra = R.drawable.barra_progreso_naranja;
            } else {
                colorPorcentaje = itemView.getContext().getResources().getColor(R.color.azul_claro);
                colorBarra = R.drawable.barra_progreso_azul;
            }

            porcentajeProgreso.setTextColor(colorPorcentaje);
            barraProgresoRellena.setBackgroundResource(colorBarra);

            // Configurar el ancho de la barra de progreso
            ViewGroup.LayoutParams params = barraProgresoRellena.getLayoutParams();
            params.width = (int) (120 * (porcentaje / 100.0)); // 120dp es el ancho total de la barra
            barraProgresoRellena.setLayoutParams(params);
        }
    }
}
