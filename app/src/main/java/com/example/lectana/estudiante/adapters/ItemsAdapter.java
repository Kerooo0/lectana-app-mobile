package com.example.lectana.estudiante.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectana.R;
import com.example.lectana.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onComprarClick(Item item);
        void onItemClick(Item item);
    }

    public ItemsAdapter(OnItemClickListener listener) {
        this.items = new ArrayList<>();
        this.listener = listener;
    }

    public void setItems(List<Item> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tienda, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenItem;
        private TextView nombreItem;
        private TextView descripcionItem;
        private TextView tipoItem;
        private TextView categoriaItem;
        private TextView precioItem;
        private LinearLayout layoutPrecio;
        private TextView badgeDesbloqueado;
        private Button botonComprar;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenItem = itemView.findViewById(R.id.imagen_item);
            nombreItem = itemView.findViewById(R.id.nombre_item);
            descripcionItem = itemView.findViewById(R.id.descripcion_item);
            tipoItem = itemView.findViewById(R.id.tipo_item);
            categoriaItem = itemView.findViewById(R.id.categoria_item);
            precioItem = itemView.findViewById(R.id.precio_item);
            layoutPrecio = itemView.findViewById(R.id.layout_precio);
            badgeDesbloqueado = itemView.findViewById(R.id.badge_desbloqueado);
            botonComprar = itemView.findViewById(R.id.boton_comprar);
        }

        public void bind(Item item, OnItemClickListener listener) {
            nombreItem.setText(item.getNombre());
            descripcionItem.setText(item.getDescripcion() != null ? item.getDescripcion() : "");
            tipoItem.setText(capitalize(item.getTipo()));
            categoriaItem.setText(capitalize(item.getCategoria()));
            precioItem.setText(String.valueOf(item.getPrecioPuntos()));

            // Si el item está desbloqueado
            if (item.isDesbloqueado()) {
                badgeDesbloqueado.setVisibility(View.VISIBLE);
                botonComprar.setVisibility(View.GONE);
                layoutPrecio.setVisibility(View.GONE);
            } else {
                badgeDesbloqueado.setVisibility(View.GONE);
                botonComprar.setVisibility(View.VISIBLE);
                layoutPrecio.setVisibility(View.VISIBLE);
            }

            // Cargar imagen
            if (item.getUrlImagen() != null && !item.getUrlImagen().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(item.getUrlImagen())
                        .placeholder(R.drawable.ic_astronaut)
                        .error(R.drawable.ic_astronaut)
                        .into(imagenItem);
            } else {
                imagenItem.setImageResource(R.drawable.ic_astronaut);
            }

            // Click en el botón comprar
            botonComprar.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onComprarClick(item);
                }
            });

            // Click en el item completo
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }

        private String capitalize(String text) {
            if (text == null || text.isEmpty()) return "";
            return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
        }
    }
}
