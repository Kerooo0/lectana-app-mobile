package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.EstudiantesApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CambiarPasswordEstudianteActivity extends AppCompatActivity {

    private ImageView botonVolver;
    private EditText inputPasswordActual;
    private EditText inputPasswordNueva;
    private EditText inputConfirmarPassword;
    private ImageView togglePasswordActual;
    private ImageView togglePasswordNueva;
    private ImageView toggleConfirmarPassword;
    private TextView linkOlvidastePassword;
    private Button botonGuardar;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private EstudiantesApiService apiService;

    private boolean passwordActualVisible = false;
    private boolean passwordNuevaVisible = false;
    private boolean confirmarPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_password_estudiante);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getEstudiantesApiService();

        inicializarVistas();
        configurarListeners();
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        inputPasswordActual = findViewById(R.id.input_password_actual);
        inputPasswordNueva = findViewById(R.id.input_password_nueva);
        inputConfirmarPassword = findViewById(R.id.input_confirmar_password);
        togglePasswordActual = findViewById(R.id.toggle_password_actual);
        togglePasswordNueva = findViewById(R.id.toggle_password_nueva);
        toggleConfirmarPassword = findViewById(R.id.toggle_confirmar_password);
        linkOlvidastePassword = findViewById(R.id.link_olvidaste_password);
        botonGuardar = findViewById(R.id.boton_guardar);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(v -> finish());

        // Link "¿Olvidaste tu contraseña?"
        linkOlvidastePassword.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecuperarPasswordActivity.class);
            startActivity(intent);
        });

        // Toggle visibilidad contraseña actual
        togglePasswordActual.setOnClickListener(v -> {
            passwordActualVisible = !passwordActualVisible;
            togglePasswordVisibility(inputPasswordActual, togglePasswordActual, passwordActualVisible);
        });

        // Toggle visibilidad contraseña nueva
        togglePasswordNueva.setOnClickListener(v -> {
            passwordNuevaVisible = !passwordNuevaVisible;
            togglePasswordVisibility(inputPasswordNueva, togglePasswordNueva, passwordNuevaVisible);
        });

        // Toggle visibilidad confirmar contraseña
        toggleConfirmarPassword.setOnClickListener(v -> {
            confirmarPasswordVisible = !confirmarPasswordVisible;
            togglePasswordVisibility(inputConfirmarPassword, toggleConfirmarPassword, confirmarPasswordVisible);
        });

        botonGuardar.setOnClickListener(v -> cambiarPassword());
    }

    private void togglePasswordVisibility(EditText editText, ImageView toggleIcon, boolean visible) {
        if (visible) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility_off);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            toggleIcon.setImageResource(R.drawable.ic_visibility);
        }
        // Mover cursor al final
        editText.setSelection(editText.getText().length());
    }

    private void cambiarPassword() {
        String passwordActual = inputPasswordActual.getText().toString().trim();
        String passwordNueva = inputPasswordNueva.getText().toString().trim();
        String confirmarPassword = inputConfirmarPassword.getText().toString().trim();

        // Validaciones
        if (passwordActual.isEmpty()) {
            inputPasswordActual.setError("Ingresa tu contraseña actual");
            inputPasswordActual.requestFocus();
            return;
        }

        if (passwordNueva.isEmpty()) {
            inputPasswordNueva.setError("Ingresa la nueva contraseña");
            inputPasswordNueva.requestFocus();
            return;
        }

        if (passwordNueva.length() < 6) {
            inputPasswordNueva.setError("La contraseña debe tener al menos 6 caracteres");
            inputPasswordNueva.requestFocus();
            return;
        }

        if (confirmarPassword.isEmpty()) {
            inputConfirmarPassword.setError("Confirma tu nueva contraseña");
            inputConfirmarPassword.requestFocus();
            return;
        }

        if (!passwordNueva.equals(confirmarPassword)) {
            inputConfirmarPassword.setError("Las contraseñas no coinciden");
            inputConfirmarPassword.requestFocus();
            return;
        }

        if (passwordActual.equals(passwordNueva)) {
            inputPasswordNueva.setError("La nueva contraseña debe ser diferente a la actual");
            inputPasswordNueva.requestFocus();
            return;
        }

        // Realizar cambio de contraseña
        mostrarCargando(true);

        try {
            org.json.JSONObject user = sessionManager.getUser();
            if (user == null) {
                Toast.makeText(this, "Error: No hay sesión activa", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            int idEstudiante = user.optInt("id_estudiante", 0);
            String token = "Bearer " + sessionManager.getToken();

            EstudiantesApiService.CambiarPasswordRequest request = 
                new EstudiantesApiService.CambiarPasswordRequest(passwordActual, passwordNueva);

            apiService.cambiarPassword(token, idEstudiante, request).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    mostrarCargando(false);

                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(CambiarPasswordEstudianteActivity.this, 
                            "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String mensaje = "Error al cambiar la contraseña";
                        if (response.code() == 401) {
                            mensaje = "La contraseña actual es incorrecta";
                        } else if (response.code() == 400) {
                            mensaje = "Datos inválidos. Verifica tu contraseña";
                        }
                        Toast.makeText(CambiarPasswordEstudianteActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                    mostrarCargando(false);
                    Log.e("CambiarPassword", "Error al cambiar contraseña", t);
                    Toast.makeText(CambiarPasswordEstudianteActivity.this, 
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        } catch (Exception e) {
            mostrarCargando(false);
            Log.e("CambiarPassword", "Error al procesar solicitud", e);
            Toast.makeText(this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        botonGuardar.setEnabled(!mostrar);
        inputPasswordActual.setEnabled(!mostrar);
        inputPasswordNueva.setEnabled(!mostrar);
        inputConfirmarPassword.setEnabled(!mostrar);
    }
}
