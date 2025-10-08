package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloCuentoDetallado;

import java.util.List;

public class AdaptadorCuentosDetallados extends RecyclerView.Adapter<AdaptadorCuentosDetallados.ViewHolderCuentoDetallado> {

    private List<ModeloCuentoDetallado> listaCuentosDetallados;

    public AdaptadorCuentosDetallados(List<ModeloCuentoDetallado> listaCuentosDetallados) {
        this.listaCuentosDetallados = listaCuentosDetallados;
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
        holder.textoEstudiantesLeidos.setText(cuento.getEstudiantesLeidos() + " estudiantes lo leyeron");
        holder.textoActividad1.setText(cuento.getActividad1());
        holder.textoCompletadas1.setText(cuento.getCompletadas1() + " completadas");
        holder.textoActividad2.setText(cuento.getActividad2());
        holder.textoCompletadas2.setText(cuento.getCompletadas2() + " completadas");
        
        // TODO: Cargar imagen desde URL cuando se implemente
        holder.imagenCuento.setImageResource(R.drawable.imagen_cuento_placeholder);
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
        }
    }
}
