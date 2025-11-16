package com.example.lectana;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.EstudiantesApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecuperarPasswordActivity extends AppCompatActivity {

    private ImageView botonVolver;
    private EditText inputEmail;
    private Button botonRecuperar;
    private ProgressBar progressBar;

    private EstudiantesApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_password);

        apiService = ApiClient.getEstudiantesApiService();

        inicializarVistas();
        configurarListeners();
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        inputEmail = findViewById(R.id.input_email);
        botonRecuperar = findViewById(R.id.boton_recuperar);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(v -> finish());
        botonRecuperar.setOnClickListener(v -> solicitarRecuperacion());
    }

    private void solicitarRecuperacion() {
        String email = inputEmail.getText().toString().trim();

        // Validaciones
        if (email.isEmpty()) {
            inputEmail.setError("Ingresa tu correo electrónico");
            inputEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Ingresa un correo válido");
            inputEmail.requestFocus();
            return;
        }

        mostrarCargando(true);

        EstudiantesApiService.RecuperarPasswordRequest request = 
            new EstudiantesApiService.RecuperarPasswordRequest(email);

        apiService.solicitarRecuperacionPassword(request).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                mostrarCargando(false);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RecuperarPasswordActivity.this, 
                        "Se ha enviado un correo con instrucciones para recuperar tu contraseña", 
                        Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String mensaje = "Error al solicitar recuperación";
                    if (response.code() == 404) {
                        mensaje = "No se encontró una cuenta con este correo";
                    }
                    Toast.makeText(RecuperarPasswordActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                mostrarCargando(false);
                Log.e("RecuperarPassword", "Error al solicitar recuperación", t);
                Toast.makeText(RecuperarPasswordActivity.this, 
                    "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        botonRecuperar.setEnabled(!mostrar);
        inputEmail.setEnabled(!mostrar);
    }
}
