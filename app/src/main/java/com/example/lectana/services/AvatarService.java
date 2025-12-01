package com.example.lectana.services;

import com.example.lectana.modelos.Avatar;
import com.example.lectana.modelos.AvatarResponse;
import com.example.lectana.modelos.CompraAvatarResponse;
import com.example.lectana.modelos.ItemsResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AvatarService {
    private static AvatarService instance;
    private final ItemsApiService itemsApiService;
    private final AlumnoApiService alumnoApiService;

    private AvatarService() {
        this.itemsApiService = ApiClient.getItemsApiService();
        this.alumnoApiService = ApiClient.getAlumnoApiService();
    }

    public static synchronized AvatarService getInstance() {
        if (instance == null) {
            instance = new AvatarService();
        }
        return instance;
    }

    /**
     * Obtiene todos los avatares disponibles para compra (que el alumno NO ha comprado)
     */
    public void obtenerAvatareDisponibles(String token, OnAvatarListListener listener) {
        Call<ItemsResponse> call = itemsApiService.obtenerItemsDisponibles("Bearer " + token);

        call.enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Avatar> avatares = convertirItemsAvatares(response.body().getData());
                    listener.onSuccess(avatares);
                } else {
                    listener.onError("Error al obtener avatares disponibles");
                }
            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                listener.onError("Error de red: " + t.getMessage());
            }
        });
    }

    /**
     * Obtiene todos los avatares que el alumno ya ha comprado
     */
    public void obtenerMisAvatares(String token, OnAvatarListListener listener) {
        Call<ItemsResponse> call = itemsApiService.obtenerMisItems("Bearer " + token);

        call.enqueue(new Callback<ItemsResponse>() {
            @Override
            public void onResponse(Call<ItemsResponse> call, Response<ItemsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Avatar> avatares = convertirItemsAvatares(response.body().getData());
                    android.util.Log.d("AvatarService", "Avatares cargados: " + avatares.size());
                    for (Avatar avatar : avatares) {
                        android.util.Log.d("AvatarService", "Avatar: " + avatar.getNombre() + " - ID: " + avatar.getIdItem());
                    }
                    listener.onSuccess(avatares);
                } else {
                    android.util.Log.e("AvatarService", "Error en respuesta: " + (response.body() == null ? "body null" : response.message()));
                    listener.onError("Error al obtener mis avatares");
                }
            }

            @Override
            public void onFailure(Call<ItemsResponse> call, Throwable t) {
                android.util.Log.e("AvatarService", "Error de red: " + t.getMessage(), t);
                listener.onError("Error de red: " + t.getMessage());
            }
        });
    }

    /**
     * Compra un avatar usando puntos del alumno
     */
    public void comprarAvatar(String token, int avatarId, OnCompraListener listener) {
        Call<CompraAvatarResponse> call = itemsApiService.comprarAvatar(avatarId, "Bearer " + token);

        call.enqueue(new Callback<CompraAvatarResponse>() {
            @Override
            public void onResponse(Call<CompraAvatarResponse> call, Response<CompraAvatarResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    listener.onSuccess(response.body());
                } else {
                    String error = "Error al comprar avatar";
                    if (response.body() != null) {
                        error = response.body().getError();
                    }
                    listener.onError(error);
                }
            }

            @Override
            public void onFailure(Call<CompraAvatarResponse> call, Throwable t) {
                listener.onError("Error de red: " + t.getMessage());
            }
        });
    }

    /**
     * Obtiene el perfil actual del alumno
     */
    public void obtenerPerfil(String token, OnPerfilListener listener) {
        Call<AlumnoApiService.PerfilAlumnoResponse> call = alumnoApiService
                .obtenerPerfilAlumno("Bearer " + token);

        call.enqueue(new Callback<AlumnoApiService.PerfilAlumnoResponse>() {
            @Override
            public void onResponse(Call<AlumnoApiService.PerfilAlumnoResponse> call, 
                                 Response<AlumnoApiService.PerfilAlumnoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    Object data = response.body().getData();
                    listener.onSuccess(
                            (String) ((Map) data).get("nombre"),
                            (String) ((Map) data).get("apellido"),
                            (String) ((Map) data).get("email"),
                            ((Number) ((Map) data).get("edad")).intValue(),
                            (String) ((Map) data).get("avatarActual"),
                            ((Number) ((Map) data).get("puntos")).intValue()
                    );
                } else {
                    listener.onError("Error al obtener perfil");
                }
            }

            @Override
            public void onFailure(Call<AlumnoApiService.PerfilAlumnoResponse> call, Throwable t) {
                listener.onError("Error de red: " + t.getMessage());
            }
        });
    }

    /**
     * Actualiza el perfil del alumno (para cambiar avatar actual)
     */
    public void actualizarAvatarActual(String token, int avatarItemId, OnActualizacionListener listener) {
        AlumnoApiService.ActualizarPerfilRequest request = new AlumnoApiService.ActualizarPerfilRequest();
        // Aquí se enviaría el ID del avatar si el backend lo soporta
        
        Call<AlumnoApiService.PerfilAlumnoResponse> call = alumnoApiService
                .actualizarPerfilAlumno("Bearer " + token, request);

        call.enqueue(new Callback<AlumnoApiService.PerfilAlumnoResponse>() {
            @Override
            public void onResponse(Call<AlumnoApiService.PerfilAlumnoResponse> call, 
                                 Response<AlumnoApiService.PerfilAlumnoResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    listener.onSuccess("Avatar actualizado exitosamente");
                } else {
                    listener.onError("Error al actualizar avatar");
                }
            }

            @Override
            public void onFailure(Call<AlumnoApiService.PerfilAlumnoResponse> call, Throwable t) {
                listener.onError("Error de red: " + t.getMessage());
            }
        });
    }

    // ============ Métodos auxiliares ============
    
    private List<Avatar> convertirItemsAvatares(List<ItemsResponse.Item> items) {
        List<Avatar> avatares = new ArrayList<>();
        if (items != null) {
            for (ItemsResponse.Item item : items) {
                Avatar avatar = new Avatar();
                avatar.setIdItem(item.getId());
                avatar.setNombre(item.getNombre());
                avatar.setDescripcion(item.getDescripcion());
                avatar.setPrecio(item.getPrecio());
                avatar.setUrlImagen(item.getUrlImagen());
                avatar.setDisponible(item.isDisponible());
                avatares.add(avatar);
            }
        }
        return avatares;
    }

    // ============ Interfaces de Callbacks ============

    public interface OnAvatarListListener {
        void onSuccess(List<Avatar> avatares);
        void onError(String error);
    }

    public interface OnCompraListener {
        void onSuccess(CompraAvatarResponse response);
        void onError(String error);
    }

    public interface OnPerfilListener {
        void onSuccess(String nombre, String apellido, String email, int edad, String avatarActual, int puntos);
        void onError(String error);
    }

    public interface OnActualizacionListener {
        void onSuccess(String mensaje);
        void onError(String error);
    }
}
