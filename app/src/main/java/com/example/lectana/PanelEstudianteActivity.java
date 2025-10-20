package com.example.lectana.estudiante;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lectana.Login;
import com.example.lectana.estudiante.fragments.BibliotecaFragment;
import com.example.lectana.estudiante.fragments.InicioFragment;
import com.example.lectana.estudiante.fragments.MiProgresoFragment;
import com.example.lectana.estudiante.fragments.PerfilFragment;
import com.example.lectana.estudiante.fragments.TiendaFragment;
import com.example.lectana.R;
import com.example.lectana.auth.SessionManager;

public class PanelEstudianteActivity extends AppCompatActivity {

    private TextView saludoEstudiante;
    private ImageView botonConfiguracionEstudiante;
    
    // Referencias a los tabs para manejar estados
    private LinearLayout tabInicio;
    private LinearLayout tabBiblioteca;
    private LinearLayout tabProgreso;
    private LinearLayout tabTienda;
    private LinearLayout tabPerfil;
    
    // Referencias a los iconos de los tabs
    private ImageView iconoInicio;
    private ImageView iconoBiblioteca;
    private ImageView iconoProgreso;
    private ImageView iconoTienda;
    private ImageView iconoPerfil;
    
    // Referencias a los textos de los tabs
    private TextView textoInicio;
    private TextView textoBiblioteca;
    private TextView textoProgreso;
    private TextView textoTienda;
    private TextView textoPerfil;
    
    private String tabActual = "inicio";
    
    // Gestión de sesión
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_estudiante);

        // Inicializar gestión de sesión
        sessionManager = new SessionManager(this);
        
        // Verificar sesión antes de continuar
        if (!verificarSesion()) {
            return;
        }

        inicializarVistas();
        configurarListeners();
        
        // Mostrar nombre del usuario desde la sesión
        mostrarSaludoPersonalizado();

        // Fragment inicial (Inicio)
        reemplazarFragment(new InicioFragment());
        actualizarEstadoTabs("inicio");
    }

    private void inicializarVistas() {
        saludoEstudiante = findViewById(R.id.saludo_estudiante);
        botonConfiguracionEstudiante = findViewById(R.id.boton_configuracion_estudiante);
        
        // Inicializar tabs
        tabInicio = findViewById(R.id.tab_inicio);
        tabBiblioteca = findViewById(R.id.tab_biblioteca);
        tabProgreso = findViewById(R.id.tab_progreso);
        tabTienda = findViewById(R.id.tab_tienda);
        tabPerfil = findViewById(R.id.tab_perfil);
        
        // Inicializar iconos
        iconoInicio = tabInicio.findViewById(R.id.icono_inicio);
        iconoBiblioteca = tabBiblioteca.findViewById(R.id.icono_biblioteca);
        iconoProgreso = tabProgreso.findViewById(R.id.icono_progreso);
        iconoTienda = tabTienda.findViewById(R.id.icono_tienda);
        iconoPerfil = tabPerfil.findViewById(R.id.icono_perfil);
        
        // Inicializar textos
        textoInicio = tabInicio.findViewById(R.id.texto_inicio);
        textoBiblioteca = tabBiblioteca.findViewById(R.id.texto_biblioteca);
        textoProgreso = tabProgreso.findViewById(R.id.texto_progreso);
        textoTienda = tabTienda.findViewById(R.id.texto_tienda);
        textoPerfil = tabPerfil.findViewById(R.id.texto_perfil);
    }

    private void configurarListeners() {
        // Botón configuración
        botonConfiguracionEstudiante.setOnClickListener(v -> {
            reemplazarFragment(new PerfilFragment());
            actualizarEstadoTabs("perfil");
        });

        // Navegación inferior
        findViewById(R.id.tab_inicio).setOnClickListener(v -> {
            reemplazarFragment(new InicioFragment());
            actualizarEstadoTabs("inicio");
        });
        findViewById(R.id.tab_biblioteca).setOnClickListener(v -> {
            reemplazarFragment(new BibliotecaFragment());
            actualizarEstadoTabs("biblioteca");
        });
        findViewById(R.id.tab_progreso).setOnClickListener(v -> {
            reemplazarFragment(new MiProgresoFragment());
            actualizarEstadoTabs("progreso");
        });
        findViewById(R.id.tab_tienda).setOnClickListener(v -> {
            reemplazarFragment(new TiendaFragment());
            actualizarEstadoTabs("tienda");
        });
        findViewById(R.id.tab_perfil).setOnClickListener(v -> {
            reemplazarFragment(new PerfilFragment());
            actualizarEstadoTabs("perfil");
        });
    }

    private void reemplazarFragment(Fragment fragment) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.contenedor_fragments, fragment);
        tx.commitAllowingStateLoss();
    }
    
    private void actualizarEstadoTabs(String tabSeleccionado) {
        // Resetear todos los tabs a estado inactivo
        resetearTabs();
        
        // Activar el tab seleccionado
        switch (tabSeleccionado) {
            case "inicio":
                iconoInicio.setColorFilter(getResources().getColor(R.color.azul_fuerte));
                textoInicio.setTextColor(getResources().getColor(R.color.azul_fuerte));
                break;
            case "biblioteca":
                iconoBiblioteca.setColorFilter(getResources().getColor(R.color.azul_fuerte));
                textoBiblioteca.setTextColor(getResources().getColor(R.color.azul_fuerte));
                break;
            case "progreso":
                iconoProgreso.setColorFilter(getResources().getColor(R.color.azul_fuerte));
                textoProgreso.setTextColor(getResources().getColor(R.color.azul_fuerte));
                break;
            case "tienda":
                iconoTienda.setColorFilter(getResources().getColor(R.color.azul_fuerte));
                textoTienda.setTextColor(getResources().getColor(R.color.azul_fuerte));
                break;
            case "perfil":
                iconoPerfil.setColorFilter(getResources().getColor(R.color.azul_fuerte));
                textoPerfil.setTextColor(getResources().getColor(R.color.azul_fuerte));
                break;
        }
        
        tabActual = tabSeleccionado;
    }
    
    private void resetearTabs() {
        // Resetear todos los iconos a gris
        iconoInicio.setColorFilter(getResources().getColor(R.color.gris_medio));
        iconoBiblioteca.setColorFilter(getResources().getColor(R.color.gris_medio));
        iconoProgreso.setColorFilter(getResources().getColor(R.color.gris_medio));
        iconoTienda.setColorFilter(getResources().getColor(R.color.gris_medio));
        iconoPerfil.setColorFilter(getResources().getColor(R.color.gris_medio));
        
        // Resetear todos los textos a gris
        textoInicio.setTextColor(getResources().getColor(R.color.gris_medio));
        textoBiblioteca.setTextColor(getResources().getColor(R.color.gris_medio));
        textoProgreso.setTextColor(getResources().getColor(R.color.gris_medio));
        textoTienda.setTextColor(getResources().getColor(R.color.gris_medio));
        textoPerfil.setTextColor(getResources().getColor(R.color.gris_medio));
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar sesión cada vez que se regrese a esta pantalla
        if (!verificarSesion()) {
            return;
        }
    }

    /**
     * Verificar si hay una sesión válida de estudiante
     */
    private boolean verificarSesion() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            irAlLogin();
            return false;
        }

        if (!sessionManager.isEstudiante()) {
            Toast.makeText(this, "Acceso denegado. Esta área es solo para estudiantes.", Toast.LENGTH_LONG).show();
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

    /**
     * Mostrar saludo personalizado con el nombre del usuario
     */
    private void mostrarSaludoPersonalizado() {
        try {
            org.json.JSONObject user = sessionManager.getUser();
            if (user != null) {
                String nombre = user.optString("nombre", "Estudiante");
                saludoEstudiante.setText("¡Hola, " + nombre + "!");
            } else {
                saludoEstudiante.setText("¡Hola, Estudiante!");
            }
        } catch (Exception e) {
            saludoEstudiante.setText("¡Hola, Estudiante!");
        }
    }

    /**
     * Realizar logout
     */
    public void logout() {
        sessionManager.clearSession();
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        irAlLogin();
    }

    /**
     * Manejar el botón "Atrás" del sistema
     * Si no está en Inicio, va a Inicio. Si está en Inicio, sale de la app.
     */
    @Override
    public void onBackPressed() {
        // Si no está en el tab de Inicio, navegar a Inicio
        if (!tabActual.equals("inicio")) {
            reemplazarFragment(new InicioFragment());
            actualizarEstadoTabs("inicio");
        } else {
            // Si ya está en Inicio, comportamiento normal (salir de la app)
            super.onBackPressed();
        }
    }
}
