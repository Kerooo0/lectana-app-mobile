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

import com.example.lectana.R;
import com.example.lectana.DetalleCuentoActivity;
import com.example.lectana.modelos.ModeloCuento;

import java.util.List;

public class AdaptadorCuentosDisponibles extends RecyclerView.Adapter<AdaptadorCuentosDisponibles.ViewHolderCuento> {

    private List<ModeloCuento> listaCuentos;
    private OnCuentoSeleccionadoListener listener;

    public interface OnCuentoSeleccionadoListener {
        void onCuentoSeleccionado(ModeloCuento cuento, boolean seleccionado);
    }

    public AdaptadorCuentosDisponibles(List<ModeloCuento> listaCuentos, OnCuentoSeleccionadoListener listener) {
        this.listaCuentos = listaCuentos;
        this.listener = listener;
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
            
            checkboxCuento.setChecked(cuento.isSeleccionado());

            // TODO: Cargar imagen del cuento si imagenUrl no es vacío
            // Glide.with(itemView.getContext()).load(cuento.getImagenUrl()).into(imagenCuento);

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

            // Click en botón de vista previa
            botonVistaPrevia.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DetalleCuentoActivity.class);
                intent.putExtra("id_cuento", cuento.getId());
                intent.putExtra("titulo_cuento", cuento.getTitulo());
                intent.putExtra("autor_cuento", cuento.getAutor());
                intent.putExtra("genero_cuento", cuento.getGenero());
                intent.putExtra("edad_cuento", cuento.getEdadRecomendada());
                intent.putExtra("rating_cuento", cuento.getRating());
                intent.putExtra("tiempo_cuento", cuento.getTiempoLectura());
                intent.putExtra("descripcion_cuento", cuento.getDescripcion());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
