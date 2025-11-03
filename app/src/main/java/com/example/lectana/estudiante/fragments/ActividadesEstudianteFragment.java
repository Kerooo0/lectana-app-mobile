package com.example.lectana.estudiante.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.estudiante.ResolverActividadActivity;
import com.example.lectana.estudiante.adaptadores.ActividadesEstudianteAdapter;
import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.RespuestaUsuario;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadesEstudianteFragment extends Fragment {
    private static final String TAG = "ActividadesEstudiante";
    
    private RecyclerView recyclerViewActividades;
    private ProgressBar progressBar;
    private LinearLayout layoutSinActividades;
    private TextView textSinActividades;
    private TextView textContadorActividades;
    private TextView textContadorPendientes;
    private TextView textContadorCompletadas;
    
    private ActividadesApiService actividadesApiService;
    private SessionManager sessionManager;
    private List<Actividad> listaActividades;
    private List<Actividad> actividadesPendientes;
    private List<Actividad> actividadesCompletadas;
    private ActividadesEstudianteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_actividades_estudiante, container, false);
        
        Log.d(TAG, "=== INICIANDO ActividadesEstudianteFragment ===");
        
        try {
            inicializarComponentes(root);
            configurarRecyclerView();
            cargarActividades();
        } catch (Exception e) {
            Log.e(TAG, "Error durante la inicialización", e);
            Toast.makeText(requireContext(), "Error inicializando actividades: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        return root;
    }

    private void inicializarComponentes(View root) {
        Log.d(TAG, "Inicializando componentes...");
        
        recyclerViewActividades = root.findViewById(R.id.recyclerViewActividades);
        progressBar = root.findViewById(R.id.progressBar);
        layoutSinActividades = root.findViewById(R.id.layoutSinActividades);
        textSinActividades = root.findViewById(R.id.textSinActividades);
        textContadorActividades = root.findViewById(R.id.textContadorActividades);
        textContadorPendientes = root.findViewById(R.id.textContadorPendientes);
        textContadorCompletadas = root.findViewById(R.id.textContadorCompletadas);
        
        sessionManager = new SessionManager(requireContext());
        actividadesApiService = ApiClient.getActividadesApiService();
        listaActividades = new ArrayList<>();
        actividadesPendientes = new ArrayList<>();
        actividadesCompletadas = new ArrayList<>();
        
        Log.d(TAG, "Componentes inicializados correctamente");
    }

    private void configurarRecyclerView() {
        Log.d(TAG, "Configurando RecyclerView...");
        
        adapter = new ActividadesEstudianteAdapter(listaActividades, new ActividadesEstudianteAdapter.OnActividadClickListener() {
            @Override
            public void onActividadClick(Actividad actividad) {
                Log.d(TAG, "Click en actividad: " + actividad.getId_actividad());
                abrirResolverActividad(actividad);
            }
        });
        
        recyclerViewActividades.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewActividades.setAdapter(adapter);
        
        Log.d(TAG, "RecyclerView configurado");
    }

    private void cargarActividades() {
        Log.d(TAG, "Cargando actividades...");
        mostrarCargando(true);
        
        int aulaId = sessionManager.getAulaId();
        int alumnoId = sessionManager.getAlumnoId();
        String token = "Bearer " + sessionManager.getToken();
        
        Log.d(TAG, "Aula ID: " + aulaId + ", Alumno ID: " + alumnoId);
        
        if (aulaId == 0) {
            mostrarCargando(false);
            Toast.makeText(requireContext(), "Error: No se encontró el aula del alumno", Toast.LENGTH_LONG).show();
            return;
        }
        
        actividadesApiService.getActividadesPorAula(token, aulaId).enqueue(new Callback<ApiResponse<List<Actividad>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Actividad>>> call, Response<ApiResponse<List<Actividad>>> response) {
                mostrarCargando(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    List<Actividad> actividades = response.body().getData();
                    Log.d(TAG, "Actividades cargadas: " + actividades.size());
                    
                    if (actividades != null && !actividades.isEmpty()) {
                        verificarActividadesCompletadas(actividades, alumnoId, token);
                    } else {
                        mostrarSinActividades(true);
                        actualizarContadores();
                    }
                } else {
                    Log.e(TAG, "Error en respuesta: " + response.code());
                    Toast.makeText(requireContext(), "Error al cargar actividades", Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<List<Actividad>>> call, Throwable t) {
                mostrarCargando(false);
                Log.e(TAG, "Error de conexión: " + t.getMessage());
                Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verificarActividadesCompletadas(List<Actividad> actividades, int alumnoId, String token) {
        Log.d(TAG, "Verificando actividades completadas...");
        
        actividadesPendientes.clear();
        actividadesCompletadas.clear();
        
        // Por ahora, necesitamos verificar cada actividad individualmente
        // En el futuro, podríamos tener un endpoint que devuelva el estado directamente
        for (Actividad actividad : actividades) {
            verificarSiActividadCompletada(actividad, alumnoId, token);
        }
    }

    private void verificarSiActividadCompletada(Actividad actividad, int alumnoId, String token) {
        actividadesApiService.getRespuestasAlumnoActividad(token, alumnoId, actividad.getId_actividad())
            .enqueue(new Callback<ApiResponse<List<RespuestaUsuario>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<RespuestaUsuario>>> call,
                                     Response<ApiResponse<List<RespuestaUsuario>>> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                        List<RespuestaUsuario> respuestas = response.body().getData();
                        
                        if (respuestas != null && respuestas.size() > 0) {
                            // Actividad completada
                            actividad.setCompletada(true);
                            actividadesCompletadas.add(actividad);
                        } else {
                            // Actividad pendiente
                            actividad.setCompletada(false);
                            actividadesPendientes.add(actividad);
                        }
                    } else {
                        // Si hay error, asumimos que está pendiente
                        actividad.setCompletada(false);
                        actividadesPendientes.add(actividad);
                    }
                    
                    // Actualizar lista cuando tengamos todas las actividades verificadas
                    if (actividadesPendientes.size() + actividadesCompletadas.size() == listaActividades.size()) {
                        actualizarListaActividades();
                    }
                }
                
                @Override
                public void onFailure(Call<ApiResponse<List<RespuestaUsuario>>> call, Throwable t) {
                    // En caso de error, asumir que está pendiente
                    actividad.setCompletada(false);
                    actividadesPendientes.add(actividad);
                    
                    if (actividadesPendientes.size() + actividadesCompletadas.size() == listaActividades.size()) {
                        actualizarListaActividades();
                    }
                }
            });
        
        listaActividades.add(actividad);
    }

    private void actualizarListaActividades() {
        Log.d(TAG, "Actualizando lista de actividades...");
        
        // Ordenar: primero pendientes, luego completadas
        listaActividades.clear();
        listaActividades.addAll(actividadesPendientes);
        listaActividades.addAll(actividadesCompletadas);
        
        adapter.notifyDataSetChanged();
        mostrarSinActividades(listaActividades.isEmpty());
        actualizarContadores();
        
        Log.d(TAG, "Lista actualizada - Total: " + listaActividades.size() + 
                 ", Pendientes: " + actividadesPendientes.size() + 
                 ", Completadas: " + actividadesCompletadas.size());
    }

    private void abrirResolverActividad(Actividad actividad) {
        if (actividad.isCompletada()) {
            Toast.makeText(requireContext(), "Esta actividad ya fue completada", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Intent intent = new Intent(requireContext(), ResolverActividadActivity.class);
        intent.putExtra("actividad_id", actividad.getId_actividad());
        intent.putExtra("actividad_descripcion", actividad.getDescripcion());
        intent.putExtra("actividad_tipo", actividad.getTipo());
        if (actividad.getCuento() != null) {
            intent.putExtra("cuento_titulo", actividad.getCuento().getTitulo());
        }
        startActivity(intent);
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        if (recyclerViewActividades != null) {
            recyclerViewActividades.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        }
    }

    private void mostrarSinActividades(boolean mostrar) {
        if (layoutSinActividades != null) {
            layoutSinActividades.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        if (recyclerViewActividades != null) {
            recyclerViewActividades.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        }
    }

    private void actualizarContadores() {
        if (textContadorActividades != null) {
            textContadorActividades.setText("Total: " + listaActividades.size() + " actividades");
        }
        if (textContadorPendientes != null) {
            textContadorPendientes.setText("Pendientes: " + actividadesPendientes.size());
        }
        if (textContadorCompletadas != null) {
            textContadorCompletadas.setText("Completadas: " + actividadesCompletadas.size());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar actividades al volver (por si completó alguna)
        cargarActividades();
    }
}
