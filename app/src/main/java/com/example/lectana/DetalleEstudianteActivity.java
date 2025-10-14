package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorActividadesEstudiante;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ModeloActividadEstudiante;
import com.example.lectana.modelos.ModeloEstudianteAula;
import com.example.lectana.modelos.ActividadesEstudianteResponse;
import com.example.lectana.repository.AulasRepository;

import java.util.ArrayList;
import java.util.List;

public class DetalleEstudianteActivity extends AppCompatActivity {

    private static final String TAG = "DetalleEstudiante";

    // Componentes de la Interfaz
    private ImageView botonVolver;
    private TextView textoNombreEstudiante;
    private TextView textoProgresoGeneral;
    private TextView textoActividadesCompletadas;
    private TextView textoActividadesPendientes;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewActividades;
    private View estadoVacio;
    private TextView textoEstadoVacio;

    // Adaptador y datos
    private AdaptadorActividadesEstudiante adaptadorActividades;
    private List<ModeloActividadEstudiante> listaActividades;

    // Datos del estudiante
    private ModeloEstudianteAula estudiante;
    private int aulaId;
    private int estudianteId;

    // Repositorio y sesión
    private SessionManager sessionManager;
    private AulasRepository aulasRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estudiante);

        sessionManager = new SessionManager(this);
        aulasRepository = new AulasRepository(sessionManager);

        if (!verificarSesion()) {
            return;
        }

        inicializarVistas();
        recibirDatosEstudiante();
        configurarListeners();
        cargarActividadesEstudiante();
    }

    private boolean verificarSesion() {
        if (!sessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
            return false;
        }
        return true;
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        textoNombreEstudiante = findViewById(R.id.texto_nombre_estudiante);
        textoProgresoGeneral = findViewById(R.id.texto_progreso_general);
        textoActividadesCompletadas = findViewById(R.id.texto_actividades_completadas);
        textoActividadesPendientes = findViewById(R.id.texto_actividades_pendientes);
        progressBar = findViewById(R.id.progress_bar);
        recyclerViewActividades = findViewById(R.id.recycler_view_actividades);
        estadoVacio = findViewById(R.id.estado_vacio_actividades);
        textoEstadoVacio = findViewById(R.id.texto_estado_vacio);

        // Configurar RecyclerView
        recyclerViewActividades.setLayoutManager(new LinearLayoutManager(this));
        
        // Inicializar lista y adaptador
        listaActividades = new ArrayList<>();
        adaptadorActividades = new AdaptadorActividadesEstudiante(listaActividades);
        recyclerViewActividades.setAdapter(adaptadorActividades);
    }

    private void recibirDatosEstudiante() {
        Intent intent = getIntent();
        if (intent != null) {
            estudianteId = intent.getIntExtra("estudiante_id", 0);
            aulaId = intent.getIntExtra("aula_id", 0);
            
            // Crear objeto estudiante desde los datos recibidos
            String nombre = intent.getStringExtra("nombre_estudiante");
            String ultimaActividad = intent.getStringExtra("ultima_actividad");
            int progreso = intent.getIntExtra("progreso", 0);
            boolean activo = intent.getBooleanExtra("activo", true);
            
            estudiante = new ModeloEstudianteAula(
                String.valueOf(estudianteId),
                nombre != null ? nombre : "Estudiante",
                ultimaActividad != null ? ultimaActividad : "Sin actividad reciente",
                progreso,
                activo
            );
            
            // Mostrar datos del estudiante
            textoNombreEstudiante.setText(estudiante.getNombre());
            textoProgresoGeneral.setText(estudiante.getProgreso() + "%");
            
            Log.d(TAG, "Datos recibidos - Estudiante: " + estudiante.getNombre() + 
                      ", ID: " + estudianteId + ", Aula: " + aulaId);
        }
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(v -> finish());
    }

    private void cargarActividadesEstudiante() {
        mostrarCargando(true);
        
        aulasRepository.getActividadesEstudiante(aulaId, estudianteId, new AulasRepository.AulasCallback<ActividadesEstudianteResponse>() {
            @Override
            public void onSuccess(ActividadesEstudianteResponse response) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    procesarDatosActividades(response);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(DetalleEstudianteActivity.this, "Error al cargar actividades: " + message, Toast.LENGTH_LONG).show();
                    // Mostrar estado vacío en caso de error
                    mostrarEstadoVacio(true);
                });
            }
        });
    }

    private void procesarDatosActividades(ActividadesEstudianteResponse response) {
        // Actualizar información del estudiante
        if (response.getEstudiante() != null) {
            ActividadesEstudianteResponse.EstudianteInfo estudianteInfo = response.getEstudiante();
            textoNombreEstudiante.setText(estudianteInfo.getNombre());
            textoProgresoGeneral.setText(estudianteInfo.getProgreso_general() + "%");
        }

        // Procesar actividades
        listaActividades.clear();
        if (response.getActividades() != null) {
            for (ActividadesEstudianteResponse.ActividadEstudiante actividadApi : response.getActividades()) {
                ModeloActividadEstudiante actividad = convertirActividadApiAModelo(actividadApi);
                listaActividades.add(actividad);
            }
        }

        // Actualizar estadísticas
        if (response.getEstadisticas() != null) {
            ActividadesEstudianteResponse.EstadisticasActividades stats = response.getEstadisticas();
            textoActividadesCompletadas.setText(String.valueOf(stats.getCompletadas()));
            textoActividadesPendientes.setText(String.valueOf(stats.getPendientes()));
        }

        // Actualizar UI
        adaptadorActividades.notifyDataSetChanged();
        mostrarEstadoVacio(listaActividades.isEmpty());
    }

    private ModeloActividadEstudiante convertirActividadApiAModelo(ActividadesEstudianteResponse.ActividadEstudiante actividadApi) {
        // Formatear fecha para mostrar
        String fechaMostrar = formatearFecha(actividadApi.getFecha_asignacion(), actividadApi.getFecha_completada());
        
        return new ModeloActividadEstudiante(
            actividadApi.getTitulo(),
            actividadApi.getEstado(),
            fechaMostrar,
            actividadApi.getProgreso(),
            actividadApi.isCompletada()
        );
    }

    private String formatearFecha(String fechaAsignacion, String fechaCompletada) {
        if (fechaCompletada != null && !fechaCompletada.isEmpty()) {
            return "Completada hace 2 horas"; // TODO: Implementar cálculo real de tiempo
        } else {
            return "Asignada hace 3 días"; // TODO: Implementar cálculo real de tiempo
        }
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    private void mostrarEstadoVacio(boolean mostrar) {
        if (estadoVacio != null) {
            estadoVacio.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        recyclerViewActividades.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        
        if (mostrar) {
            textoEstadoVacio.setText("No hay actividades asignadas");
        }
    }
}
