package com.example.lectana.estudiante;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.R;
import com.example.lectana.modelos.ActividadCompleta;
import com.example.lectana.modelos.ActividadCompletaResponse;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.PreguntaActividad;
import com.example.lectana.modelos.RespuestaActividad;
import com.example.lectana.modelos.RespuestaUsuario;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.AlumnoApiService;
import com.example.lectana.auth.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResolverActividadActivity extends AppCompatActivity {

    // Componentes de UI
    private ImageButton btnVolver;
    private TextView textTituloActividad;
    private TextView textProgresoPregunta;
    private ProgressBar progressBarActividad;
    private TextView textEnunciado;
    private RadioGroup radioGroupOpciones;
    private EditText editTextRespuestaAbierta;
    private LinearLayout layoutRespuestaMultiple;
    private LinearLayout layoutRespuestaAbierta;
    private Button btnAnterior;
    private Button btnSiguiente;
    private Button btnEnviarRespuestas;
    private ProgressBar progressBarCarga;

    // Datos
    private Actividad actividad;
    private List<PreguntaActividad> preguntas;
    private Map<Integer, String> respuestasUsuario; // preguntaId -> respuestaTexto
    private Map<Integer, Integer> respuestasSeleccionadas; // preguntaId -> respuestaActividadId
    private int preguntaActualIndex = 0;
    
    // Servicios
    private ActividadesApiService actividadesApiService;
    private AlumnoApiService alumnoApiService;
    private SessionManager sessionManager;
    private String token;
    private int alumnoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolver_actividad);

        // Inicializar servicios
        sessionManager = new SessionManager(this);
        token = sessionManager.getToken();
        alumnoId = sessionManager.getAlumnoId();
        actividadesApiService = ApiClient.getClient().create(ActividadesApiService.class);
        alumnoApiService = ApiClient.getClient().create(AlumnoApiService.class);

        // Obtener actividad del intent
        actividad = (Actividad) getIntent().getSerializableExtra("actividad");
        if (actividad == null) {
            Toast.makeText(this, "Error: No se pudo cargar la actividad", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar estructuras de datos
        preguntas = new ArrayList<>();
        respuestasUsuario = new HashMap<>();
        respuestasSeleccionadas = new HashMap<>();

        // Inicializar componentes
        inicializarComponentes();
        configurarListeners();
        
        // Cargar preguntas
        cargarPreguntas();
    }

    private void inicializarComponentes() {
        btnVolver = findViewById(R.id.btnVolver);
        textTituloActividad = findViewById(R.id.textTituloActividad);
        textProgresoPregunta = findViewById(R.id.textProgresoPregunta);
        progressBarActividad = findViewById(R.id.progressBarActividad);
        textEnunciado = findViewById(R.id.textEnunciado);
        radioGroupOpciones = findViewById(R.id.radioGroupOpciones);
        editTextRespuestaAbierta = findViewById(R.id.editTextRespuestaAbierta);
        layoutRespuestaMultiple = findViewById(R.id.layoutRespuestaMultiple);
        layoutRespuestaAbierta = findViewById(R.id.layoutRespuestaAbierta);
        btnAnterior = findViewById(R.id.btnAnterior);
        btnSiguiente = findViewById(R.id.btnSiguiente);
        btnEnviarRespuestas = findViewById(R.id.btnEnviarRespuestas);
        progressBarCarga = findViewById(R.id.progressBarCarga);

        // Configurar título
        if (actividad.getCuento() != null) {
            textTituloActividad.setText(actividad.getCuento().getTitulo());
        }
    }

    private void configurarListeners() {
        btnVolver.setOnClickListener(v -> {
            if (hayRespuestasPendientes()) {
                mostrarDialogoSalir();
            } else {
                finish();
            }
        });

        btnAnterior.setOnClickListener(v -> {
            if (preguntaActualIndex > 0) {
                guardarRespuestaActual();
                preguntaActualIndex--;
                mostrarPreguntaActual();
            }
        });

        btnSiguiente.setOnClickListener(v -> {
            if (validarRespuestaActual()) {
                guardarRespuestaActual();
                if (preguntaActualIndex < preguntas.size() - 1) {
                    preguntaActualIndex++;
                    mostrarPreguntaActual();
                }
            }
        });

        btnEnviarRespuestas.setOnClickListener(v -> {
            if (validarRespuestaActual()) {
                guardarRespuestaActual();
                if (validarTodasRespuestas()) {
                    mostrarDialogoConfirmacion();
                }
            }
        });
    }

    private void cargarPreguntas() {
        progressBarCarga.setVisibility(View.VISIBLE);
        
        Call<ActividadCompletaResponse> call = actividadesApiService.getActividadCompleta(
            "Bearer " + token,
            actividad.getIdActividad()
        );

        call.enqueue(new Callback<ActividadCompletaResponse>() {
            @Override
            public void onResponse(Call<ActividadCompletaResponse> call, Response<ActividadCompletaResponse> response) {
                progressBarCarga.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ActividadCompletaResponse actividadResponse = response.body();
                    if (actividadResponse.getActividad() != null) {
                        preguntas = actividadResponse.getActividad().getPreguntaActividad();
                        
                        if (preguntas == null || preguntas.isEmpty()) {
                            Toast.makeText(ResolverActividadActivity.this, 
                                "Esta actividad no tiene preguntas", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        
                        mostrarPreguntaActual();
                    } else {
                        Toast.makeText(ResolverActividadActivity.this, 
                            "Error al cargar actividad", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(ResolverActividadActivity.this, 
                        "Error al cargar preguntas", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ActividadCompletaResponse> call, Throwable t) {
                progressBarCarga.setVisibility(View.GONE);
                Toast.makeText(ResolverActividadActivity.this, 
                    "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void mostrarPreguntaActual() {
        if (preguntas.isEmpty()) return;

        PreguntaActividad pregunta = preguntas.get(preguntaActualIndex);
        
        // Actualizar progreso
        textProgresoPregunta.setText(String.format("Pregunta %d de %d", 
            preguntaActualIndex + 1, preguntas.size()));
        
        int progreso = (int) (((float) (preguntaActualIndex + 1) / preguntas.size()) * 100);
        progressBarActividad.setProgress(progreso);

        // Mostrar enunciado
        textEnunciado.setText(pregunta.getEnunciado());

        // Determinar tipo de pregunta
        if (pregunta.getRespuestaActividad() != null && !pregunta.getRespuestaActividad().isEmpty()) {
            // Pregunta de opción múltiple
            mostrarPreguntaMultiple(pregunta);
        } else {
            // Pregunta abierta
            mostrarPreguntaAbierta(pregunta);
        }

        // Actualizar botones de navegación
        actualizarBotonesNavegacion();
    }

    private void mostrarPreguntaMultiple(PreguntaActividad pregunta) {
        layoutRespuestaMultiple.setVisibility(View.VISIBLE);
        layoutRespuestaAbierta.setVisibility(View.GONE);
        
        // Limpiar opciones anteriores
        radioGroupOpciones.removeAllViews();
        
        // Agregar opciones
        for (RespuestaActividad respuesta : pregunta.getRespuestaActividad()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(respuesta.getRespuestas());
            radioButton.setTextSize(16);
            radioButton.setPadding(16, 16, 16, 16);
            radioButton.setId(respuesta.getIdRespuestaActividad());
            
            radioGroupOpciones.addView(radioButton);
        }
        
        // Restaurar selección previa si existe
        Integer respuestaId = respuestasSeleccionadas.get(pregunta.getIdPreguntaActividad());
        if (respuestaId != null) {
            radioGroupOpciones.check(respuestaId);
        } else {
            radioGroupOpciones.clearCheck();
        }
    }

    private void mostrarPreguntaAbierta(PreguntaActividad pregunta) {
        layoutRespuestaMultiple.setVisibility(View.GONE);
        layoutRespuestaAbierta.setVisibility(View.VISIBLE);
        
        // Restaurar respuesta previa si existe
        String respuestaPrevia = respuestasUsuario.get(pregunta.getIdPreguntaActividad());
        if (respuestaPrevia != null) {
            editTextRespuestaAbierta.setText(respuestaPrevia);
        } else {
            editTextRespuestaAbierta.setText("");
        }
    }

    private void actualizarBotonesNavegacion() {
        // Botón Anterior
        btnAnterior.setEnabled(preguntaActualIndex > 0);
        btnAnterior.setAlpha(preguntaActualIndex > 0 ? 1.0f : 0.5f);

        // Botones Siguiente y Enviar
        boolean esUltimaPregunta = preguntaActualIndex == preguntas.size() - 1;
        btnSiguiente.setVisibility(esUltimaPregunta ? View.GONE : View.VISIBLE);
        btnEnviarRespuestas.setVisibility(esUltimaPregunta ? View.VISIBLE : View.GONE);
    }

    private boolean validarRespuestaActual() {
        PreguntaActividad pregunta = preguntas.get(preguntaActualIndex);
        
        if (pregunta.getRespuestaActividad() != null && !pregunta.getRespuestaActividad().isEmpty()) {
            // Pregunta de opción múltiple
            if (radioGroupOpciones.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Por favor selecciona una opción", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            // Pregunta abierta
            String respuesta = editTextRespuestaAbierta.getText().toString().trim();
            if (respuesta.isEmpty()) {
                Toast.makeText(this, "Por favor escribe tu respuesta", Toast.LENGTH_SHORT).show();
                editTextRespuestaAbierta.setError("La respuesta no puede estar vacía");
                return false;
            }
        }
        
        return true;
    }

    private void guardarRespuestaActual() {
        PreguntaActividad pregunta = preguntas.get(preguntaActualIndex);
        
        if (pregunta.getRespuestaActividad() != null && !pregunta.getRespuestaActividad().isEmpty()) {
            // Pregunta de opción múltiple
            int selectedId = radioGroupOpciones.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedRadio = findViewById(selectedId);
                String respuestaTexto = selectedRadio.getText().toString();
                
                respuestasUsuario.put(pregunta.getIdPreguntaActividad(), respuestaTexto);
                respuestasSeleccionadas.put(pregunta.getIdPreguntaActividad(), selectedId);
            }
        } else {
            // Pregunta abierta
            String respuesta = editTextRespuestaAbierta.getText().toString().trim();
            if (!respuesta.isEmpty()) {
                respuestasUsuario.put(pregunta.getIdPreguntaActividad(), respuesta);
            }
        }
    }

    private boolean validarTodasRespuestas() {
        if (respuestasUsuario.size() < preguntas.size()) {
            Toast.makeText(this, "Por favor responde todas las preguntas", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean hayRespuestasPendientes() {
        return !respuestasUsuario.isEmpty();
    }

    private void mostrarDialogoSalir() {
        new AlertDialog.Builder(this)
            .setTitle("Salir sin guardar")
            .setMessage("Tienes respuestas sin enviar. ¿Estás seguro de que quieres salir?")
            .setPositiveButton("Salir", (dialog, which) -> finish())
            .setNegativeButton("Cancelar", null)
            .show();
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
            .setTitle("Enviar respuestas")
            .setMessage(String.format("¿Estás seguro de enviar tus %d respuestas? No podrás modificarlas después.", 
                preguntas.size()))
            .setPositiveButton("Enviar", (dialog, which) -> enviarRespuestas())
            .setNegativeButton("Revisar", null)
            .show();
    }

    private void enviarRespuestas() {
        progressBarCarga.setVisibility(View.VISIBLE);
        btnEnviarRespuestas.setEnabled(false);

        // Preparar lista de respuestas
        List<Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>>> llamadas = new ArrayList<>();
        
        for (PreguntaActividad pregunta : preguntas) {
            String respuestaTexto = respuestasUsuario.get(pregunta.getIdPreguntaActividad());
            
            if (respuestaTexto != null) {
                AlumnoApiService.ResponderPreguntaRequest request = 
                        new AlumnoApiService.ResponderPreguntaRequest(respuestaTexto);
                
                Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call = alumnoApiService.responderPregunta(
                    "Bearer " + token,
                    pregunta.getIdPreguntaActividad(),
                    request
                );
                
                llamadas.add(call);
            }
        }

        // Enviar todas las respuestas secuencialmente
        enviarRespuestaRecursiva(llamadas, 0);
    }

    private void enviarRespuestaRecursiva(List<Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>>> llamadas, int index) {
        if (index >= llamadas.size()) {
            // Todas las respuestas enviadas exitosamente
            progressBarCarga.setVisibility(View.GONE);
            
            new AlertDialog.Builder(this)
                .setTitle("¡Éxito!")
                .setMessage("Tus respuestas han sido enviadas correctamente")
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    setResult(RESULT_OK);
                    finish();
                })
                .setCancelable(false)
                .show();
            return;
        }

        Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call = llamadas.get(index);
        call.enqueue(new Callback<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call, 
                                 Response<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    // Continuar con la siguiente respuesta
                    enviarRespuestaRecursiva(llamadas, index + 1);
                } else {
                    // Error al enviar
                    progressBarCarga.setVisibility(View.GONE);
                    btnEnviarRespuestas.setEnabled(true);
                    
                    Toast.makeText(ResolverActividadActivity.this, 
                        "Error al enviar respuesta " + (index + 1), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AlumnoApiService.RespuestaPreguntaResponse>> call, Throwable t) {
                progressBarCarga.setVisibility(View.GONE);
                btnEnviarRespuestas.setEnabled(true);
                
                Toast.makeText(ResolverActividadActivity.this, 
                    "Error de conexión al enviar respuesta " + (index + 1), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (hayRespuestasPendientes()) {
            mostrarDialogoSalir();
        } else {
            super.onBackPressed();
        }
    }
}
