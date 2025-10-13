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
        mostrarPestanaEstudiantes();
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
                    mostrarCargando(false);
                    aulaActual = aula;
                    actualizarInterfazConDatosReales();
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
        if (aulaActual.getEstudiantes() != null) {
            for (ModeloAula.Estudiante estudianteApi : aulaActual.getEstudiantes()) {
                // Convertir datos de la API al modelo local
                String nombreCompleto = "Estudiante";
                if (estudianteApi.getUsuario() != null) {
                    String nombre = estudianteApi.getUsuario().getNombre() != null ? estudianteApi.getUsuario().getNombre() : "";
                    String apellido = estudianteApi.getUsuario().getApellido() != null ? estudianteApi.getUsuario().getApellido() : "";
                    nombreCompleto = nombre + " " + apellido;
                }
                
                ModeloEstudianteAula estudiante = new ModeloEstudianteAula(
                    String.valueOf(estudianteApi.getId()),
                    nombreCompleto,
                    "Última actividad: Hace 1 hora", // Por ahora usar valor por defecto
                    75, // Por ahora usar valor por defecto
                    true // Por ahora usar valor por defecto
                );
                listaEstudiantes.add(estudiante);
            }
        }
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
        if (listaEstudiantes == null || listaEstudiantes.isEmpty()) {
            mostrarEstadoVacio(true);
            textoEstadoVacio.setText("Por ahora no hay alumnos");
            botonAccionEstadoVacio.setText("Compartir código del aula");
            botonAccionEstadoVacio.setVisibility(View.VISIBLE);
            botonAccionEstadoVacio.setOnClickListener(v -> compartirCodigoAula());
        } else {
            mostrarEstadoVacio(false);
            recyclerViewContenido.setAdapter(adaptadorEstudiantes);
            adaptadorEstudiantes.notifyDataSetChanged();
        }
    }

    private void mostrarPestanaCuentos() {
        pestanaActual = "cuentos";
        actualizarBotonesPestanas();
        barraAccionesCuentos.setVisibility(View.VISIBLE);
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
        // Ocultar botón gestionar cuentos según solicitud
        botonGestionarCuentos.setVisibility(View.GONE);

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
        // Por ahora, si no hay actividades pendientes, mostrar CTA para crear
        mostrarEstadoVacio(true);
        textoEstadoVacio.setText("No hay actividades pendientes");
        botonAccionEstadoVacio.setText("Crear nueva actividad");
        botonAccionEstadoVacio.setVisibility(View.VISIBLE);
        botonAccionEstadoVacio.setOnClickListener(v -> {
            Intent intento = new Intent(VisualizarAulaActivity.this, CrearActividadActivity.class);
            intento.putExtra("aula_id", aulaId);
            startActivity(intento);
        });
    }

    private void mostrarPestanaProgreso() {
        pestanaActual = "progreso";
        actualizarBotonesPestanas();
        if (barraAccionesCuentos != null) barraAccionesCuentos.setVisibility(View.GONE);
        mostrarEstadoVacio(true);
        textoEstadoVacio.setText("PRÓXIMAMENTE");
        botonAccionEstadoVacio.setVisibility(View.GONE);
    }

    private void compartirCodigoAula() {
        String codigo = textoCodigoAula.getText().toString().replace("Código: ", "");
        Intent intent = new Intent(VisualizarAulaActivity.this, MostrarCodigoAulaActivity.class);
        intent.putExtra("codigo_aula", codigo);
        startActivity(intent);
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

    private void mostrarOpcionesConfiguracion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Opciones de Configuración");
        
        String[] opciones = {"Editar información del aula"};
        
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
                default:
                    break;
            }
        });
        
        builder.show();
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
