package com.example.lectana.services;

import com.example.lectana.models.ApiResponse;
import com.example.lectana.models.EstadisticasLogrosResponse;
import com.example.lectana.models.LogrosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LogrosApiService {
    
    /**
     * Obtiene todos los logros disponibles con el progreso del alumno
     */
    @GET("logros/disponibles")
    Call<LogrosResponse> obtenerLogrosDisponibles(
            @Header("Authorization") String token
    );

    /**
     * Obtiene solo los logros desbloqueados del alumno
     */
    @GET("logros/mis-logros")
    Call<LogrosResponse> obtenerMisLogros(
            @Header("Authorization") String token
    );

    /**
     * Obtiene estadísticas de logros del alumno
     */
    @GET("logros/estadisticas")
    Call<EstadisticasLogrosResponse> obtenerEstadisticasLogros(
            @Header("Authorization") String token
    );
}
