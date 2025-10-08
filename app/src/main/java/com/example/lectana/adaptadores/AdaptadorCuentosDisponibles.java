package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
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
        private TextView textoNombreCuento;
        private TextView textoAutorCuento;
        private TextView textoNivelCuento;
        private ImageView iconoSeleccionado;

        public ViewHolderCuento(@NonNull View itemView) {
            super(itemView);
            checkboxCuento = itemView.findViewById(R.id.checkbox_cuento);
            textoNombreCuento = itemView.findViewById(R.id.texto_titulo_cuento);
            textoAutorCuento = itemView.findViewById(R.id.texto_autor_cuento);
            textoNivelCuento = itemView.findViewById(R.id.texto_nivel_cuento);
            iconoSeleccionado = itemView.findViewById(R.id.icono_seleccionado);
        }

        public void configurarCuento(ModeloCuento cuento) {
            textoNombreCuento.setText(cuento.getTitulo());
            textoAutorCuento.setText(cuento.getAutor());
            textoNivelCuento.setText("Nivel: " + cuento.getNivel());
            
            checkboxCuento.setChecked(cuento.isSeleccionado());
            iconoSeleccionado.setVisibility(cuento.isSeleccionado() ? View.VISIBLE : View.GONE);

            checkboxCuento.setOnCheckedChangeListener((buttonView, isChecked) -> {
                cuento.setSeleccionado(isChecked);
                iconoSeleccionado.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                
                if (listener != null) {
                    listener.onCuentoSeleccionado(cuento, isChecked);
                }
            });

            // Click en toda la tarjeta para seleccionar/deseleccionar
            itemView.setOnClickListener(v -> {
                checkboxCuento.setChecked(!checkboxCuento.isChecked());
            });
        }
    }
}
