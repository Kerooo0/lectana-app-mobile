package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectana.R;
import com.example.lectana.modelos.ModeloCuentoDetallado;

import java.util.List;

    public class AdaptadorCuentosDetallados extends RecyclerView.Adapter<AdaptadorCuentosDetallados.ViewHolderCuentoDetallado> {

    private List<ModeloCuentoDetallado> listaCuentosDetallados;
    private OnCuentoClickListener listener;

    public interface OnCuentoClickListener {
        void onClickCuento(ModeloCuentoDetallado cuento);
        void onQuitarCuento(ModeloCuentoDetallado cuento);
    }

    public AdaptadorCuentosDetallados(List<ModeloCuentoDetallado> listaCuentosDetallados) {
        this(listaCuentosDetallados, null);
    }

    public AdaptadorCuentosDetallados(List<ModeloCuentoDetallado> listaCuentosDetallados, OnCuentoClickListener listener) {
        this.listaCuentosDetallados = listaCuentosDetallados;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderCuentoDetallado onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuento_detallado, parent, false);
        return new ViewHolderCuentoDetallado(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCuentoDetallado holder, int position) {
        ModeloCuentoDetallado cuento = listaCuentosDetallados.get(position);
        
        holder.textoTituloCuento.setText(cuento.getTitulo());
        holder.textoSubtituloCuento.setText(cuento.getSubtitulo());
        
        // Datos de estudiantes y actividades vacíos por ahora - no hay datos reales disponibles
        holder.textoEstudiantesLeidos.setText("Sin datos de lectura");
        holder.textoActividad1.setText("Sin actividades disponibles");
        holder.textoCompletadas1.setText("0 completadas");
        holder.textoActividad2.setText("Sin actividades disponibles");
        holder.textoCompletadas2.setText("0 completadas");
        
        // Imagen del cuento (si hay URL)
        if (cuento.getImagenUrl() != null && !cuento.getImagenUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(cuento.getImagenUrl())
                    .placeholder(R.drawable.imagen_cuento_placeholder)
                    .error(R.drawable.imagen_cuento_placeholder)
                    .into(holder.imagenCuento);
        } else {
            holder.imagenCuento.setImageResource(R.drawable.imagen_cuento_placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClickCuento(cuento);
        });
        holder.botonQuitar.setOnClickListener(v -> {
            if (listener != null) listener.onQuitarCuento(cuento);
        });
    }

    @Override
    public int getItemCount() {
        return listaCuentosDetallados.size();
    }

    public static class ViewHolderCuentoDetallado extends RecyclerView.ViewHolder {
        ImageView imagenCuento;
        TextView textoTituloCuento;
        TextView textoSubtituloCuento;
        TextView textoEstudiantesLeidos;
        TextView textoActividad1;
        TextView textoCompletadas1;
        TextView textoActividad2;
        TextView textoCompletadas2;
        ImageView botonQuitar;

        public ViewHolderCuentoDetallado(@NonNull View itemView) {
            super(itemView);
            imagenCuento = itemView.findViewById(R.id.imagen_cuento);
            textoTituloCuento = itemView.findViewById(R.id.texto_titulo_cuento);
            textoSubtituloCuento = itemView.findViewById(R.id.texto_subtitulo_cuento);
            textoEstudiantesLeidos = itemView.findViewById(R.id.texto_estudiantes_leidos);
            textoActividad1 = itemView.findViewById(R.id.texto_actividad_1);
            textoCompletadas1 = itemView.findViewById(R.id.texto_completadas_1);
            textoActividad2 = itemView.findViewById(R.id.texto_actividad_2);
            textoCompletadas2 = itemView.findViewById(R.id.texto_completadas_2);
            botonQuitar = itemView.findViewById(R.id.boton_quitar);
        }
    }
}
