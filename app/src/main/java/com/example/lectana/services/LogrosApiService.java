package com.example.lectana.services;

import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.EstadisticasLogrosResponse;
import com.example.lectana.modelos.LogrosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface LogrosApiService {
    
    /**
     * Obtiene todos los logros bloqueados (disponibles para desbloquear)
     * Backend endpoint: GET /api/logros/bloqueados
     */
    @GET("logros/bloqueados")
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
