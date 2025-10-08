package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilDocenteActivity extends AppCompatActivity {

    private ImageView botonVolverPerfil;
    private ImageView botonEditarPerfil;
    private TextView nombreDocentePerfil;
    private TextView especialidadDocentePerfil;
    private TextView emailDocentePerfil;
    private TextView telefonoDocentePerfil;
    private TextView colegioDocentePerfil;
    private TextView experienciaDocentePerfil;
    private TextView totalAulasPerfil;
    private TextView totalEstudiantesPerfil;
    private TextView totalCuentosPerfil;
    private Button botonCambiarPassword;
    private Button botonCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_docente);

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
        colegioDocentePerfil = findViewById(R.id.colegio_docente_perfil);
        experienciaDocentePerfil = findViewById(R.id.experiencia_docente_perfil);
        totalAulasPerfil = findViewById(R.id.total_aulas_perfil);
        totalEstudiantesPerfil = findViewById(R.id.total_estudiantes_perfil);
        totalCuentosPerfil = findViewById(R.id.total_cuentos_perfil);
        botonCambiarPassword = findViewById(R.id.boton_cambiar_password);
        botonCerrarSesion = findViewById(R.id.boton_cerrar_sesion);
    }

    private void cargarDatosDocente() {
        // TODO: Cargar datos reales desde la base de datos
        // Por ahora datos de ejemplo
        nombreDocentePerfil.setText("Prof. María García");
        especialidadDocentePerfil.setText("Lengua y Literatura");
        emailDocentePerfil.setText("maria.garcia@colegio.edu");
        telefonoDocentePerfil.setText("+54 11 1234-5678");
        colegioDocentePerfil.setText("Colegio San Martín");
        experienciaDocentePerfil.setText("8 años");
        
        // Estadísticas
        totalAulasPerfil.setText("5");
        totalEstudiantesPerfil.setText("120");
        totalCuentosPerfil.setText("25");
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
                // TODO: Implementar edición de perfil
                android.widget.Toast.makeText(PerfilDocenteActivity.this, 
                    "Funcionalidad de edición en desarrollo", 
                    android.widget.Toast.LENGTH_SHORT).show();
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
                // TODO: Implementar cierre de sesión
                // Por ahora solo regresa al login
                Intent intent = new Intent(PerfilDocenteActivity.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
