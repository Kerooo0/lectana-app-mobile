package com.example.lectana.docente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.CrearActividadRequest;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.repository.ActividadesRepository;
import com.example.lectana.repository.AulasRepository;
import com.example.lectana.services.CuentosApiService;
import com.example.lectana.auth.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CrearActividadActivity extends AppCompatActivity {
    private static final String TAG = "CrearActividad";
    
    // Componentes principales
    private EditText editDescripcion;
    private Spinner spinnerTipo;
    private Spinner spinnerCuento;
    private RecyclerView recyclerViewAulas;
    private RecyclerView recyclerViewPreguntas;
    private Button btnAgregarPregunta;
    private Button btnGuardarActividad;
    private ProgressBar progressBar;
    
    // Repositories y servicios
    private ActividadesRepository actividadesRepository;
    private AulasRepository aulasRepository;
    private SessionManager sessionManager;
    
    // Datos
    private List<CuentoApi> listaCuentos;
    private List<ModeloAula> listaAulas;
    private List<ModeloAula> aulasSeleccionadas;
    private List<PreguntaItem> listaPreguntas;
    private PreguntasAdapter preguntasAdapter;
    private AulasSeleccionAdapter aulasAdapter;
    
    // Modo edición
    private boolean modoEdicion = false;
    private int actividadId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "=== INICIANDO CrearActividadActivity ===");
        Log.d(TAG, "Intent extras: " + (getIntent().getExtras() != null ? getIntent().getExtras().toString() : "null"));
        
        try {
            Log.d(TAG, "Intentando cargar layout activity_crear_actividad...");
            setContentView(R.layout.activity_crear_actividad);
            Log.d(TAG, "Layout activity_crear_actividad cargado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "=== ERROR CRÍTICO CARGANDO LAYOUT ===", e);
            Log.e(TAG, "Tipo de error: " + e.getClass().getSimpleName());
            Log.e(TAG, "Mensaje: " + e.getMessage());
            if (e.getCause() != null) {
                Log.e(TAG, "Causa: " + e.getCause().getMessage());
            }
            
            // Intentar cargar un layout simple como fallback
            try {
                Log.d(TAG, "Intentando cargar layout simple como fallback...");
                setContentView(android.R.layout.simple_list_item_1);
                Log.d(TAG, "Layout simple cargado como fallback");
                
                // Mostrar mensaje de error
                android.widget.TextView textView = findViewById(android.R.id.text1);
                if (textView != null) {
                    textView.setText("Error cargando interfaz: " + e.getMessage());
                }
            } catch (Exception e2) {
                Log.e(TAG, "Error crítico incluso con layout simple", e2);
                Toast.makeText(this, "Error crítico: " + e2.getMessage(), Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        
        Log.d(TAG, "CrearActividadActivity iniciada");
        
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
        
        // Verificar si es modo edición
        Intent intent = getIntent();
        if (intent.hasExtra("modo_edicion") && intent.getBooleanExtra("modo_edicion", false)) {
            modoEdicion = true;
            actividadId = intent.getIntExtra("actividad_id", -1);
            Log.d(TAG, "=== MODO EDICIÓN ACTIVADO ===");
            Log.d(TAG, "ID de actividad a editar: " + actividadId);
        } else {
            Log.d(TAG, "=== MODO CREACIÓN ===");
        }
        
        try {
            Log.d(TAG, "Inicializando componentes...");
            inicializarComponentes();
            
            Log.d(TAG, "Configurando spinners...");
            configurarSpinners();
            
            Log.d(TAG, "Configurando RecyclerViews...");
            configurarRecyclerViews();
            
            Log.d(TAG, "Configurando listeners...");
            configurarListeners();
            
            Log.d(TAG, "Cargando datos...");
            cargarDatos();
            
            Log.d(TAG, "CrearActividadActivity inicializada completamente");
        } catch (Exception e) {
            Log.e(TAG, "Error durante la inicialización", e);
            Toast.makeText(this, "Error inicializando la actividad: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void inicializarComponentes() {
        editDescripcion = findViewById(R.id.editDescripcion);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerCuento = findViewById(R.id.spinnerCuento);
        recyclerViewAulas = findViewById(R.id.recyclerViewAulas);
        recyclerViewPreguntas = findViewById(R.id.recyclerViewPreguntas);
        btnAgregarPregunta = findViewById(R.id.btnAgregarPregunta);
        btnGuardarActividad = findViewById(R.id.btnGuardarActividad);
        progressBar = findViewById(R.id.progressBar);
        
        Log.d(TAG, "Botón agregar pregunta inicializado: " + (btnAgregarPregunta != null ? "SÍ" : "NO"));
        
        // SessionManager ya inicializado en onCreate
        actividadesRepository = new ActividadesRepository(sessionManager);
        aulasRepository = new AulasRepository(sessionManager);
        
        listaCuentos = new ArrayList<>();
        listaAulas = new ArrayList<>();
        aulasSeleccionadas = new ArrayList<>();
        listaPreguntas = new ArrayList<>();
        
        // Configurar título según modo
        TextView textTitulo = findViewById(R.id.textTitulo);
        if (modoEdicion) {
            textTitulo.setText("Editar Actividad");
            btnGuardarActividad.setText("Actualizar Actividad");
        } else {
            textTitulo.setText("Crear Nueva Actividad");
            btnGuardarActividad.setText("Crear Actividad");
        }
    }

    private void configurarSpinners() {
        // Spinner de tipo de actividad
        String[] tipos = {"Opción Múltiple", "Respuesta Abierta"};
        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(tipoAdapter);
        
        // Listener para cuando cambie el tipo de actividad
        spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Actualizar todos los adapters de preguntas cuando cambie el tipo
                if (preguntasAdapter != null) {
                    preguntasAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
        
        // Spinner de cuentos
        ArrayAdapter<CuentoApi> cuentoAdapter = new ArrayAdapter<CuentoApi>(this, android.R.layout.simple_spinner_item, listaCuentos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) super.getView(position, convertView, parent);
                text.setText(getItem(position).getTitulo());
                return text;
            }
            
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView text = (TextView) super.getDropDownView(position, convertView, parent);
                text.setText(getItem(position).getTitulo());
                return text;
            }
        };
        spinnerCuento.setAdapter(cuentoAdapter);
    }

    private void configurarRecyclerViews() {
        // RecyclerView de aulas
        aulasAdapter = new AulasSeleccionAdapter(listaAulas, aulasSeleccionadas, new AulasSeleccionAdapter.OnAulaSeleccionListener() {
            @Override
            public void onAulaSeleccionada(ModeloAula aula, boolean seleccionada) {
                if (seleccionada) {
                    if (!aulasSeleccionadas.contains(aula)) {
                        aulasSeleccionadas.add(aula);
                    }
                } else {
                    aulasSeleccionadas.remove(aula);
                }
                aulasAdapter.notifyDataSetChanged();
            }
        });
        recyclerViewAulas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAulas.setAdapter(aulasAdapter);
        
        // RecyclerView de preguntas
        preguntasAdapter = new PreguntasAdapter(listaPreguntas, new PreguntasAdapter.OnPreguntaListener() {
            @Override
            public void onEliminarPregunta(int position) {
                listaPreguntas.remove(position);
                preguntasAdapter.notifyDataSetChanged();
            }
        }, this);
        recyclerViewPreguntas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPreguntas.setAdapter(preguntasAdapter);
    }

    private void configurarListeners() {
        btnAgregarPregunta.setOnClickListener(v -> {
            Log.d(TAG, "Botón agregar pregunta presionado");
            agregarPregunta();
        });
        btnGuardarActividad.setOnClickListener(v -> guardarActividad());
    }

    private void cargarDatos() {
        mostrarCargando(true);
        
        // Cargar cuentos
        cargarCuentos();
        
        // Cargar aulas
        cargarAulas();
        
        // Si es modo edición, cargar datos de la actividad
        if (modoEdicion && actividadId != -1) {
            cargarActividadParaEdicion();
        } else {
            // Si no es modo edición, ocultar el loading después de cargar cuentos y aulas
            mostrarCargando(false);
        }
    }

    private void cargarCuentos() {
        // Usar el servicio de cuentos existente
        CuentosApiService apiService = com.example.lectana.services.ApiClient.getCuentosApiService();
        if (apiService != null) {
            apiService.getCuentosPublicos(1, 50, null, null, null, null).enqueue(new retrofit2.Callback<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>>() {
                @Override
                public void onResponse(retrofit2.Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, retrofit2.Response<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> response) {
                    runOnUiThread(() -> {
                        if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                            listaCuentos.clear();
                            listaCuentos.addAll(response.body().getData().getCuentos());
                            
                            ((ArrayAdapter) spinnerCuento.getAdapter()).notifyDataSetChanged();
                            if (!listaCuentos.isEmpty()) {
                                spinnerCuento.setSelection(0);
                            }
                        } else {
                            Log.e(TAG, "Error en respuesta de cuentos: " + response.code());
                            Toast.makeText(CrearActividadActivity.this, "Error cargando cuentos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(retrofit2.Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, Throwable t) {
                    runOnUiThread(() -> {
                        Log.e(TAG, "Error cargando cuentos", t);
                        Toast.makeText(CrearActividadActivity.this, "Error de conexión al cargar cuentos", Toast.LENGTH_SHORT).show();
                    });
                }
            });
        }
    }

    private void cargarAulas() {
        aulasRepository.getAulasDocente(new AulasRepository.AulasCallback<List<ModeloAula>>() {
            @Override
            public void onSuccess(List<ModeloAula> aulas) {
                runOnUiThread(() -> {
                    listaAulas.clear();
                    listaAulas.addAll(aulas);
                    aulasAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Error cargando aulas: " + message);
                    Toast.makeText(CrearActividadActivity.this, "Error cargando aulas: " + message, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void cargarActividadParaEdicion() {
        Log.d(TAG, "=== CARGANDO ACTIVIDAD PARA EDICIÓN ===");
        Log.d(TAG, "ID de actividad: " + actividadId);
        
        actividadesRepository.getActividadDetalle(actividadId, new ActividadesRepository.ActividadesCallback<Actividad>() {
            @Override
            public void onSuccess(Actividad actividad) {
                Log.d(TAG, "Actividad cargada exitosamente para edición");
                Log.d(TAG, "Descripción: " + actividad.getDescripcion());
                Log.d(TAG, "Tipo: " + actividad.getTipo());
                
                runOnUiThread(() -> {
                    // Cargar datos en los campos
                    editDescripcion.setText(actividad.getDescripcion());
                    
                    // Seleccionar tipo
                    String tipo = actividad.getTipo();
                    if ("multiple_choice".equals(tipo)) {
                        spinnerTipo.setSelection(0);
                    } else if ("respuesta_abierta".equals(tipo)) {
                        spinnerTipo.setSelection(1);
                    }
                    
                    // Seleccionar cuento
                    for (int i = 0; i < listaCuentos.size(); i++) {
                        if (listaCuentos.get(i).getId_cuento() == actividad.getCuento_id_cuento()) {
                            spinnerCuento.setSelection(i);
                            break;
                        }
                    }
                    
                    // Seleccionar aulas
                    aulasSeleccionadas.clear();
                    if (actividad.getAulas_ids() != null) {
                        for (Integer aulaId : actividad.getAulas_ids()) {
                            for (ModeloAula aula : listaAulas) {
                                if (aula.getId_aula() == aulaId) {
                                    aulasSeleccionadas.add(aula);
                                    break;
                                }
                            }
                        }
                    }
                    aulasAdapter.notifyDataSetChanged();
                    
                    mostrarCargando(false);
                });
            }

            @Override
            public void onError(String message) {
                Log.e(TAG, "=== ERROR CARGANDO ACTIVIDAD PARA EDICIÓN ===");
                Log.e(TAG, "Mensaje de error: " + message);
                Log.e(TAG, "ID de actividad: " + actividadId);
                
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(CrearActividadActivity.this, "Error cargando actividad: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void agregarPregunta() {
        Log.d(TAG, "Agregando nueva pregunta...");
        
        PreguntaItem nuevaPregunta = new PreguntaItem();
        nuevaPregunta.setEnunciado("");
        nuevaPregunta.setRespuestas(new ArrayList<>());
        
        // Agregar respuesta por defecto
        RespuestaItem respuesta = new RespuestaItem();
        respuesta.setRespuesta("Respuesta 1"); // Texto por defecto en lugar de vacío
        respuesta.setEsCorrecta(false);
        nuevaPregunta.getRespuestas().add(respuesta);
        
        listaPreguntas.add(nuevaPregunta);
        preguntasAdapter.notifyDataSetChanged();
        
        Log.d(TAG, "Pregunta agregada. Total preguntas: " + listaPreguntas.size());
    }

    private void guardarActividad() {
        Log.d(TAG, "=== INICIANDO GUARDAR ACTIVIDAD ===");
        
        Log.d(TAG, "Validando datos...");
        if (!validarDatos()) {
            Log.d(TAG, "Validación falló, cancelando guardado");
            return;
        }
        Log.d(TAG, "Validación exitosa");
        
        Log.d(TAG, "Mostrando loading...");
        mostrarCargando(true);
        
        Log.d(TAG, "Creando request...");
        CrearActividadRequest request = crearRequest();
        Log.d(TAG, "Request creado correctamente");
        
        if (modoEdicion) {
            Log.d(TAG, "Modo edición: actualizando actividad ID " + actividadId);
            actividadesRepository.actualizarActividad(actividadId, request, new ActividadesRepository.ActividadesCallback<Actividad>() {
                @Override
                public void onSuccess(Actividad actividad) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        mostrarDialogoExito("¡Actividad actualizada exitosamente!", "Los cambios en tu actividad han sido guardados correctamente.");
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Toast.makeText(CrearActividadActivity.this, "Error al actualizar actividad: " + message, Toast.LENGTH_LONG).show();
                    });
                }
            });
        } else {
            Log.d(TAG, "Modo creación: creando nueva actividad");
            actividadesRepository.crearActividad(request, new ActividadesRepository.ActividadesCallback<Actividad>() {
                @Override
                public void onSuccess(Actividad actividad) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        mostrarDialogoExito("¡Actividad creada exitosamente!", "Tu actividad ha sido creada y asignada a las aulas seleccionadas.");
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Toast.makeText(CrearActividadActivity.this, "Error al crear actividad: " + message, Toast.LENGTH_LONG).show();
                    });
                }
            });
        }
    }

    private boolean validarDatos() {
        if (editDescripcion.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "La descripción es obligatoria", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (spinnerCuento.getSelectedItem() == null) {
            Toast.makeText(this, "Debe seleccionar un cuento", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (aulasSeleccionadas.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar al menos una aula", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (listaPreguntas.isEmpty()) {
            Toast.makeText(this, "Debe agregar al menos una pregunta", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        // Validar preguntas
        for (PreguntaItem pregunta : listaPreguntas) {
            if (pregunta.getEnunciado().trim().isEmpty()) {
                Toast.makeText(this, "Todas las preguntas deben tener un enunciado", Toast.LENGTH_SHORT).show();
                return false;
            }
            
            if (pregunta.getRespuestas().isEmpty()) {
                Toast.makeText(this, "Todas las preguntas deben tener al menos una respuesta", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Validar que ninguna respuesta esté vacía
            for (RespuestaItem respuesta : pregunta.getRespuestas()) {
                if (respuesta.getRespuesta() == null || respuesta.getRespuesta().trim().isEmpty()) {
                    Toast.makeText(this, "Todas las respuestas deben tener texto", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            
            // Validaciones según el tipo de actividad
            String tipoSeleccionado = (String) spinnerTipo.getSelectedItem();
            if ("Opción Múltiple".equals(tipoSeleccionado)) {
                // Para opción múltiple, debe haber al menos una respuesta correcta
                boolean tieneCorrecta = false;
                for (RespuestaItem respuesta : pregunta.getRespuestas()) {
                    if (respuesta.isEsCorrecta()) {
                        tieneCorrecta = true;
                        break;
                    }
                }
                if (!tieneCorrecta) {
                    Toast.makeText(this, "Las preguntas de opción múltiple deben tener al menos una respuesta correcta", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if ("Respuesta Abierta".equals(tipoSeleccionado)) {
                // Para respuesta abierta, ninguna respuesta puede estar marcada como correcta
                for (RespuestaItem respuesta : pregunta.getRespuestas()) {
                    if (respuesta.isEsCorrecta()) {
                        Toast.makeText(this, "Las actividades de respuesta abierta no pueden tener respuestas marcadas como correctas", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            }
        }
        
        return true;
    }

    private CrearActividadRequest crearRequest() {
        String descripcion = editDescripcion.getText().toString().trim();
        String tipo = ((String) spinnerTipo.getSelectedItem()).equals("Opción Múltiple") ? "multiple_choice" : "respuesta_abierta";
        CuentoApi cuentoSeleccionado = (CuentoApi) spinnerCuento.getSelectedItem();
        
        List<Integer> aulasIds = new ArrayList<>();
        for (ModeloAula aula : aulasSeleccionadas) {
            aulasIds.add(aula.getId_aula());
        }
        
        List<CrearActividadRequest.PreguntaRequest> preguntasRequest = new ArrayList<>();
        for (PreguntaItem pregunta : listaPreguntas) {
            List<CrearActividadRequest.RespuestaRequest> respuestasRequest = new ArrayList<>();
            for (RespuestaItem respuesta : pregunta.getRespuestas()) {
                String texto = respuesta.getRespuesta() != null ? respuesta.getRespuesta().trim() : "";
                if (!texto.isEmpty()) { // Excluir respuestas vacías
                    respuestasRequest.add(new CrearActividadRequest.RespuestaRequest(texto, respuesta.isEsCorrecta()));
                }
            }
            preguntasRequest.add(new CrearActividadRequest.PreguntaRequest(pregunta.getEnunciado().trim(), respuestasRequest));
        }
        
        return new CrearActividadRequest(descripcion, tipo, cuentoSeleccionado.getId_cuento(), aulasIds, preguntasRequest);
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    private void mostrarDialogoExito(String titulo, String mensaje) {
        Log.d(TAG, "Mostrando diálogo de éxito: " + titulo);
        
        new android.app.AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(mensaje)
            .setIcon(android.R.drawable.ic_dialog_info)
            .setPositiveButton("Continuar", (dialog, which) -> {
                Log.d(TAG, "Usuario confirmó diálogo de éxito, cerrando actividad");
                finish();
            })
            .setCancelable(false)
            .show();
    }

    public String obtenerTipoActividad() {
        if (spinnerTipo != null && spinnerTipo.getSelectedItem() != null) {
            return (String) spinnerTipo.getSelectedItem();
        }
        return "Opción Múltiple"; // Valor por defecto
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
            Toast.makeText(this, "Acceso denegado. Solo los docentes pueden crear actividades.", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        return true;
    }

    // Clases internas para manejar preguntas y respuestas
    public static class PreguntaItem {
        private String enunciado;
        private List<RespuestaItem> respuestas;

        public String getEnunciado() {
            return enunciado;
        }

        public void setEnunciado(String enunciado) {
            this.enunciado = enunciado;
        }

        public List<RespuestaItem> getRespuestas() {
            return respuestas;
        }

        public void setRespuestas(List<RespuestaItem> respuestas) {
            this.respuestas = respuestas;
        }
    }

    public static class RespuestaItem {
        private String respuesta;
        private boolean esCorrecta;

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }

        public boolean isEsCorrecta() {
            return esCorrecta;
        }

        public void setEsCorrecta(boolean esCorrecta) {
            this.esCorrecta = esCorrecta;
        }
    }
}
