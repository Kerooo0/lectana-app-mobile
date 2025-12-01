package com.example.lectana.services;

import com.example.lectana.models.ApiResponse;
import com.example.lectana.models.ItemsResponse;
import com.example.lectana.models.CompraAvatarResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ItemsApiService {
    
    /**
     * Obtiene items disponibles en la tienda (que el alumno NO ha comprado)
     */
    @GET("items/disponibles")
    Call<ItemsResponse> obtenerItemsDisponibles(
            @Header("Authorization") String token
    );

    /**
     * Obtiene items por tipo (avatar, marco, fondo, etc)
     */
    @GET("items/tipo/{tipo}")
    Call<ItemsResponse> obtenerItemsPorTipo(
            @Header("Authorization") String token,
            @Path("tipo") String tipo
    );

    /**
     * Obtiene items que el alumno YA compró
     */
    @GET("items/mis-items")
    Call<ItemsResponse> obtenerMisItems(
            @Header("Authorization") String token
    );

    /**
     * Compra un avatar usando puntos
     */
    @POST("items/comprar/{id}")
    Call<CompraAvatarResponse> comprarAvatar(
            @Path("id") int id,
            @Header("Authorization") String token
    );
}
