package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloAula;

import java.util.List;

public class AdaptadorAulasSeleccionar extends RecyclerView.Adapter<AdaptadorAulasSeleccionar.ViewHolderAula> {

    private List<ModeloAula> listaAulas;
    private OnAulaSeleccionadaListener listener;
    private int aulaSeleccionadaId = -1;

    public interface OnAulaSeleccionadaListener {
        void onAulaSeleccionada(ModeloAula aula);
    }

    public AdaptadorAulasSeleccionar(List<ModeloAula> listaAulas, OnAulaSeleccionadaListener listener) {
        this.listaAulas = listaAulas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderAula onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aula_seleccionar, parent, false);
        return new ViewHolderAula(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAula holder, int position) {
        ModeloAula aula = listaAulas.get(position);
        holder.configurarAula(aula);
    }

    @Override
    public int getItemCount() {
        return listaAulas.size();
    }

    public class ViewHolderAula extends RecyclerView.ViewHolder {
        private RadioButton radioButtonAula;
        private TextView nombreAulaItem;
        private TextView codigoAulaItem;
        private TextView numeroEstudiantesItem;
        private TextView numeroCuentosItem;
        private ImageView iconoAulaSeleccionada;

        public ViewHolderAula(@NonNull View itemView) {
            super(itemView);
            radioButtonAula = itemView.findViewById(R.id.radio_button_aula);
            nombreAulaItem = itemView.findViewById(R.id.nombre_aula_item);
            codigoAulaItem = itemView.findViewById(R.id.codigo_aula_item);
            numeroEstudiantesItem = itemView.findViewById(R.id.numero_estudiantes_item);
            numeroCuentosItem = itemView.findViewById(R.id.numero_cuentos_item);
            iconoAulaSeleccionada = itemView.findViewById(R.id.icono_aula_seleccionada);
        }

        public void configurarAula(ModeloAula aula) {
            nombreAulaItem.setText(aula.getNombre_aula());
            codigoAulaItem.setText("Código: " + aula.getCodigo_aula());
            numeroEstudiantesItem.setText(aula.getNumero_estudiantes_aula() + " estudiantes");
            numeroCuentosItem.setText("5 cuentos"); // TODO: Agregar campo numero_cuentos al modelo
            
            // Configurar selección
            boolean isSelected = aula.getId_aula().equals(String.valueOf(aulaSeleccionadaId));
            radioButtonAula.setChecked(isSelected);
            iconoAulaSeleccionada.setVisibility(isSelected ? View.VISIBLE : View.GONE);

            radioButtonAula.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    aulaSeleccionadaId = Integer.parseInt(aula.getId_aula());
                    notifyDataSetChanged(); // Actualizar todas las vistas
                    
                    if (listener != null) {
                        listener.onAulaSeleccionada(aula);
                    }
                }
            });

            // Click en toda la tarjeta para seleccionar
            itemView.setOnClickListener(v -> {
                radioButtonAula.setChecked(true);
            });
        }
    }

    public String getAulaSeleccionadaId() {
        return aulaSeleccionadaId != -1 ? String.valueOf(aulaSeleccionadaId) : null;
    }
}
