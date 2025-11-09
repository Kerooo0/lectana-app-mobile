package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.docente.PantallaPrincipalDocente;
import com.example.lectana.estudiante.PanelEstudianteActivity;


public class Inicio extends AppCompatActivity {

    Button boton;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        sessionManager = new SessionManager(this);
        
        // Verificar si hay una sesión válida al iniciar
        verificarSesion();

        boton = findViewById(R.id.boton_inicio);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, Login.class);
                startActivity(intent);
            }
        });
    }
    
    /**
     * Verificar si existe una sesión válida y redirigir automáticamente
     */
    private void verificarSesion() {
        // Si hay una sesión guardada
        if (sessionManager.isLoggedIn()) {
            // Verificar si el token no ha expirado
            if (sessionManager.isTokenExpired()) {
                // Token expirado - limpiar sesión y mostrar mensaje
                sessionManager.clearSession();
                Toast.makeText(this, 
                    "Tu sesión ha expirado. Por favor, inicia sesión nuevamente.", 
                    Toast.LENGTH_LONG).show();
                // No redirigir, dejar que el usuario inicie sesión manualmente
            } else {
                // Token válido - redirigir automáticamente según el rol
                String role = sessionManager.getRole();
                Intent intent = null;
                
                if ("docente".equals(role)) {
                    intent = new Intent(this, PantallaPrincipalDocente.class);
                } else if ("alumno".equals(role)) {
                    intent = new Intent(this, PanelEstudianteActivity.class);
                }
                
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish(); // Cerrar la pantalla de inicio
                }
            }
        }
        // Si no hay sesión, simplemente mostrar la pantalla de inicio
    }
}