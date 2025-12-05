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
import com.example.lectana.modelos.Logro;

import java.util.ArrayList;
import java.util.List;

public class LogrosExpandidosAdapter extends RecyclerView.Adapter<LogrosExpandidosAdapter.LogroExpandidoViewHolder> {

    private List<Logro> logros;

    public LogrosExpandidosAdapter() {
        this.logros = new ArrayList<>();
    }

    public void setLogros(List<Logro> logros) {
        this.logros = logros != null ? logros : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LogroExpandidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_logro_expandido, parent, false);
        return new LogroExpandidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogroExpandidoViewHolder holder, int position) {
        Logro logro = logros.get(position);
        holder.bind(logro);
    }

    @Override
    public int getItemCount() {
        return logros.size();
    }

    static class LogroExpandidoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenLogro;
        private TextView nombreLogro;
        private TextView descripcionLogro;
        private ProgressBar progresoLogro;
        private TextView textoProgresoLogro;
        private TextView badgeDesbloqueado;

        public LogroExpandidoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenLogro = itemView.findViewById(R.id.imagen_logro_expandido);
            nombreLogro = itemView.findViewById(R.id.nombre_logro_expandido);
            descripcionLogro = itemView.findViewById(R.id.descripcion_logro_expandido);
            progresoLogro = itemView.findViewById(R.id.progreso_logro_expandido);
            textoProgresoLogro = itemView.findViewById(R.id.texto_progreso_logro_expandido);
            badgeDesbloqueado = itemView.findViewById(R.id.badge_desbloqueado_expandido);
        }

        public void bind(Logro logro) {
            nombreLogro.setText(logro.getNombre());
            descripcionLogro.setText(logro.getDescripcion());
            
            // Configurar progreso
            int progreso = logro.getProgreso();
            progresoLogro.setProgress(progreso);
            textoProgresoLogro.setText(progreso + "%");

            // Aplicar estilo según estado (bloqueado/desbloqueado)
            if (logro.isDesbloqueado()) {
                // DESBLOQUEADO
                badgeDesbloqueado.setVisibility(View.VISIBLE);
                progresoLogro.setVisibility(View.GONE);
                textoProgresoLogro.setVisibility(View.GONE);
                
                // Opacidad 100% - totalmente visible
                itemView.setAlpha(1.0f);
                imagenLogro.setAlpha(1.0f);
                nombreLogro.setAlpha(1.0f);
                descripcionLogro.setAlpha(1.0f);
                
                // Colores normales
                nombreLogro.setTextColor(itemView.getContext().getResources().getColor(R.color.negro));
                descripcionLogro.setTextColor(itemView.getContext().getResources().getColor(R.color.gris_oscuro));
                
                // Sin filtro - imagen en color
                imagenLogro.setColorFilter(null);
                
            } else {
                // BLOQUEADO
                badgeDesbloqueado.setVisibility(View.GONE);
                progresoLogro.setVisibility(View.VISIBLE);
                textoProgresoLogro.setVisibility(View.VISIBLE);
                
                // Opacidad 60% - más visible que en el view compacto
                itemView.setAlpha(0.65f);
                imagenLogro.setAlpha(0.65f);
                nombreLogro.setAlpha(0.9f);
                descripcionLogro.setAlpha(0.85f);
                
                // Colores más claros para mejor legibilidad
                nombreLogro.setTextColor(itemView.getContext().getResources().getColor(R.color.gris_oscuro));
                descripcionLogro.setTextColor(itemView.getContext().getResources().getColor(R.color.gris_medio));
                
                // Filtro grisáceo más suave
                imagenLogro.setColorFilter(itemView.getContext().getResources().getColor(R.color.gris_claro), 
                    android.graphics.PorterDuff.Mode.MULTIPLY);
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
