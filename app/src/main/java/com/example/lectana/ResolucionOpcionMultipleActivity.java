package com.example.lectana;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ActividadCompletaResponse;
import com.example.lectana.modelos.ActividadCompletaResponseWrapper;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.PreguntaActividad;
import com.example.lectana.modelos.RespuestaUsuario;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.AlumnoApiService;
import com.example.lectana.services.ApiClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResolucionOpcionMultipleActivity extends AppCompatActivity {

    private int idActividad;
    private String tituloActividad;
    private SessionManager sessionManager;
    private ActividadesApiService actividadesService;
    private AlumnoApiService alumnoService;
    private ExecutorService executorService;

    private ProgressBar progressBar;
    private TextView tvTitulo;
    private LinearLayout containerPreguntas;
    private Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolucion_opcion_multiple);

        inicializarVistas();
        
        sessionManager = new SessionManager(this);
        actividadesService = ApiClient.getActividadesApiService();
        alumnoService = ApiClient.getAlumnoApiService();
        executorService = Executors.newSingleThreadExecutor();

        // Obtener datos del intent
        if (getIntent() != null) {
            idActividad = getIntent().getIntExtra("id_actividad", 0);
            tituloActividad = getIntent().getStringExtra("titulo_actividad");
        }

        if (idActividad > 0) {
            cargarActividad();
        } else {
            mostrarError("ID de actividad no válido");
        }
    }

    private void inicializarVistas() {
        progressBar = new ProgressBar(this);
        tvTitulo = new TextView(this);
        containerPreguntas = new LinearLayout(this);
        btnConfirmar = new Button(this);
        
        btnConfirmar.setOnClickListener(v -> enviarRespuestas());
    }

    private void cargarActividad() {
        mostrarCargando(true);
        String token = "Bearer " + sessionManager.getToken();

        executorService.execute(() -> {
            Call<ActividadCompletaResponseWrapper> call = actividadesService.getActividadCompleta(token, idActividad);

            call.enqueue(new Callback<ActividadCompletaResponseWrapper>() {
                @Override
                public void onResponse(Call<ActividadCompletaResponseWrapper> call, Response<ActividadCompletaResponseWrapper> response) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        if (response.isSuccessful() && response.body() != null) {
                            ActividadCompletaResponseWrapper wrapper = response.body();
                            List<PreguntaActividad> preguntas = wrapper.getActividadCompleta();
                            if (preguntas != null && !preguntas.isEmpty()) {
                                mostrarActividadCompleta(preguntas);
                            } else {
                                mostrarError("No se pudo cargar la actividad");
                            }
                        } else {
                            Log.e("ResolucionOM", "Error: " + response.code());
                            mostrarError("Error al cargar la actividad");
                        }
                    });
                }

                @Override
                public void onFailure(Call<ActividadCompletaResponseWrapper> call, Throwable t) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Log.e("ResolucionOM", "Error: " + t.getMessage());
                        mostrarError("Error de conexión");
                    });
                }
            });
        });
    }

    private void mostrarActividadCompleta(List<PreguntaActividad> preguntas) {
        tvTitulo.setText(tituloActividad != null ? tituloActividad : "Actividad");
        
        containerPreguntas.removeAllViews();

        if (preguntas != null) {
            
            for (var pregunta : preguntas) {
                // Crear TextView para la pregunta
                TextView tvPregunta = new TextView(this);
                tvPregunta.setText(pregunta.getEnunciado());
                tvPregunta.setTextSize(16);
                tvPregunta.setTextColor(getResources().getColor(R.color.gris_oscuro));
                tvPregunta.setPadding(0, 16, 0, 8);
                tvPregunta.setTag(pregunta.getIdPreguntaActividad()); // Guardar ID
                containerPreguntas.addView(tvPregunta);

                // Crear RadioGroup para las opciones
                RadioGroup radioGroup = new RadioGroup(this);
                radioGroup.setTag("radio_" + pregunta.getIdPreguntaActividad());

                if (pregunta.getRespuestaActividad() != null) {
                    for (var respuesta : pregunta.getRespuestaActividad()) {
                        RadioButton radio = new RadioButton(this);
                        radio.setText(respuesta.getRespuestas() != null && respuesta.getRespuestas().size() > 0 ? 
                                respuesta.getRespuestas().get(0) : "Opción");
                        radio.setTextSize(14);
                        radio.setTag(respuesta.getIdRespuestaActividad());
                        radioGroup.addView(radio);
                    }
                }

                containerPreguntas.addView(radioGroup);
            }
        }
    }

    private void enviarRespuestas() {
        mostrarCargando(true);
        String token = "Bearer " + sessionManager.getToken();

        // Obtener todas las respuestas seleccionadas
        for (int i = 0; i < containerPreguntas.getChildCount(); i++) {
            var child = containerPreguntas.getChildAt(i);
            
            if (child instanceof RadioGroup) {
                RadioGroup radioGroup = (RadioGroup) child;
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId != -1) {
                    RadioButton selectedRadio = findViewById(selectedId);
                    
                    // Obtener ID de pregunta
                    String tagRadio = radioGroup.getTag().toString();
                    int idPregunta = Integer.parseInt(tagRadio.replace("radio_", ""));

                // Crear respuesta
                RespuestaUsuario respuesta = new RespuestaUsuario();
                respuesta.setPreguntaActividadId(idPregunta);
                respuesta.setRespuestaTexto(selectedRadio.getText().toString());                    // Enviar respuesta
                    enviarRespuesta(token, respuesta);
                } else {
                    mostrarCargando(false);
                    Toast.makeText(this, "Por favor responde todas las preguntas", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    private void enviarRespuesta(String token, RespuestaUsuario respuesta) {
        executorService.execute(() -> {
            Call<AlumnoApiService.RespuestaPreguntaResponse> call = alumnoService.responderPregunta(token, respuesta.getPreguntaActividadId(),
                    new AlumnoApiService.ResponderPreguntaRequest(respuesta.getRespuestaTexto()));

            call.enqueue(new Callback<AlumnoApiService.RespuestaPreguntaResponse>() {
                @Override
                public void onResponse(Call<AlumnoApiService.RespuestaPreguntaResponse> call, Response<AlumnoApiService.RespuestaPreguntaResponse> response) {
                    runOnUiThread(() -> {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("ResolucionOM", "Respuesta enviada");
                            mostrarCargando(false);
                            Toast.makeText(ResolucionOpcionMultipleActivity.this, 
                                "¡Respuesta enviada correctamente!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            mostrarCargando(false);
                            mostrarError("Error al enviar la respuesta");
                        }
                    });
                }

                @Override
                public void onFailure(Call<AlumnoApiService.RespuestaPreguntaResponse> call, Throwable t) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Log.e("ResolucionOM", "Error: " + t.getMessage());
                        mostrarError("Error: " + t.getMessage());
                    });
                }
            });
        });
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}


