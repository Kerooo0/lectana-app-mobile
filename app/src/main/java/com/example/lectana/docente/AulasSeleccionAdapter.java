package com.example.lectana.docente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloAula;

import java.util.List;

public class AulasSeleccionAdapter extends RecyclerView.Adapter<AulasSeleccionAdapter.AulaViewHolder> {
    private List<ModeloAula> listaAulas;
    private List<ModeloAula> aulasSeleccionadas;
    private OnAulaSeleccionListener listener;

    public interface OnAulaSeleccionListener {
        void onAulaSeleccionada(ModeloAula aula, boolean seleccionada);
    }

    public AulasSeleccionAdapter(List<ModeloAula> listaAulas, List<ModeloAula> aulasSeleccionadas, OnAulaSeleccionListener listener) {
        this.listaAulas = listaAulas;
        this.aulasSeleccionadas = aulasSeleccionadas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AulaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aula_seleccion, parent, false);
        return new AulaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AulaViewHolder holder, int position) {
        ModeloAula aula = listaAulas.get(position);
        boolean seleccionada = aulasSeleccionadas.contains(aula);
        holder.bind(aula, seleccionada, listener);
    }

    @Override
    public int getItemCount() {
        return listaAulas.size();
    }

    static class AulaViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBoxAula;
        private TextView textNombreAula;
        private TextView textGrado;

        public AulaViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxAula = itemView.findViewById(R.id.checkBoxAula);
            textNombreAula = itemView.findViewById(R.id.textNombreAula);
            textGrado = itemView.findViewById(R.id.textGrado);
        }

        public void bind(ModeloAula aula, boolean seleccionada, OnAulaSeleccionListener listener) {
            textNombreAula.setText(aula.getNombre_aula());
            textGrado.setText("Grado " + aula.getGrado());
            checkBoxAula.setChecked(seleccionada);

            checkBoxAula.setOnCheckedChangeListener((buttonView, isChecked) -> {
                listener.onAulaSeleccionada(aula, isChecked);
            });

            itemView.setOnClickListener(v -> {
                checkBoxAula.setChecked(!checkBoxAula.isChecked());
            });
        }
    }
}

