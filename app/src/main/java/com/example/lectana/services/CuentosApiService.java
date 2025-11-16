package com.example.lectana.services;

import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.CuentosResponse;
import com.example.lectana.modelos.GeneroApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CuentosApiService {
    
    @GET("cuentos/publicos")
    Call<ApiResponse<CuentosResponse>> getCuentosPublicos(
        @Query("page") int page,
        @Query("limit") int limit,
        @Query("edad_publico") Integer edadPublico,
        @Query("genero_id") Integer generoId,
        @Query("autor_id") Integer autorId,
        @Query("titulo") String titulo
    );
    
    @GET("cuentos/publicos/{id}")
    Call<ApiResponse<CuentoApi>> getCuentoDetalle(@Path("id") int id);
    
    @GET("generos/publicos")
    Call<ApiResponse<List<GeneroApi>>> getGenerosPublicos();
    
    @GET("cuentos/asignados/alumno/{alumnoId}")
    Call<ApiResponse<CuentosResponse>> getCuentosAsignadosAlumno(@Path("alumnoId") int alumnoId);
}
