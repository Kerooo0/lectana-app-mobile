package com.example.lectana.estudiante.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.ActividadesPorAulaResponse;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.CuentosResponse;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.CuentosApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InicioFragment extends Fragment {
    private static final String TAG = "InicioFragment";

    private CuentosApiService cuentosApiService;
    private ActividadesApiService actividadesApiService;
    private SessionManager sessionManager;
    
    // Vistas - Novedad
    private CardView cardNovedad;
    private TextView textoNovedad;
    private Button botonVerLectura;
    
    // Vistas - Actividad pendiente
    private CardView cardActividadPendiente;
    private LinearLayout contenedorActividadPendiente;
    private TextView textoActividadPendiente;
    private TextView nombreActividadPendiente;
    private Button botonHacerActividad;
    
    // Vistas - Cuentos recomendados
    private LinearLayout contenedorCuentosAsignados;
    private ProgressBar progressBarCuentos;
    private TextView textoCuentosVacio;
    
    // Vistas - Continuar lectura (ocultamos por ahora)
    private CardView cardContinuarLectura;
    
    private CuentoApi cuentoMasReciente;
    private Actividad actividadPendiente;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        // Inicializar servicios
        cuentosApiService = ApiClient.getCuentosApiService();
        actividadesApiService = ApiClient.getClient().create(ActividadesApiService.class);
        sessionManager = new SessionManager(requireContext());

        // Inicializar vistas
        inicializarVistas(root);

        // Cargar datos reales
        cargarCuentoMasReciente();
        cargarActividadesPendientes();
        cargarCuentosRecomendados();

        return root;
    }

    private void inicializarVistas(View root) {
        // Novedad
        cardNovedad = root.findViewById(R.id.card_novedad);
        textoNovedad = root.findViewById(R.id.texto_novedad);
        botonVerLectura = root.findViewById(R.id.boton_ver_lectura);
        
        // Actividad pendiente
        cardActividadPendiente = root.findViewById(R.id.card_actividad_pendiente);
        contenedorActividadPendiente = root.findViewById(R.id.contenedor_actividad_pendiente);
        textoActividadPendiente = root.findViewById(R.id.texto_actividad_pendiente);
        nombreActividadPendiente = root.findViewById(R.id.nombre_actividad_pendiente);
        botonHacerActividad = root.findViewById(R.id.boton_hacer_actividad);
        
        // Cuentos recomendados
        contenedorCuentosAsignados = root.findViewById(R.id.contenedor_cuentos_asignados);
        progressBarCuentos = root.findViewById(R.id.progress_bar_cuentos_asignados);
        textoCuentosVacio = root.findViewById(R.id.texto_cuentos_vacio);
        
        // Ocultar secciones hardcodeadas por ahora
        cardContinuarLectura = root.findViewById(R.id.card_continuar_lectura);
        if (cardContinuarLectura != null) {
            cardContinuarLectura.setVisibility(View.GONE);
        }
        
        // Botón "Ver todos" para ir a la biblioteca
        View botonVerTodos = root.findViewById(R.id.boton_ver_todos_cuentos);
        if (botonVerTodos != null) {
            botonVerTodos.setOnClickListener(v -> irABiblioteca());
        }
    }

    private void cargarCuentoMasReciente() {
        Log.d(TAG, "Cargando cuento más reciente...");
        
        // Cargar el cuento más reciente (página 1, 1 resultado, ordenado por fecha descendente)
        Call<ApiResponse<CuentosResponse>> call = cuentosApiService.getCuentosPublicos(
            1, 1, null, null, null, null
        );
        
        call.enqueue(new Callback<ApiResponse<CuentosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CuentosResponse>> call, Response<ApiResponse<CuentosResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    CuentosResponse cuentosResponse = response.body().getData();
                    if (cuentosResponse != null && cuentosResponse.getCuentos() != null && !cuentosResponse.getCuentos().isEmpty()) {
                        cuentoMasReciente = cuentosResponse.getCuentos().get(0);
                        mostrarNovedad();
                        Log.d(TAG, "Cuento más reciente cargado: " + cuentoMasReciente.getTitulo());
                    } else {
                        ocultarNovedad();
                        Log.w(TAG, "No hay cuentos disponibles");
                    }
                } else {
                    ocultarNovedad();
                    Log.w(TAG, "Error al cargar cuento: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<CuentosResponse>> call, Throwable t) {
                ocultarNovedad();
                Log.e(TAG, "Error de conexión al cargar cuento: " + t.getMessage());
            }
        });
    }

    private void mostrarNovedad() {
        if (cardNovedad != null && cuentoMasReciente != null) {
            cardNovedad.setVisibility(View.VISIBLE);
            
            String mensaje = "Se ha añadido un nuevo texto: " + cuentoMasReciente.getTitulo();
            if (textoNovedad != null) {
                textoNovedad.setText(mensaje);
            }
            
            if (botonVerLectura != null) {
                botonVerLectura.setOnClickListener(v -> abrirDetalleCuento(cuentoMasReciente));
            }
        }
    }

    private void ocultarNovedad() {
        if (cardNovedad != null) {
            cardNovedad.setVisibility(View.GONE);
        }
    }

    private void cargarActividadesPendientes() {
        Log.d(TAG, "Cargando actividades pendientes...");
        
        int aulaId = sessionManager.getAulaId();
        String token = "Bearer " + sessionManager.getToken();
        
        if (aulaId == 0) {
            mostrarSinAulaAsignada();
            Log.w(TAG, "No hay aula asignada - mostrando mensaje");
            return;
        }
        
        actividadesApiService.getActividadesPorAula(token, aulaId).enqueue(new Callback<ActividadesPorAulaResponse>() {
            @Override
            public void onResponse(Call<ActividadesPorAulaResponse> call, Response<ActividadesPorAulaResponse> response) {
                Log.d(TAG, "Respuesta actividades - Código: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    ActividadesPorAulaResponse data = response.body();
                    List<Actividad> actividades = data.getActividades();
                    
                    Log.d(TAG, "Actividades recibidas: " + (actividades != null ? actividades.size() : "null"));
                    
                    if (actividades != null && !actividades.isEmpty()) {
                        // Buscar la primera actividad pendiente
                        actividadPendiente = actividades.get(0); // Usamos la primera por ahora
                        mostrarActividadPendiente(actividades.size());
                        Log.d(TAG, "Actividades pendientes encontradas: " + actividades.size());
                    } else {
                        mostrarTodasActividadesCompletadas();
                        Log.d(TAG, "No hay actividades pendientes - mostrando tarjeta de completado");
                    }
                } else if (response.code() == 401) {
                    cerrarSesionYRedireccionar();
                } else {
                    // Incluso si hay error, mostrar que no hay actividades
                    mostrarTodasActividadesCompletadas();
                    Log.w(TAG, "Error al cargar actividades: " + response.code() + " - mostrando completado");
                }
            }
            
            @Override
            public void onFailure(Call<ActividadesPorAulaResponse> call, Throwable t) {
                // En caso de fallo, mostrar que no hay actividades
                mostrarTodasActividadesCompletadas();
                Log.e(TAG, "Error de conexión al cargar actividades: " + t.getMessage());
            }
        });
    }

    private void mostrarActividadPendiente(int totalPendientes) {
        if (cardActividadPendiente != null && actividadPendiente != null) {
            cardActividadPendiente.setVisibility(View.VISIBLE);
            
            // Fondo naranja para actividades pendientes
            if (contenedorActividadPendiente != null) {
                contenedorActividadPendiente.setBackgroundColor(
                    getResources().getColor(R.color.naranjaPastel, null)
                );
            }
            
            String mensaje = totalPendientes == 1 ? 
                "Tenés 1 actividad pendiente" : 
                "Tenés " + totalPendientes + " actividades pendientes";
            
            if (textoActividadPendiente != null) {
                textoActividadPendiente.setText(mensaje);
            }
            
            if (nombreActividadPendiente != null) {
                nombreActividadPendiente.setVisibility(View.VISIBLE);
                String nombre = actividadPendiente.getDescripcion() != null ? 
                    actividadPendiente.getDescripcion() : "Actividad";
                nombreActividadPendiente.setText(nombre);
            }
            
            if (botonHacerActividad != null) {
                botonHacerActividad.setVisibility(View.VISIBLE);
                botonHacerActividad.setText("Hacer");
                botonHacerActividad.setOnClickListener(v -> irAActividades());
            }
        }
    }

    private void mostrarTodasActividadesCompletadas() {
        if (cardActividadPendiente != null) {
            cardActividadPendiente.setVisibility(View.VISIBLE);
            
            // Fondo verde para todas completadas
            if (contenedorActividadPendiente != null) {
                contenedorActividadPendiente.setBackgroundColor(
                    getResources().getColor(R.color.verdeClaro, null)
                );
            }
            
            if (textoActividadPendiente != null) {
                textoActividadPendiente.setText("✅ ¡Genial! Has completado todas las actividades");
            }
            
            if (nombreActividadPendiente != null) {
                nombreActividadPendiente.setVisibility(View.GONE);
            }
            
            if (botonHacerActividad != null) {
                botonHacerActividad.setVisibility(View.VISIBLE);
                botonHacerActividad.setText("Ver todas");
                botonHacerActividad.setOnClickListener(v -> irAActividades());
            }
        }
    }

    private void mostrarSinAulaAsignada() {
        if (cardActividadPendiente != null) {
            cardActividadPendiente.setVisibility(View.VISIBLE);
            
            // Fondo azul claro para sin aula
            if (contenedorActividadPendiente != null) {
                contenedorActividadPendiente.setBackgroundColor(
                    getResources().getColor(R.color.celeste_claro, null)
                );
            }
            
            if (textoActividadPendiente != null) {
                textoActividadPendiente.setText("📚 Aún no estás en un aula");
            }
            
            if (nombreActividadPendiente != null) {
                nombreActividadPendiente.setVisibility(View.VISIBLE);
                nombreActividadPendiente.setText("Pedile a tu docente el código de acceso");
            }
            
            if (botonHacerActividad != null) {
                botonHacerActividad.setVisibility(View.VISIBLE);
                botonHacerActividad.setText("Unirse");
                botonHacerActividad.setOnClickListener(v -> irAUnirseAula());
            }
        }
    }

    private void ocultarActividadPendiente() {
        if (cardActividadPendiente != null) {
            cardActividadPendiente.setVisibility(View.GONE);
        }
    }

    private void cargarCuentosRecomendados() {
        Log.d(TAG, "Cargando cuentos recomendados...");
        mostrarCargandoCuentos(true);

        Call<ApiResponse<CuentosResponse>> call = cuentosApiService.getCuentosPublicos(
            1, 10, null, null, null, null
        );
        
        call.enqueue(new Callback<ApiResponse<CuentosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CuentosResponse>> call, Response<ApiResponse<CuentosResponse>> response) {
                if (!isAdded()) return; // Check if fragment is still attached
                
                mostrarCargandoCuentos(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    CuentosResponse cuentosResponse = response.body().getData();
                    if (cuentosResponse != null && cuentosResponse.getCuentos() != null && !cuentosResponse.getCuentos().isEmpty()) {
                        mostrarCuentos(cuentosResponse.getCuentos());
                        Log.d(TAG, "Cuentos cargados: " + cuentosResponse.getCuentos().size());
                    } else {
                        mostrarMensajeCuentosVacio();
                        Log.w(TAG, "No hay cuentos disponibles");
                    }
                } else {
                    mostrarMensajeCuentosVacio();
                    Log.w(TAG, "Error al cargar cuentos: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse<CuentosResponse>> call, Throwable t) {
                if (!isAdded()) return; // Check if fragment is still attached
                
                mostrarCargandoCuentos(false);
                mostrarMensajeCuentosVacio();
                Log.e(TAG, "Error de conexión al cargar cuentos: " + t.getMessage());
            }
        });
    }

    private void mostrarCuentos(List<CuentoApi> cuentos) {
        if (contenedorCuentosAsignados == null) return;
        
        contenedorCuentosAsignados.removeAllViews();
        contenedorCuentosAsignados.setVisibility(View.VISIBLE);
        if (textoCuentosVacio != null) textoCuentosVacio.setVisibility(View.GONE);

        for (CuentoApi cuento : cuentos) {
            CardView cardCuento = crearTarjetaCuento(cuento);
            contenedorCuentosAsignados.addView(cardCuento);
        }
    }

    private CardView crearTarjetaCuento(CuentoApi cuento) {
        // Check if fragment is attached before creating views
        if (!isAdded() || getContext() == null) {
            return new CardView(requireActivity()); // Return empty card as fallback
        }
        
        CardView card = new CardView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 24);
        card.setLayoutParams(params);
        card.setRadius(12f);
        card.setCardElevation(4f);
        card.setUseCompatPadding(true);

        TextView textView = new TextView(getContext());
        String autor = cuento.getAutor() != null ? cuento.getAutor().getNombre() : "Autor desconocido";
        textView.setText(cuento.getTitulo() + "\n" + autor);
        textView.setPadding(32, 32, 32, 32);
        textView.setTextSize(16f);
        textView.setTextColor(getResources().getColor(R.color.gris_oscuro, null));
        
        card.addView(textView);
        card.setOnClickListener(v -> abrirDetalleCuento(cuento));
        
        return card;
    }

    private void abrirDetalleCuento(CuentoApi cuento) {
        if (!isAdded() || getContext() == null) return;
        
        Intent intent = new Intent(getContext(), DetalleCuentoActivity.class);
        intent.putExtra("cuento_id", cuento.getId_cuento());
        intent.putExtra("cuento_titulo", cuento.getTitulo());
        
        if (cuento.getAutor() != null) {
            intent.putExtra("cuento_autor", cuento.getAutor().getNombre());
        }
        if (cuento.getGenero() != null) {
            intent.putExtra("cuento_genero", cuento.getGenero().getNombre());
        }
        intent.putExtra("cuento_edad", cuento.getEdad_publico());
        
        // Duracion can be null, int, or string
        if (cuento.getDuracion() != null) {
            intent.putExtra("cuento_duracion", cuento.getDuracion().toString());
        }
        
        // CuentoApi doesn't have descripcion/resumen - skip it
        
        startActivity(intent);
    }

    private void mostrarCargandoCuentos(boolean mostrar) {
        if (progressBarCuentos != null) {
            progressBarCuentos.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    private void mostrarMensajeCuentosVacio() {
        if (contenedorCuentosAsignados != null) {
            contenedorCuentosAsignados.setVisibility(View.GONE);
        }
        if (textoCuentosVacio != null) {
            textoCuentosVacio.setVisibility(View.VISIBLE);
            textoCuentosVacio.setText("No hay cuentos disponibles en este momento");
        }
    }

    private void irABiblioteca() {
        if (getActivity() != null && getActivity() instanceof com.example.lectana.estudiante.PanelEstudianteActivity) {
            com.example.lectana.estudiante.PanelEstudianteActivity activity = 
                (com.example.lectana.estudiante.PanelEstudianteActivity) getActivity();
            
            androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragments, new BibliotecaFragment())
                .commit();
            
            activity.actualizarEstadoTabs("biblioteca");
        }
    }

    private void irAActividades() {
        if (getActivity() != null && getActivity() instanceof com.example.lectana.estudiante.PanelEstudianteActivity) {
            com.example.lectana.estudiante.PanelEstudianteActivity activity = 
                (com.example.lectana.estudiante.PanelEstudianteActivity) getActivity();
            
            androidx.fragment.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction()
                .replace(R.id.contenedor_fragments, new ActividadesEstudianteFragment())
                .commit();
            
            activity.actualizarEstadoTabs("actividades");
        }
    }

    private void irAUnirseAula() {
        // Mostrar diálogo para unirse a un aula
        if (getContext() == null) return;
        
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Unirse a un Aula");
        builder.setMessage("Para acceder a actividades y contenido, necesitas unirte a un aula con el código que te proporcionó tu docente.");
        
        // Input para el código
        final android.widget.EditText input = new android.widget.EditText(getContext());
        input.setHint("Código del aula");
        input.setInputType(android.text.InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        
        builder.setPositiveButton("Unirse", (dialog, which) -> {
            String codigoAula = input.getText().toString().trim();
            if (!codigoAula.isEmpty()) {
                // TODO: Implementar llamada al endpoint para unirse al aula
                Toast.makeText(getContext(), "Funcionalidad en desarrollo. Código: " + codigoAula, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Por favor ingresa un código válido", Toast.LENGTH_SHORT).show();
            }
        });
        
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void cerrarSesionYRedireccionar() {
        if (!isAdded() || getContext() == null) return;
        
        if (sessionManager != null) {
            sessionManager.clearSession();
        }
        
        Intent intent = new Intent(getContext(), com.example.lectana.Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    private void mostrarError(String mensaje) {
        if (getContext() != null) {
            Toast.makeText(getContext(), mensaje, Toast.LENGTH_LONG).show();
        }
    }
}


