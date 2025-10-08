package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PerfilEstudianteActivity extends AppCompatActivity {

    private ImageView botonVolverPerfilEstudiante;
    private ImageView botonConfiguracionPerfilEstudiante;
    private ImageView fotoPerfilEstudiante;
    private ImageView botonEditarFoto;
    private TextView nombreEstudiantePerfil;
    private TextView nivelEstudiantePerfil;
    private TextView puntosEstudiantePerfil;
    private LinearLayout opcionCambiarPassword;
    private LinearLayout opcionCentroAyuda;
    private LinearLayout opcionCerrarSesion;
    private View botonIrTienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_estudiante);

        inicializarVistas();
        configurarListeners();
        cargarDatosPerfil();
    }

    private void inicializarVistas() {
        botonVolverPerfilEstudiante = findViewById(R.id.boton_volver_perfil_estudiante);
        botonConfiguracionPerfilEstudiante = findViewById(R.id.boton_configuracion_perfil_estudiante);
        fotoPerfilEstudiante = findViewById(R.id.foto_perfil_estudiante);
        botonEditarFoto = findViewById(R.id.boton_editar_foto);
        nombreEstudiantePerfil = findViewById(R.id.nombre_estudiante_perfil);
        nivelEstudiantePerfil = findViewById(R.id.nivel_estudiante_perfil);
        puntosEstudiantePerfil = findViewById(R.id.puntos_estudiante_perfil);
        opcionCambiarPassword = findViewById(R.id.opcion_cambiar_password);
        opcionCentroAyuda = findViewById(R.id.opcion_centro_ayuda);
        opcionCerrarSesion = findViewById(R.id.opcion_cerrar_sesion);
        botonIrTienda = findViewById(R.id.boton_ir_tienda);
    }

    private void configurarListeners() {
        // Botón volver
        botonVolverPerfilEstudiante.setOnClickListener(v -> finish());

        // Botón configuración
        botonConfiguracionPerfilEstudiante.setOnClickListener(v -> {
            // TODO: Implementar configuración adicional si es necesario
            Toast.makeText(this, "Configuración adicional", Toast.LENGTH_SHORT).show();
        });

        // Botón editar foto
        botonEditarFoto.setOnClickListener(v -> {
            // TODO: Implementar selección de foto de perfil
            Toast.makeText(this, "Seleccionar foto de perfil", Toast.LENGTH_SHORT).show();
        });

        // Opciones de configuración
        opcionCambiarPassword.setOnClickListener(v -> {
            // TODO: Implementar cambio de contraseña
            Toast.makeText(this, "Cambiar contraseña", Toast.LENGTH_SHORT).show();
        });

        opcionCentroAyuda.setOnClickListener(v -> {
            // Navegar al centro de ayuda
            Intent intent = new Intent(this, centro_ayuda.class);
            startActivity(intent);
        });

        opcionCerrarSesion.setOnClickListener(v -> {
            // TODO: Implementar cerrar sesión
            Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show();
            // Por ahora, volver al login
            Intent intent = new Intent(this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Botón ir a tienda
        botonIrTienda.setOnClickListener(v -> {
            // TODO: Implementar navegación a tienda
            Toast.makeText(this, "Ir a Tienda", Toast.LENGTH_SHORT).show();
        });

        // Navegación inferior
        configurarNavegacionInferior();
    }

    private void configurarNavegacionInferior() {
        findViewById(R.id.tab_inicio_perfil).setOnClickListener(v -> {
            Intent intent = new Intent(this, PanelEstudianteActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.tab_biblioteca_perfil).setOnClickListener(v -> {
            // TODO: Implementar navegación a biblioteca
            Toast.makeText(this, "Biblioteca", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.tab_progreso_perfil).setOnClickListener(v -> {
            // TODO: Implementar navegación a progreso
            Toast.makeText(this, "Progreso", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.tab_tienda_perfil).setOnClickListener(v -> {
            // TODO: Implementar navegación a tienda
            Toast.makeText(this, "Tienda", Toast.LENGTH_SHORT).show();
        });

        // El tab perfil ya está activo, no necesita acción
    }

    private void cargarDatosPerfil() {
        // TODO: Cargar datos reales del estudiante desde la base de datos
        // Por ahora, datos de ejemplo
        nombreEstudiantePerfil.setText("Juanito Lector");
        nivelEstudiantePerfil.setText("Lector Avanzado");
        puntosEstudiantePerfil.setText("120 puntos");
    }
}
