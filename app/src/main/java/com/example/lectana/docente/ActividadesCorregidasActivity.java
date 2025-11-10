package com.example.lectana.docente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.ActividadesDocenteResponse;
import com.example.lectana.repository.ActividadesRepository;
import com.example.lectana.auth.SessionManager;
import java.util.ArrayList;
import java.util.List;

public class ActividadesCorregidasActivity extends AppCompatActivity {
    private static final String TAG = "ActividadesCorregidas";
    
    private RecyclerView recyclerViewActividades;
    private ProgressBar progressBar;
    private LinearLayout layoutSinActividades;
    private TextView textSinActividades;
    private TextView textContadorActividades;
    private android.widget.ImageButton botonVolver;
    
    private ActividadesRepository actividadesRepository;
    private SessionManager sessionManager;
    private List<Actividad> listaActividades;
    private ActividadesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "=== INICIANDO ActividadesCorregidasActivity ===");
        
        try {
            setContentView(R.layout.activity_actividades_corregidas);
            Log.d(TAG, "Layout cargado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error cargando layout", e);
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
            
            Log.d(TAG, "Cargando actividades corregidas...");
            cargarActividadesCorregidas();
            
            Log.d(TAG, "ActividadesCorregidasActivity inicializada completamente");
        } catch (Exception e) {
            Log.e(TAG, "Error durante la inicialización", e);
            Toast.makeText(this, "Error inicializando la actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void inicializarComponentes() {
        Log.d(TAG, "Inicializando componentes de UI...");
        
        try {
            botonVolver = findViewById(R.id.botonVolver);
            recyclerViewActividades = findViewById(R.id.recyclerViewActividades);
            progressBar = findViewById(R.id.progressBar);
            layoutSinActividades = findViewById(R.id.layoutSinActividades);
            textSinActividades = findViewById(R.id.textSinActividades);
            textContadorActividades = findViewById(R.id.textContadorActividades);
            
            actividadesRepository = new ActividadesRepository(sessionManager);
            listaActividades = new ArrayList<>();
            
            Log.d(TAG, "Componentes inicializados correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando componentes", e);
            Toast.makeText(this, "Error inicializando componentes: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void configurarListeners() {
        Log.d(TAG, "Configurando listeners...");
        
        if (botonVolver != null) {
            botonVolver.setOnClickListener(v -> finish());
        }
    }

    private void configurarRecyclerView() {
        Log.d(TAG, "Configurando RecyclerView...");
        
        if (recyclerViewActividades == null) {
            Log.e(TAG, "Error: recyclerViewActividades es null");
            return;
        }
        
        try {
            adapter = new ActividadesAdapter(listaActividades, new ActividadesAdapter.OnActividadClickListener() {
                @Override
                public void onActividadClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN ACTIVIDAD ===");
                    Intent intent = new Intent(ActividadesCorregidasActivity.this, DetalleActividadActivity.class);
                    intent.putExtra("actividad_id", actividad.getId_actividad());
                    startActivity(intent);
                }

                @Override
                public void onEditarClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN EDITAR ===");
                    Intent intent = new Intent(ActividadesCorregidasActivity.this, CrearActividadActivityTest.class);
                    intent.putExtra("actividad_id", actividad.getId_actividad());
                    intent.putExtra("modo_edicion", true);
                    startActivity(intent);
                }

                @Override
                public void onEliminarClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN ELIMINAR ===");
                    eliminarActividad(actividad);
                }

                @Override
                public void onAsignarAulasClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN ASIGNAR AULAS ===");
                    Intent intent = new Intent(ActividadesCorregidasActivity.this, AsignarAulasActivity.class);
                    intent.putExtra("actividad_id", actividad.getId_actividad());
                    startActivity(intent);
                }
            });
            
            recyclerViewActividades.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewActividades.setAdapter(adapter);
            
            Log.d(TAG, "RecyclerView configurado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error configurando RecyclerView", e);
            Toast.makeText(this, "Error configurando RecyclerView: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void cargarActividadesCorregidas() {
        Log.d(TAG, "Iniciando carga de actividades corregidas...");
        
        try {
            mostrarCargando(true);
            
            // Reutilizar endpoint existente y filtrar en el frontend
            actividadesRepository.getActividadesDocente(new ActividadesRepository.ActividadesCallback<List<Actividad>>() {
                @Override
                public void onSuccess(List<Actividad> actividades) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        // Filtrar actividades corregidas (sin_corregir = 0)
                        List<Actividad> actividadesCorregidas = filtrarCorregidas(actividades);
                        actualizarDatosActividades(actividadesCorregidas);
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Log.e(TAG, "Error cargando actividades: " + message);
                        Toast.makeText(ActividadesCorregidasActivity.this, "Error al cargar actividades: " + message, Toast.LENGTH_LONG).show();
                        mostrarMensajeSinActividades();
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error en cargarActividadesCorregidas", e);
            runOnUiThread(() -> {
                mostrarCargando(false);
                Toast.makeText(this, "Error crítico: " + e.getMessage(), Toast.LENGTH_LONG).show();
            });
        }
    }

    /**
     * Filtrar actividades ya corregidas
     * Actividades donde sin_corregir = 0
     */
    private List<Actividad> filtrarCorregidas(List<Actividad> actividades) {
        List<Actividad> corregidas = new ArrayList<>();
        if (actividades != null) {
            for (Actividad actividad : actividades) {
                if (actividad.getResultados_actividad() != null) {
                    int sinCorregir = actividad.getResultados_actividad().getSin_corregir();
                    if (sinCorregir == 0 && actividad.getResultados_actividad().getCorregidas() > 0) {
                        corregidas.add(actividad);
                    }
                }
            }
        }
        return corregidas;
    }

    private void actualizarDatosActividades(List<Actividad> actividades) {
        Log.d(TAG, "actualizarDatosActividades: Iniciando con " + (actividades != null ? actividades.size() : 0) + " actividades");
        
        try {
            if (listaActividades != null) {
                listaActividades.clear();
                if (actividades != null) {
                    listaActividades.addAll(actividades);
                }
            }
            
            if (adapter == null && actividades != null && !actividades.isEmpty()) {
                configurarRecyclerView();
            } else if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            
            actualizarContadorActividades();
            
            if (actividades == null || actividades.isEmpty()) {
                mostrarMensajeSinActividades();
            } else {
                ocultarMensajeSinActividades();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error en actualizarDatosActividades", e);
            Toast.makeText(this, "Error actualizando datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void actualizarContadorActividades() {
        int total = listaActividades.size();
        if (textContadorActividades != null) {
            textContadorActividades.setText(total + " actividades corregidas");
        }
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        if (recyclerViewActividades != null) {
            recyclerViewActividades.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        }
    }

    private void mostrarMensajeSinActividades() {
        if (layoutSinActividades != null) {
            layoutSinActividades.setVisibility(View.VISIBLE);
        }
        if (recyclerViewActividades != null) {
            recyclerViewActividades.setVisibility(View.GONE);
        }
    }

    private void ocultarMensajeSinActividades() {
        if (layoutSinActividades != null) {
            layoutSinActividades.setVisibility(View.GONE);
        }
        if (recyclerViewActividades != null) {
            recyclerViewActividades.setVisibility(View.VISIBLE);
        }
    }

    private void eliminarActividad(Actividad actividad) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Actividad")
                .setMessage("¿Estás seguro de que quieres eliminar la actividad \"" + actividad.getDescripcion() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    actividadesRepository.eliminarActividad(actividad.getId_actividad(), new ActividadesRepository.ActividadesCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            runOnUiThread(() -> {
                                Toast.makeText(ActividadesCorregidasActivity.this, "Actividad eliminada exitosamente", Toast.LENGTH_SHORT).show();
                                cargarActividadesCorregidas();
                            });
                        }

                        @Override
                        public void onError(String message) {
                            runOnUiThread(() -> {
                                Toast.makeText(ActividadesCorregidasActivity.this, "Error al eliminar actividad: " + message, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private boolean verificarSesionYRole() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        String role = sessionManager.getRole();
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
        cargarActividadesCorregidas();
    }
}

