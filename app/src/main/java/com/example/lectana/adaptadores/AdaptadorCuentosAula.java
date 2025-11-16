package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloCuentoAula;

import java.util.List;

public class AdaptadorCuentosAula extends RecyclerView.Adapter<AdaptadorCuentosAula.ViewHolderCuento> {

    private List<ModeloCuentoAula> listaCuentos;

    public AdaptadorCuentosAula(List<ModeloCuentoAula> listaCuentos) {
        this.listaCuentos = listaCuentos;
    }

    @NonNull
    @Override
    public ViewHolderCuento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuento_aula, parent, false);
        return new ViewHolderCuento(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCuento holder, int position) {
        ModeloCuentoAula cuento = listaCuentos.get(position);
        
        holder.textoTituloCuento.setText(cuento.getTitulo());
        holder.textoAutorCuento.setText(cuento.getAutor());
        holder.textoProgresoCuento.setText(cuento.getProgreso() + "%");
        
        // Cambiar color del indicador según el estado
        if (cuento.isCompletado()) {
            holder.indicadorEstadoCuento.setBackgroundResource(R.drawable.circulo_verde);
        } else {
            holder.indicadorEstadoCuento.setBackgroundResource(R.drawable.circulo_gris);
        }
    }

    @Override
    public int getItemCount() {
        return listaCuentos.size();
    }

    public static class ViewHolderCuento extends RecyclerView.ViewHolder {
        TextView textoTituloCuento;
        TextView textoAutorCuento;
        TextView textoProgresoCuento;
        View indicadorEstadoCuento;

        public ViewHolderCuento(@NonNull View itemView) {
            super(itemView);
            textoTituloCuento = itemView.findViewById(R.id.texto_titulo_cuento);
            textoAutorCuento = itemView.findViewById(R.id.texto_autor_cuento);
            textoProgresoCuento = itemView.findViewById(R.id.texto_progreso_cuento);
            indicadorEstadoCuento = itemView.findViewById(R.id.indicador_estado_cuento);
        }
    }
}
