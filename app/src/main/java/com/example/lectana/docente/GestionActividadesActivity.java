package com.example.lectana.docente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.lectana.R;
import com.example.lectana.modelos.Actividad;
import com.example.lectana.repository.ActividadesRepository;
import com.example.lectana.auth.SessionManager;

import java.util.List;

public class GestionActividadesActivity extends AppCompatActivity {
    private static final String TAG = "GestionActividades";
    
    // Componentes del menú
    private CardView cardTodasActividades;
    private CardView cardPendientesCorreccion;
    private CardView cardCorregidas;
    private TextView textContadorTodas;
    private TextView textContadorPendientes;
    private TextView textContadorCorregidas;
    private ProgressBar progressBar;
    private android.widget.ImageButton botonVolverGestion;
    private com.google.android.material.button.MaterialButton btnCrearActividad;
    
    private ActividadesRepository actividadesRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "=== INICIANDO GestionActividadesActivity (Menú Principal) ===");
        
        try {
            setContentView(R.layout.activity_gestion_actividades);
            Log.d(TAG, "Layout activity_gestion_actividades cargado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error cargando layout activity_gestion_actividades", e);
            Toast.makeText(this, "Error cargando interfaz: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        // Inicializar SessionManager
        try {
            sessionManager = new SessionManager(this);
            Log.d(TAG, "SessionManager inicializado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando SessionManager", e);
            Toast.makeText(this, "Error inicializando sesión", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Verificar sesión antes de continuar
        if (!verificarSesionYRole()) {
            return;
        }
        
        try {
            Log.d(TAG, "Inicializando componentes...");
            inicializarComponentes();
            
            Log.d(TAG, "Configurando listeners...");
            configurarListeners();
            
            Log.d(TAG, "Cargando estadísticas...");
            cargarEstadisticas();
            
            Log.d(TAG, "GestionActividadesActivity inicializada completamente");
        } catch (Exception e) {
            Log.e(TAG, "Error durante la inicialización", e);
            Toast.makeText(this, "Error inicializando la actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void inicializarComponentes() {
        Log.d(TAG, "Inicializando componentes de UI...");
        
        try {
            botonVolverGestion = findViewById(R.id.botonVolverGestion);
            cardTodasActividades = findViewById(R.id.cardTodasActividades);
            cardPendientesCorreccion = findViewById(R.id.cardPendientesCorreccion);
            cardCorregidas = findViewById(R.id.cardCorregidas);
            textContadorTodas = findViewById(R.id.textContadorTodas);
            textContadorPendientes = findViewById(R.id.textContadorPendientes);
            textContadorCorregidas = findViewById(R.id.textContadorCorregidas);
            progressBar = findViewById(R.id.progressBar);
            btnCrearActividad = findViewById(R.id.btnCrearActividad);
            
            actividadesRepository = new ActividadesRepository(sessionManager);
            
            Log.d(TAG, "Componentes inicializados correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando componentes", e);
            Toast.makeText(this, "Error inicializando componentes: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void configurarListeners() {
        Log.d(TAG, "Configurando listeners...");
        
        // Botón volver
        if (botonVolverGestion != null) {
            botonVolverGestion.setOnClickListener(v -> finish());
        }
        
        // Card: Todas las Actividades
        if (cardTodasActividades != null) {
            cardTodasActividades.setOnClickListener(v -> {
                Log.d(TAG, "Click en 'Todas las Actividades'");
                Intent intent = new Intent(GestionActividadesActivity.this, ListaTodasActividadesActivity.class);
                startActivity(intent);
            });
        }
        
        // Card: Pendientes de Corrección
        if (cardPendientesCorreccion != null) {
            cardPendientesCorreccion.setOnClickListener(v -> {
                Log.d(TAG, "Click en 'Pendientes de Corrección'");
                Intent intent = new Intent(GestionActividadesActivity.this, ActividadesPendientesActivity.class);
                startActivity(intent);
            });
        }
        
        // Card: Ya Corregidas
        if (cardCorregidas != null) {
            cardCorregidas.setOnClickListener(v -> {
                Log.d(TAG, "Click en 'Ya Corregidas'");
                Intent intent = new Intent(GestionActividadesActivity.this, ActividadesCorregidasActivity.class);
                startActivity(intent);
            });
        }
        
        // Botón Crear Actividad
        if (btnCrearActividad != null) {
            btnCrearActividad.setOnClickListener(v -> {
                Log.d(TAG, "Click en 'Crear Nueva Actividad'");
                Intent intent = new Intent(GestionActividadesActivity.this, CrearActividadActivity.class);
                startActivity(intent);
            });
        }
    }

    private void cargarEstadisticas() {
        Log.d(TAG, "Iniciando carga de estadísticas...");
        
        try {
            mostrarCargando(true);
            
            // Cargar todas las actividades para obtener estadísticas
            actividadesRepository.getActividadesDocente(new ActividadesRepository.ActividadesCallback<List<Actividad>>() {
                @Override
                public void onSuccess(List<Actividad> actividades) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        actualizarEstadisticas(actividades);
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Log.e(TAG, "Error cargando estadísticas: " + message);
                        // Mostrar valores por defecto en caso de error
                        actualizarEstadisticas(null);
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error en cargarEstadisticas", e);
            runOnUiThread(() -> {
                mostrarCargando(false);
                actualizarEstadisticas(null);
            });
        }
    }

    private void actualizarEstadisticas(List<Actividad> actividades) {
        try {
            int total = actividades != null ? actividades.size() : 0;
            int pendientes = 0;
            int corregidas = 0;
            
            if (actividades != null) {
                // Contar actividades pendientes y corregidas usando resultados_actividad
                for (Actividad actividad : actividades) {
                    if (actividad.getResultados_actividad() != null) {
                        int sinCorregir = actividad.getResultados_actividad().getSin_corregir();
                        if (sinCorregir > 0) {
                            pendientes++;
                        } else if (sinCorregir == 0 && actividad.getResultados_actividad().getCorregidas() > 0) {
                            corregidas++;
                        }
                    }
                }
            }
            
            // Actualizar contadores
            if (textContadorTodas != null) {
                textContadorTodas.setText(total + " actividades");
            }
            
            if (textContadorPendientes != null) {
                textContadorPendientes.setText(pendientes + " actividades");
            }
            
            if (textContadorCorregidas != null) {
                textContadorCorregidas.setText(corregidas + " actividades");
            }
            
            Log.d(TAG, "Estadísticas actualizadas - Total: " + total + ", Pendientes: " + pendientes + ", Corregidas: " + corregidas);
        } catch (Exception e) {
            Log.e(TAG, "Error actualizando estadísticas", e);
        }
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    private boolean verificarSesionYRole() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        String role = sessionManager.getRole();
        String token = sessionManager.getToken();
        
        Log.d(TAG, "Rol actual: " + role);
        Log.d(TAG, "Token presente: " + (token != null ? "Sí" : "No"));
        
        if (!"docente".equals(role)) {
            Toast.makeText(this, "Acceso denegado. Solo los docentes pueden gestionar actividades.", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar estadísticas cuando se regrese de otras pantallas
        cargarEstadisticas();
    }
}
