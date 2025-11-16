package com.example.lectana.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.lectana.R;
import com.example.lectana.modelos.ModeloCuento;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorBiblioteca extends RecyclerView.Adapter<AdaptadorBiblioteca.ViewHolder> {

    public interface OnCuentoClickListener {
        void onClickVerDetalle(ModeloCuento cuento);
        void onClickReproducir(ModeloCuento cuento);
    }

    private final Context context;
    private final List<ModeloCuento> listaOriginal;
    private final List<ModeloCuento> listaFiltrada;
    private final OnCuentoClickListener listener;

    public AdaptadorBiblioteca(Context context, List<ModeloCuento> lista, OnCuentoClickListener listener) {
        this.context = context;
        this.listaOriginal = new ArrayList<>(lista);
        this.listaFiltrada = new ArrayList<>(lista);
        this.listener = listener;
    }

    public void submitList(List<ModeloCuento> nuevaLista) {
        listaOriginal.clear();
        listaOriginal.addAll(nuevaLista);
        filtrarPorTexto("");
    }

    public void filtrarPorTexto(String texto) {
        String q = texto == null ? "" : texto.trim().toLowerCase();
        listaFiltrada.clear();
        for (ModeloCuento c : listaOriginal) {
            if (q.isEmpty() || c.getTitulo().toLowerCase().contains(q) || c.getAutor().toLowerCase().contains(q)) {
                listaFiltrada.add(c);
            }
        }
        notifyDataSetChanged();
    }

    public void filtrarPorGenero(String genero) {
        listaFiltrada.clear();
        if (genero == null) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            for (ModeloCuento c : listaOriginal) {
                if (genero.equalsIgnoreCase(c.getGenero())) {
                    listaFiltrada.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_cuento_biblioteca, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloCuento cuento = listaFiltrada.get(position);
        holder.titulo.setText(cuento.getTitulo());
        holder.autor.setText(cuento.getAutor());
        holder.genero.setText(cuento.getGenero());
        holder.tiempo.setText(" • " + cuento.getTiempoLectura());
        holder.rating.setText(" " + (cuento.getRating() != null ? cuento.getRating() : "4.5"));

        // Cargar imagen del cuento desde Supabase
        if (cuento.getImagenUrl() != null && !cuento.getImagenUrl().isEmpty()) {
            Glide.with(context)
                .load(cuento.getImagenUrl())
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(12)))
                .placeholder(R.drawable.imagen_cuento_placeholder) // Imagen mientras carga
                .error(R.drawable.imagen_cuento_placeholder) // Imagen si falla
                .into(holder.imagen);
        } else {
            // Si no hay URL, usar imagen placeholder
            holder.imagen.setImageResource(R.drawable.imagen_cuento_placeholder);
        }

        holder.botonDetalle.setOnClickListener(v -> listener.onClickVerDetalle(cuento));
        holder.botonPlay.setOnClickListener(v -> listener.onClickReproducir(cuento));
        // Evitar NPE por si faltan vistas
        if (holder.botonPlay == null) {
            holder.itemView.setOnClickListener(v -> listener.onClickReproducir(cuento));
        }
    }

    @Override
    public int getItemCount() {
        return listaFiltrada.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;
        TextView titulo;
        TextView autor;
        TextView genero;
        TextView tiempo;
        TextView rating;
        Button botonDetalle;
        ImageView botonPlay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagen_portada);
            titulo = itemView.findViewById(R.id.texto_titulo);
            autor = itemView.findViewById(R.id.texto_autor);
            genero = itemView.findViewById(R.id.texto_genero);
            tiempo = itemView.findViewById(R.id.texto_tiempo);
            rating = itemView.findViewById(R.id.texto_rating);
            botonDetalle = itemView.findViewById(R.id.boton_ver_detalle);
            botonPlay = itemView.findViewById(R.id.boton_play);
        }
    }
}


