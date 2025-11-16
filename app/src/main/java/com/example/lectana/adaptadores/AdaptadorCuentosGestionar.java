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
import com.example.lectana.modelos.CuentoApi;

import java.util.List;

public class AdaptadorCuentosGestionar extends RecyclerView.Adapter<AdaptadorCuentosGestionar.CuentoViewHolder> {

    private List<CuentoApi> cuentos;
    private OnCuentoClickListener listener;

    public interface OnCuentoClickListener {
        void onEliminarCuento(CuentoApi cuento);
    }

    public AdaptadorCuentosGestionar(List<CuentoApi> cuentos, OnCuentoClickListener listener) {
        this.cuentos = cuentos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CuentoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cuento_gestionar, parent, false);
        return new CuentoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuentoViewHolder holder, int position) {
        CuentoApi cuento = cuentos.get(position);
        holder.bind(cuento);
    }

    @Override
    public int getItemCount() {
        return cuentos.size();
    }

    class CuentoViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenCuento;
        private TextView textoNombreCuento;
        private TextView textoAutorCuento;
        private TextView textoEdadCuento;
        private ImageView botonEliminar;

        public CuentoViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenCuento = itemView.findViewById(R.id.imagen_cuento);
            textoNombreCuento = itemView.findViewById(R.id.texto_nombre_cuento);
            textoAutorCuento = itemView.findViewById(R.id.texto_autor_cuento);
            textoEdadCuento = itemView.findViewById(R.id.texto_edad_cuento);
            botonEliminar = itemView.findViewById(R.id.boton_eliminar);
        }

        public void bind(CuentoApi cuento) {
            textoNombreCuento.setText(cuento.getTitulo());
            
            // Autor
            String autor = "Autor desconocido";
            if (cuento.getAutor() != null) {
                String nombre = cuento.getAutor().getNombre() != null ? cuento.getAutor().getNombre() : "";
                String apellido = cuento.getAutor().getApellido() != null ? cuento.getAutor().getApellido() : "";
                autor = nombre + " " + apellido;
            }
            textoAutorCuento.setText(autor);
            
            // Edad
            textoEdadCuento.setText("Edad: " + cuento.getEdad_publico() + " años");
            
            // Imagen
            if (cuento.getUrl_img() != null && !cuento.getUrl_img().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(cuento.getUrl_img())
                        .placeholder(R.drawable.imagen_cuento_placeholder)
                        .error(R.drawable.imagen_cuento_placeholder)
                        .into(imagenCuento);
            } else {
                imagenCuento.setImageResource(R.drawable.imagen_cuento_placeholder);
            }

            // Botón eliminar
            botonEliminar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEliminarCuento(cuento);
                }
            });
        }
    }
}
