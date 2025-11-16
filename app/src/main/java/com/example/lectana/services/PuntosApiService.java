package com.example.lectana.services;

import com.example.lectana.models.PerfilAlumnoResponse;
import com.example.lectana.models.PuntosResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PuntosApiService {
    
    /**
     * Obtener puntos del estudiante desde el perfil de alumno
     * GET /alumnos/obtener-perfil-alumno
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
