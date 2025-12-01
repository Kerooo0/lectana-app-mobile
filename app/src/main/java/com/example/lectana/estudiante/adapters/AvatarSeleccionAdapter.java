package com.example.lectana.estudiante.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lectana.R;
import com.example.lectana.modelos.Item;

import java.util.ArrayList;
import java.util.List;

public class AvatarSeleccionAdapter extends RecyclerView.Adapter<AvatarSeleccionAdapter.AvatarViewHolder> {

    private List<Item> avatares;
    private OnAvatarClickListener listener;
    private String avatarEquipadoId;
    private static final String REMOVE_AVATAR_ID = "remove_avatar";

    public interface OnAvatarClickListener {
        void onAvatarClick(Item avatar);
        void onRemoveAvatarClick();
    }

    public AvatarSeleccionAdapter(OnAvatarClickListener listener) {
        this.avatares = new ArrayList<>();
        this.listener = listener;
    }

    public void setAvatares(List<Item> avatares) {
        this.avatares = avatares != null ? avatares : new ArrayList<>();
        notifyDataSetChanged();
    }
    
    /**
     * Agrega un item especial al principio de la lista para "Quitar Avatar"
     */
    public void addRemoveAvatarOption() {
        Item removeOption = new Item();
        removeOption.setId(REMOVE_AVATAR_ID);
        removeOption.setNombre("Quitar Avatar");
        removeOption.setDescripcion("Desequipar avatar actual");
        removeOption.setUrlImagen("");
        
        List<Item> updatedList = new ArrayList<>();
        updatedList.add(removeOption);
        updatedList.addAll(avatares);
        this.avatares = updatedList;
        notifyDataSetChanged();
    }

    public void setAvatarEquipado(String avatarId) {
        this.avatarEquipadoId = avatarId;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avatar_seleccion, parent, false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder holder, int position) {
        Item avatar = avatares.get(position);
        holder.bind(avatar, listener, avatarEquipadoId);
    }

    @Override
    public int getItemCount() {
        return avatares.size();
    }

    static class AvatarViewHolder extends RecyclerView.ViewHolder {
        private ImageView imagenAvatar;
        private TextView nombreAvatar;
        private TextView badgeEquipado;

        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenAvatar = itemView.findViewById(R.id.imagen_avatar_item);
            nombreAvatar = itemView.findViewById(R.id.nombre_avatar_item);
            badgeEquipado = itemView.findViewById(R.id.badge_equipado);
        }

        public void bind(Item avatar, OnAvatarClickListener listener, String avatarEquipadoId) {
            // Verificar si es el item especial "Quitar Avatar"
            if (REMOVE_AVATAR_ID.equals(avatar.getId())) {
                nombreAvatar.setText("✕ Quitar Avatar");
                badgeEquipado.setVisibility(View.GONE);
                
                // Usar un ícono predeterminado para la opción de quitar
                imagenAvatar.setImageResource(R.drawable.ic_delete);
                imagenAvatar.setScaleType(ImageView.ScaleType.CENTER);
                imagenAvatar.setBackgroundResource(R.drawable.circle_red);
                
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onRemoveAvatarClick();
                    }
                });
                return;
            }
            
            nombreAvatar.setText(avatar.getNombre());

            // Mostrar badge si está equipado
            if (avatar.getId().equals(avatarEquipadoId)) {
                badgeEquipado.setVisibility(View.VISIBLE);
            } else {
                badgeEquipado.setVisibility(View.GONE);
            }

            // Cargar imagen
            if (avatar.getUrlImagen() != null && !avatar.getUrlImagen().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(avatar.getUrlImagen())
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(imagenAvatar);
            } else {
                imagenAvatar.setImageResource(R.drawable.ic_person);
            }

            // Click en el item
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAvatarClick(avatar);
                }
            });
        }
    }
}
