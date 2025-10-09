package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloRecompensa;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorTienda extends RecyclerView.Adapter<AdaptadorTienda.ViewHolder> {

    private List<ModeloRecompensa> listaOriginal;
    private List<ModeloRecompensa> listaFiltrada;
    private OnRecompensaClickListener listener;

    public interface OnRecompensaClickListener {
        void onRecompensaClick(ModeloRecompensa recompensa);
    }

    public AdaptadorTienda(List<ModeloRecompensa> listaRecompensas, OnRecompensaClickListener listener) {
        this.listaOriginal = new ArrayList<>(listaRecompensas);
        this.listaFiltrada = new ArrayList<>(listaRecompensas);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recompensa_tienda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloRecompensa recompensa = listaFiltrada.get(position);
        
        holder.imagenRecompensa.setImageResource(recompensa.getImagen());
        holder.nombreRecompensa.setText(recompensa.getNombre());
        holder.costoRecompensa.setText(String.valueOf(recompensa.getCosto()));
        holder.botonAccion.setText(recompensa.getAccion());
        
        // Cambiar color del botón según el estado
        if (recompensa.isComprado()) {
            holder.botonAccion.setBackgroundResource(R.drawable.boton_verde_rectangular);
            holder.botonAccion.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.verde_fuerte));
            holder.botonAccion.setText("Equipado");
        } else {
            holder.botonAccion.setBackgroundResource(R.drawable.boton_verde_rectangular);
            holder.botonAccion.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        }
        
        // Ocultar costo si ya está comprado
        if (recompensa.isComprado()) {
            holder.costoRecompensa.setVisibility(View.GONE);
        } else {
            holder.costoRecompensa.setVisibility(View.VISIBLE);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRecompensaClick(recompensa);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    public void filtrarPorCategoria(String categoria) {
        listaFiltrada.clear();
        for (ModeloRecompensa recompensa : listaOriginal) {
            if (recompensa.getCategoria().equals(categoria)) {
                listaFiltrada.add(recompensa);
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarTodos() {
        listaFiltrada.clear();
        listaFiltrada.addAll(listaOriginal);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenRecompensa;
        TextView nombreRecompensa;
        TextView costoRecompensa;
        Button botonAccion;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenRecompensa = itemView.findViewById(R.id.imagen_recompensa);
            nombreRecompensa = itemView.findViewById(R.id.nombre_recompensa);
            costoRecompensa = itemView.findViewById(R.id.costo_recompensa);
            botonAccion = itemView.findViewById(R.id.boton_accion_recompensa);
        }
    }
}
