package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.network.AuthClient;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.registro.registro_pregunta;
import com.example.lectana.estudiante.PanelEstudianteActivity;
import com.example.lectana.docente.PantallaPrincipalDocente;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private TextView registro;
    private Button boton_iniciar_sesion;
    private EditText edtEmail;
    private EditText edtPassword;
    private AuthClient authClient;
    private SessionManager sessionManager;
    
    private static final String BASE_URL = "https://lectana-backend.onrender.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Inicializar componentes
        registro = findViewById(R.id.txtRegister);
        boton_iniciar_sesion = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        
        // Inicializar cliente de autenticación y sesión
        authClient = new AuthClient(BASE_URL);
        sessionManager = new SessionManager(this);

        // Verificar si ya hay una sesión activa (igual que en el frontend web)
        if (sessionManager.isLoggedIn()) {
            redirigirSegunRol();
            return;
        }

        // Configurar click listeners
        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, registro_pregunta.class);
                startActivity(intent);
            }
        });

        boton_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarLogin();
            }
        });

        // Botón temporal para probar conexión con backend
        findViewById(R.id.btnGuest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, TestConnectionActivity.class);
                startActivity(intent);
            }
        });
    }

    private void realizarLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validaciones básicas
        if (email.isEmpty()) {
            edtEmail.setError("Ingresa tu email");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Ingresa tu contraseña");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 8) {
            edtPassword.setError("La contraseña debe tener al menos 8 caracteres");
            edtPassword.requestFocus();
            return;
        }

        // Mostrar loading (opcional)
        boton_iniciar_sesion.setEnabled(false);
        boton_iniciar_sesion.setText("Iniciando sesión...");

        // Log para diagnóstico
        android.util.Log.d("Login", "Intentando login con email: " + email);
        android.util.Log.d("Login", "Backend URL: " + BASE_URL);

        // Realizar login
        authClient.loginComplete(email, password, new AuthClient.LoginCallbackComplete() {
            @Override
            public void onSuccess(String token, String role, JSONObject user, JSONObject docente) {
                runOnUiThread(() -> {
                    // Guardar sesión completa con datos de usuario y docente
                    sessionManager.saveSession(token, role, user, docente);
                    
                    // Si es alumno, obtener datos adicionales (id_alumno y aula_id)
                    if ("alumno".equals(role)) {
                        obtenerDatosAlumno(token);
                    } else {
                        boton_iniciar_sesion.setEnabled(true);
                        boton_iniciar_sesion.setText("Iniciar Sesión");
                        redirigirDespuesDeLogin(role);
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    boton_iniciar_sesion.setEnabled(true);
                    boton_iniciar_sesion.setText("Iniciar Sesión");
                    Toast.makeText(Login.this, "Error: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * Obtener datos adicionales del alumno desde /api/auth/me
     */
    private void obtenerDatosAlumno(String token) {
        android.util.Log.d("Login", "Obteniendo datos del alumno desde /api/auth/me");
        
        com.example.lectana.services.AuthApiService authApiService = 
            com.example.lectana.services.ApiClient.getAuthApiService();
        
        authApiService.obtenerDatosUsuario("Bearer " + token).enqueue(
            new retrofit2.Callback<com.example.lectana.services.AuthApiService.MeResponse>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.lectana.services.AuthApiService.MeResponse> call,
                                     retrofit2.Response<com.example.lectana.services.AuthApiService.MeResponse> response) {
                    runOnUiThread(() -> {
                        boton_iniciar_sesion.setEnabled(true);
                        boton_iniciar_sesion.setText("Iniciar Sesión");
                        
                        if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                            com.example.lectana.services.AuthApiService.MeResponse meData = response.body();
                            com.example.lectana.services.AuthApiService.Alumno alumno = meData.getAlumno();
                            
                            if (alumno != null) {
                                int idAlumno = alumno.getIdAlumno();
                                Integer aulaId = alumno.getAulaIdAula();
                                
                                android.util.Log.d("Login", "Datos obtenidos - ID Alumno: " + idAlumno + ", Aula ID: " + aulaId);
                                
                                // Guardar IDs en SessionManager
                                if (aulaId != null && aulaId > 0) {
                                    sessionManager.saveAlumnoData(idAlumno, aulaId);
                                    Toast.makeText(Login.this, "¡Bienvenido estudiante!", Toast.LENGTH_SHORT).show();
                                } else {
                                    sessionManager.saveAlumnoId(idAlumno);
                                    Toast.makeText(Login.this, "¡Bienvenido! Por favor completa tu perfil", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            android.util.Log.w("Login", "No se pudieron obtener datos adicionales del alumno");
                            Toast.makeText(Login.this, "¡Bienvenido estudiante!", Toast.LENGTH_SHORT).show();
                        }
                        
                        redirigirDespuesDeLogin("alumno");
                    });
                }
                
                @Override
                public void onFailure(retrofit2.Call<com.example.lectana.services.AuthApiService.MeResponse> call, Throwable t) {
                    runOnUiThread(() -> {
                        boton_iniciar_sesion.setEnabled(true);
                        boton_iniciar_sesion.setText("Iniciar Sesión");
                        
                        android.util.Log.e("Login", "Error al obtener datos del alumno", t);
                        Toast.makeText(Login.this, "¡Bienvenido estudiante!", Toast.LENGTH_SHORT).show();
                        
                        redirigirDespuesDeLogin("alumno");
                    });
                }
            }
        );
    }

    /**
     * Redirigir después del login según el rol
     */
    private void redirigirDespuesDeLogin(String role) {
        Intent intent;
        
        if ("alumno".equals(role)) {
            intent = new Intent(Login.this, PanelEstudianteActivity.class);
        } else if ("docente".equals(role)) {
            intent = new Intent(Login.this, PantallaPrincipalDocente.class);
            Toast.makeText(Login.this, "¡Bienvenido docente!", Toast.LENGTH_SHORT).show();
        } else if ("administrador".equals(role)) {
            Toast.makeText(Login.this, "Los administradores deben usar el panel web", Toast.LENGTH_LONG).show();
            return;
        } else {
            Toast.makeText(Login.this, "Rol no soportado en la app móvil", Toast.LENGTH_LONG).show();
            return;
        }
        
        startActivity(intent);
        finish();
    }

    /**
     * Redirigir según el rol del usuario (igual que en el frontend web)
     */
    private void redirigirSegunRol() {
        String role = sessionManager.getRole();
        Intent intent;
        
        if ("alumno".equals(role)) {
            intent = new Intent(Login.this, PanelEstudianteActivity.class);
        } else if ("docente".equals(role)) {
            intent = new Intent(Login.this, PantallaPrincipalDocente.class);
        } else if ("administrador".equals(role)) {
            // Los administradores van al panel web
            Toast.makeText(this, "Los administradores deben usar el panel web", Toast.LENGTH_LONG).show();
            sessionManager.clearSession(); // Limpiar sesión inválida
            return;
        } else {
            // Rol no reconocido, limpiar sesión
            sessionManager.clearSession();
            return;
        }
        
        startActivity(intent);
        finish();
    }
}