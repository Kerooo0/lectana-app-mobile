package com.example.lectana.estudiante.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectana.R;
import com.example.lectana.models.Logro;

import java.util.ArrayList;
import java.util.List;

public class LogrosAdapter extends RecyclerView.Adapter<LogrosAdapter.LogroViewHolder> {

    private List<Logro> logros;

    public LogrosAdapter() {
        this.logros = new ArrayList<>();
    }

    public void setLogros(List<Logro> logros) {
        this.logros = logros != null ? logros : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logro, parent, false);
        return new LogroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogroViewHolder holder, int position) {
        Logro logro = logros.get(position);
        holder.bind(logro);
    }

    @Override
    public int getItemCount() {
        return logros.size();
    }

    static class LogroViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenLogro;
        private TextView nombreLogro;
        private TextView descripcionLogro;
        private ProgressBar progresoLogro;
        private TextView textoProgresoLogro;
        private TextView badgeDesbloqueado;

        public LogroViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenLogro = itemView.findViewById(R.id.imagen_logro);
            nombreLogro = itemView.findViewById(R.id.nombre_logro);
            descripcionLogro = itemView.findViewById(R.id.descripcion_logro);
            progresoLogro = itemView.findViewById(R.id.progreso_logro);
            textoProgresoLogro = itemView.findViewById(R.id.texto_progreso_logro);
            badgeDesbloqueado = itemView.findViewById(R.id.badge_desbloqueado);
        }

        public void bind(Logro logro) {
            nombreLogro.setText(logro.getNombre());
            descripcionLogro.setText(logro.getDescripcion());
            
            // Configurar progreso
            int progreso = logro.getProgreso();
            progresoLogro.setProgress(progreso);
            textoProgresoLogro.setText(progreso + "%");

            // Mostrar badge si está desbloqueado
            if (logro.isDesbloqueado()) {
                badgeDesbloqueado.setVisibility(View.VISIBLE);
                progresoLogro.setVisibility(View.GONE);
                textoProgresoLogro.setVisibility(View.GONE);
            } else {
                badgeDesbloqueado.setVisibility(View.GONE);
                progresoLogro.setVisibility(View.VISIBLE);
                textoProgresoLogro.setVisibility(View.VISIBLE);
            }

            // Cargar imagen
            if (logro.getUrlImagen() != null && !logro.getUrlImagen().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(logro.getUrlImagen())
                        .placeholder(R.drawable.ic_check_circle)
                        .error(R.drawable.ic_check_circle)
                        .into(imagenLogro);
            } else {
                imagenLogro.setImageResource(R.drawable.ic_check_circle);
            }
        }
    }
}
