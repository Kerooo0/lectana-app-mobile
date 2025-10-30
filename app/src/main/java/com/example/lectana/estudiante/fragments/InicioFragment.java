package com.example.lectana.estudiante.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.lectana.DetalleCuentoActivity;
import com.example.lectana.R;
import com.example.lectana.ReproductorAudiolibroActivity;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ModeloCuento;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.CuentosApiService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {

    private CuentosApiService apiService;
    private ExecutorService executorService;
    private SessionManager sessionManager;
    private LinearLayout contenedorCuentosAsignados;
    private ProgressBar progressBarCuentos;
    private TextView textoCuentosVacio;
    private List<ModeloCuento> cuentosAsignados = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Inicializar servicios
        apiService = ApiClient.getCuentosApiService();
        executorService = Executors.newSingleThreadExecutor();
        sessionManager = new SessionManager(requireContext());

        // Referencias de vistas
        contenedorCuentosAsignados = root.findViewById(R.id.contenedor_cuentos_asignados);
        progressBarCuentos = root.findViewById(R.id.progress_bar_cuentos_asignados);
        textoCuentosVacio = root.findViewById(R.id.texto_cuentos_vacio);

        View botonPlay = root.findViewById(R.id.boton_reproducir_lectura);
        if (botonPlay != null) {
            botonPlay.setOnClickListener(v -> {
                if (getContext() != null) {
                    startActivity(new Intent(getContext(), ReproductorAudiolibroActivity.class));
                }
            });
        }

        // Botón "Ver todos" para ir a la biblioteca completa
        View botonVerTodos = root.findViewById(R.id.boton_ver_todos_cuentos);
        if (botonVerTodos != null) {
            botonVerTodos.setOnClickListener(v -> {
                // Cambiar al tab de Biblioteca
                if (getActivity() != null && getActivity() instanceof com.example.lectana.estudiante.PanelEstudianteActivity) {
                    com.example.lectana.estudiante.PanelEstudianteActivity activity = 
                        (com.example.lectana.estudiante.PanelEstudianteActivity) getActivity();
                    
                    // Cambiar al fragment de Biblioteca
                    androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                        .replace(R.id.contenedor_fragments, new BibliotecaFragment())
                        .commit();
                    
                    // Actualizar el estado de los tabs
                    activity.actualizarEstadoTabs("biblioteca");
                }
            });
        }

        // Cargar cuentos disponibles para explorar
        cargarCuentosAsignados();

        return root;
    }

    private void cargarCuentosAsignados() {
        android.util.Log.d("InicioFragment", "Cargando cuentos disponibles para explorar...");
        mostrarCargando(true);

        executorService.execute(() -> {
            try {
                // Cargar cuentos públicos disponibles (máximo 10 para la vista de inicio)
                Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call = 
                    apiService.getCuentosPublicos(1, 10, null, null, null, null);
                
                call.enqueue(new Callback<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>>() {
                    @Override
                    public void onResponse(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, 
                                         Response<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> response) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                mostrarCargando(false);
                                
                                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                                    com.example.lectana.modelos.CuentosResponse cuentosResponse = response.body().getData();
                                    if (cuentosResponse != null && cuentosResponse.getCuentos() != null && !cuentosResponse.getCuentos().isEmpty()) {
                                        cuentosAsignados.clear();
                                        for (com.example.lectana.modelos.CuentoApi cuentoApi : cuentosResponse.getCuentos()) {
                                            cuentosAsignados.add(cuentoApi.toModeloCuento());
                                        }
                                        mostrarCuentosAsignados();
                                        android.util.Log.d("InicioFragment", "Cuentos cargados: " + cuentosAsignados.size());
                                    } else {
                                        android.util.Log.w("InicioFragment", "No hay cuentos disponibles");
                                        mostrarMensajeVacio();
                                    }
                                } else {
                                    android.util.Log.w("InicioFragment", "API error: " + response.code());
                                    mostrarMensajeVacio();
                                }
                            });
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentosResponse>> call, Throwable t) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                mostrarCargando(false);
                                android.util.Log.e("InicioFragment", "Error de conexión: " + t.getMessage());
                                mostrarMensajeVacio();
                            });
                        }
                    }
                });
            } catch (Exception e) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        mostrarCargando(false);
                        android.util.Log.e("InicioFragment", "Error inesperado: " + e.getMessage());
                        mostrarError("Error inesperado: " + e.getMessage());
                    });
                }
            }
        });
    }

    private void mostrarCuentosAsignados() {
        if (contenedorCuentosAsignados == null) return;
        
        contenedorCuentosAsignados.removeAllViews();
        contenedorCuentosAsignados.setVisibility(View.VISIBLE);
        if (textoCuentosVacio != null) textoCuentosVacio.setVisibility(View.GONE);

        for (ModeloCuento cuento : cuentosAsignados) {
            CardView cardCuento = crearTarjetaCuento(cuento);
            contenedorCuentosAsignados.addView(cardCuento);
        }
    }

    private CardView crearTarjetaCuento(ModeloCuento cuento) {
        CardView card = new CardView(requireContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(12f);
        card.setCardElevation(4f);
        card.setUseCompatPadding(true);

        TextView textView = new TextView(requireContext());
        textView.setText(cuento.getTitulo() + "\n" + cuento.getAutor());
        textView.setPadding(32, 32, 32, 32);
        textView.setTextSize(16f);
        textView.setTextColor(getResources().getColor(R.color.gris_oscuro, null));
        
        card.addView(textView);
        card.setOnClickListener(v -> abrirDetalleCuento(cuento));
        
        return card;
    }

    private void abrirDetalleCuento(ModeloCuento cuento) {
        Intent intent = new Intent(requireContext(), DetalleCuentoActivity.class);
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
        if (progressBarCuentos != null) {
            progressBarCuentos.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    private void mostrarMensajeVacio() {
        if (contenedorCuentosAsignados != null) {
            contenedorCuentosAsignados.setVisibility(View.GONE);
        }
        if (textoCuentosVacio != null) {
            textoCuentosVacio.setVisibility(View.VISIBLE);
            textoCuentosVacio.setText("No hay cuentos disponibles en este momento");
        }
    }

    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}


