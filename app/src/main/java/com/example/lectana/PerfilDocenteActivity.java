package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.Login;
import com.example.lectana.auth.SessionManager;

public class PerfilDocenteActivity extends AppCompatActivity {

    private ImageView botonVolverPerfil;
    private ImageView botonEditarPerfil;
    private TextView nombreDocentePerfil;
    private TextView especialidadDocentePerfil;
    private TextView emailDocentePerfil;
    private TextView telefonoDocentePerfil;
    private TextView institucionDocentePerfil;
    private TextView totalAulasPerfil;
    private TextView totalEstudiantesPerfil;
    private TextView totalCuentosPerfil;
    private Button botonCambiarPassword;
    private Button botonCerrarSesion;
    
    // Gestión de sesión
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_docente);

        // Inicializar gestión de sesión
        sessionManager = new SessionManager(this);
        
        // Verificar sesión antes de continuar
        if (!verificarSesion()) {
            return;
        }

        inicializarVistas();
        cargarDatosDocente();
        configurarListeners();
    }

    private void inicializarVistas() {
        botonVolverPerfil = findViewById(R.id.boton_volver_perfil);
        botonEditarPerfil = findViewById(R.id.boton_editar_perfil);
        nombreDocentePerfil = findViewById(R.id.nombre_docente_perfil);
        especialidadDocentePerfil = findViewById(R.id.especialidad_docente_perfil);
        emailDocentePerfil = findViewById(R.id.email_docente_perfil);
        telefonoDocentePerfil = findViewById(R.id.telefono_docente_perfil);
        institucionDocentePerfil = findViewById(R.id.institucion_docente_perfil);
        totalAulasPerfil = findViewById(R.id.total_aulas_perfil);
        totalEstudiantesPerfil = findViewById(R.id.total_estudiantes_perfil);
        totalCuentosPerfil = findViewById(R.id.total_cuentos_perfil);
        botonCambiarPassword = findViewById(R.id.boton_cambiar_password);
        botonCerrarSesion = findViewById(R.id.boton_cerrar_sesion);
    }

    private void cargarDatosDocente() {
        try {
            // Cargar datos reales del docente desde la sesión
            org.json.JSONObject user = sessionManager.getUser();
            if (user != null) {
                String nombre = user.optString("nombre", "Docente");
                String apellido = user.optString("apellido", "");
                String nombreCompleto = "Prof. " + nombre + (apellido.isEmpty() ? "" : " " + apellido);
                
                nombreDocentePerfil.setText(nombreCompleto);
                emailDocentePerfil.setText(user.optString("email", ""));
                
                // Cargar datos específicos del docente si están disponibles
                org.json.JSONObject docente = sessionManager.getDocente();
                Log.d("PerfilDocente", "Docente cargado: " + (docente != null ? docente.toString() : "NULL"));
                
                if (docente != null) {
                    String telefono = docente.optString("telefono", "Sin especificar");
                    String institucion = docente.optString("institucion_nombre", "Sin especificar");
                    String nivelEducativo = docente.optString("nivel_educativo", "");
                    
                    telefonoDocentePerfil.setText(telefono);
                    institucionDocentePerfil.setText(institucion);
                    
                    Log.d("PerfilDocente", "Datos cargados - Teléfono: " + telefono + ", Institución: " + institucion + ", Nivel: " + nivelEducativo);
                    
                    // Mostrar nivel educativo como especialidad
                    if (!nivelEducativo.isEmpty()) {
                        especialidadDocentePerfil.setText("Nivel: " + nivelEducativo);
                    } else {
                        especialidadDocentePerfil.setText("Docente");
                    }
                } else {
                    // Valores por defecto si no hay datos del docente
                    telefonoDocentePerfil.setText("Sin especificar");
                    institucionDocentePerfil.setText("Sin especificar");
                    especialidadDocentePerfil.setText("Docente");
                    Log.w("PerfilDocente", "No hay datos del docente disponibles");
                }
                
                // Estadísticas (TODO: Obtener desde backend)
                totalAulasPerfil.setText("0");
                totalEstudiantesPerfil.setText("0");
                totalCuentosPerfil.setText("0");
            } else {
                // Datos por defecto si no hay sesión
                nombreDocentePerfil.setText("Docente");
                especialidadDocentePerfil.setText("Sin especificar");
                emailDocentePerfil.setText("Sin especificar");
                telefonoDocentePerfil.setText("Sin especificar");
                institucionDocentePerfil.setText("Sin especificar");
                totalAulasPerfil.setText("0");
                totalEstudiantesPerfil.setText("0");
                totalCuentosPerfil.setText("0");
            }
        } catch (Exception e) {
            // Datos por defecto en caso de error
            nombreDocentePerfil.setText("Docente");
            especialidadDocentePerfil.setText("Sin especificar");
            emailDocentePerfil.setText("Sin especificar");
            telefonoDocentePerfil.setText("Sin especificar");
            institucionDocentePerfil.setText("Sin especificar");
            totalAulasPerfil.setText("0");
            totalEstudiantesPerfil.setText("0");
            totalCuentosPerfil.setText("0");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar sesión cada vez que se regrese a esta pantalla
        if (!verificarSesion()) {
            return;
        }
        // Recargar datos para mostrar cambios en vivo
        cargarDatosDocente();
    }

    private void configurarListeners() {
        botonVolverPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la pantalla de edición de perfil
                Intent intent = new Intent(PerfilDocenteActivity.this, EditarPerfilDocenteActivity.class);
                startActivity(intent);
            }
        });

        botonCambiarPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implementar cambio de contraseña
                android.widget.Toast.makeText(PerfilDocenteActivity.this, 
                    "Funcionalidad de cambio de contraseña en desarrollo", 
                    android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realizar logout usando SessionManager
                sessionManager.clearSession();
                Toast.makeText(PerfilDocenteActivity.this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
                
                // Volver al login
                Intent intent = new Intent(PerfilDocenteActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Verificar si hay una sesión válida de docente
     */
    private boolean verificarSesion() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            irAlLogin();
            return false;
        }

        if (!sessionManager.isDocente()) {
            Toast.makeText(this, "Acceso denegado. Esta área es solo para docentes.", Toast.LENGTH_LONG).show();
            irAlLogin();
            return false;
        }

        return true;
    }

    /**
     * Redirigir al login y limpiar sesión
     */
    private void irAlLogin() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
