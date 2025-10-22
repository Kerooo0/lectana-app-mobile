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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;

import java.util.List;

public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.PreguntaViewHolder> {
    private List<CrearActividadActivity.PreguntaItem> listaPreguntas;
    private OnPreguntaListener listener;
    private CrearActividadActivity actividad;

    public interface OnPreguntaListener {
        void onEliminarPregunta(int position);
    }

    public PreguntasAdapter(List<CrearActividadActivity.PreguntaItem> listaPreguntas, OnPreguntaListener listener, CrearActividadActivity actividad) {
        this.listaPreguntas = listaPreguntas;
        this.listener = listener;
        this.actividad = actividad;
    }

    private String obtenerTipoActividad() {
        if (actividad != null) {
            return actividad.obtenerTipoActividad();
        }
        return "Opción Múltiple"; // Valor por defecto
    }

    @NonNull
    @Override
    public PreguntaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pregunta, parent, false);
        return new PreguntaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreguntaViewHolder holder, int position) {
        CrearActividadActivity.PreguntaItem pregunta = listaPreguntas.get(position);
        holder.bind(pregunta, position, listener);
    }

    @Override
    public int getItemCount() {
        return listaPreguntas.size();
    }

    static class PreguntaViewHolder extends RecyclerView.ViewHolder {
        private EditText editEnunciado;
        private RecyclerView recyclerViewRespuestas;
        private Button btnAgregarRespuesta;
        private ImageButton btnEliminarPregunta;
        private TextView textNumeroPregunta;

        public PreguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            editEnunciado = itemView.findViewById(R.id.editEnunciado);
            recyclerViewRespuestas = itemView.findViewById(R.id.recyclerViewRespuestas);
            btnAgregarRespuesta = itemView.findViewById(R.id.btnAgregarRespuesta);
            btnEliminarPregunta = itemView.findViewById(R.id.btnEliminarPregunta);
            textNumeroPregunta = itemView.findViewById(R.id.textNumeroPregunta);
        }

        public void bind(CrearActividadActivity.PreguntaItem pregunta, int position, OnPreguntaListener listener) {
            textNumeroPregunta.setText("Pregunta " + (position + 1));
            editEnunciado.setText(pregunta.getEnunciado());
            
            // Configurar listener para actualizar el enunciado
            editEnunciado.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    pregunta.setEnunciado(editEnunciado.getText().toString());
                }
            });

            // Configurar RecyclerView de respuestas
            // Por ahora usamos "Opción Múltiple" como valor por defecto
            // TODO: Implementar comunicación con la actividad principal
            RespuestasAdapter respuestasAdapter = new RespuestasAdapter(pregunta.getRespuestas(), "Opción Múltiple");
            recyclerViewRespuestas.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerViewRespuestas.setAdapter(respuestasAdapter);

            // Botón agregar respuesta
            btnAgregarRespuesta.setOnClickListener(v -> {
                CrearActividadActivity.RespuestaItem nuevaRespuesta = new CrearActividadActivity.RespuestaItem();
                nuevaRespuesta.setRespuesta("Nueva respuesta"); // Texto por defecto
                nuevaRespuesta.setEsCorrecta(false);
                pregunta.getRespuestas().add(nuevaRespuesta);
                respuestasAdapter.notifyDataSetChanged();
            });

            // Botón eliminar pregunta
            btnEliminarPregunta.setOnClickListener(v -> {
                listener.onEliminarPregunta(position);
            });
        }
    }

    // Adapter interno para respuestas
    static class RespuestasAdapter extends RecyclerView.Adapter<RespuestasAdapter.RespuestaViewHolder> {
        private List<CrearActividadActivity.RespuestaItem> respuestas;
        private String tipoActividad;

        public RespuestasAdapter(List<CrearActividadActivity.RespuestaItem> respuestas, String tipoActividad) {
            this.respuestas = respuestas;
            this.tipoActividad = tipoActividad;
        }

        @NonNull
        @Override
        public RespuestaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respuesta, parent, false);
            return new RespuestaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RespuestaViewHolder holder, int position) {
            CrearActividadActivity.RespuestaItem respuesta = respuestas.get(position);
            holder.bind(respuesta, position, this);
        }

        @Override
        public int getItemCount() {
            return respuestas.size();
        }

        static class RespuestaViewHolder extends RecyclerView.ViewHolder {
            private EditText editRespuesta;
            private CheckBox checkBoxCorrecta;
            private ImageButton btnEliminarRespuesta;

            public RespuestaViewHolder(@NonNull View itemView) {
                super(itemView);
                editRespuesta = itemView.findViewById(R.id.editRespuesta);
                checkBoxCorrecta = itemView.findViewById(R.id.checkBoxCorrecta);
                btnEliminarRespuesta = itemView.findViewById(R.id.btnEliminarRespuesta);
            }

            public void bind(CrearActividadActivity.RespuestaItem respuesta, int position, RespuestasAdapter adapter) {
                editRespuesta.setText(respuesta.getRespuesta());
                checkBoxCorrecta.setChecked(respuesta.isEsCorrecta());

                // Deshabilitar checkbox si es respuesta abierta
                boolean esRespuestaAbierta = "Respuesta Abierta".equals(adapter.tipoActividad);
                checkBoxCorrecta.setEnabled(!esRespuestaAbierta);
                if (esRespuestaAbierta) {
                    checkBoxCorrecta.setChecked(false);
                    respuesta.setEsCorrecta(false);
                }

                // Configurar listeners
                editRespuesta.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        respuesta.setRespuesta(editRespuesta.getText().toString());
                    }
                });

                checkBoxCorrecta.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!esRespuestaAbierta) {
                        respuesta.setEsCorrecta(isChecked);
                    }
                });

                btnEliminarRespuesta.setOnClickListener(v -> {
                    adapter.respuestas.remove(position);
                    adapter.notifyDataSetChanged();
                });
            }
        }
    }
}
