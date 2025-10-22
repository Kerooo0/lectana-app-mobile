package com.example.lectana;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorActividadesPorCuento;
import com.example.lectana.adaptadores.AdaptadorCuentosDetallados;
import com.example.lectana.adaptadores.AdaptadorEstudiantesAula;
import com.example.lectana.adaptadores.AdaptadorProgresoEstudiantes;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.ModeloActividadDetallada;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.modelos.ModeloCuentoConActividades;
import com.example.lectana.modelos.ModeloCuentoDetallado;
import com.example.lectana.modelos.ModeloEstudianteAula;
import com.example.lectana.modelos.ModeloProgresoEstudiante;
import com.example.lectana.repository.AulasRepository;

import java.util.ArrayList;
import java.util.List;

public class VisualizarAulaActivity extends AppCompatActivity {

    private static final String TAG = "VisualizarAula";

    // Componentes de la Interfaz
    private ImageView botonVolver;
    private TextView textoNombreAula;
    private TextView textoCodigoAula;
    private ImageView botonConfiguracionAula;
    private ProgressBar progressBar;
    
    // Estadísticas
    private TextView numeroEstudiantesAula;
    private TextView numeroCuentosAula;
    private TextView numeroActividadesAula;
    
    // Pestañas
    private Button botonPestanaEstudiantes;
    private Button botonPestanaCuentos;
    private Button botonPestanaActividades;
    private Button botonPestanaProgreso;
    
    // RecyclerView
    private RecyclerView recyclerViewContenido;
    private View estadoVacio;
    private TextView textoEstadoVacio;
    private Button botonAccionEstadoVacio;
    private View barraAccionesCuentos;
    private Button botonAgregarCuento;
    private Button botonGestionarCuentos;
    
    // Adaptadores
    private AdaptadorEstudiantesAula adaptadorEstudiantes;
    private AdaptadorCuentosDetallados adaptadorCuentosDetallados;
    private AdaptadorActividadesPorCuento adaptadorActividadesPorCuento;
    private AdaptadorProgresoEstudiantes adaptadorProgresoEstudiantes;
    
    // Datos
    private List<ModeloEstudianteAula> listaEstudiantes;
    private List<ModeloCuentoDetallado> listaCuentosDetallados;
    private List<ModeloCuentoConActividades> listaCuentosConActividades;
    private List<ModeloProgresoEstudiante> listaProgresoEstudiantes;
    
    // Estado actual de la pestaña
    private String pestanaActual = "estudiantes";
    
    // Repositorio y sesión
    private AulasRepository aulasRepository;
    private SessionManager sessionManager;
    private ModeloAula aulaActual;
    private int aulaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_aula);

        // Inicializar repositorio y sesión
        sessionManager = new SessionManager(this);
        aulasRepository = new AulasRepository(sessionManager);
        
        // Obtener ID del aula desde el Intent
        Intent intent = getIntent();
        aulaId = intent.getIntExtra("aula_id", -1);
        
        if (aulaId == -1) {
            Toast.makeText(this, "Error: ID de aula no válido", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        inicializarVistas();
        configurarListeners();
        cargarDatosAula();
        // mostrarPestanaEstudiantes() se llamará después de cargar los datos
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        textoNombreAula = findViewById(R.id.texto_nombre_aula);
        textoCodigoAula = findViewById(R.id.texto_codigo_aula);
        botonConfiguracionAula = findViewById(R.id.boton_configuracion_aula);
        progressBar = findViewById(R.id.progress_bar);
        
        numeroEstudiantesAula = findViewById(R.id.numero_estudiantes_aula);
        numeroCuentosAula = findViewById(R.id.numero_cuentos_aula);
        numeroActividadesAula = findViewById(R.id.numero_actividades_aula);
        
        botonPestanaEstudiantes = findViewById(R.id.boton_pestana_estudiantes);
        botonPestanaCuentos = findViewById(R.id.boton_pestana_cuentos);
        botonPestanaActividades = findViewById(R.id.boton_pestana_actividades);
        botonPestanaProgreso = findViewById(R.id.boton_pestana_progreso);
        
        recyclerViewContenido = findViewById(R.id.recycler_view_contenido);
        recyclerViewContenido.setLayoutManager(new LinearLayoutManager(this));
        estadoVacio = findViewById(R.id.estado_vacio_contenido);
        textoEstadoVacio = findViewById(R.id.texto_estado_vacio);
        botonAccionEstadoVacio = findViewById(R.id.boton_accion_estado_vacio);
        barraAccionesCuentos = findViewById(R.id.barra_acciones_cuentos);
        botonAgregarCuento = findViewById(R.id.boton_agregar_cuento);
        botonGestionarCuentos = findViewById(R.id.boton_gestionar_cuentos);
        
        // Inicializar listas
        listaEstudiantes = new ArrayList<>();
        listaCuentosDetallados = new ArrayList<>();
        listaCuentosConActividades = new ArrayList<>();
        listaProgresoEstudiantes = new ArrayList<>();
        
        // Inicializar adaptadores
        adaptadorEstudiantes = new AdaptadorEstudiantesAula(listaEstudiantes);
        adaptadorEstudiantes.setOnClickListenerEstudiante(new AdaptadorEstudiantesAula.OnClickListenerEstudiante() {
            @Override
            public void onClicEstudiante(ModeloEstudianteAula estudiante) {
                irADetalleEstudiante(estudiante);
            }

            @Override
            public void onClicQuitarEstudiante(ModeloEstudianteAula estudiante) {
                mostrarDialogoConfirmacionRemoverEstudiante(estudiante);
            }
        });
        adaptadorCuentosDetallados = new AdaptadorCuentosDetallados(listaCuentosDetallados, new AdaptadorCuentosDetallados.OnCuentoClickListener() {
            @Override
            public void onClickCuento(ModeloCuentoDetallado cuento) {
                Intent intent = new Intent(VisualizarAulaActivity.this, DetalleCuentoActivity.class);
                
                // Obtener el id_cuento real del backend
                int idCuentoReal = obtenerIdCuentoDirecto(cuento);
                
                if (idCuentoReal > 0) {
                    // Usar el ID real del backend
                    intent.putExtra("cuento_id", idCuentoReal);
                } else {
                    // Fallback: pasar datos básicos
                    intent.putExtra("cuento_id", 0);
                    intent.putExtra("cuento_titulo", cuento.getTitulo());
                    intent.putExtra("cuento_autor", cuento.getSubtitulo());
                }
                
                // Pasar modo "explorar" para ocultar el botón "Seleccionar Cuento"
                intent.putExtra("modo", "explorar");
                
                startActivity(intent);
            }

            @Override
            public void onQuitarCuento(ModeloCuentoDetallado cuento) {
                new android.app.AlertDialog.Builder(VisualizarAulaActivity.this)
                    .setTitle("Quitar cuento")
                    .setMessage("¿Estás seguro que deseas quitar este cuento del aula?")
                    .setPositiveButton("Quitar", (d, w) -> {
                        // Usar directamente id_cuento del backend
                        int idCuento = obtenerIdCuentoDirecto(cuento);
                        Log.d(TAG, "Quitar cuento -> titulo=" + cuento.getTitulo() + ", id_cuento=" + idCuento);
                        
                        if (idCuento <= 0) {
                            Toast.makeText(VisualizarAulaActivity.this, "Error: ID de cuento no válido", Toast.LENGTH_LONG).show();
                            return;
                        }
                        
                        mostrarCargando(true);
                        aulasRepository.quitarCuentoAula(aulaId, idCuento, new com.example.lectana.repository.AulasRepository.AulasCallback<com.example.lectana.modelos.AsignarCuentosResponse>() {
                            @Override
                            public void onSuccess(com.example.lectana.modelos.AsignarCuentosResponse response) {
                                runOnUiThread(() -> {
                                    mostrarCargando(false);
                                    Toast.makeText(VisualizarAulaActivity.this, "Cuento quitado del aula", Toast.LENGTH_SHORT).show();
                                    // Refrescar datos para actualizar contadores
                                    cargarDatosAula();
                                });
                            }

                            @Override
                            public void onError(String message) {
                                runOnUiThread(() -> {
                                    mostrarCargando(false);
                                    Toast.makeText(VisualizarAulaActivity.this, "Error al quitar cuento: " + message, Toast.LENGTH_LONG).show();
                                    // Refrescar datos para sincronizar estado
                                    cargarDatosAula();
                                });
                            }
                        });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
            }
        });
        adaptadorActividadesPorCuento = new AdaptadorActividadesPorCuento(listaCuentosConActividades);
        adaptadorProgresoEstudiantes = new AdaptadorProgresoEstudiantes(listaProgresoEstudiantes);
    }

    private void configurarListeners() {
        // Botón Volver
        botonVolver.setOnClickListener(v -> finish());
        
        // Botón Configuración
        botonConfiguracionAula.setOnClickListener(v -> {
            // Mostrar opciones de configuración
            mostrarOpcionesConfiguracion();
        });
        
        // Pestañas
        botonPestanaEstudiantes.setOnClickListener(v -> mostrarPestanaEstudiantes());
        botonPestanaCuentos.setOnClickListener(v -> mostrarPestanaCuentos());
        botonPestanaActividades.setOnClickListener(v -> mostrarPestanaActividades());
        botonPestanaProgreso.setOnClickListener(v -> mostrarPestanaProgreso());
    }

    private void cargarDatosAula() {
        mostrarCargando(true);
        
        // Log temporal para mostrar el JWT actual
        String token = sessionManager.getToken();
        Log.d(TAG, "JWT actual: " + (token != null ? token : "null"));
        
        aulasRepository.getAulaDetalle(aulaId, new AulasRepository.AulasCallback<ModeloAula>() {
            @Override
            public void onSuccess(ModeloAula aula) {
                runOnUiThread(() -> {
                    Log.d(TAG, "=== DATOS DEL AULA OBTENIDOS ===");
                    Log.d(TAG, "aula.getEstudiantes(): " + (aula.getEstudiantes() != null ? "No nulo" : "NULO"));
                    if (aula.getEstudiantes() != null) {
                        Log.d(TAG, "Cantidad de estudiantes en respuesta: " + aula.getEstudiantes().size());
                    }
                    mostrarCargando(false);
                    aulaActual = aula;
                    actualizarInterfazConDatosReales();
                    
                    // Mostrar la pestaña de estudiantes después de cargar los datos
                    mostrarPestanaEstudiantes();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(VisualizarAulaActivity.this, "Error al cargar aula: " + message, Toast.LENGTH_LONG).show();
                    // Cargar datos de ejemplo como fallback
                    cargarDatosEjemplo();
                });
            }
        });
    }
    
    private void actualizarInterfazConDatosReales() {
        if (aulaActual == null) return;
        
        // Actualizar información básica del aula
        textoNombreAula.setText(aulaActual.getNombre_aula());
        textoCodigoAula.setText("Código: " + aulaActual.getCodigo_acceso());
        
        // Actualizar estadísticas
        numeroEstudiantesAula.setText(String.valueOf(aulaActual.getTotal_estudiantes()));
        numeroCuentosAula.setText(String.valueOf(aulaActual.getTotal_cuentos()));
        
        // Convertir estudiantes de la API a modelo local
        convertirEstudiantesApiAModelo();
        
        // Convertir cuentos de la API a modelo local
        convertirCuentosApiAModelo();
        
        // Cargar datos de progreso (vacío por ahora)
        cargarDatosProgresoEstudiantes();
        
        // Actualizar número de actividades (vacío por ahora)
        numeroActividadesAula.setText("0");
    }
    
    private void convertirEstudiantesApiAModelo() {
        listaEstudiantes.clear();
        Log.d(TAG, "=== CONVERTIR ESTUDIANTES API A MODELO ===");
        Log.d(TAG, "aulaActual: " + (aulaActual != null ? "No nulo" : "NULO"));
        Log.d(TAG, "aulaActual.getEstudiantes(): " + (aulaActual != null && aulaActual.getEstudiantes() != null ? "No nulo" : "NULO"));
        
        if (aulaActual != null && aulaActual.getEstudiantes() != null) {
            Log.d(TAG, "Cantidad de estudiantes en API: " + aulaActual.getEstudiantes().size());
            
            for (ModeloAula.Estudiante estudianteApi : aulaActual.getEstudiantes()) {
                Log.d(TAG, "Procesando estudiante API - ID: " + estudianteApi.getId());
                
                // Convertir datos de la API al modelo local
                String nombreCompleto = "Estudiante";
                if (estudianteApi.getUsuario() != null) {
                    String nombre = estudianteApi.getUsuario().getNombre() != null ? estudianteApi.getUsuario().getNombre() : "";
                    String apellido = estudianteApi.getUsuario().getApellido() != null ? estudianteApi.getUsuario().getApellido() : "";
                    nombreCompleto = nombre + " " + apellido;
                    Log.d(TAG, "Nombre completo: " + nombreCompleto);
                } else {
                    Log.d(TAG, "Usuario del estudiante es NULO");
                }
                
                ModeloEstudianteAula estudiante = new ModeloEstudianteAula(
                    String.valueOf(estudianteApi.getId()),
                    nombreCompleto,
                    "Última actividad: Hace 1 hora", // Por ahora usar valor por defecto
                    75, // Por ahora usar valor por defecto
                    true // Por ahora usar valor por defecto
                );
                listaEstudiantes.add(estudiante);
                Log.d(TAG, "Estudiante agregado a lista local: " + estudiante.getNombre());
            }
        } else {
            Log.d(TAG, "No hay estudiantes para procesar");
        }
        
        Log.d(TAG, "Cantidad final en listaEstudiantes: " + listaEstudiantes.size());
    }
    
    private void convertirCuentosApiAModelo() {
        listaCuentosDetallados.clear();
        if (aulaActual.getCuentos() != null) {
            for (CuentoApi cuentoApi : aulaActual.getCuentos()) {
                Log.d(TAG, "API cuento -> titulo='" + (cuentoApi.getTitulo() != null ? cuentoApi.getTitulo() : "") + "', id_cuento=" + cuentoApi.getId_cuento());
                
                // Convertir datos de la API al modelo local
                String autor = "Autor desconocido";
                if (cuentoApi.getAutor() != null) {
                    String nombre = cuentoApi.getAutor().getNombre() != null ? cuentoApi.getAutor().getNombre() : "";
                    String apellido = cuentoApi.getAutor().getApellido() != null ? cuentoApi.getAutor().getApellido() : "";
                    autor = nombre + " " + apellido;
                }
                
                ModeloCuentoDetallado cuento = new ModeloCuentoDetallado(
                    String.valueOf(cuentoApi.getId_cuento()),
                    cuentoApi.getTitulo(),
                    autor,
                    cuentoApi.getUrl_img() != null ? cuentoApi.getUrl_img() : "",
                    15, // Por ahora usar valor por defecto para páginas
                    "Comprensión",
                    2, // Por ahora usar valor por defecto
                    "Vocabulario",
                    3 // Por ahora usar valor por defecto
                );
                listaCuentosDetallados.add(cuento);
            }
            Log.d(TAG, "convertirCuentosApiAModelo: cargados " + listaCuentosDetallados.size() + " cuentos");
        }
    }
    
    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        if (recyclerViewContenido != null) {
            recyclerViewContenido.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        }
    }
    
    private void cargarDatosEjemplo() {
        // Datos del aula (recibidos del Intent)
        Intent intent = getIntent();
        String nombreAula = intent.getStringExtra("nombre_aula");
        String codigoAula = intent.getStringExtra("codigo_aula");
        
        if (nombreAula != null) {
            textoNombreAula.setText(nombreAula);
        }
        if (codigoAula != null) {
            textoCodigoAula.setText("Código: " + codigoAula);
        }
        
        // Cargar estudiantes de ejemplo
        listaEstudiantes.clear();
        listaEstudiantes.add(new ModeloEstudianteAula("1", "Ana García", "Hace 2 min", 85, true));
        listaEstudiantes.add(new ModeloEstudianteAula("2", "Carlos López", "Hace 1 hora", 72, false));
        listaEstudiantes.add(new ModeloEstudianteAula("3", "María Rodríguez", "Activo ahora", 91, true));
        listaEstudiantes.add(new ModeloEstudianteAula("4", "Juan Pérez", "Hace 2 días", 45, false));
        listaEstudiantes.add(new ModeloEstudianteAula("5", "Sofía Martínez", "Hace 30 min", 68, true));
        
        // Cargar cuentos detallados de ejemplo (sin datos hardcodeados)
        listaCuentosDetallados.clear();
        // No agregar datos hardcodeados de cuentos
        
        // Cargar actividades organizadas por cuento (vacío por ahora)
        listaCuentosConActividades.clear();
        // No agregar datos hardcodeados de actividades
        
        // Cargar datos de progreso de estudiantes
        cargarDatosProgresoEstudiantes();
        
        // Actualizar estadísticas
        numeroEstudiantesAula.setText(String.valueOf(listaEstudiantes.size()));
        numeroCuentosAula.setText(String.valueOf(listaCuentosDetallados.size()));
        numeroActividadesAula.setText("0"); // Sin actividades por ahora
    }

    private void cargarDatosProgresoEstudiantes() {
        // Datos de progreso vacíos por ahora - no hay datos reales disponibles
        listaProgresoEstudiantes.clear();
        // No agregar datos hardcodeados
    }

    private void mostrarPestanaEstudiantes() {
        pestanaActual = "estudiantes";
        actualizarBotonesPestanas();
        if (barraAccionesCuentos != null) barraAccionesCuentos.setVisibility(View.GONE);
        
        // Mostrar barra de acciones para estudiantes
        mostrarBarraAccionesEstudiantes();
        
        Log.d(TAG, "=== MOSTRAR PESTANA ESTUDIANTES ===");
        Log.d(TAG, "listaEstudiantes: " + (listaEstudiantes != null ? "No nulo" : "NULO"));
        Log.d(TAG, "listaEstudiantes.isEmpty(): " + (listaEstudiantes != null ? listaEstudiantes.isEmpty() : "NULO"));
        Log.d(TAG, "listaEstudiantes.size(): " + (listaEstudiantes != null ? listaEstudiantes.size() : "NULO"));
        
        // Verificar si hay datos del aula pero la lista local está vacía
        boolean hayEstudiantesEnAula = aulaActual != null && aulaActual.getEstudiantes() != null && !aulaActual.getEstudiantes().isEmpty();
        boolean listaLocalVacia = listaEstudiantes == null || listaEstudiantes.isEmpty();
        
        Log.d(TAG, "hayEstudiantesEnAula: " + hayEstudiantesEnAula);
        Log.d(TAG, "listaLocalVacia: " + listaLocalVacia);
        
        if (listaLocalVacia && hayEstudiantesEnAula) {
            // Hay estudiantes en el aula pero la lista local está vacía - reconvertir
            Log.d(TAG, "Reconvirtiendo estudiantes de la API");
            convertirEstudiantesApiAModelo();
        }
        
        if (listaEstudiantes == null || listaEstudiantes.isEmpty()) {
            Log.d(TAG, "Mostrando estado vacío - No hay estudiantes");
            mostrarEstadoVacio(true);
            textoEstadoVacio.setText("Por ahora no hay alumnos");
            botonAccionEstadoVacio.setText("Compartir código del aula");
            botonAccionEstadoVacio.setVisibility(View.VISIBLE);
            botonAccionEstadoVacio.setOnClickListener(v -> compartirCodigoAula());
        } else {
            Log.d(TAG, "Mostrando lista de estudiantes - Cantidad: " + listaEstudiantes.size());
            mostrarEstadoVacio(false);
            Log.d(TAG, "Configurando adaptador de estudiantes");
            recyclerViewContenido.setAdapter(adaptadorEstudiantes);
            adaptadorEstudiantes.notifyDataSetChanged();
            Log.d(TAG, "Adaptador configurado y notificado");
        }
    }

    private void mostrarBarraAccionesEstudiantes() {
        // Usar la misma barra de acciones que los cuentos pero con botones diferentes
        if (barraAccionesCuentos != null) {
            barraAccionesCuentos.setVisibility(View.VISIBLE);
            
            // Cambiar texto de los botones para estudiantes
            botonAgregarCuento.setText("Compartir Código");
            botonGestionarCuentos.setText("Gestionar Estudiantes");
            
            // Configurar listeners para estudiantes
            botonAgregarCuento.setOnClickListener(v -> compartirCodigoAula());
            botonGestionarCuentos.setOnClickListener(v -> mostrarOpcionesGestionEstudiantes());
        }
    }

    private void mostrarPestanaCuentos() {
        pestanaActual = "cuentos";
        actualizarBotonesPestanas();
        barraAccionesCuentos.setVisibility(View.VISIBLE);
        
        // Restaurar botones para cuentos
        botonAgregarCuento.setText("Agregar Cuento");
        botonGestionarCuentos.setText("Gestionar Cuentos");
        
        botonAgregarCuento.setOnClickListener(v -> {
            // Ir a seleccionar cuentos públicos
            Intent intento = new Intent(VisualizarAulaActivity.this, SeleccionarCuentosActivity.class);
            intento.putExtra("aula_id", aulaId);
            intento.putExtra("modo", "gestionar");
            
            // Pasar datos del aula para mostrar en la UI
            if (aulaActual != null) {
                intento.putExtra("nombre_aula", aulaActual.getNombre_aula());
                intento.putExtra("codigo_acceso", aulaActual.getCodigo_acceso());
                
                // Extraer grado del nombre del aula
                String nombreCompleto = aulaActual.getNombre_aula();
                String grado = "";
                if (nombreCompleto != null && nombreCompleto.contains("°")) {
                    grado = nombreCompleto.split("°")[0] + "°";
                }
                intento.putExtra("grado", grado);
            }
            
            startActivityForResult(intento, 2);
        });
        
        botonGestionarCuentos.setOnClickListener(v -> {
            // Ir a gestionar cuentos del aula
            Intent intento = new Intent(VisualizarAulaActivity.this, GestionarCuentosAulaActivity.class);
            intento.putExtra("aula_id", aulaId);
            intento.putExtra("nombre_aula", aulaActual != null ? aulaActual.getNombre_aula() : "");
            startActivityForResult(intento, 3);
        });

        if (listaCuentosDetallados == null || listaCuentosDetallados.isEmpty()) {
            mostrarEstadoVacio(true);
            textoEstadoVacio.setText("No hay cuentos asignados");
            botonAccionEstadoVacio.setText("Gestionar cuentos");
            botonAccionEstadoVacio.setVisibility(View.VISIBLE);
            botonAccionEstadoVacio.setOnClickListener(v -> mostrarOpcionesConfiguracion());
        } else {
            mostrarEstadoVacio(false);
            recyclerViewContenido.setAdapter(adaptadorCuentosDetallados);
            adaptadorCuentosDetallados.notifyDataSetChanged();
        }
    }

    private void mostrarPestanaActividades() {
        pestanaActual = "actividades";
        actualizarBotonesPestanas();
        if (barraAccionesCuentos != null) barraAccionesCuentos.setVisibility(View.GONE);
        
        // Cargar actividades del aula desde el backend
        cargarActividadesAula();
    }
    
    private void cargarActividadesAula() {
        mostrarCargando(true);
        
        com.example.lectana.repository.ActividadesRepository actividadesRepository = 
            new com.example.lectana.repository.ActividadesRepository(sessionManager);
        
        actividadesRepository.getActividadesAula(aulaId, new com.example.lectana.repository.ActividadesRepository.ActividadesCallback<java.util.List<com.example.lectana.modelos.Actividad>>() {
            @Override
            public void onSuccess(java.util.List<com.example.lectana.modelos.Actividad> actividades) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    
                    if (actividades == null || actividades.isEmpty()) {
                        // No hay actividades, mostrar estado vacío
                        mostrarEstadoVacio(true);
                        textoEstadoVacio.setText("No hay actividades asignadas a esta aula");
                        botonAccionEstadoVacio.setText("Crear nueva actividad");
                        botonAccionEstadoVacio.setVisibility(View.VISIBLE);
                        botonAccionEstadoVacio.setOnClickListener(v -> {
                            Intent intento = new Intent(VisualizarAulaActivity.this, com.example.lectana.docente.CrearActividadActivity.class);
                            intento.putExtra("aula_id", aulaId);
                            startActivity(intento);
                        });
                    } else {
                        // Hay actividades, convertirlas al modelo local y mostrarlas
                        convertirActividadesApiAModelo(actividades);
                        mostrarEstadoVacio(false);
                        recyclerViewContenido.setAdapter(adaptadorActividadesPorCuento);
                        adaptadorActividadesPorCuento.notifyDataSetChanged();
                        
                        // Actualizar contador
                        numeroActividadesAula.setText(String.valueOf(actividades.size()));
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(VisualizarAulaActivity.this, "Error al cargar actividades: " + message, Toast.LENGTH_LONG).show();
                    
                    // Mostrar estado vacío con opción de crear
                    mostrarEstadoVacio(true);
                    textoEstadoVacio.setText("Error al cargar actividades");
                    botonAccionEstadoVacio.setText("Crear nueva actividad");
                    botonAccionEstadoVacio.setVisibility(View.VISIBLE);
                    botonAccionEstadoVacio.setOnClickListener(v -> {
                        Intent intento = new Intent(VisualizarAulaActivity.this, com.example.lectana.docente.CrearActividadActivity.class);
                        intento.putExtra("aula_id", aulaId);
                        startActivity(intento);
                    });
                });
            }
        });
    }
    
    private void convertirActividadesApiAModelo(java.util.List<com.example.lectana.modelos.Actividad> actividades) {
        listaCuentosConActividades.clear();
        
        // Agrupar actividades por cuento
        java.util.Map<Integer, java.util.List<com.example.lectana.modelos.Actividad>> actividadesPorCuento = new java.util.HashMap<>();
        
        for (com.example.lectana.modelos.Actividad actividad : actividades) {
            int cuentoId = actividad.getCuento_id_cuento();
            
            if (!actividadesPorCuento.containsKey(cuentoId)) {
                actividadesPorCuento.put(cuentoId, new java.util.ArrayList<>());
            }
            
            actividadesPorCuento.get(cuentoId).add(actividad);
        }
        
        // Crear ModeloCuentoConActividades para cada cuento
        for (java.util.Map.Entry<Integer, java.util.List<com.example.lectana.modelos.Actividad>> entry : actividadesPorCuento.entrySet()) {
            int cuentoId = entry.getKey();
            java.util.List<com.example.lectana.modelos.Actividad> actividadesCuento = entry.getValue();
            
            // Buscar el cuento en la lista de cuentos del aula
            String tituloCuento = "Cuento ID " + cuentoId;
            if (aulaActual != null && aulaActual.getCuentos() != null) {
                for (CuentoApi cuento : aulaActual.getCuentos()) {
                    if (cuento.getId_cuento() == cuentoId) {
                        tituloCuento = cuento.getTitulo();
                        break;
                    }
                }
            }
            
            // Crear lista de ModeloActividadDetallada a partir de las actividades
            java.util.List<ModeloActividadDetallada> actividadesDetalladas = new java.util.ArrayList<>();
            for (int i = 0; i < actividadesCuento.size(); i += 2) {
                com.example.lectana.modelos.Actividad act1 = actividadesCuento.get(i);
                com.example.lectana.modelos.Actividad act2 = (i + 1 < actividadesCuento.size()) ? actividadesCuento.get(i + 1) : null;
                
                ModeloActividadDetallada actividadDetallada = new ModeloActividadDetallada(
                    String.valueOf(act1.getId_actividad()),
                    tituloCuento,
                    act1.getDescripcion(),
                    act1.getTipoDisplay(),
                    0, // Por ahora usar valor por defecto para estudiantes
                    "Sin completar",
                    act2 != null ? act2.getDescripcion() : "",
                    act2 != null ? act2.getTipoDisplay() : "",
                    0, // Por ahora usar valor por defecto para estudiantes
                    act2 != null ? "Sin completar" : ""
                );
                
                actividadesDetalladas.add(actividadDetallada);
            }
            
            ModeloCuentoConActividades cuentoConActividades = new ModeloCuentoConActividades(
                String.valueOf(cuentoId),
                tituloCuento,
                actividadesCuento.size(),
                actividadesDetalladas
            );
            
            listaCuentosConActividades.add(cuentoConActividades);
        }
    }

    private void mostrarPestanaProgreso() {
        pestanaActual = "progreso";
        actualizarBotonesPestanas();
        if (barraAccionesCuentos != null) barraAccionesCuentos.setVisibility(View.GONE);
        mostrarEstadoVacio(true);
        textoEstadoVacio.setText("PRÓXIMAMENTE");
        botonAccionEstadoVacio.setVisibility(View.GONE);
    }

    private void mostrarEstadoVacio(boolean mostrar) {
        if (estadoVacio != null) {
            estadoVacio.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        recyclerViewContenido.setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    private void actualizarBotonesPestanas() {
        // Resetear todos los botones
        botonPestanaEstudiantes.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        botonPestanaEstudiantes.setTextColor(getResources().getColor(R.color.gris_medio));
        
        botonPestanaCuentos.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        botonPestanaCuentos.setTextColor(getResources().getColor(R.color.gris_medio));
        
        botonPestanaActividades.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        botonPestanaActividades.setTextColor(getResources().getColor(R.color.gris_medio));
        
        botonPestanaProgreso.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        botonPestanaProgreso.setTextColor(getResources().getColor(R.color.gris_medio));
        
        // Activar el botón correspondiente
        switch (pestanaActual) {
            case "estudiantes":
                botonPestanaEstudiantes.setBackgroundResource(R.drawable.boton_pestana_activa);
                botonPestanaEstudiantes.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
            case "cuentos":
                botonPestanaCuentos.setBackgroundResource(R.drawable.boton_pestana_activa);
                botonPestanaCuentos.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
            case "actividades":
                botonPestanaActividades.setBackgroundResource(R.drawable.boton_pestana_activa);
                botonPestanaActividades.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
            case "progreso":
                botonPestanaProgreso.setBackgroundResource(R.drawable.boton_pestana_activa);
                botonPestanaProgreso.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Actualizar los datos del aula con los cambios guardados
            String nombreActualizado = data.getStringExtra("nombre_aula_actualizado");
            String gradoActualizado = data.getStringExtra("grado_aula_actualizado");

            if (nombreActualizado != null) {
                textoNombreAula.setText(nombreActualizado);
            }
            
            if (gradoActualizado != null) {
                // Actualizar el código del aula si es necesario
                // Por ahora solo mostramos un mensaje de éxito
                android.widget.Toast.makeText(this, 
                    "¡Información del aula actualizada!", 
                    android.widget.Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            boolean cuentosActualizados = data.getBooleanExtra("cuentos_actualizados", false);
            if (cuentosActualizados) {
                // Mostrar mensaje de actualización
                Toast.makeText(this, "Actualizando lista de cuentos...", Toast.LENGTH_SHORT).show();
                
                // Pequeño delay para asegurar que el backend esté sincronizado
                new android.os.Handler().postDelayed(() -> {
                    cargarDatosAula();
                    mostrarPestanaCuentos();
                }, 500); // 500ms delay
            }
        }
    }

    private void irADetalleEstudiante(ModeloEstudianteAula estudiante) {
        Intent intent = new Intent(this, DetalleEstudianteActivity.class);
        intent.putExtra("estudiante_id", Integer.parseInt(estudiante.getId()));
        intent.putExtra("aula_id", aulaId);
        intent.putExtra("nombre_estudiante", estudiante.getNombre());
        intent.putExtra("ultima_actividad", estudiante.getUltimaActividad());
        intent.putExtra("progreso", estudiante.getProgreso());
        intent.putExtra("activo", estudiante.isActivo());
        startActivity(intent);
    }

    private void compartirCodigoAula() {
        String codigoAula = aulaActual != null ? aulaActual.getCodigo_acceso() : "N/A";
        String mensajeCompartir = "¡Únete a mi aula!\n\n" +
                                "Código del aula: " + codigoAula + "\n\n" +
                                "Usa este código en la app Lectana para unirte a la clase.";
        
        Intent intentCompartir = new Intent(Intent.ACTION_SEND);
        intentCompartir.setType("text/plain");
        intentCompartir.putExtra(Intent.EXTRA_TEXT, mensajeCompartir);
        intentCompartir.putExtra(Intent.EXTRA_SUBJECT, "Código del aula - Lectana");
        
        startActivity(Intent.createChooser(intentCompartir, "Compartir código del aula"));
    }

    private void mostrarOpcionesGestionEstudiantes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Gestionar Estudiantes");
        
        String[] opciones = {"Remover estudiante del aula"};
        
        builder.setItems(opciones, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Remover estudiante
                    mostrarListaEstudiantesParaRemover();
                    break;
                default:
                    break;
            }
        });
        
        builder.show();
    }

    private void mostrarListaEstudiantesParaRemover() {
        if (listaEstudiantes == null || listaEstudiantes.isEmpty()) {
            Toast.makeText(this, "No hay estudiantes para remover", Toast.LENGTH_SHORT).show();
            return;
        }
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar Estudiante a Remover");
        
        // Crear array de nombres de estudiantes
        String[] nombresEstudiantes = new String[listaEstudiantes.size()];
        for (int i = 0; i < listaEstudiantes.size(); i++) {
            nombresEstudiantes[i] = listaEstudiantes.get(i).getNombre();
        }
        
        builder.setItems(nombresEstudiantes, (dialog, which) -> {
            ModeloEstudianteAula estudianteSeleccionado = listaEstudiantes.get(which);
            mostrarDialogoConfirmacionRemoverEstudiante(estudianteSeleccionado);
        });
        
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoConfirmacionRemoverEstudiante(ModeloEstudianteAula estudiante) {
        new AlertDialog.Builder(this)
                .setTitle("Remover Estudiante")
                .setMessage("¿Estás seguro de que quieres remover a '" + estudiante.getNombre() + "' del aula?\n\n" +
                           "Esta acción:\n" +
                           "• Eliminará al estudiante del aula\n" +
                           "• Perderá acceso a todos los cuentos asignados\n" +
                           "• Se perderá su historial de progreso\n\n" +
                           "El estudiante podrá volver a unirse usando el código del aula.")
                .setPositiveButton("Remover", (dialog, which) -> {
                    removerEstudianteDelAula(estudiante);
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void removerEstudianteDelAula(ModeloEstudianteAula estudiante) {
        mostrarCargando(true);
        
        // Convertir ID de String a int
        int idEstudiante;
        try {
            idEstudiante = Integer.parseInt(estudiante.getId());
        } catch (NumberFormatException e) {
            mostrarCargando(false);
            Toast.makeText(this, "Error: ID de estudiante no válido", Toast.LENGTH_LONG).show();
            return;
        }
        
        aulasRepository.removerEstudianteAula(aulaId, idEstudiante, new AulasRepository.AulasCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(VisualizarAulaActivity.this, "Estudiante removido del aula correctamente", Toast.LENGTH_SHORT).show();
                    
                    // Remover estudiante de la lista local
                    listaEstudiantes.remove(estudiante);
                    adaptadorEstudiantes.notifyDataSetChanged();
                    
                    // Actualizar contador
                    numeroEstudiantesAula.setText(String.valueOf(listaEstudiantes.size()));
                    
                    // Si no quedan estudiantes, mostrar estado vacío
                    if (listaEstudiantes.isEmpty()) {
                        mostrarPestanaEstudiantes();
                    }
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(VisualizarAulaActivity.this, "Error al remover estudiante: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void mostrarOpcionesConfiguracion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones de Configuración");
        
        String[] opciones = {"Editar información del aula", "Eliminar aula"};
        
        builder.setItems(opciones, (dialog, which) -> {
            switch (which) {
                case 0:
                    // Editar información del aula
                    Intent intentConfig = new Intent(VisualizarAulaActivity.this, ConfiguracionAulaActivity.class);
                    intentConfig.putExtra("aula_id", aulaId);
                    intentConfig.putExtra("nombre_aula", textoNombreAula.getText().toString());
                    intentConfig.putExtra("codigo_aula", textoCodigoAula.getText().toString().replace("Código: ", ""));
                    // Extraer el grado del nombre del aula
                    String nombreCompleto = textoNombreAula.getText().toString();
                    String grado = "";
                    if (nombreCompleto.contains("°")) {
                        grado = nombreCompleto.split("°")[0] + "°";
                    }
                    intentConfig.putExtra("grado_aula", grado);
                    startActivityForResult(intentConfig, 1);
                    break;
                case 1:
                    // Eliminar aula
                    mostrarDialogoConfirmacionEliminar();
                    break;
                default:
                    break;
            }
        });
        
        builder.show();
    }

    private void mostrarDialogoConfirmacionEliminar() {
        String nombreAula = aulaActual != null ? aulaActual.getNombre_aula() : "esta aula";
        
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Aula")
                .setMessage("¿Estás seguro de que quieres eliminar el aula '" + nombreAula + "'?\n\n" +
                           "Esta acción eliminará:\n" +
                           "• Todos los estudiantes del aula\n" +
                           "• Todas las asignaciones de cuentos\n" +
                           "• Todo el historial de actividades\n\n" +
                           "Esta acción NO se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    eliminarAula();
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void eliminarAula() {
        mostrarCargando(true);
        aulasRepository.eliminarAula(aulaId, new AulasRepository.AulasCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(VisualizarAulaActivity.this, "Aula eliminada correctamente", Toast.LENGTH_SHORT).show();
                    // Regresar al Panel Docente
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(VisualizarAulaActivity.this, "Error al eliminar aula: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private int obtenerIdCuentoDirecto(ModeloCuentoDetallado cuento) {
        // Buscar el id_cuento real en aulaActual.getCuentos() por título
        if (aulaActual != null && aulaActual.getCuentos() != null && cuento != null && cuento.getTitulo() != null) {
            for (CuentoApi c : aulaActual.getCuentos()) {
                if (c != null && c.getTitulo() != null && c.getTitulo().equalsIgnoreCase(cuento.getTitulo())) {
                    return c.getId_cuento();
                }
            }
        }
        return 0;
    }
}
