package com.example.lectana;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.services.AlumnoApiService;
import com.example.lectana.services.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarAulaActivity extends AppCompatActivity {

    private ImageView botonVolver;
    private EditText campoIdAula;
    private Button botonCambiarAula;
    private ProgressBar progressBar;
    private TextView textoAulaActual;
    private TextView textoInstrucciones;
    
    private SessionManager sessionManager;
    private AlumnoApiService alumnoApiService;
    
    private Integer aulaActualId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_aula);

        inicializarComponentes();
        configurarListeners();
        cargarAulaActual();
    }

    private void inicializarComponentes() {
        botonVolver = findViewById(R.id.boton_volver);
        campoIdAula = findViewById(R.id.campo_codigo_aula);
        botonCambiarAula = findViewById(R.id.boton_cambiar_aula);
        progressBar = findViewById(R.id.progress_bar);
        textoAulaActual = findViewById(R.id.texto_aula_actual);
        textoInstrucciones = findViewById(R.id.texto_instrucciones);

        sessionManager = new SessionManager(this);
        alumnoApiService = ApiClient.getClient().create(AlumnoApiService.class);

        // Inicialmente deshabilitar el botón
        botonCambiarAula.setEnabled(false);
        
        // Actualizar instrucciones
        textoInstrucciones.setText("Ingresa el código de 6 caracteres proporcionado por tu docente para unirte a su aula.");
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(v -> finish());

        // Habilitar botón solo si el código tiene 6 caracteres
        campoIdAula.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String texto = s.toString().trim();
                boolean esValido = texto.length() == 6;
                botonCambiarAula.setEnabled(esValido);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        botonCambiarAula.setOnClickListener(v -> {
            String codigoAula = campoIdAula.getText().toString().trim().toUpperCase();
            if (codigoAula.length() == 6) {
                unirseAula(codigoAula);
            } else {
                Toast.makeText(this, "Por favor ingresa un código de 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarAulaActual() {
        mostrarCargando(true);
        
        String token = "Bearer " + sessionManager.getToken();
        
        Call<ApiResponse<AlumnoApiService.AulaAlumnoResponse>> call = alumnoApiService.obtenerAulaAlumno(token);
        call.enqueue(new Callback<ApiResponse<AlumnoApiService.AulaAlumnoResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<AlumnoApiService.AulaAlumnoResponse>> call, @NonNull Response<ApiResponse<AlumnoApiService.AulaAlumnoResponse>> response) {
                mostrarCargando(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AlumnoApiService.AulaAlumnoResponse> apiResponse = response.body();
                    
                    if (apiResponse.getData() != null) {
                        AlumnoApiService.AulaAlumnoResponse aulaResponse = apiResponse.getData();
                        if (aulaResponse.getAula() != null) {
                            aulaActualId = aulaResponse.getAula().getIdAula();
                            String nombreAula = aulaResponse.getAula().getNombreAula();
                            textoAulaActual.setText("Aula actual: " + nombreAula + " (ID: " + aulaActualId + ")");
                            textoAulaActual.setVisibility(View.VISIBLE);
                        } else {
                            textoAulaActual.setText("No estás asignado a ninguna aula");
                            textoAulaActual.setVisibility(View.VISIBLE);
                        }
                    } else {
                        textoAulaActual.setText("No estás asignado a ninguna aula");
                        textoAulaActual.setVisibility(View.VISIBLE);
                    }
                } else {
                    handleError(response.code(), "Error al cargar aula actual");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<AlumnoApiService.AulaAlumnoResponse>> call, @NonNull Throwable t) {
                mostrarCargando(false);
                Toast.makeText(CambiarAulaActivity.this, 
                    "Error de conexión: " + t.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    private void unirseAula(String codigoAcceso) {
        mostrarCargando(true);
        botonCambiarAula.setEnabled(false);

        String token = "Bearer " + sessionManager.getToken();
        
        // Usar el endpoint POST /alumnos/unirse-aula
        AlumnoApiService.UnirseAulaRequest request = new AlumnoApiService.UnirseAulaRequest(codigoAcceso);
        Call<ApiResponse<AlumnoApiService.UnirseAulaResponse>> call = alumnoApiService.unirseAula(token, request);
        
        call.enqueue(new Callback<ApiResponse<AlumnoApiService.UnirseAulaResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<AlumnoApiService.UnirseAulaResponse>> call, 
                                 @NonNull Response<ApiResponse<AlumnoApiService.UnirseAulaResponse>> response) {
                mostrarCargando(false);
                botonCambiarAula.setEnabled(true);
                
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    ApiResponse<AlumnoApiService.UnirseAulaResponse> apiResponse = response.body();
                    AlumnoApiService.UnirseAulaResponse data = apiResponse.getData();
                    
                    if (data != null && data.getAula() != null) {
                        int nuevoAulaId = data.getAula().getIdAula();
                        String nombreAula = data.getAula().getNombreAula();
                        
                        Toast.makeText(CambiarAulaActivity.this, 
                            data.getMensaje() != null ? data.getMensaje() : "¡Te has unido a " + nombreAula + "!", 
                            Toast.LENGTH_LONG).show();
                        
                        // Actualizar SessionManager con el nuevo aula ID
                        int alumnoId = sessionManager.getAlumnoId();
                        sessionManager.saveAlumnoData(alumnoId, nuevoAulaId);
                        
                        // Actualizar UI
                        textoAulaActual.setText("Aula actual: " + nombreAula + " (ID: " + nuevoAulaId + ")");
                        textoAulaActual.setVisibility(View.VISIBLE);
                        campoIdAula.setText("");
                        
                        // Cerrar la actividad después de 2 segundos
                        campoIdAula.postDelayed(() -> {
                            setResult(RESULT_OK);
                            finish();
                        }, 2000);
                    } else {
                        Toast.makeText(CambiarAulaActivity.this, 
                            "Error: No se recibió información del aula", 
                            Toast.LENGTH_LONG).show();
                    }
                } else {
                    handleError(response.code(), "Error al unirse al aula");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<AlumnoApiService.UnirseAulaResponse>> call, 
                                @NonNull Throwable t) {
                mostrarCargando(false);
                botonCambiarAula.setEnabled(true);
                Toast.makeText(CambiarAulaActivity.this, 
                    "Error de conexión: " + t.getMessage(), 
                    Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleError(int code, String defaultMessage) {
        String message = defaultMessage;
        
        switch (code) {
            case 400:
                message = "Código de aula inválido";
                break;
            case 401:
                message = "Sesión expirada. Por favor, inicia sesión nuevamente";
                sessionManager.clearSession();
                finish();
                break;
            case 404:
                message = "Aula no encontrada con ese código";
                break;
            case 500:
                message = "Error del servidor. Intenta más tarde";
                break;
        }
        
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        campoIdAula.setEnabled(!mostrar);
        
        // Solo habilitar el botón si no está cargando y el campo tiene 6 caracteres
        if (!mostrar) {
            String texto = campoIdAula.getText() != null ? campoIdAula.getText().toString().trim() : "";
            boolean esValido = texto.length() == 6;
            botonCambiarAula.setEnabled(esValido);
        } else {
            botonCambiarAula.setEnabled(false);
        }
    }
}
