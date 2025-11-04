package com.example.lectana.docente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.Actividad;
import com.example.lectana.repository.ActividadesRepository;
import com.example.lectana.auth.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class GestionActividadesActivity extends AppCompatActivity {
    private static final String TAG = "GestionActividades";
    
    private RecyclerView recyclerViewActividades;
    private ProgressBar progressBar;
    private LinearLayout layoutSinActividades;
    private TextView textSinActividades;
    private Button btnCrearActividad;
    private TextView textContadorActividades;
    private android.widget.ImageButton botonVolverGestion;
    
    private ActividadesRepository actividadesRepository;
    private SessionManager sessionManager;
    private List<Actividad> listaActividades;
    private ActividadesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "=== INICIANDO GestionActividadesActivity ===");
        Log.d(TAG, "Intent extras: " + (getIntent().getExtras() != null ? getIntent().getExtras().toString() : "null"));
        
        try {
            setContentView(R.layout.activity_gestion_actividades);
            Log.d(TAG, "Layout activity_gestion_actividades cargado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error cargando layout activity_gestion_actividades", e);
            Toast.makeText(this, "Error cargando interfaz: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        Log.d(TAG, "GestionActividadesActivity iniciada");
        
        // Inicializar SessionManager primero
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
            
            Log.d(TAG, "Cargando actividades...");
            cargarActividades();
            
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
            recyclerViewActividades = findViewById(R.id.recyclerViewActividades);
            Log.d(TAG, "recyclerViewActividades: " + (recyclerViewActividades != null ? "OK" : "NULL"));
            
            progressBar = findViewById(R.id.progressBar);
            Log.d(TAG, "progressBar: " + (progressBar != null ? "OK" : "NULL"));
            
            layoutSinActividades = findViewById(R.id.layoutSinActividades);
            Log.d(TAG, "layoutSinActividades: " + (layoutSinActividades != null ? "OK" : "NULL"));
            
            textSinActividades = findViewById(R.id.textSinActividades);
            Log.d(TAG, "textSinActividades: " + (textSinActividades != null ? "OK" : "NULL"));
            
            btnCrearActividad = findViewById(R.id.btnCrearActividad);
            Log.d(TAG, "btnCrearActividad: " + (btnCrearActividad != null ? "OK" : "NULL"));
            
            textContadorActividades = findViewById(R.id.textContadorActividades);
            Log.d(TAG, "textContadorActividades: " + (textContadorActividades != null ? "OK" : "NULL"));

            botonVolverGestion = findViewById(R.id.botonVolverGestion);
            if (botonVolverGestion != null) {
                botonVolverGestion.setOnClickListener(v -> finish());
            }
            
            // Verificar que todos los componentes críticos estén inicializados
            if (recyclerViewActividades == null || progressBar == null || btnCrearActividad == null) {
                Log.e(TAG, "Error: Componentes críticos de UI no inicializados");
                Toast.makeText(this, "Error: Componentes de UI no encontrados", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            
            // SessionManager ya inicializado en onCreate
            actividadesRepository = new ActividadesRepository(sessionManager);
            listaActividades = new ArrayList<>();
            
            Log.d(TAG, "Componentes inicializados correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando componentes", e);
            Toast.makeText(this, "Error inicializando componentes: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void configurarRecyclerView() {
        Log.d(TAG, "Configurando RecyclerView...");
        
        if (recyclerViewActividades == null) {
            Log.e(TAG, "Error: recyclerViewActividades es null");
            Toast.makeText(this, "Error: RecyclerView no inicializado", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        
        try {
            Log.d(TAG, "Creando ActividadesAdapter...");
            adapter = new ActividadesAdapter(listaActividades, new ActividadesAdapter.OnActividadClickListener() {
                @Override
                public void onActividadClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN ACTIVIDAD ===");
                    Log.d(TAG, "ID: " + actividad.getId_actividad());
                    Log.d(TAG, "Descripción: " + actividad.getDescripcion());
                    
                    try {
                        // Abrir detalle de actividad
                        Intent intent = new Intent(GestionActividadesActivity.this, DetalleActividadActivity.class);
                        intent.putExtra("actividad_id", actividad.getId_actividad());
                        Log.d(TAG, "Intent creado, iniciando actividad...");
                        startActivity(intent);
                        Log.d(TAG, "Actividad iniciada correctamente");
                    } catch (Exception e) {
                        Log.e(TAG, "Error iniciando DetalleActividadActivity", e);
                        Toast.makeText(GestionActividadesActivity.this, "Error abriendo actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onEditarClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN EDITAR ===");
                    try {
                        // Abrir pantalla de edición (usando actividad de prueba)
                        Intent intent = new Intent(GestionActividadesActivity.this, CrearActividadActivityTest.class);
                        intent.putExtra("actividad_id", actividad.getId_actividad());
                        intent.putExtra("modo_edicion", true);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error iniciando CrearActividadActivity", e);
                        Toast.makeText(GestionActividadesActivity.this, "Error abriendo edición: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onEliminarClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN ELIMINAR ===");
                    eliminarActividad(actividad);
                }

                @Override
                public void onAsignarAulasClick(Actividad actividad) {
                    Log.d(TAG, "=== CLICK EN ASIGNAR AULAS ===");
                    try {
                        // Abrir pantalla de asignación de aulas
                        Intent intent = new Intent(GestionActividadesActivity.this, AsignarAulasActivity.class);
                        intent.putExtra("actividad_id", actividad.getId_actividad());
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e(TAG, "Error iniciando AsignarAulasActivity", e);
                        Toast.makeText(GestionActividadesActivity.this, "Error abriendo asignación: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            
            Log.d(TAG, "Adapter creado correctamente");
            
            Log.d(TAG, "Configurando LayoutManager...");
            recyclerViewActividades.setLayoutManager(new LinearLayoutManager(this));
            Log.d(TAG, "LayoutManager configurado");
            
            Log.d(TAG, "Configurando Adapter...");
            recyclerViewActividades.setAdapter(adapter);
            Log.d(TAG, "Adapter configurado");
            
            Log.d(TAG, "RecyclerView configurado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error configurando RecyclerView", e);
            Toast.makeText(this, "Error configurando RecyclerView: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void configurarListeners() {
        Log.d(TAG, "Configurando listeners...");
        
        if (btnCrearActividad != null) {
            Log.d(TAG, "btnCrearActividad encontrado, configurando listener...");
            Log.d(TAG, "btnCrearActividad visible: " + (btnCrearActividad.getVisibility() == View.VISIBLE ? "SÍ" : "NO"));
            Log.d(TAG, "btnCrearActividad enabled: " + btnCrearActividad.isEnabled());
            
            btnCrearActividad.setOnClickListener(v -> {
                Log.d(TAG, "=== CLICK EN CREAR ACTIVIDAD ===");
                Log.d(TAG, "Botón presionado: " + v.getId());
                try {
                    Intent intent = new Intent(this, CrearActividadActivity.class);
                    Log.d(TAG, "Intent creado, iniciando CrearActividadActivity...");
                    startActivity(intent);
                    Log.d(TAG, "CrearActividadActivity iniciada correctamente");
                } catch (Exception e) {
                    Log.e(TAG, "Error iniciando CrearActividadActivity", e);
                    Toast.makeText(this, "Error abriendo crear actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            Log.d(TAG, "Listener de btnCrearActividad configurado");
        } else {
            Log.e(TAG, "Error: btnCrearActividad es null");
            Toast.makeText(this, "Error: Botón crear actividad no inicializado", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void cargarActividades() {
        Log.d(TAG, "Iniciando carga de actividades...");
        
        try {
            mostrarCargando(true);
            
            Log.d(TAG, "Llamando a actividadesRepository.getActividadesDocente()...");
            actividadesRepository.getActividadesDocente(new ActividadesRepository.ActividadesCallback<List<Actividad>>() {
                @Override
                public void onSuccess(List<Actividad> actividades) {
                    Log.d(TAG, "onSuccess: Recibidas " + (actividades != null ? actividades.size() : 0) + " actividades");
                    runOnUiThread(() -> {
                        try {
                            mostrarCargando(false);
                            actualizarDatosActividades(actividades);
                            Log.d(TAG, "Datos de actividades actualizados correctamente");
                        } catch (Exception e) {
                            Log.e(TAG, "Error actualizando datos de actividades", e);
                            Toast.makeText(GestionActividadesActivity.this, "Error actualizando datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    Log.e(TAG, "onError: " + message);
                    runOnUiThread(() -> {
                        try {
                            mostrarCargando(false);
                            Toast.makeText(GestionActividadesActivity.this, "Error al cargar actividades: " + message, Toast.LENGTH_LONG).show();
                            mostrarMensajeSinActividades();
                        } catch (Exception e) {
                            Log.e(TAG, "Error manejando error de carga", e);
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Error en cargarActividades", e);
            runOnUiThread(() -> {
                mostrarCargando(false);
                Toast.makeText(this, "Error crítico: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            });
        }
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
            
            // Configurar RecyclerView solo si no está configurado y tenemos datos
            if (adapter == null && actividades != null && !actividades.isEmpty()) {
                Log.d(TAG, "Configurando RecyclerView por primera vez...");
                configurarRecyclerView();
            } else if (adapter != null) {
                adapter.notifyDataSetChanged();
                Log.d(TAG, "Adapter notificado correctamente");
            } else {
                Log.e(TAG, "Error: adapter es null");
            }
            
            actualizarContadorActividades();
            
            if (actividades == null || actividades.isEmpty()) {
                Log.d(TAG, "No hay actividades, mostrando mensaje sin actividades");
                mostrarMensajeSinActividades();
            } else {
                Log.d(TAG, "Hay " + actividades.size() + " actividades, ocultando mensaje sin actividades");
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
            textContadorActividades.setText("Total: " + total + " actividades");
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
        Log.d(TAG, "=== MOSTRANDO MENSAJE SIN ACTIVIDADES ===");
        if (layoutSinActividades != null) {
            layoutSinActividades.setVisibility(View.VISIBLE);
            Log.d(TAG, "layoutSinActividades visible");
        }
        if (recyclerViewActividades != null) {
            recyclerViewActividades.setVisibility(View.GONE);
            Log.d(TAG, "recyclerViewActividades oculto");
        }
        if (textSinActividades != null) {
            textSinActividades.setText("No tienes actividades creadas.\n¡Crea tu primera actividad!");
            Log.d(TAG, "textSinActividades configurado");
        }
        
        // Verificar estado del botón crear actividad
        if (btnCrearActividad != null) {
            Log.d(TAG, "btnCrearActividad en mostrarMensajeSinActividades - visible: " + (btnCrearActividad.getVisibility() == View.VISIBLE ? "SÍ" : "NO"));
            Log.d(TAG, "btnCrearActividad en mostrarMensajeSinActividades - enabled: " + btnCrearActividad.isEnabled());
        }
    }

    private void ocultarMensajeSinActividades() {
        Log.d(TAG, "=== OCULTANDO MENSAJE SIN ACTIVIDADES ===");
        if (layoutSinActividades != null) {
            layoutSinActividades.setVisibility(View.GONE);
            Log.d(TAG, "layoutSinActividades oculto");
        }
        if (recyclerViewActividades != null) {
            recyclerViewActividades.setVisibility(View.VISIBLE);
            Log.d(TAG, "recyclerViewActividades visible");
        }
        
        // Verificar estado del botón crear actividad
        if (btnCrearActividad != null) {
            Log.d(TAG, "btnCrearActividad en ocultarMensajeSinActividades - visible: " + (btnCrearActividad.getVisibility() == View.VISIBLE ? "SÍ" : "NO"));
            Log.d(TAG, "btnCrearActividad en ocultarMensajeSinActividades - enabled: " + btnCrearActividad.isEnabled());
        }
    }

    private void eliminarActividad(Actividad actividad) {
        // Mostrar diálogo de confirmación
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Eliminar Actividad")
                .setMessage("¿Estás seguro de que quieres eliminar la actividad \"" + actividad.getDescripcion() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    actividadesRepository.eliminarActividad(actividad.getId_actividad(), new ActividadesRepository.ActividadesCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            runOnUiThread(() -> {
                                Toast.makeText(GestionActividadesActivity.this, "Actividad eliminada exitosamente", Toast.LENGTH_SHORT).show();
                                cargarActividades(); // Recargar la lista
                            });
                        }

                        @Override
                        public void onError(String message) {
                            runOnUiThread(() -> {
                                Toast.makeText(GestionActividadesActivity.this, "Error al eliminar actividad: " + message, Toast.LENGTH_LONG).show();
                            });
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar actividades cuando se regrese de otras pantallas
        // cargarActividades(); // Comentado temporalmente para evitar llamada doble
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
}
