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
    
    private static final String BASE_URL = "http://192.168.1.33:3000";

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
                    boton_iniciar_sesion.setEnabled(true);
                    boton_iniciar_sesion.setText("Iniciar Sesión");
                    
                    // Redirigir según el tipo de usuario (igual que en el frontend web)
                    Intent intent;
                    if ("alumno".equals(role)) {
                        intent = new Intent(Login.this, PanelEstudianteActivity.class);
                        Toast.makeText(Login.this, "¡Bienvenido estudiante!", Toast.LENGTH_SHORT).show();
                    } else if ("docente".equals(role)) {
                        intent = new Intent(Login.this, PantallaPrincipalDocente.class);
                        Toast.makeText(Login.this, "¡Bienvenido docente!", Toast.LENGTH_SHORT).show();
                    } else if ("administrador".equals(role)) {
                        // Los administradores van al panel web, no a la app móvil
                        Toast.makeText(Login.this, "Los administradores deben usar el panel web", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        // Para roles no reconocidos
                        Toast.makeText(Login.this, "Rol no soportado en la app móvil", Toast.LENGTH_LONG).show();
                        return;
                    }
                    
                    // Guardar sesión completa con datos de usuario y docente
                    // El backend ahora incluye todos los datos del docente en el login
                    sessionManager.saveSession(token, role, user, docente);
                    
                    startActivity(intent);
                    finish(); // Cerrar la actividad de login
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