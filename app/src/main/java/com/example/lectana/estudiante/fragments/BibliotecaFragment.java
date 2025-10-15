package com.example.lectana.estudiante.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
    private CuentosApiService apiService;
    private ExecutorService executorService;
    private ProgressBar progressBar;

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

        EditText campoBusqueda = root.findViewById(R.id.campo_busqueda_biblioteca);
        ImageView botonFiltros = root.findViewById(R.id.boton_filtros_biblioteca);

        campoBusqueda.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { adaptador.filtrarPorTexto(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });

        View filtroTodos = root.findViewById(R.id.filtro_bibl_todos);
        View filtroClasicos = root.findViewById(R.id.filtro_bibl_clasicos);
        View filtroAventura = root.findViewById(R.id.filtro_bibl_aventura);
        View filtroFantasia = root.findViewById(R.id.filtro_bibl_fantasia);

        View.OnClickListener listenerFiltro = v -> {
            int id = v.getId();
            if (id == R.id.filtro_bibl_todos) adaptador.filtrarPorGenero(null);
            else if (id == R.id.filtro_bibl_clasicos) adaptador.filtrarPorGenero("Clásicos");
            else if (id == R.id.filtro_bibl_aventura) adaptador.filtrarPorGenero("Aventura");
            else if (id == R.id.filtro_bibl_fantasia) adaptador.filtrarPorGenero("Fantasía");
        };

        filtroTodos.setOnClickListener(listenerFiltro);
        filtroClasicos.setOnClickListener(listenerFiltro);
        filtroAventura.setOnClickListener(listenerFiltro);
        filtroFantasia.setOnClickListener(listenerFiltro);

        botonFiltros.setOnClickListener(v -> mostrarDialogoFiltros());

        // Cargar cuentos desde la API
        cargarCuentosDesdeAPI();
        return root;
    }

    private void cargarCuentosDesdeAPI() {
        android.util.Log.d("BibliotecaFragment", "Iniciando carga de cuentos desde API...");
        mostrarCargando(true);
        
        // Llamada directa sin executorService para mejor debugging
        Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call = 
            apiService.getCuentosPublicos(1, 50, null, null, null, null);
        
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
        final String[] generos = new String[]{"Todos", "Clásicos", "Aventura", "Fantasía"};
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Filtrar por género")
                .setItems(generos, (dialog, which) -> {
                    String sel = generos[which];
                    if ("Todos".equals(sel)) adaptador.filtrarPorGenero(null);
                    else adaptador.filtrarPorGenero(sel);
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
        startActivity(new android.content.Intent(requireContext(), ReproductorAudiolibroActivity.class));
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}



