package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorActividades;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ActividadAula;
import com.example.lectana.modelos.ActividadesPorAulaResponse;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.ApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadesAsignadasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewActividades;
    private ProgressBar progressBar;
    private AdaptadorActividades adaptador;
    private List<ActividadAula> actividades;
    private ActividadesApiService apiService;
    private SessionManager sessionManager;
    private ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_asignadas);

        inicializarVistas();
        sessionManager = new SessionManager(this);
        apiService = ApiClient.getActividadesApiService();
        executorService = Executors.newSingleThreadExecutor();
        actividades = new ArrayList<>();

        cargarActividades();
    }

    private void inicializarVistas() {
        recyclerViewActividades = findViewById(R.id.recycler_view_actividades);
        progressBar = new ProgressBar(this);

        recyclerViewActividades.setLayoutManager(new LinearLayoutManager(this));
        adaptador = new AdaptadorActividades(actividades, this::abrirActividad);
        recyclerViewActividades.setAdapter(adaptador);
    }

    private void cargarActividades() {
        mostrarCargando(true);
        String token = "Bearer " + sessionManager.getToken();
        int aulaId = sessionManager.getAulaId();

        executorService.execute(() -> {
            Call<ActividadesPorAulaResponse> call = apiService.getActividadesPorAula(token, aulaId);

            call.enqueue(new Callback<ActividadesPorAulaResponse>() {
                @Override
                public void onResponse(Call<ActividadesPorAulaResponse> call, Response<ActividadesPorAulaResponse> response) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        if (response.isSuccessful() && response.body() != null) {
                            ActividadesPorAulaResponse res = response.body();
                            if (res.getActividades() != null) {
                                actividades.clear();
                                actividades.addAll(res.getActividades());
                                adaptador.notifyDataSetChanged();
                                Log.d("Actividades", "Cargadas: " + actividades.size());
                            } else {
                                mostrarError("No hay actividades disponibles");
                            }
                        } else {
                            Log.e("Actividades", "Error: " + response.code());
                            mostrarError("Error al cargar actividades");
                        }
                    });
                }

                @Override
                public void onFailure(Call<ActividadesPorAulaResponse> call, Throwable t) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Log.e("Actividades", "Error: " + t.getMessage());
                        mostrarError("Error de conexión: " + t.getMessage());
                    });
                }
            });
        });
    }

    private void abrirActividad(ActividadAula actividad) {
        if (actividad == null || actividad.getActividad() == null) return;
        Intent intent = new Intent(this, ResolucionOpcionMultipleActivity.class);
        intent.putExtra("id_actividad", actividad.getActividad().getId_actividad());
        intent.putExtra("titulo_actividad", actividad.getActividad().getDescripcion());
        startActivity(intent);
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}


