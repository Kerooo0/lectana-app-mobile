package com.example.lectana.estudiante;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ActividadCompleta;
import com.example.lectana.modelos.ActividadCompletaResponse;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.PreguntaActividad;
import com.example.lectana.modelos.RespuestaActividad;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.AlumnoApiService;
import com.example.lectana.services.ApiClient;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para que el estudiante responda las preguntas de una actividad
 */
public class ResponderActividadActivity extends AppCompatActivity {
    private static final String TAG = "ResponderActividad";

    // Vistas
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private LinearLayout layoutPregunta;
    private TextView textProgresoPregunta;
    private TextView textPorcentajeProgreso;
    private ProgressBar progressBarPreguntas;
    private TextView textNumeroPregunta;
    private TextView textPregunta;
    private RecyclerView recyclerViewOpciones;
    private TextInputLayout layoutRespuestaAbierta;
    private TextInputEditText editRespuestaAbierta;
    private Button btnAnterior;
    private Button btnSiguiente;

    // Servicios y datos
    private ActividadesApiService actividadesApiService;
    private AlumnoApiService alumnoApiService;
    private SessionManager sessionManager;
    private int idActividad;
    private ActividadCompleta actividadCompleta;
    private List<PreguntaActividad> preguntas;
    private int indicePreguntaActual = 0;
    private OpcionesRespuestaAdapter opcionesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder_actividad);

        Log.d(TAG, "=== INICIANDO ResponderActividadActivity ===");

        inicializarComponentes();
        configurarToolbar();
        obtenerDatosIntent();
        cargarActividadCompleta();
    }

    private void inicializarComponentes() {
        Log.d(TAG, "Inicializando componentes...");

        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        layoutPregunta = findViewById(R.id.layoutPregunta);
        textProgresoPregunta = findViewById(R.id.textProgresoPregunta);
        textPorcentajeProgreso = findViewById(R.id.textPorcentajeProgreso);
        progressBarPreguntas = findViewById(R.id.progressBarPreguntas);
        textNumeroPregunta = findViewById(R.id.textNumeroPregunta);
        textPregunta = findViewById(R.id.textPregunta);
        recyclerViewOpciones = findViewById(R.id.recyclerViewOpciones);
        layoutRespuestaAbierta = findViewById(R.id.layoutRespuestaAbierta);
        editRespuestaAbierta = findViewById(R.id.editRespuestaAbierta);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);

        sessionManager = new SessionManager(this);
        actividadesApiService = ApiClient.getActividadesApiService();
        alumnoApiService = ApiClient.getAlumnoApiService();

        configurarEventos();

        Log.d(TAG, "Componentes inicializados");
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Responder Actividad");
        }
        toolbar.setNavigationOnClickListener(v -> mostrarDialogoSalir());
    }

    private void configurarEventos() {
        btnAnterior.setOnClickListener(v -> preguntaAnterior());
        btnSiguiente.setOnClickListener(v -> preguntaSiguiente());
    }

    private void obtenerDatosIntent() {
        idActividad = getIntent().getIntExtra("actividad_id", 0);
        Log.d(TAG, "ID Actividad recibido: " + idActividad);

        if (idActividad == 0) {
            Toast.makeText(this, "Error: ID de actividad no válido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarActividadCompleta() {
        Log.d(TAG, "Cargando actividad completa ID: " + idActividad);
        mostrarCargando(true);

        String token = "Bearer " + sessionManager.getToken();

        actividadesApiService.getActividadCompleta(token, idActividad)
                .enqueue(new Callback<ActividadCompletaResponse>() {
                    @Override
                    public void onResponse(Call<ActividadCompletaResponse> call,
                                         Response<ActividadCompletaResponse> response) {
                        mostrarCargando(false);

                        if (response.isSuccessful() && response.body() != null) {
                            ActividadCompletaResponse data = response.body();
                            if (data.getActividad() != null) {
                                actividadCompleta = data.getActividad();
                                preguntas = actividadCompleta.getPreguntaActividad();
                                
                                if (preguntas != null && !preguntas.isEmpty()) {
                                    Log.d(TAG, "Actividad cargada con " + preguntas.size() + " preguntas");
                                    mostrarPreguntaActual();
                                } else {
                                    Toast.makeText(ResponderActividadActivity.this,
                                            "Esta actividad no tiene preguntas",
                                            Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                Toast.makeText(ResponderActividadActivity.this,
                                        "Error al cargar la actividad",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Log.e(TAG, "Error en respuesta: " + response.code());
                            Toast.makeText(ResponderActividadActivity.this,
                                    "Error al cargar la actividad",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ActividadCompletaResponse> call, Throwable t) {
                        mostrarCargando(false);
                        Log.e(TAG, "Error de conexión: " + t.getMessage(), t);
                        Toast.makeText(ResponderActividadActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void mostrarPreguntaActual() {
        if (preguntas == null || preguntas.isEmpty()) {
            return;
        }

        PreguntaActividad preguntaActual = preguntas.get(indicePreguntaActual);
        Log.d(TAG, "Mostrando pregunta " + (indicePreguntaActual + 1) + " de " + preguntas.size());

        // Actualizar progreso
        int totalPreguntas = preguntas.size();
        int preguntaNumero = indicePreguntaActual + 1;
        textProgresoPregunta.setText("Pregunta " + preguntaNumero + " de " + totalPreguntas);
        
        int porcentaje = (int) ((preguntaNumero * 100.0) / totalPreguntas);
        textPorcentajeProgreso.setText(porcentaje + "%");
        progressBarPreguntas.setProgress(porcentaje);

        // Mostrar pregunta
        textNumeroPregunta.setText("Pregunta " + preguntaNumero);
        textPregunta.setText(preguntaActual.getEnunciado());

        // Determinar tipo de pregunta y mostrar opciones apropiadas
        if (preguntaActual.tieneRespuestas()) {
            // Pregunta de opción múltiple
            mostrarOpcionesMultiples(preguntaActual.getRespuestaActividad());
        } else {
            // Pregunta abierta
            mostrarRespuestaAbierta();
        }

        // Actualizar botones de navegación
        btnAnterior.setEnabled(indicePreguntaActual > 0);
        btnSiguiente.setText(indicePreguntaActual < totalPreguntas - 1 ? "Siguiente" : "Finalizar");
    }

    private void mostrarOpcionesMultiples(List<RespuestaActividad> opciones) {
        recyclerViewOpciones.setVisibility(View.VISIBLE);
        layoutRespuestaAbierta.setVisibility(View.GONE);

        opcionesAdapter = new OpcionesRespuestaAdapter(opciones, new OpcionesRespuestaAdapter.OnOpcionClickListener() {
            @Override
            public void onOpcionClick(RespuestaActividad opcion, int position) {
                enviarRespuestaOpcionMultiple(opcion);
            }
        });

        recyclerViewOpciones.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOpciones.setAdapter(opcionesAdapter);
    }

    private void mostrarRespuestaAbierta() {
        recyclerViewOpciones.setVisibility(View.GONE);
        layoutRespuestaAbierta.setVisibility(View.VISIBLE);
        editRespuestaAbierta.setText(""); // Limpiar campo
    }

    private void preguntaAnterior() {
        if (indicePreguntaActual > 0) {
            indicePreguntaActual--;
            mostrarPreguntaActual();
        }
    }

    private void preguntaSiguiente() {
        PreguntaActividad preguntaActual = preguntas.get(indicePreguntaActual);

        // Si es la última pregunta, mostrar diálogo de confirmación
        if (indicePreguntaActual == preguntas.size() - 1) {
            if (preguntaActual.tieneRespuestas()) {
                // Ya respondió, finalizar
                mostrarDialogoFinalizar();
            } else {
                // Pregunta abierta, enviar respuesta y finalizar
                enviarRespuestaAbierta();
            }
        } else {
            // No es la última, avanzar
            indicePreguntaActual++;
            mostrarPreguntaActual();
        }
    }

    private void enviarRespuestaOpcionMultiple(RespuestaActividad opcionSeleccionada) {
        PreguntaActividad preguntaActual = preguntas.get(indicePreguntaActual);
        String token = "Bearer " + sessionManager.getToken();

        Log.d(TAG, "Enviando respuesta para pregunta ID: " + preguntaActual.getIdPreguntaActividad());
        
        AlumnoApiService.ResponderPreguntaRequest request = 
                new AlumnoApiService.ResponderPreguntaRequest(opcionSeleccionada.getRespuestas());

        alumnoApiService.responderPregunta(token, preguntaActual.getIdPreguntaActividad(), request)
                .enqueue(new Callback<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call,
                                         Response<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                            AlumnoApiService.RespuestaPreguntaResponse data = response.body().getData();
                            
                            boolean esCorrecta = data != null && Boolean.TRUE.equals(data.getEsCorrecta());
                            mostrarResultadoRespuesta(esCorrecta, opcionSeleccionada.getRespuestas());
                            
                        } else {
                            Log.e(TAG, "Error al enviar respuesta: " + response.code());
                            Toast.makeText(ResponderActividadActivity.this,
                                    "Error al enviar respuesta",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call, Throwable t) {
                        Log.e(TAG, "Error de conexión al enviar respuesta", t);
                        Toast.makeText(ResponderActividadActivity.this,
                                "Error de conexión",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void enviarRespuestaAbierta() {
        String respuestaTexto = editRespuestaAbierta.getText() != null ? 
                editRespuestaAbierta.getText().toString().trim() : "";

        if (respuestaTexto.isEmpty()) {
            Toast.makeText(this, "Por favor escribe una respuesta", Toast.LENGTH_SHORT).show();
            return;
        }

        PreguntaActividad preguntaActual = preguntas.get(indicePreguntaActual);
        String token = "Bearer " + sessionManager.getToken();

        Log.d(TAG, "Enviando respuesta abierta para pregunta ID: " + preguntaActual.getIdPreguntaActividad());

        AlumnoApiService.ResponderPreguntaRequest request = 
                new AlumnoApiService.ResponderPreguntaRequest(respuestaTexto);

        alumnoApiService.responderPregunta(token, preguntaActual.getIdPreguntaActividad(), request)
                .enqueue(new Callback<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call,
                                         Response<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                            Toast.makeText(ResponderActividadActivity.this,
                                    "Respuesta enviada",
                                    Toast.LENGTH_SHORT).show();
                            mostrarDialogoFinalizar();
                        } else {
                            Log.e(TAG, "Error al enviar respuesta: " + response.code());
                            Toast.makeText(ResponderActividadActivity.this,
                                    "Error al enviar respuesta",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call, Throwable t) {
                        Log.e(TAG, "Error de conexión al enviar respuesta", t);
                        Toast.makeText(ResponderActividadActivity.this,
                                "Error de conexión",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarResultadoRespuesta(boolean esCorrecta, String respuesta) {
        String mensaje = esCorrecta ? 
                "¡Correcto! 🎉" : 
                "Incorrecto. La respuesta fue: " + respuesta;

        new AlertDialog.Builder(this)
                .setTitle(esCorrecta ? "¡Bien hecho!" : "Respuesta incorrecta")
                .setMessage(mensaje)
                .setPositiveButton("Continuar", (dialog, which) -> {
                    dialog.dismiss();
                    // Avanzar automáticamente si no es la última pregunta
                    if (indicePreguntaActual < preguntas.size() - 1) {
                        preguntaSiguiente();
                    } else {
                        mostrarDialogoFinalizar();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void mostrarDialogoFinalizar() {
        new AlertDialog.Builder(this)
                .setTitle("Finalizar actividad")
                .setMessage("¿Has completado todas las preguntas. ¿Deseas finalizar la actividad?")
                .setPositiveButton("Finalizar", (dialog, which) -> {
                    Toast.makeText(this, "Actividad completada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Revisar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void mostrarDialogoSalir() {
        new AlertDialog.Builder(this)
                .setTitle("Salir de la actividad")
                .setMessage("¿Estás seguro de que deseas salir? Tu progreso se guardará.")
                .setPositiveButton("Salir", (dialog, which) -> finish())
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        layoutPregunta.setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        mostrarDialogoSalir();
    }

    /**
     * Adaptador para las opciones de respuesta
     */
    private static class OpcionesRespuestaAdapter extends RecyclerView.Adapter<OpcionesRespuestaAdapter.ViewHolder> {
        private final List<RespuestaActividad> opciones;
        private final OnOpcionClickListener listener;
        private int selectedPosition = -1;

        public interface OnOpcionClickListener {
            void onOpcionClick(RespuestaActividad opcion, int position);
        }

        public OpcionesRespuestaAdapter(List<RespuestaActividad> opciones, OnOpcionClickListener listener) {
            this.opciones = opciones;
            this.listener = listener;
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_opcion_respuesta, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RespuestaActividad opcion = opciones.get(position);
            
            // Letras A, B, C, D...
            char letra = (char) ('A' + position);
            holder.textLetraOpcion.setText(String.valueOf(letra));
            holder.textOpcion.setText(opcion.getRespuestas());

            // Resaltar opción seleccionada
            boolean isSelected = position == selectedPosition;
            holder.cardOpcion.setStrokeColor(isSelected ? 
                    holder.itemView.getContext().getResources().getColor(R.color.azul_fuerte) : 
                    android.graphics.Color.TRANSPARENT);
            holder.cardOpcion.setStrokeWidth(isSelected ? 4 : 0);

            holder.cardOpcion.setOnClickListener(v -> {
                int oldPosition = selectedPosition;
                selectedPosition = holder.getAdapterPosition();
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                listener.onOpcionClick(opcion, selectedPosition);
            });
        }

        @Override
        public int getItemCount() {
            return opciones != null ? opciones.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            com.google.android.material.card.MaterialCardView cardOpcion;
            TextView textLetraOpcion;
            TextView textOpcion;

            ViewHolder(View itemView) {
                super(itemView);
                cardOpcion = itemView.findViewById(R.id.cardOpcion);
                textLetraOpcion = itemView.findViewById(R.id.textLetraOpcion);
                textOpcion = itemView.findViewById(R.id.textOpcion);
            }
        }
    }
}
