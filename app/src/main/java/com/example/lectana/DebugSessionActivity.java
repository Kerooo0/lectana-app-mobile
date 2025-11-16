package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.auth.SessionManager;

import org.json.JSONObject;

public class DebugSessionActivity extends AppCompatActivity {

    private TextView textoSesion;
    private Button botonLimpiarSesion;
    private Button botonVolverLogin;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_session);
        
        sessionManager = new SessionManager(this);
        
        inicializarVistas();
        mostrarEstadoSesion();
        configurarListeners();
    }

    private void inicializarVistas() {
        textoSesion = findViewById(R.id.texto_sesion);
        botonLimpiarSesion = findViewById(R.id.boton_limpiar_sesion);
        botonVolverLogin = findViewById(R.id.boton_volver_login);
    }

    private void mostrarEstadoSesion() {
        StringBuilder estado = new StringBuilder();
        
        estado.append("=== ESTADO DE LA SESIÓN ===\n\n");
        
        // Estado de login
        boolean isLoggedIn = sessionManager.isLoggedIn();
        estado.append("¿Está logueado?: ").append(isLoggedIn ? "SÍ" : "NO").append("\n");
        
        // Token
        String token = sessionManager.getToken();
        estado.append("Token: ").append(token != null ? "PRESENTE (" + token.length() + " caracteres)" : "AUSENTE").append("\n");
        
        // Rol
        String role = sessionManager.getRole();
        estado.append("Rol: ").append(role != null ? role : "NO DEFINIDO").append("\n");
        
        // Datos del usuario
        JSONObject user = sessionManager.getUser();
        estado.append("Datos de usuario: ").append(user != null ? "PRESENTES" : "AUSENTES").append("\n");
        if (user != null) {
            estado.append("  - Nombre: ").append(user.optString("nombre", "N/A")).append("\n");
            estado.append("  - Email: ").append(user.optString("email", "N/A")).append("\n");
        }
        
        // Datos del docente
        JSONObject docente = sessionManager.getDocente();
        estado.append("Datos de docente: ").append(docente != null ? "PRESENTES" : "AUSENTES").append("\n");
        if (docente != null) {
            estado.append("  - ID Docente: ").append(docente.optString("id_docente", "N/A")).append("\n");
        }
        
        // Verificaciones específicas
        estado.append("\n=== VERIFICACIONES ===\n");
        estado.append("¿Es docente?: ").append(sessionManager.isDocente() ? "SÍ" : "NO").append("\n");
        estado.append("¿Es estudiante?: ").append(sessionManager.isEstudiante() ? "SÍ" : "NO").append("\n");
        estado.append("¿Es admin?: ").append(sessionManager.isAdmin() ? "SÍ" : "NO").append("\n");
        estado.append("¿Sesión de docente válida?: ").append(sessionManager.isDocenteSessionValid() ? "SÍ" : "NO").append("\n");
        
        textoSesion.setText(estado.toString());
    }

    private void configurarListeners() {
        botonLimpiarSesion.setOnClickListener(v -> {
            sessionManager.clearSession();
            Toast.makeText(this, "Sesión limpiada", Toast.LENGTH_SHORT).show();
            mostrarEstadoSesion();
        });

        botonVolverLogin.setOnClickListener(v -> {
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
