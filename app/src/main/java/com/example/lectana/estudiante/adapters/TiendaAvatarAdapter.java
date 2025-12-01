package com.example.lectana.estudiante.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectana.R;
import com.example.lectana.modelos.Avatar;

import java.util.ArrayList;
import java.util.List;

public class TiendaAvatarAdapter extends RecyclerView.Adapter<TiendaAvatarAdapter.AvatarViewHolder> {

    private List<Avatar> avatares;
    private OnCompraClickListener listener;
    private int puntosActuales;

    public interface OnCompraClickListener {
        void onCompraClick(Avatar avatar);
    }

    public TiendaAvatarAdapter(OnCompraClickListener listener) {
        this.avatares = new ArrayList<>();
        this.listener = listener;
        this.puntosActuales = 0;
    }

    public void setAvatares(List<Avatar> avatares) {
        this.avatares = avatares != null ? avatares : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setPuntosActuales(int puntos) {
        this.puntosActuales = puntos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tienda_avatar, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        Avatar avatar = avatares.get(position);
        holder.bind(avatar, listener, puntosActuales);
    }

    @Override
    public int getItemCount() {
        return avatares.size();
    }

    static class AvatarViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenAvatar;
        private TextView nombreAvatar;
        private TextView precioAvatar;
        private TextView descripcionAvatar;
        private Button btnComprar;

        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenAvatar = itemView.findViewById(R.id.imagen_avatar_tienda);
            nombreAvatar = itemView.findViewById(R.id.nombre_avatar_tienda);
            precioAvatar = itemView.findViewById(R.id.precio_avatar_tienda);
            descripcionAvatar = itemView.findViewById(R.id.descripcion_avatar_tienda);
            btnComprar = itemView.findViewById(R.id.btn_comprar_avatar);
        }

        public void bind(Avatar avatar, OnCompraClickListener listener, int puntosActuales) {
            nombreAvatar.setText(avatar.getNombre());
            descripcionAvatar.setText(avatar.getDescripcion());
            precioAvatar.setText(avatar.getPrecio() + " pts");

            // Cargar imagen
            if (avatar.getUrlImagen() != null && !avatar.getUrlImagen().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(avatar.getUrlImagen())
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.ic_default_avatar)
                        .into(imagenAvatar);
            } else {
                imagenAvatar.setImageResource(R.drawable.ic_default_avatar);
            }

            // Habilitar/deshabilitar botón según puntos disponibles
            boolean tienePuntos = puntosActuales >= avatar.getPrecio();
            btnComprar.setEnabled(tienePuntos);

            if (!tienePuntos) {
                btnComprar.setText("Puntos insuficientes");
                btnComprar.setAlpha(0.5f);
            } else {
                btnComprar.setText("Comprar");
                btnComprar.setAlpha(1.0f);
            }

            // Click para comprar
            btnComprar.setOnClickListener(v -> {
                if (listener != null && tienePuntos) {
                    listener.onCompraClick(avatar);
                }
            });
        }
    }
}
