package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorCuentosDisponibles;
import com.example.lectana.modelos.ModeloCuento;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.CuentosApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BibliotecaCuentosActivity extends AppCompatActivity implements AdaptadorCuentosDisponibles.OnCuentoSeleccionadoListener {

    // Constantes para los modos
    public static final String MODO_ASIGNAR = "asignar";
    public static final String MODO_EXPLORAR = "explorar";
    
    // Parámetros
    private String modoActual;

    private EditText barraBusquedaCuentos;
    private Button botonFiltroTodos, botonFiltroClasicos, botonFiltroAventura, botonFiltroEdad;
    private RecyclerView recyclerViewCuentosAsignar;
    private Button botonConfirmarAsignaciones;
    private TextView textoNombreAulaAsignarCuento;

    private AdaptadorCuentosDisponibles adaptadorCuentos;
    private List<ModeloCuento> listaCuentosCompleta;
    private List<ModeloCuento> listaCuentosFiltrada;
    private String filtroActual = "todos";
    
    // API y threading
    private CuentosApiService apiService;
    private ExecutorService executorService;
    private ProgressBar progressBar;
    private TextView textoContadorCuentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.util.Log.d("BibliotecaCuentos", "onCreate iniciado");
        
        try {
            setContentView(R.layout.activity_asignar_cuento);
            android.util.Log.d("BibliotecaCuentos", "Layout cargado correctamente");

            // Detectar el modo desde el Intent
            modoActual = getIntent().getStringExtra("modo");
            if (modoActual == null) {
                modoActual = MODO_ASIGNAR; // Modo por defecto
            }
            
            // Log para debug
            android.util.Log.d("BibliotecaCuentos", "Modo actual: " + modoActual);

            // Inicializar API y threading
            apiService = ApiClient.getCuentosApiService();
            executorService = Executors.newSingleThreadExecutor();
            android.util.Log.d("BibliotecaCuentos", "API y threading inicializados");

            inicializarVistas();
            android.util.Log.d("BibliotecaCuentos", "Vistas inicializadas");
            
            configurarListeners();
            android.util.Log.d("BibliotecaCuentos", "Listeners configurados");
            
            // Inicializar listas primero
            listaCuentosCompleta = new ArrayList<>();
            listaCuentosFiltrada = new ArrayList<>();
            android.util.Log.d("BibliotecaCuentos", "Listas inicializadas");
            
            configurarRecyclerView();
            android.util.Log.d("BibliotecaCuentos", "RecyclerView configurado");
            
            cargarCuentosDesdeAPI();
            android.util.Log.d("BibliotecaCuentos", "Carga de cuentos iniciada");
            
            aplicarFiltro("todos");
            android.util.Log.d("BibliotecaCuentos", "Filtro aplicado");
            
            configurarInterfazSegunModo();
            android.util.Log.d("BibliotecaCuentos", "Interfaz configurada según modo");
            
            android.util.Log.d("BibliotecaCuentos", "onCreate completado exitosamente");
            
        } catch (Exception e) {
            android.util.Log.e("BibliotecaCuentos", "Error en onCreate: " + e.getMessage(), e);
            throw e;
        }
    }

    private void inicializarVistas() {
        barraBusquedaCuentos = findViewById(R.id.campo_busqueda_cuentos);
        botonFiltroTodos = findViewById(R.id.filtro_todos);
        botonFiltroClasicos = findViewById(R.id.filtro_clasicos);
        botonFiltroAventura = findViewById(R.id.filtro_aventura);
        botonFiltroEdad = findViewById(R.id.filtro_edad);
        recyclerViewCuentosAsignar = findViewById(R.id.recycler_view_cuentos_disponibles);
        botonConfirmarAsignaciones = findViewById(R.id.boton_confirmar_asignaciones);
        textoNombreAulaAsignarCuento = findViewById(R.id.texto_nombre_aula_asignar);
        
        // Inicializar ProgressBar (lo agregaremos al layout)
        progressBar = findViewById(R.id.progress_bar_cuentos);
        textoContadorCuentos = findViewById(R.id.texto_contador_cuentos);
    }

    private void configurarListeners() {
        findViewById(R.id.boton_volver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Filtros
        botonFiltroTodos.setOnClickListener(v -> aplicarFiltro("todos"));
        botonFiltroClasicos.setOnClickListener(v -> aplicarFiltro("clasicos"));
        botonFiltroAventura.setOnClickListener(v -> aplicarFiltro("aventura"));
        botonFiltroEdad.setOnClickListener(v -> aplicarFiltro("edad"));

        // Búsqueda
        barraBusquedaCuentos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarPorTexto(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Botón de acción principal (cambia según el modo)
        botonConfirmarAsignaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MODO_ASIGNAR.equals(modoActual)) {
                    manejarModoAsignar();
                } else if (MODO_EXPLORAR.equals(modoActual)) {
                    manejarModoExplorar();
                }
            }
        });
    }

    private void cargarDatosEjemplo() {
        listaCuentosCompleta = new ArrayList<>();
        
        // Cuentos clásicos
        listaCuentosCompleta.add(new ModeloCuento(1, "Caperucita Roja", "Hermanos Grimm", "Clásico", 
            "4-6", "4.5★", "", "15 min", "Un cuento clásico sobre una niña que lleva comida a su abuela enferma.", ""));
        listaCuentosCompleta.add(new ModeloCuento(2, "Los Tres Cerditos", "Cuento tradicional", "Clásico", 
            "3-5", "4.2★", "", "12 min", "La historia de tres cerditos que construyen casas de diferentes materiales.", ""));
        listaCuentosCompleta.add(new ModeloCuento(3, "El Patito Feo", "Hans Christian Andersen", "Clásico", 
            "4-7", "4.7★", "", "18 min", "Un patito diferente que descubre su verdadera identidad.", ""));
        
        // Cuentos de aventura
        listaCuentosCompleta.add(new ModeloCuento(4, "La Isla del Tesoro", "Robert Louis Stevenson", "Aventura", 
            "8-12", "4.3★", "", "45 min", "Una emocionante aventura pirata en busca de un tesoro oculto.", ""));
        listaCuentosCompleta.add(new ModeloCuento(5, "Las Aventuras de Tom Sawyer", "Mark Twain", "Aventura", 
            "9-13", "4.4★", "", "60 min", "Las travesuras y aventuras de un niño travieso en el Mississippi.", ""));
        
        // Cuentos para 8-12 años
        listaCuentosCompleta.add(new ModeloCuento(6, "Matilda", "Roald Dahl", "Fantasía", 
            "8-12", "4.6★", "", "35 min", "Una niña con poderes especiales que ama la lectura.", ""));
        listaCuentosCompleta.add(new ModeloCuento(7, "Charlie y la Fábrica de Chocolate", "Roald Dahl", "Fantasía", 
            "8-12", "4.8★", "", "40 min", "Un niño pobre que gana un tour por la fábrica de chocolate más famosa.", ""));
        
        listaCuentosFiltrada = new ArrayList<>(listaCuentosCompleta);
    }

    private void cargarCuentosDesdeAPI() {
        try {
            android.util.Log.d("BibliotecaCuentos", "Iniciando carga de cuentos desde API...");
            mostrarCargando(true);
            
            executorService.execute(() -> {
                try {
                    Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call = 
                        apiService.getCuentosPublicos(1, 50, null, null, null, null);
                    
                    call.enqueue(new Callback<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>>() {
                        @Override
                        public void onResponse(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, 
                                             Response<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> response) {
                            runOnUiThread(() -> {
                                mostrarCargando(false);
                                
                                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                                    com.example.lectana.modelos.CuentosResponse cuentosResponse = response.body().getData();
                                    if (cuentosResponse != null && cuentosResponse.getCuentos() != null) {
                                        listaCuentosCompleta = new ArrayList<>();
                                        for (com.example.lectana.modelos.CuentoApi cuentoApi : cuentosResponse.getCuentos()) {
                                            listaCuentosCompleta.add(cuentoApi.toModeloCuento());
                                        }
                                        aplicarFiltro(filtroActual);
                                        actualizarContadorCuentos();
                                        android.util.Log.d("BibliotecaCuentos", "Cuentos cargados desde API: " + listaCuentosCompleta.size());
                                    } else {
                                        android.util.Log.w("BibliotecaCuentos", "API responde pero sin cuentos");
                                        mostrarError("No se encontraron cuentos en el servidor");
                                    }
                                } else {
                                    android.util.Log.w("BibliotecaCuentos", "API no disponible: " + response.code());
                                    mostrarError("Error del servidor: " + response.code());
                                }
                            });
                        }
                        
                        @Override
                        public void onFailure(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, Throwable t) {
                            runOnUiThread(() -> {
                                mostrarCargando(false);
                                android.util.Log.e("BibliotecaCuentos", "Error de conexión: " + t.getMessage());
                                mostrarError("Error de conexión: " + t.getMessage());
                            });
                        }
                    });
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        android.util.Log.e("BibliotecaCuentos", "Error inesperado: " + e.getMessage());
                        mostrarError("Error inesperado: " + e.getMessage());
                    });
                }
            });
            
        } catch (Exception e) {
            android.util.Log.e("BibliotecaCuentos", "Error cargando cuentos: " + e.getMessage(), e);
            mostrarCargando(false);
            mostrarError("Error cargando cuentos: " + e.getMessage());
        }
    }

    private void configurarRecyclerView() {
        try {
            android.util.Log.d("BibliotecaCuentos", "Configurando RecyclerView...");
            
            if (listaCuentosFiltrada == null) {
                android.util.Log.w("BibliotecaCuentos", "listaCuentosFiltrada es null, inicializando...");
                listaCuentosFiltrada = new ArrayList<>();
            }
            
            adaptadorCuentos = new AdaptadorCuentosDisponibles(listaCuentosFiltrada, this, modoActual);
            recyclerViewCuentosAsignar.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewCuentosAsignar.setAdapter(adaptadorCuentos);
            
            android.util.Log.d("BibliotecaCuentos", "RecyclerView configurado correctamente");
        } catch (Exception e) {
            android.util.Log.e("BibliotecaCuentos", "Error configurando RecyclerView: " + e.getMessage(), e);
            throw e;
        }
    }

    private void aplicarFiltro(String filtro) {
        try {
            android.util.Log.d("BibliotecaCuentos", "Aplicando filtro: " + filtro);
            
            filtroActual = filtro;
            actualizarBotonesFiltro();
            
            if (listaCuentosCompleta == null) {
                android.util.Log.w("BibliotecaCuentos", "listaCuentosCompleta es null, inicializando...");
                listaCuentosCompleta = new ArrayList<>();
            }
            
            if (listaCuentosFiltrada == null) {
                android.util.Log.w("BibliotecaCuentos", "listaCuentosFiltrada es null, inicializando...");
                listaCuentosFiltrada = new ArrayList<>();
            }
            
            listaCuentosFiltrada.clear();
            
            switch (filtro) {
                case "todos":
                    listaCuentosFiltrada.addAll(listaCuentosCompleta);
                    break;
                case "clasicos":
                    for (ModeloCuento cuento : listaCuentosCompleta) {
                        if ("Clásico".equalsIgnoreCase(cuento.getGenero()) || 
                            "clasico".equalsIgnoreCase(cuento.getGenero())) {
                            listaCuentosFiltrada.add(cuento);
                        }
                    }
                    break;
                case "aventura":
                    for (ModeloCuento cuento : listaCuentosCompleta) {
                        if ("Aventura".equalsIgnoreCase(cuento.getGenero()) || 
                            "aventura".equalsIgnoreCase(cuento.getGenero())) {
                            listaCuentosFiltrada.add(cuento);
                        }
                    }
                    break;
                case "edad":
                    for (ModeloCuento cuento : listaCuentosCompleta) {
                        if (cuento.getEdadRecomendada().contains("8") || 
                            cuento.getEdadRecomendada().contains("9") ||
                            cuento.getEdadRecomendada().contains("10") ||
                            cuento.getEdadRecomendada().contains("11") ||
                            cuento.getEdadRecomendada().contains("12")) {
                            listaCuentosFiltrada.add(cuento);
                        }
                    }
                    break;
            }
            
            // Actualizar el adaptador
            if (adaptadorCuentos != null) {
                adaptadorCuentos.notifyDataSetChanged();
                android.util.Log.d("BibliotecaCuentos", "Adaptador actualizado");
            } else {
                android.util.Log.w("BibliotecaCuentos", "Adaptador es null, no se puede actualizar");
            }
            
            android.util.Log.d("BibliotecaCuentos", "Filtro aplicado. Cuentos filtrados: " + listaCuentosFiltrada.size());
        } catch (Exception e) {
            android.util.Log.e("BibliotecaCuentos", "Error aplicando filtro: " + e.getMessage(), e);
            throw e;
        }
    }

    private void actualizarBotonesFiltro() {
        // Resetear todos los botones
        botonFiltroTodos.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroTodos.setTextColor(getResources().getColor(R.color.gris_oscuro));
        botonFiltroClasicos.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroClasicos.setTextColor(getResources().getColor(R.color.gris_oscuro));
        botonFiltroAventura.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroAventura.setTextColor(getResources().getColor(R.color.gris_oscuro));
        botonFiltroEdad.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroEdad.setTextColor(getResources().getColor(R.color.gris_oscuro));

        // Aplicar estilo al botón activo
        switch (filtroActual) {
            case "todos":
                botonFiltroTodos.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroTodos.setTextColor(getResources().getColor(R.color.white));
                break;
            case "clasicos":
                botonFiltroClasicos.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroClasicos.setTextColor(getResources().getColor(R.color.white));
                break;
            case "aventura":
                botonFiltroAventura.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroAventura.setTextColor(getResources().getColor(R.color.white));
                break;
            case "edad":
                botonFiltroEdad.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroEdad.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void filtrarPorTexto(String texto) {
        listaCuentosFiltrada.clear();
        
        for (ModeloCuento cuento : listaCuentosCompleta) {
            if (cuento.getTitulo().toLowerCase().contains(texto.toLowerCase()) ||
                cuento.getAutor().toLowerCase().contains(texto.toLowerCase()) ||
                cuento.getGenero().toLowerCase().contains(texto.toLowerCase())) {
                listaCuentosFiltrada.add(cuento);
            }
        }
        
        adaptadorCuentos.notifyDataSetChanged();
    }

    @Override
    public void onCuentoSeleccionado(ModeloCuento cuento, boolean seleccionado) {
        if (MODO_ASIGNAR.equals(modoActual)) {
            // Actualizar contador de cuentos seleccionados para asignar
            int cuentosSeleccionados = 0;
            for (ModeloCuento c : listaCuentosCompleta) {
                if (c.isSeleccionado()) {
                    cuentosSeleccionados++;
                }
            }
            
            // Actualizar texto del botón
            if (cuentosSeleccionados > 0) {
                botonConfirmarAsignaciones.setText("Confirmar Asignaciones (" + cuentosSeleccionados + ")");
            } else {
                botonConfirmarAsignaciones.setText("Confirmar Asignaciones");
            }
        } else if (MODO_EXPLORAR.equals(modoActual)) {
            // En modo explorar, al hacer clic en un cuento, ir directamente a leerlo
            navegarALecturaCuento(cuento);
        }
    }

    private void configurarInterfazSegunModo() {
        TextView texto_titulo_asignar = findViewById(R.id.texto_titulo_asignar);
        
        if (MODO_EXPLORAR.equals(modoActual)) {
            // Cambiar título y comportamiento para modo explorar
            texto_titulo_asignar.setText("Explorar Cuentos");
            textoNombreAulaAsignarCuento.setText("Biblioteca Completa de Cuentos");
            botonConfirmarAsignaciones.setVisibility(View.GONE); // Ocultar botón en modo explorar
            
            // Cambiar color del header a morado para modo explorar
            LinearLayout header = findViewById(R.id.header_asignar_cuento);
            header.setBackgroundColor(getResources().getColor(R.color.morado));
            
        } else {
            // Modo asignar - mantener estética original
            texto_titulo_asignar.setText("Asignar Cuento");
            textoNombreAulaAsignarCuento.setText("Selecciona cuentos para asignar");
            botonConfirmarAsignaciones.setVisibility(View.VISIBLE);
            
            // Mantener color verde para modo asignar
            LinearLayout header = findViewById(R.id.header_asignar_cuento);
            header.setBackgroundColor(getResources().getColor(R.color.verde));
        }
    }

    private void manejarModoAsignar() {
        // Verificar que hay cuentos seleccionados
        List<ModeloCuento> cuentosSeleccionados = new ArrayList<>();
        for (ModeloCuento cuento : listaCuentosCompleta) {
            if (cuento.isSeleccionado()) {
                cuentosSeleccionados.add(cuento);
            }
        }
        
        if (cuentosSeleccionados.isEmpty()) {
            android.widget.Toast.makeText(BibliotecaCuentosActivity.this, 
                "Por favor selecciona al menos un cuento", 
                android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Navegar a seleccionar aula
        Intent intent = new Intent(BibliotecaCuentosActivity.this, SeleccionarAulaActivity.class);
        // TODO: Pasar los cuentos seleccionados a la siguiente actividad
        startActivity(intent);
    }

    private void manejarModoExplorar() {
        // En modo explorar, mostrar todos los cuentos disponibles
        android.widget.Toast.makeText(BibliotecaCuentosActivity.this, 
            "Explora los cuentos haciendo clic en cualquiera de ellos", 
            android.widget.Toast.LENGTH_LONG).show();
    }

    private void navegarALecturaCuento(ModeloCuento cuento) {
        // Navegar a la pantalla de lectura del cuento
        Intent intent = new Intent(this, DetalleCuentoActivity.class);
        intent.putExtra("cuento_id", cuento.getId());
        intent.putExtra("cuento_titulo", cuento.getTitulo());
        intent.putExtra("cuento_autor", cuento.getAutor());
        intent.putExtra("cuento_genero", cuento.getGenero());
        intent.putExtra("cuento_edad", cuento.getEdadRecomendada());
        intent.putExtra("cuento_duracion", cuento.getTiempoLectura());
        intent.putExtra("cuento_descripcion", cuento.getDescripcion());
        startActivity(intent);
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        if (recyclerViewCuentosAsignar != null) {
            recyclerViewCuentosAsignar.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        android.util.Log.e("BibliotecaCuentos", "Error: " + mensaje);
        
        // Crear lista vacía en caso de error
        if (listaCuentosCompleta == null) {
            listaCuentosCompleta = new ArrayList<>();
        }
        if (listaCuentosFiltrada == null) {
            listaCuentosFiltrada = new ArrayList<>();
        }
        aplicarFiltro(filtroActual);
        actualizarContadorCuentos();
    }

    private void actualizarContadorCuentos() {
        try {
            if (textoContadorCuentos != null && listaCuentosFiltrada != null) {
                String texto = listaCuentosFiltrada.size() + " cuentos encontrados";
                textoContadorCuentos.setText(texto);
                textoContadorCuentos.setVisibility(View.VISIBLE);
                android.util.Log.d("BibliotecaCuentos", "Contador actualizado: " + texto);
            } else {
                android.util.Log.w("BibliotecaCuentos", "No se puede actualizar contador - textoContadorCuentos: " + 
                    (textoContadorCuentos != null) + ", listaCuentosFiltrada: " + (listaCuentosFiltrada != null));
            }
        } catch (Exception e) {
            android.util.Log.e("BibliotecaCuentos", "Error actualizando contador: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
