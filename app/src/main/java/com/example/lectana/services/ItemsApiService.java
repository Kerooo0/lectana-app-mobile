package com.example.lectana.services;

import com.example.lectana.models.ApiResponse;
import com.example.lectana.models.ItemsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ItemsApiService {
    
    /**
     * Obtiene todos los items disponibles
     */
    @GET("items")
    Call<ItemsResponse> obtenerItems(
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
     * Obtiene items por categoría
     */
    @GET("items/categoria/{categoria}")
    Call<ItemsResponse> obtenerItemsPorCategoria(
            @Header("Authorization") String token,
            @Path("categoria") String categoria
    );

    /**
     * Obtiene items desbloqueados del alumno
     */
    @GET("items/desbloqueados")
    Call<ItemsResponse> obtenerItemsDesbloqueados(
            @Header("Authorization") String token
    );

    /**
     * Desbloquea un item (compra con puntos)
     */
    @POST("items/desbloquear")
    Call<ApiResponse<Object>> desbloquearItem(
            @Header("Authorization") String token,
            @Body DesbloquearItemRequest request
    );

    class DesbloquearItemRequest {
        private String item_id;

        public DesbloquearItemRequest(String itemId) {
            this.item_id = itemId;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }
    }
}
