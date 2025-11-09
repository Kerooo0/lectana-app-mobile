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
    private EditText campoCodigo;
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
        campoCodigo = findViewById(R.id.campo_codigo_aula);
        botonCambiarAula = findViewById(R.id.boton_cambiar_aula);
        progressBar = findViewById(R.id.progress_bar);
        textoAulaActual = findViewById(R.id.texto_aula_actual);
        textoInstrucciones = findViewById(R.id.texto_instrucciones);

        sessionManager = new SessionManager(this);
        alumnoApiService = ApiClient.getClient().create(AlumnoApiService.class);

        // Inicialmente deshabilitar el botón
        botonCambiarAula.setEnabled(false);
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(v -> finish());

        // Habilitar botón solo si el código tiene 6 caracteres
        campoCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                botonCambiarAula.setEnabled(s.length() == 6);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Convertir a mayúsculas automáticamente
                String upper = s.toString().toUpperCase();
                if (!s.toString().equals(upper)) {
                    campoCodigo.setText(upper);
                    campoCodigo.setSelection(upper.length());
                }
            }
        });

        botonCambiarAula.setOnClickListener(v -> {
            String codigo = campoCodigo.getText().toString().trim();
            if (codigo.length() == 6) {
                cambiarAula(codigo);
            } else {
                Toast.makeText(this, "El código debe tener 6 caracteres", Toast.LENGTH_SHORT).show();
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
                            textoAulaActual.setText("Aula actual: " + nombreAula);
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

    private void cambiarAula(String codigo) {
        mostrarCargando(true);
        botonCambiarAula.setEnabled(false);

        String token = "Bearer " + sessionManager.getToken();
        
        // Como no hay endpoint directo para buscar por código, tenemos que pedirle al backend que agregue uno
        // Por ahora mostramos un mensaje indicando que necesita implementación backend
        Toast.makeText(this, 
            "Esta funcionalidad requiere que el backend implemente:\n" +
            "1. POST /api/alumnos/unirse-aula (con codigo_acceso)\n" +
            "O alternativamente:\n" +
            "2. GET /api/aulas/buscar-por-codigo/:codigo", 
            Toast.LENGTH_LONG).show();
        
        mostrarCargando(false);
        botonCambiarAula.setEnabled(true);
        
        // TODO: Cuando el backend implemente el endpoint, usar este código:
        /*
        // Opción A: Si implementan POST /api/alumnos/unirse-aula
        AlumnoApiService.UnirseAulaRequest request = new AlumnoApiService.UnirseAulaRequest(codigo);
        Call<ApiResponse<AlumnoApiService.UnirseAulaResponse>> call = alumnoApiService.unirseAula(token, request);
        
        // Opción B: Si implementan GET /aulas/buscar-por-codigo/:codigo
        // 1. Primero buscar el aula por código para obtener su ID
        // 2. Luego actualizar perfil con ese ID usando actualizarPerfilAlumno
        */
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
        campoCodigo.setEnabled(!mostrar);
        botonCambiarAula.setEnabled(!mostrar && campoCodigo.getText().length() == 6);
    }
}
