package com.example.lectana.estudiante.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

import com.example.lectana.CambiarPasswordEstudianteActivity;
import com.example.lectana.EditarPerfilEstudianteActivity;
import com.example.lectana.Login;
import com.example.lectana.R;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.centro_ayuda;
import com.example.lectana.estudiante.adapters.AvatarSeleccionAdapter;
import com.example.lectana.estudiante.adapters.LogrosAdapter;
import com.example.lectana.modelos.Item;
import com.example.lectana.modelos.ItemsResponse;
import com.example.lectana.modelos.Logro;
import com.example.lectana.modelos.LogrosResponse;
import com.example.lectana.modelos.PerfilAlumnoResponse;
import com.example.lectana.modelos.PuntosResponse;
import com.example.lectana.modelos.EstadisticasLogrosResponse;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.ItemsApiService;
import com.example.lectana.services.LogrosApiService;
import com.example.lectana.services.PuntosApiService;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragment extends Fragment {

    private static final String TAG = "PerfilFragment";
    private static final String PREFS_NAME = "AvatarPreferences";
    private static final String KEY_AVATAR_ID = "avatar_id";
    private static final String KEY_AVATAR_URL = "avatar_url";

    private ImageView fotoPerfilEstudiante;
    private ImageView botonEditarFoto;
    private TextView nombreEstudiantePerfil;
    private TextView nivelEstudiantePerfil;
    private TextView puntosEstudiantePerfil;
    private LinearLayout opcionEditarDatos;
    private LinearLayout opcionCambiarPassword;
    private LinearLayout opcionCambiarAula;
    private LinearLayout opcionCentroAyuda;
    private LinearLayout opcionCerrarSesion;
    private View botonIrTienda;
    private RecyclerView recyclerViewLogros;
    private ProgressBar progressBarLogros;
    
    // Gestión de sesión
    private SessionManager sessionManager;
    
    // Adapter y API
    private LogrosAdapter logrosAdapter;
    private LogrosApiService logrosApiService;
    private ItemsApiService itemsApiService;
    private PuntosApiService puntosApiService;
    private String avatarEquipadoActual;
    
    // Puntos del estudiante
    private int puntosActuales = 0;
    
    // Track de logros desbloqueados para notificaciones
    private int logrosDesbloqueadosAnteriores = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar gestión de sesión y API
        sessionManager = new SessionManager(requireContext());
        logrosApiService = ApiClient.getLogrosApiService();
        itemsApiService = ApiClient.getItemsApiService();
        puntosApiService = ApiClient.getPuntosApiService();
        
        inicializarVistas(view);
        configurarListeners();
        configurarRecyclerViewLogros();
        cargarDatosPerfil();
        cargarPuntosReales();
        cargarEstadisticasLogros();
        cargarLogros();
        cargarAvatarGuardado();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        // Verificar si se compró un nuevo avatar en TiendaFragment
        SharedPreferences prefs = requireContext().getSharedPreferences("avatar_prefs", android.content.Context.MODE_PRIVATE);
        if (prefs.getBoolean("should_refresh_avatars", false)) {
            Log.d(TAG, "Refrescando avatares después de compra en PerfilFragment");
            // Limpiar el flag
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("should_refresh_avatars", false);
            editor.apply();
        }
        
        // Recargar datos del perfil y logros al volver
        cargarDatosPerfil();
        cargarPuntosReales();
        cargarEstadisticasLogros();
        cargarLogros();
        cargarAvatarGuardado();
    }

    private void inicializarVistas(View view) {
        fotoPerfilEstudiante = view.findViewById(R.id.foto_perfil_estudiante);
        botonEditarFoto = view.findViewById(R.id.boton_editar_foto);
        nombreEstudiantePerfil = view.findViewById(R.id.nombre_estudiante_perfil);
        nivelEstudiantePerfil = view.findViewById(R.id.nivel_estudiante_perfil);
        puntosEstudiantePerfil = view.findViewById(R.id.puntos_estudiante_perfil);
        opcionEditarDatos = view.findViewById(R.id.opcion_editar_datos);
        opcionCambiarPassword = view.findViewById(R.id.opcion_cambiar_password);
        opcionCambiarAula = view.findViewById(R.id.opcion_cambiar_aula);
        opcionCentroAyuda = view.findViewById(R.id.opcion_centro_ayuda);
        opcionCerrarSesion = view.findViewById(R.id.opcion_cerrar_sesion);
        botonIrTienda = view.findViewById(R.id.boton_ir_tienda);
        recyclerViewLogros = view.findViewById(R.id.recycler_view_logros);
        progressBarLogros = view.findViewById(R.id.progress_bar_logros);
    }

    private void configurarRecyclerViewLogros() {
        logrosAdapter = new LogrosAdapter();
        recyclerViewLogros.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerViewLogros.setAdapter(logrosAdapter);
    }

    private void configurarListeners() {
        // Botón editar foto - Mostrar bottom sheet de selección
        botonEditarFoto.setOnClickListener(v -> mostrarBottomSheetSeleccionAvatar());

        // Editar Datos Personales - REMOVIDO según requisito del cliente
        // Solo se permite cambiar la contraseña, no los datos personales
        opcionEditarDatos.setVisibility(View.GONE);

        // Opciones de configuración
        opcionCambiarPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CambiarPasswordEstudianteActivity.class);
            startActivity(intent);
        });

        opcionCambiarAula.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), com.example.lectana.CambiarAulaActivity.class);
            startActivity(intent);
        });

        opcionCentroAyuda.setOnClickListener(v -> {
            // Navegar al centro de ayuda
            Intent intent = new Intent(getContext(), centro_ayuda.class);
            startActivity(intent);
        });

        opcionCerrarSesion.setOnClickListener(v -> {
            // Realizar logout usando SessionManager
            sessionManager.clearSession();
            Toast.makeText(getContext(), "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
            
            // Volver al login
            Intent intent = new Intent(getContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        // Botón ir a tienda
        botonIrTienda.setOnClickListener(v -> {
            // TODO: Implementar navegación a tienda
            Toast.makeText(getContext(), "Ir a Tienda", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarDatosPerfil() {
        try {
            // Cargar datos reales del estudiante desde la sesión
            org.json.JSONObject user = sessionManager.getUser();
            if (user != null) {
                String nombre = user.optString("nombre", "Estudiante");
                String apellido = user.optString("apellido", "");
                String nombreCompleto = nombre + (apellido.isEmpty() ? "" : " " + apellido);
                
                nombreEstudiantePerfil.setText(nombreCompleto);
                nivelEstudiantePerfil.setText("Estudiante");
            } else {
                // Datos por defecto si no hay sesión
                nombreEstudiantePerfil.setText("Estudiante");
                nivelEstudiantePerfil.setText("Nivel Básico");
            }
        } catch (Exception e) {
            // Datos por defecto en caso de error
            nombreEstudiantePerfil.setText("Estudiante");
            nivelEstudiantePerfil.setText("Nivel Básico");
        }
    }
    
    private void cargarPuntosReales() {
        String token = "Bearer " + sessionManager.getToken();
        
        puntosApiService.obtenerMisPuntos(token).enqueue(new Callback<PuntosResponse>() {
            @Override
            public void onResponse(Call<PuntosResponse> call, Response<PuntosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    PuntosResponse.PuntosData puntosData = response.body().getPuntos();
                    puntosActuales = puntosData.getPuntos();
                    puntosEstudiantePerfil.setText(puntosActuales + " puntos");
                    Log.d(TAG, "Puntos cargados: " + puntosActuales);
                } else {
                    Log.e(TAG, "Error al cargar puntos: " + response.message());
                    puntosEstudiantePerfil.setText("0 puntos");
                }
            }

            @Override
            public void onFailure(Call<PuntosResponse> call, Throwable t) {
                Log.e(TAG, "Error de red al cargar puntos", t);
                puntosEstudiantePerfil.setText("0 puntos");
            }
        });
    }
    
    private void cargarEstadisticasLogros() {
        String token = "Bearer " + sessionManager.getToken();
        
        logrosApiService.obtenerEstadisticasLogros(token).enqueue(new Callback<EstadisticasLogrosResponse>() {
            @Override
            public void onResponse(Call<EstadisticasLogrosResponse> call, Response<EstadisticasLogrosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    EstadisticasLogrosResponse.EstadisticasLogros stats = response.body().getData();
                    Log.d(TAG, "Estadísticas de logros - Total: " + stats.getTotalLogros() + 
                            ", Desbloqueados: " + stats.getDesbloqueados() + 
                            ", Progreso: " + stats.getProgresoPromedio() + "%");
                    
                    // Verificar si hay nuevos logros desbloqueados
                    if (logrosDesbloqueadosAnteriores >= 0 && stats.getDesbloqueados() > logrosDesbloqueadosAnteriores) {
                        int nuevosLogros = stats.getDesbloqueados() - logrosDesbloqueadosAnteriores;
                        mostrarNotificacionLogroDesbloqueado(nuevosLogros);
                    }
                    
                    logrosDesbloqueadosAnteriores = stats.getDesbloqueados();
                    
                    // TODO: Mostrar estadísticas en la UI si se agrega un TextView dedicado
                } else {
                    Log.e(TAG, "Error al cargar estadísticas de logros: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<EstadisticasLogrosResponse> call, Throwable t) {
                Log.e(TAG, "Error de red al cargar estadísticas de logros", t);
            }
        });
    }
    
    private void mostrarNotificacionLogroDesbloqueado(int cantidad) {
        String mensaje = cantidad == 1 
            ? "🎉 ¡Felicidades! Has desbloqueado un nuevo logro" 
            : "🎉 ¡Increíble! Has desbloqueado " + cantidad + " nuevos logros";
            
        new android.app.AlertDialog.Builder(requireContext())
            .setTitle("¡Logro Desbloqueado!")
            .setMessage(mensaje)
            .setPositiveButton("Ver mis logros", (dialog, which) -> {
                // Los logros ya están visibles en el fragmento
                Toast.makeText(getContext(), "Revisa tus logros desbloqueados abajo 👇", Toast.LENGTH_LONG).show();
            })
            .setNegativeButton("Cerrar", null)
            .setCancelable(true)
            .show();
            
        Log.d(TAG, "Notificación de logro mostrada: " + cantidad + " nuevos logros");
    }

    private void cargarLogros() {
        if (progressBarLogros != null) {
            progressBarLogros.setVisibility(View.VISIBLE);
        }
        if (recyclerViewLogros != null) {
            recyclerViewLogros.setVisibility(View.GONE);
        }

        String token = "Bearer " + sessionManager.getToken();

        // Usar obtenerLogrosDisponibles que trae TODOS los logros (bloqueados y desbloqueados)
        logrosApiService.obtenerLogrosDisponibles(token).enqueue(new Callback<LogrosResponse>() {
            @Override
            public void onResponse(Call<LogrosResponse> call, Response<LogrosResponse> response) {
                if (progressBarLogros != null) {
                    progressBarLogros.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Log.d(TAG, "Logros cargados exitosamente: " + response.body().getData().size());
                    if (logrosAdapter != null) {
                        logrosAdapter.setLogros(response.body().getData());
                    }
                    if (recyclerViewLogros != null) {
                        recyclerViewLogros.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e(TAG, "Error al cargar logros: " + response.message());
                    Toast.makeText(getContext(), "No se pudieron cargar los logros", Toast.LENGTH_SHORT).show();
                    if (recyclerViewLogros != null) {
                        recyclerViewLogros.setVisibility(View.VISIBLE); // Mostrar vacío
                    }
                }
            }

            @Override
            public void onFailure(Call<LogrosResponse> call, Throwable t) {
                if (progressBarLogros != null) {
                    progressBarLogros.setVisibility(View.GONE);
                }
                if (recyclerViewLogros != null) {
                    recyclerViewLogros.setVisibility(View.VISIBLE);
                }
                
                Log.e(TAG, "Error de red al cargar logros", t);
                Toast.makeText(getContext(), "Error de conexión. Verifica tu internet.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarBottomSheetSeleccionAvatar() {
        // Crear el BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_seleccionar_avatar, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        // Referencias a las vistas
        RecyclerView recyclerViewAvatares = bottomSheetView.findViewById(R.id.recycler_view_avatares);
        ProgressBar progressBarAvatares = bottomSheetView.findViewById(R.id.progress_bar_avatares);
        Button botonCancelar = bottomSheetView.findViewById(R.id.boton_cancelar);

        // Configurar RecyclerView
        AvatarSeleccionAdapter avatarAdapter = new AvatarSeleccionAdapter(new AvatarSeleccionAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(Item avatar) {
                // Al seleccionar un avatar
                seleccionarAvatar(avatar);
                bottomSheetDialog.dismiss();
            }

            @Override
            public void onRemoveAvatarClick() {
                // Al hacer click en "Quitar Avatar"
                quitarAvatar();
                bottomSheetDialog.dismiss();
            }
        });
        
        recyclerViewAvatares.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerViewAvatares.setAdapter(avatarAdapter);

        // Cargar avatares comprados (mis items)
        progressBarAvatares.setVisibility(View.VISIBLE);
        recyclerViewAvatares.setVisibility(View.GONE);

        String token = "Bearer " + sessionManager.getToken();
        Log.d(TAG, "Cargando mis items (avatares comprados)...");
        itemsApiService.obtenerMisItems(token).enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                progressBarAvatares.setVisibility(View.GONE);
                
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Log.d(TAG, "Respuesta exitosa, convirtiendo items...");
                    // Convertir los items de la respuesta
                    List<Item> avataresMios = new ArrayList<>();
                    List<Item> items = new ArrayList<>();
                    for (ItemsResponse.Item itemResponse : response.body().getData()) {
                        Item item = new Item();
                        item.setId(itemResponse.getId());
                        item.setNombre(itemResponse.getNombre());
                        item.setDescripcion(itemResponse.getDescripcion());
                        item.setPrecio(itemResponse.getPrecio());
                        item.setUrlImagen(itemResponse.getUrlImagen());
                        items.add(item);
                    }
                    avataresMios.addAll(items);
                    
                    Log.d(TAG, "Total de avatares comprados: " + avataresMios.size());
                    
                    // Siempre mostrar la opción de quitar avatar
                    avatarAdapter.setAvatares(avataresMios);
                    avatarAdapter.addRemoveAvatarOption();
                    avatarAdapter.setAvatarEquipado(avatarEquipadoActual);
                    recyclerViewAvatares.setVisibility(View.VISIBLE);
                    
                    if (avataresMios.isEmpty()) {
                        Log.d(TAG, "Sin avatares comprados, mostrando solo la opción de quitar avatar");
                    } else {
                        Log.d(TAG, "Mostrando " + avataresMios.size() + " avatares en el bottom sheet");
                        for (Item item : avataresMios) {
                            Log.d(TAG, "Avatar: " + item.getNombre() + " - ID: " + item.getId());
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Error al cargar avatares", Toast.LENGTH_SHORT).show();
                    bottomSheetDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                progressBarAvatares.setVisibility(View.GONE);
                Log.e(TAG, "Error al cargar avatares", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        // Botón cancelar
        botonCancelar.setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Mostrar el BottomSheet
        bottomSheetDialog.show();
    }

    private void seleccionarAvatar(Item avatar) {
        // Guardar el avatar seleccionado
        avatarEquipadoActual = avatar.getId();
        
        // Actualizar la imagen de perfil
        if (avatar.getUrlImagen() != null && !avatar.getUrlImagen().isEmpty()) {
            Glide.with(requireContext())
                    .load(avatar.getUrlImagen())
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(fotoPerfilEstudiante);
        }
        
        // Guardar en SharedPreferences
        guardarAvatarEnPreferencias(avatar.getId(), avatar.getUrlImagen());
        
        Toast.makeText(getContext(), "Avatar equipado: " + avatar.getNombre(), Toast.LENGTH_SHORT).show();
        
        // TODO: Llamar al backend para guardar el avatar equipado cuando se agregue el campo al schema
        // actualizarAvatarEnBackend(avatar.getId());
    }
    
    private void quitarAvatar() {
        // Establecer el avatar actual como vacío
        avatarEquipadoActual = "";
        
        // Restaurar la imagen de perfil al ícono predeterminado
        fotoPerfilEstudiante.setImageResource(R.drawable.ic_person);
        
        // Guardar en SharedPreferences como vacío
        guardarAvatarEnPreferencias("", "");
        
        Toast.makeText(getContext(), "Avatar desequipado", Toast.LENGTH_SHORT).show();
        
        Log.d(TAG, "Avatar desequipado");
        
        // TODO: Llamar al backend para limpiar el avatar equipado
        // actualizarAvatarEnBackend("");
    }
    
    private void guardarAvatarEnPreferencias(String avatarId, String avatarUrl) {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_AVATAR_ID, avatarId);
        editor.putString(KEY_AVATAR_URL, avatarUrl);
        editor.apply();
        Log.d(TAG, "Avatar guardado en SharedPreferences: " + avatarId);
    }
    
    private void cargarAvatarGuardado() {
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE);
        String avatarId = prefs.getString(KEY_AVATAR_ID, null);
        String avatarUrl = prefs.getString(KEY_AVATAR_URL, null);
        
        if (avatarId != null && avatarUrl != null) {
            avatarEquipadoActual = avatarId;
            Glide.with(requireContext())
                    .load(avatarUrl)
                    .placeholder(R.drawable.ic_person)
                    .error(R.drawable.ic_person)
                    .into(fotoPerfilEstudiante);
            Log.d(TAG, "Avatar cargado desde SharedPreferences: " + avatarId);
        }
    }
}
