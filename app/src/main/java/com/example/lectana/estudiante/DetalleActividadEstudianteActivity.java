package com.example.lectana.estudiante;

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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ActividadCompleta;
import com.example.lectana.modelos.ActividadCompletaResponse;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.PreguntaActividad;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.ApiClient;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Actividad para mostrar los detalles completos de una actividad a un estudiante
 */
public class DetalleActividadEstudianteActivity extends AppCompatActivity {
    private static final String TAG = "DetalleActividadEst";

    // Vistas
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private LinearLayout layoutContenido;
    private TextView textTituloActividad;
    private TextView textTipoActividad;
    private TextView textDescripcionActividad;
    private TextView textFechaActividad;
    private MaterialCardView cardCuento;
    private TextView textTituloCuento;
    private Button btnLeerCuento;
    private TextView textCantidadPreguntas;
    private RecyclerView recyclerViewPreguntas;
    private Button btnComenzarActividad;

    // Servicios y datos
    private ActividadesApiService actividadesApiService;
    private SessionManager sessionManager;
    private int idActividad;
    private String descripcionActividad;
    private String tipoActividad;
    private String fechaActividad;
    private int cuentoId;
    private ActividadCompleta actividadCompleta;
    private PreguntasPreviewAdapter preguntasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_actividad_estudiante);

        Log.d(TAG, "=== INICIANDO DetalleActividadEstudianteActivity ===");

        inicializarComponentes();
        configurarToolbar();
        obtenerDatosIntent();
        cargarDetalleActividad();
    }

    private void inicializarComponentes() {
        Log.d(TAG, "Inicializando componentes...");

        toolbar = findViewById(R.id.toolbar);
        progressBar = findViewById(R.id.progressBar);
        layoutContenido = findViewById(R.id.layoutContenido);
        textTituloActividad = findViewById(R.id.textTituloActividad);
        textTipoActividad = findViewById(R.id.textTipoActividad);
        textDescripcionActividad = findViewById(R.id.textDescripcionActividad);
        textFechaActividad = findViewById(R.id.textFechaActividad);
        cardCuento = findViewById(R.id.cardCuento);
        textTituloCuento = findViewById(R.id.textTituloCuento);
        btnLeerCuento = findViewById(R.id.btnLeerCuento);
        textCantidadPreguntas = findViewById(R.id.textCantidadPreguntas);
        recyclerViewPreguntas = findViewById(R.id.recyclerViewPreguntas);
        btnComenzarActividad = findViewById(R.id.btnComenzarActividad);

        sessionManager = new SessionManager(this);
        actividadesApiService = ApiClient.getActividadesApiService();

        Log.d(TAG, "Componentes inicializados");
    }

    private void configurarToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalle de Actividad");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void obtenerDatosIntent() {
        idActividad = getIntent().getIntExtra("actividad_id", 0);
        descripcionActividad = getIntent().getStringExtra("actividad_descripcion");
        tipoActividad = getIntent().getStringExtra("actividad_tipo");
        fechaActividad = getIntent().getStringExtra("actividad_fecha");
        cuentoId = getIntent().getIntExtra("cuento_id", 0);
        
        Log.d(TAG, "ID Actividad recibido: " + idActividad);
        Log.d(TAG, "Descripción: " + descripcionActividad);
        Log.d(TAG, "Tipo: " + tipoActividad);

        if (idActividad == 0) {
            Toast.makeText(this, "Error: ID de actividad no válido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarDetalleActividad() {
        Log.d(TAG, "Cargando detalle de actividad ID: " + idActividad);
        mostrarCargando(true);

        String token = "Bearer " + sessionManager.getToken();

        actividadesApiService.getActividadCompleta(token, idActividad)
                .enqueue(new Callback<ActividadCompletaResponse>() {
                    @Override
                    public void onResponse(Call<ActividadCompletaResponse> call,
                                         Response<ActividadCompletaResponse> response) {
                        mostrarCargando(false);

                        if (response.isSuccessful() && response.body() != null) {
                            ActividadCompletaResponse data = response.body();
                            List<PreguntaActividad> preguntas = data.getPreguntas();
                            
                            if (preguntas != null && !preguntas.isEmpty()) {
                                Log.d(TAG, "Preguntas cargadas: " + preguntas.size());
                                
                                // Crear objeto ActividadCompleta con las preguntas
                                actividadCompleta = new ActividadCompleta();
                                actividadCompleta.setIdActividad(idActividad);
                                actividadCompleta.setDescripcion(descripcionActividad);
                                actividadCompleta.setTipo(tipoActividad);
                                actividadCompleta.setFechaPublicacion(fechaActividad);
                                actividadCompleta.setCuentoIdCuento(cuentoId);
                                actividadCompleta.setPreguntaActividad(preguntas);
                                
                                mostrarDetalleActividad();
                            } else {
                                Toast.makeText(DetalleActividadEstudianteActivity.this,
                                        "No se encontraron preguntas para esta actividad",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Log.e(TAG, "Error en respuesta: " + response.code());
                            Toast.makeText(DetalleActividadEstudianteActivity.this,
                                    "Error al cargar detalles de la actividad",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<ActividadCompletaResponse> call, Throwable t) {
                        mostrarCargando(false);
                        Log.e(TAG, "Error de conexión: " + t.getMessage(), t);
                        Toast.makeText(DetalleActividadEstudianteActivity.this,
                                "Error de conexión: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void mostrarDetalleActividad() {
        Log.d(TAG, "Mostrando detalles de la actividad...");

        // Título y descripción
        textTituloActividad.setText(actividadCompleta.getDescripcion());
        textDescripcionActividad.setText(actividadCompleta.getDescripcion());

        // Tipo de actividad
        String tipoDisplay = actividadCompleta.getTipoDisplay();
        textTipoActividad.setText(tipoDisplay);

        // Fecha
        if (actividadCompleta.getFechaPublicacion() != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date fecha = sdf.parse(actividadCompleta.getFechaPublicacion());
                textFechaActividad.setText("Fecha: " + sdf.format(fecha));
            } catch (Exception e) {
                textFechaActividad.setText("Fecha: " + actividadCompleta.getFechaPublicacion());
            }
        }

        // Cuento (si existe)
        if (actividadCompleta.tieneCuento()) {
            cardCuento.setVisibility(View.VISIBLE);
            textTituloCuento.setText(actividadCompleta.getCuento().getTitulo());
            btnLeerCuento.setOnClickListener(v -> abrirCuento());
        } else {
            cardCuento.setVisibility(View.GONE);
        }

        // Preguntas
        if (actividadCompleta.tienePreguntas()) {
            List<PreguntaActividad> preguntas = actividadCompleta.getPreguntaActividad();
            textCantidadPreguntas.setText(String.valueOf(preguntas.size()));
            configurarRecyclerViewPreguntas(preguntas);
        } else {
            textCantidadPreguntas.setText("0");
        }

        // Botón comenzar actividad
        btnComenzarActividad.setOnClickListener(v -> comenzarActividad());

        Log.d(TAG, "Detalles mostrados correctamente");
    }

    private void configurarRecyclerViewPreguntas(List<PreguntaActividad> preguntas) {
        preguntasAdapter = new PreguntasPreviewAdapter(preguntas);
        recyclerViewPreguntas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPreguntas.setAdapter(preguntasAdapter);
    }

    private void abrirCuento() {
        if (actividadCompleta.getCuento() != null) {
            Intent intent = new Intent(this, com.example.lectana.DetalleCuentoActivity.class);
            intent.putExtra("cuento_id", actividadCompleta.getCuento().getId_cuento());
            startActivity(intent);
        }
    }

    private void comenzarActividad() {
        if (actividadCompleta == null) {
            Toast.makeText(this, "Error: Actividad no cargada", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!actividadCompleta.tienePreguntas()) {
            Toast.makeText(this, "Esta actividad no tiene preguntas", Toast.LENGTH_SHORT).show();
            return;
        }

        // Abrir ResponderActividadActivity
        Intent intent = new Intent(this, ResponderActividadActivity.class);
        intent.putExtra("actividad_id", actividadCompleta.getIdActividad());
        intent.putExtra("actividad_descripcion", actividadCompleta.getDescripcion());
        intent.putExtra("actividad_tipo", actividadCompleta.getTipo());
        intent.putExtra("cantidad_preguntas", actividadCompleta.getTotalPreguntas());
        startActivity(intent);
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        layoutContenido.setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    /**
     * Adaptador simple para mostrar preview de preguntas
     */
    private static class PreguntasPreviewAdapter extends RecyclerView.Adapter<PreguntasPreviewAdapter.ViewHolder> {
        private final List<PreguntaActividad> preguntas;

        public PreguntasPreviewAdapter(List<PreguntaActividad> preguntas) {
            this.preguntas = preguntas;
        }

        @Override
        public ViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_pregunta_preview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            PreguntaActividad pregunta = preguntas.get(position);
            holder.textNumeroPregunta.setText(String.valueOf(position + 1));
            holder.textPregunta.setText(pregunta.getEnunciado());
        }

        @Override
        public int getItemCount() {
            return preguntas != null ? preguntas.size() : 0;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textNumeroPregunta;
            TextView textPregunta;

            ViewHolder(View itemView) {
                super(itemView);
                textNumeroPregunta = itemView.findViewById(R.id.textNumeroPregunta);
                textPregunta = itemView.findViewById(R.id.textPregunta);
            }
        }
    }
}
