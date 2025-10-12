package com.example.lectana.adaptadores;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.lectana.R;
import com.example.lectana.DetalleCuentoActivity;
import com.example.lectana.modelos.ModeloCuento;

import java.util.List;

public class AdaptadorCuentosDisponibles extends RecyclerView.Adapter<AdaptadorCuentosDisponibles.ViewHolderCuento> {

    private List<ModeloCuento> listaCuentos;
    private OnCuentoSeleccionadoListener listener;
    private String modo; // "asignar" o "explorar"

    public interface OnCuentoSeleccionadoListener {
        void onCuentoSeleccionado(ModeloCuento cuento, boolean seleccionado);
    }

    public AdaptadorCuentosDisponibles(List<ModeloCuento> listaCuentos, OnCuentoSeleccionadoListener listener, String modo) {
        this.listaCuentos = listaCuentos;
        this.listener = listener;
        this.modo = modo;
    }

    @NonNull
    @Override
    public ViewHolderCuento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cuento_disponible, parent, false);
        return new ViewHolderCuento(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCuento holder, int position) {
        ModeloCuento cuento = listaCuentos.get(position);
        holder.configurarCuento(cuento);
    }

    @Override
    public int getItemCount() {
        return listaCuentos.size();
    }

    public class ViewHolderCuento extends RecyclerView.ViewHolder {
        private CheckBox checkboxCuento;
        private ImageView imagenCuento;
        private TextView tituloCuento;
        private TextView autorCuento;
        private TextView badgeEdad;
        private TextView ratingCuento;
        private TextView tiempoLectura;
        private TextView generoCuento;
        private ImageView botonVistaPrevia;

        public ViewHolderCuento(@NonNull View itemView) {
            super(itemView);
            checkboxCuento = itemView.findViewById(R.id.checkbox_cuento);
            imagenCuento = itemView.findViewById(R.id.imagen_cuento);
            tituloCuento = itemView.findViewById(R.id.titulo_cuento);
            autorCuento = itemView.findViewById(R.id.autor_cuento);
            badgeEdad = itemView.findViewById(R.id.badge_edad);
            ratingCuento = itemView.findViewById(R.id.rating_cuento);
            tiempoLectura = itemView.findViewById(R.id.tiempo_lectura);
            generoCuento = itemView.findViewById(R.id.genero_cuento);
            botonVistaPrevia = itemView.findViewById(R.id.boton_vista_previa);
        }

        public void configurarCuento(ModeloCuento cuento) {
            tituloCuento.setText(cuento.getTitulo());
            autorCuento.setText(cuento.getAutor());
            badgeEdad.setText(cuento.getEdadRecomendada());
            ratingCuento.setText(cuento.getRating());
            tiempoLectura.setText(cuento.getTiempoLectura());
            generoCuento.setText(cuento.getGenero());
            
            // Cargar imagen del cuento desde Supabase
            if (cuento.getImagenUrl() != null && !cuento.getImagenUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                    .load(cuento.getImagenUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(12)))
                    .placeholder(R.drawable.imagen_cuento_placeholder) // Imagen mientras carga
                    .error(R.drawable.imagen_cuento_placeholder) // Imagen si falla
                    .into(imagenCuento);
            } else {
                // Si no hay URL, usar imagen placeholder
                imagenCuento.setImageResource(R.drawable.imagen_cuento_placeholder);
            }

            if ("asignar".equals(modo)) {
                // Modo asignar: mostrar checkbox y permitir selección
                checkboxCuento.setVisibility(View.VISIBLE);
                checkboxCuento.setChecked(cuento.isSeleccionado());
                
                checkboxCuento.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    cuento.setSeleccionado(isChecked);
                    if (listener != null) {
                        listener.onCuentoSeleccionado(cuento, isChecked);
                    }
                });

                // Click en toda la tarjeta para seleccionar/deseleccionar
                itemView.setOnClickListener(v -> {
                    checkboxCuento.setChecked(!checkboxCuento.isChecked());
                });
                
            } else {
                // Modo explorar: ocultar checkbox y navegar directamente al detalle
                checkboxCuento.setVisibility(View.GONE);
                
                // Click en toda la tarjeta para abrir detalle
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(itemView.getContext(), DetalleCuentoActivity.class);
                    intent.putExtra("cuento_id", cuento.getId());
                    intent.putExtra("cuento_titulo", cuento.getTitulo());
                    intent.putExtra("cuento_autor", cuento.getAutor());
                    intent.putExtra("cuento_genero", cuento.getGenero());
                    intent.putExtra("cuento_edad", cuento.getEdadRecomendada());
                    intent.putExtra("cuento_duracion", cuento.getTiempoLectura());
                    intent.putExtra("cuento_descripcion", cuento.getDescripcion());
                    itemView.getContext().startActivity(intent);
                });
            }

            // Click en botón de vista previa (siempre disponible)
            botonVistaPrevia.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetalleCuentoActivity.class);
                intent.putExtra("cuento_id", cuento.getId());
                intent.putExtra("cuento_titulo", cuento.getTitulo());
                intent.putExtra("cuento_autor", cuento.getAutor());
                intent.putExtra("cuento_genero", cuento.getGenero());
                intent.putExtra("cuento_edad", cuento.getEdadRecomendada());
                intent.putExtra("cuento_duracion", cuento.getTiempoLectura());
                intent.putExtra("cuento_descripcion", cuento.getDescripcion());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
