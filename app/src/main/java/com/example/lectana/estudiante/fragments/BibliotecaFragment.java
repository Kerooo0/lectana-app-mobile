package com.example.lectana.estudiante.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.DetalleCuentoActivity;
import com.example.lectana.R;
import com.example.lectana.ReproductorAudiolibroActivity;
import com.example.lectana.adaptadores.AdaptadorBiblioteca;
import com.example.lectana.modelos.GeneroApi;
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

public class BibliotecaFragment extends Fragment implements AdaptadorBiblioteca.OnCuentoClickListener {

    private AdaptadorBiblioteca adaptador;
    private List<ModeloCuento> listaCuentos = new ArrayList<>();
    private List<GeneroApi> listaGeneros = new ArrayList<>();
    private CuentosApiService apiService;
    private ExecutorService executorService;
    private ProgressBar progressBar;
    private LinearLayout contenedorChipsGenero;
    private Integer generoIdSeleccionado = null;
    private Button botonSeleccionado = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_biblioteca, container, false);

        // Inicializar API y threading
        apiService = ApiClient.getCuentosApiService();
        executorService = Executors.newSingleThreadExecutor();

        RecyclerView recycler = root.findViewById(R.id.recycler_biblioteca);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new AdaptadorBiblioteca(requireContext(), listaCuentos, this);
        recycler.setAdapter(adaptador);

        progressBar = root.findViewById(R.id.progress_bar_biblioteca);
        contenedorChipsGenero = root.findViewById(R.id.contenedor_chips_genero);

        EditText campoBusqueda = root.findViewById(R.id.campo_busqueda_biblioteca);
        ImageView botonFiltros = root.findViewById(R.id.boton_filtros_biblioteca);

        campoBusqueda.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { adaptador.filtrarPorTexto(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });

        botonFiltros.setOnClickListener(v -> mostrarDialogoFiltros());

        // Cargar géneros y cuentos desde la API
        cargarGenerosDesdeAPI();
        cargarCuentosDesdeAPI();
        return root;
    }

    private void cargarGenerosDesdeAPI() {
        Log.d("BibliotecaFragment", "Cargando géneros desde API...");
        
        Call<com.example.lectana.modelos.ApiResponse<List<GeneroApi>>> call = apiService.getGenerosPublicos();
        
        call.enqueue(new Callback<com.example.lectana.modelos.ApiResponse<List<GeneroApi>>>() {
            @Override
            public void onResponse(Call<com.example.lectana.modelos.ApiResponse<List<GeneroApi>>> call, 
                                 Response<com.example.lectana.modelos.ApiResponse<List<GeneroApi>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    listaGeneros = response.body().getData();
                    Log.d("BibliotecaFragment", "Géneros cargados: " + listaGeneros.size());
                    crearBotonesGenero();
                } else {
                    Log.w("BibliotecaFragment", "Error al cargar géneros: " + response.code());
                    mostrarError("Error al cargar géneros");
                }
            }
            
            @Override
            public void onFailure(Call<com.example.lectana.modelos.ApiResponse<List<GeneroApi>>> call, Throwable t) {
                Log.e("BibliotecaFragment", "Error de conexión al cargar géneros: " + t.getMessage());
                mostrarError("Error de conexión al cargar géneros");
            }
        });
    }
    
    private void crearBotonesGenero() {
        if (contenedorChipsGenero == null || getContext() == null) return;
        
        // Limpiar botones existentes
        contenedorChipsGenero.removeAllViews();
        
        // Crear botón "Todos" (sin filtro)
        Button botonTodos = new Button(getContext());
        botonTodos.setText("Todos");
        botonTodos.setBackground(getResources().getDrawable(R.drawable.boton_verde));
        botonTodos.setTextColor(getResources().getColor(android.R.color.white));
        
        LinearLayout.LayoutParams paramsTodos = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, 
            (int) (40 * getResources().getDisplayMetrics().density)
        );
        paramsTodos.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
        botonTodos.setLayoutParams(paramsTodos);
        botonTodos.setBackgroundTintList(null);
        
        botonTodos.setOnClickListener(v -> {
            seleccionarBoton(botonTodos);
            generoIdSeleccionado = null;
            cargarCuentosDesdeAPI();
        });
        
        contenedorChipsGenero.addView(botonTodos);
        botonSeleccionado = botonTodos; // Inicialmente seleccionado
        
        // Crear botones para cada género del backend
        for (GeneroApi genero : listaGeneros) {
            Button botonGenero = new Button(getContext());
            botonGenero.setText(genero.getNombre());
            botonGenero.setBackground(getResources().getDrawable(R.drawable.boton_blanco_rectangular));
            botonGenero.setTextColor(getResources().getColor(R.color.gris_oscuro));
            
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 
                (int) (40 * getResources().getDisplayMetrics().density)
            );
            params.setMarginEnd((int) (8 * getResources().getDisplayMetrics().density));
            botonGenero.setLayoutParams(params);
            botonGenero.setBackgroundTintList(null);
            
            botonGenero.setOnClickListener(v -> {
                seleccionarBoton(botonGenero);
                generoIdSeleccionado = genero.getId_genero();
                cargarCuentosDesdeAPI();
            });
            
            contenedorChipsGenero.addView(botonGenero);
        }
    }
    
    private void seleccionarBoton(Button boton) {
        // Deseleccionar el botón anterior
        if (botonSeleccionado != null) {
            botonSeleccionado.setBackground(getResources().getDrawable(R.drawable.boton_blanco_rectangular));
            botonSeleccionado.setTextColor(getResources().getColor(R.color.gris_oscuro));
        }
        
        // Seleccionar el nuevo botón
        boton.setBackground(getResources().getDrawable(R.drawable.boton_verde));
        boton.setTextColor(getResources().getColor(android.R.color.white));
        botonSeleccionado = boton;
    }

    private void cargarCuentosDesdeAPI() {
        android.util.Log.d("BibliotecaFragment", "Iniciando carga de cuentos desde API...");
        Log.d("BibliotecaFragment", "Filtro de género ID: " + generoIdSeleccionado);
        mostrarCargando(true);
        
        // Llamada con filtro de género si está seleccionado
        Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call = 
            apiService.getCuentosPublicos(1, 50, null, generoIdSeleccionado, null, null);
        
        call.enqueue(new Callback<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>>() {
            @Override
            public void onResponse(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, 
                                 Response<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> response) {
                mostrarCargando(false);
                
                android.util.Log.d("BibliotecaFragment", "Respuesta recibida - Código: " + response.code());
                android.util.Log.d("BibliotecaFragment", "Respuesta exitosa: " + response.isSuccessful());
                
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        android.util.Log.d("BibliotecaFragment", "Body no es null");
                        android.util.Log.d("BibliotecaFragment", "Body ok: " + response.body().isOk());
                        
                        if (response.body().isOk()) {
                            com.example.lectana.modelos.CuentosResponse cuentosResponse = response.body().getData();
                            if (cuentosResponse != null) {
                                android.util.Log.d("BibliotecaFragment", "CuentosResponse no es null");
                                if (cuentosResponse.getCuentos() != null) {
                                    android.util.Log.d("BibliotecaFragment", "Lista de cuentos no es null, tamaño: " + cuentosResponse.getCuentos().size());
                                    
                                    listaCuentos.clear();
                                    for (com.example.lectana.modelos.CuentoApi cuentoApi : cuentosResponse.getCuentos()) {
                                        listaCuentos.add(cuentoApi.toModeloCuento());
                                    }
                                    adaptador.submitList(new ArrayList<>(listaCuentos));
                                    android.util.Log.d("BibliotecaFragment", "Cuentos cargados desde API: " + listaCuentos.size());
                                } else {
                                    android.util.Log.w("BibliotecaFragment", "Lista de cuentos es null");
                                    mostrarError("No se encontraron cuentos en el servidor");
                                }
                            } else {
                                android.util.Log.w("BibliotecaFragment", "CuentosResponse es null");
                                mostrarError("Error en la respuesta del servidor");
                            }
                        } else {
                            android.util.Log.w("BibliotecaFragment", "Respuesta no es ok");
                            mostrarError("Error del servidor: respuesta no válida");
                        }
                    } else {
                        android.util.Log.w("BibliotecaFragment", "Body es null");
                        mostrarError("Error del servidor: sin datos");
                    }
                } else {
                    android.util.Log.w("BibliotecaFragment", "API no disponible: " + response.code());
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        errorBody = "Error leyendo respuesta";
                    }
                    android.util.Log.w("BibliotecaFragment", "Error body: " + errorBody);
                    mostrarError("Error del servidor: " + response.code() + " - " + errorBody);
                }
            }
            
            @Override
            public void onFailure(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, Throwable t) {
                mostrarCargando(false);
                android.util.Log.e("BibliotecaFragment", "Error de conexión: " + t.getMessage());
                android.util.Log.e("BibliotecaFragment", "Tipo de error: " + t.getClass().getSimpleName());
                if (t.getCause() != null) {
                    android.util.Log.e("BibliotecaFragment", "Causa: " + t.getCause().getMessage());
                }
                mostrarError("Error de conexión: " + t.getMessage());
            }
        });
    }
    
    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }
    
    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        }
    }

    private void mostrarDialogoFiltros() {
        // Crear array de géneros con "Todos" al inicio
        List<String> generosNombres = new ArrayList<>();
        generosNombres.add("Todos");
        for (GeneroApi genero : listaGeneros) {
            generosNombres.add(genero.getNombre());
        }
        
        final String[] generos = generosNombres.toArray(new String[0]);
        
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Filtrar por género")
                .setItems(generos, (dialog, which) -> {
                    if (which == 0) {
                        // "Todos" seleccionado
                        generoIdSeleccionado = null;
                    } else {
                        // Un género específico seleccionado
                        GeneroApi generoSeleccionado = listaGeneros.get(which - 1);
                        generoIdSeleccionado = generoSeleccionado.getId_genero();
                    }
                    cargarCuentosDesdeAPI();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onClickVerDetalle(ModeloCuento cuento) {
        android.content.Intent intent = new android.content.Intent(requireContext(), DetalleCuentoActivity.class);
        intent.putExtra("cuento_id", cuento.getId());
        intent.putExtra("cuento_titulo", cuento.getTitulo());
        intent.putExtra("cuento_autor", cuento.getAutor());
        intent.putExtra("cuento_genero", cuento.getGenero());
        intent.putExtra("cuento_edad", cuento.getEdadRecomendada());
        intent.putExtra("cuento_duracion", cuento.getTiempoLectura());
        intent.putExtra("cuento_descripcion", cuento.getDescripcion());
        startActivity(intent);
    }

    @Override
    public void onClickReproducir(ModeloCuento cuento) {

        Intent intent = new Intent(requireContext(), ReproductorAudiolibroActivity.class);

        intent.putExtra("cuento_id", cuento.getId());
        intent.putExtra("cuento_titulo", cuento.getTitulo());
        intent.putExtra("cuento_autor", cuento.getAutor());
        intent.putExtra("cuento_url", cuento.getPdfUrl());
        Log.d("BibliotecaFragment", "PDF URL: " + cuento.getPdfUrl());


        startActivity(intent);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}



