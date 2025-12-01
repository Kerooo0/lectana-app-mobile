package com.example.lectana.services;

import com.example.lectana.modelos.PerfilAlumnoResponse;
import com.example.lectana.modelos.PuntosResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PuntosApiService {
    
    /**
     * Obtener puntos del estudiante autenticado
     * GET /api/puntos/mis-puntos
     * Este es el endpoint correcto para obtener puntos
     */
    @GET("puntos/mis-puntos")
    Call<PuntosResponse> obtenerMisPuntos(@Header("Authorization") String token);
    
    /**
     * Obtener perfil del alumno (para información adicional, no para puntos)
     * GET /alumnos/obtener-perfil-alumno
     * NOTA: Este endpoint NO devuelve puntos, usar obtenerMisPuntos() para eso
     */
    @GET("alumnos/obtener-perfil-alumno")
    Call<PerfilAlumnoResponse> obtenerPerfilAlumno(@Header("Authorization") String token);
    
    /**
     * Canjear/Actualizar puntos del estudiante
     * POST /puntos/canjear
     * Body: { "puntos": cantidad } (negativo para restar, positivo para sumar)
     */
    @POST("puntos/canjear")
    Call<PuntosResponse> canjearPuntos(
            @Header("Authorization") String token,
            @Body PuntosRequest request
    );
    
    public static class PuntosRequest {
        private int puntos;
        
        public PuntosRequest(int puntos) {
            this.puntos = puntos;
        }
        
        public int getPuntos() {
            return puntos;
        }
        
        public void setPuntos(int puntos) {
            this.puntos = puntos;
        }
    }
}
