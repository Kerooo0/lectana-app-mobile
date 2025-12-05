package com.example.lectana.estudiante.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.ArrayList;

import com.example.lectana.R;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.estudiante.adapters.ItemsAdapter;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.Item;
import com.example.lectana.modelos.ItemsResponse;
import com.example.lectana.modelos.CompraAvatarResponse;
import com.example.lectana.modelos.PerfilAlumnoResponse;
import com.example.lectana.modelos.PuntosResponse;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.ItemsApiService;
import com.example.lectana.services.PuntosApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiendaFragment extends Fragment implements ItemsAdapter.OnItemClickListener {

    private static final String TAG = "TiendaFragment";

    private TextView puntosUsuario;
    private RecyclerView recyclerViewItems;
    private ProgressBar progressBarItems;
    
    // Tabs de categorías
    private LinearLayout tabTodos;
    private LinearLayout tabAvatares;
    private LinearLayout tabInsignias;
    private LinearLayout tabMisAvatares;
    
    // API y gestión
    private SessionManager sessionManager;
    private ItemsApiService itemsApiService;
    private PuntosApiService puntosApiService;
    private ItemsAdapter itemsAdapter;
    
    private String categoriaActual = "todos";
    private int puntosActualesUsuario = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tienda_simple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Inicializar gestión de sesión y API
        sessionManager = new SessionManager(requireContext());
        itemsApiService = ApiClient.getItemsApiService();
        puntosApiService = ApiClient.getPuntosApiService();
        
        inicializarVistas(view);
        configurarListeners();
        configurarRecyclerView();
        cargarItems();
        actualizarEstadoTabs("todos");
    }

    private void inicializarVistas(View view) {
        puntosUsuario = view.findViewById(R.id.puntos_usuario_tienda);
        recyclerViewItems = view.findViewById(R.id.recycler_view_items);
        progressBarItems = view.findViewById(R.id.progress_bar_items);
        
        // Tabs
        tabTodos = view.findViewById(R.id.tab_todos);
        tabAvatares = view.findViewById(R.id.tab_avatares);
        tabInsignias = view.findViewById(R.id.tab_insignias);
        tabMisAvatares = view.findViewById(R.id.tab_mis_avatares);
        
        // Botón información
        Button botonInfoPuntos = view.findViewById(R.id.boton_info_puntos);
        if (botonInfoPuntos != null) {
            botonInfoPuntos.setOnClickListener(v -> mostrarInfoPuntos());
        }

        // Cargar puntos del usuario
        cargarPuntosUsuario();
    }

    private void configurarRecyclerView() {
        itemsAdapter = new ItemsAdapter(this);
        recyclerViewItems.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewItems.setAdapter(itemsAdapter);
    }

    private void configurarListeners() {
        if (tabTodos != null) {
            tabTodos.setOnClickListener(v -> {
                categoriaActual = "todos";
                actualizarEstadoTabs("todos");
                cargarItems();
            });
        }
        
        if (tabAvatares != null) {
            tabAvatares.setOnClickListener(v -> {
                categoriaActual = "avatar";
                actualizarEstadoTabs("avatares");
                cargarItemsPorTipo("avatar");
            });
        }
        
        if (tabInsignias != null) {
            tabInsignias.setOnClickListener(v -> {
                categoriaActual = "badge";
                actualizarEstadoTabs("insignias");
                cargarItemsPorTipo("badge");
            });
        }
        
        if (tabMisAvatares != null) {
            tabMisAvatares.setOnClickListener(v -> {
                categoriaActual = "mis_avatares";
                actualizarEstadoTabs("mis_avatares");
                cargarItemsDesbloqueados();
            });
        }
    }

    private void cargarPuntosUsuario() {
        String token = "Bearer " + sessionManager.getToken();
        
        puntosApiService.obtenerMisPuntos(token).enqueue(new Callback<PuntosResponse>() {
            @Override
            public void onResponse(Call<PuntosResponse> call, Response<PuntosResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    PuntosResponse.PuntosData puntosData = response.body().getPuntos();
                    puntosActualesUsuario = puntosData.getPuntos();
                    puntosUsuario.setText(puntosActualesUsuario + " puntos disponibles");
                    Log.d(TAG, "Puntos cargados: " + puntosActualesUsuario);
                } else {
                    Log.e(TAG, "Error al cargar puntos: " + response.message());
                    puntosUsuario.setText("0 puntos disponibles");
                }
            }

            @Override
            public void onFailure(Call<PuntosResponse> call, Throwable t) {
                Log.e(TAG, "Error de red al cargar puntos", t);
                puntosUsuario.setText("0 puntos disponibles");
            }
        });
    }

    /**
     * Carga items disponibles en la tienda (que el alumno NO ha comprado)
     */
    private void cargarItems() {
        if (progressBarItems != null) {
            progressBarItems.setVisibility(View.VISIBLE);
        }
        recyclerViewItems.setVisibility(View.GONE);

        String token = "Bearer " + sessionManager.getToken();

        itemsApiService.obtenerItemsDisponibles(token).enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Log.d(TAG, "Items disponibles cargados: " + response.body().getData().size());
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
                    itemsAdapter.setItems(items);
                    recyclerViewItems.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "Error al cargar items disponibles: " + response.message());
                    Toast.makeText(getContext(), "No hay items disponibles", Toast.LENGTH_SHORT).show();
                    recyclerViewItems.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }
                recyclerViewItems.setVisibility(View.VISIBLE);
                
                Log.e(TAG, "Error de red al cargar items disponibles", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Carga los items que el alumno YA compró
     */
    private void cargarMisItems() {
        if (progressBarItems != null) {
            progressBarItems.setVisibility(View.VISIBLE);
        }
        recyclerViewItems.setVisibility(View.GONE);

        String token = "Bearer " + sessionManager.getToken();

        itemsApiService.obtenerMisItems(token).enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Log.d(TAG, "Items por tipo cargados: " + response.body().getData().size());
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
                    itemsAdapter.setItems(items);
                    recyclerViewItems.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "Error al cargar items comprados: " + response.message());
                    Toast.makeText(getContext(), "No tienes items comprados", Toast.LENGTH_SHORT).show();
                    recyclerViewItems.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }
                recyclerViewItems.setVisibility(View.VISIBLE);
                
                Log.e(TAG, "Error de red al cargar items comprados", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarItemsPorTipo(String tipo) {
        if (progressBarItems != null) {
            progressBarItems.setVisibility(View.VISIBLE);
        }
        recyclerViewItems.setVisibility(View.GONE);

        String token = "Bearer " + sessionManager.getToken();

        // Use obtenerItemsDisponibles instead of obtenerItemsPorTipo (which returns 404)
        itemsApiService.obtenerItemsDisponibles(token).enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Log.d(TAG, "Items de tipo " + tipo + " cargados: " + response.body().getData().size());
                    // Convertir ItemsResponse.Item a Item
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
                    itemsAdapter.setItems(items);
                    recyclerViewItems.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "Error al cargar items por tipo: " + response.message());
                    Toast.makeText(getContext(), "No se encontraron items", Toast.LENGTH_SHORT).show();
                    recyclerViewItems.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }
                recyclerViewItems.setVisibility(View.VISIBLE);
                
                Log.e(TAG, "Error de red al cargar items por tipo", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarItemsDesbloqueados() {
        if (progressBarItems != null) {
            progressBarItems.setVisibility(View.VISIBLE);
        }
        recyclerViewItems.setVisibility(View.GONE);

        String token = "Bearer " + sessionManager.getToken();

        itemsApiService.obtenerMisItems(token).enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }

                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Log.d(TAG, "Items por búsqueda cargados: " + response.body().getData().size());
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
                    itemsAdapter.setItems(items);
                    recyclerViewItems.setVisibility(View.VISIBLE);
                } else {
                    Log.e(TAG, "Error al cargar items desbloqueados: " + response.message());
                    Toast.makeText(getContext(), "No tienes items desbloqueados", Toast.LENGTH_SHORT).show();
                    recyclerViewItems.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                if (progressBarItems != null) {
                    progressBarItems.setVisibility(View.GONE);
                }
                recyclerViewItems.setVisibility(View.VISIBLE);
                
                Log.e(TAG, "Error de red al cargar items desbloqueados", t);
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onComprarClick(Item item) {
        // Mostrar diálogo de confirmación
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirmar compra")
                .setMessage("¿Quieres comprar \"" + item.getNombre() + "\" por " + item.getPrecioPuntos() + " puntos?")
                .setPositiveButton("Comprar", (dialog, which) -> comprarItem(item))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onItemClick(Item item) {
        // Mostrar detalles del item
        Toast.makeText(getContext(), item.getNombre() + " - " + item.getDescripcion(), Toast.LENGTH_SHORT).show();
    }

    private void comprarItem(Item item) {
        // Verificar puntos suficientes
        if (puntosActualesUsuario < item.getPrecioPuntos()) {
            Toast.makeText(getContext(), "No tienes suficientes puntos", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String token = "Bearer " + sessionManager.getToken();
        
        // Usar el endpoint correcto: POST /api/items/comprar/{id}
        // El id en Item es String, convertir a int
        int itemId = Integer.parseInt(item.getId());
        itemsApiService.comprarAvatar(itemId, token).enqueue(new Callback<CompraAvatarResponse>() {
            @Override
            public void onResponse(Call<CompraAvatarResponse> call, Response<CompraAvatarResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    // Actualizar puntos locales desde la respuesta
                    puntosActualesUsuario = response.body().getPuntosActuales();
                    puntosUsuario.setText(puntosActualesUsuario + " puntos disponibles");
                    
                    String mensaje = "¡Comprado exitosamente! ";
                    if (response.body().getItem() != null) {
                        mensaje += response.body().getItem().nombre;
                    }
                    mensaje += " - Puntos restantes: " + puntosActualesUsuario;
                    
                    Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Compra exitosa: " + mensaje);
                    
                    // Establecer flag para que PerfilAlumnoActivity recargue avatares
                    SharedPreferences prefs = requireContext().getSharedPreferences("avatar_prefs", android.content.Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("should_refresh_avatars", true);
                    
                    // Si hay logros desbloqueados, establecer flag para refrescar logros
                    if (response.body().getLogrosDesbloqueados() != null && 
                        !response.body().getLogrosDesbloqueados().isEmpty()) {
                        editor.putBoolean("should_refresh_logros", true);
                        Log.d(TAG, "Flag 'should_refresh_logros' establecido a true - Logros desbloqueados: " + 
                              response.body().getLogrosDesbloqueados().size());
                    }
                    
                    editor.apply();
                    Log.d(TAG, "Flag 'should_refresh_avatars' establecido a true");
                    
                    // Recargar items disponibles
                    recargarItemsSegunCategoria();
                } else {
                    String error = "Error al procesar compra";
                    if (response.body() != null && response.body().getError() != null) {
                        error = response.body().getError();
                    } else if (response.body() != null && response.body().getMensaje() != null) {
                        error = response.body().getMensaje();
                    }
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CompraAvatarResponse> call, Throwable t) {
                Log.e(TAG, "Error al comprar item", t);
                Toast.makeText(getContext(), "Error de conexión al comprar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recargarItemsSegunCategoria() {
        switch (categoriaActual) {
            case "todos":
                cargarItems();
                break;
            case "avatar":
                cargarItemsPorTipo("avatar");
                break;
            case "badge":
                cargarItemsPorTipo("badge");
                break;
            case "mis_avatares":
                cargarItemsDesbloqueados();
                break;
        }
    }

    private void actualizarEstadoTabs(String tabSeleccionada) {
        // Reset all tabs
        if (tabTodos != null) {
            tabTodos.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        }
        if (tabAvatares != null) {
            tabAvatares.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        }
        if (tabInsignias != null) {
            tabInsignias.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        }
        if (tabMisAvatares != null) {
            tabMisAvatares.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        }
        
        // Activate selected tab
        switch (tabSeleccionada) {
            case "todos":
                if (tabTodos != null) tabTodos.setBackgroundResource(R.drawable.boton_pestana_activa);
                break;
            case "avatares":
                if (tabAvatares != null) tabAvatares.setBackgroundResource(R.drawable.boton_pestana_activa);
                break;
            case "insignias":
                if (tabInsignias != null) tabInsignias.setBackgroundResource(R.drawable.boton_pestana_activa);
                break;
            case "mis_avatares":
                if (tabMisAvatares != null) tabMisAvatares.setBackgroundResource(R.drawable.boton_pestana_activa);
                break;
        }
    }

    private void mostrarInfoPuntos() {
        new AlertDialog.Builder(requireContext())
                .setTitle("¿Cómo ganar puntos?")
                .setMessage("Gana puntos completando:\n\n" +
                        "• Cuentos leídos\n" +
                        "• Actividades completadas\n" +
                        "• Rachas de días consecutivos\n" +
                        "• Logros desbloqueados\n\n" +
                        "Usa tus puntos para desbloquear avatares, insignias y más!")
                .setPositiveButton("Entendido", null)
                .show();
    }
}
