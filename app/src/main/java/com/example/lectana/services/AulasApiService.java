package com.example.lectana.services;

import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.AsignarCuentosRequest;
import com.example.lectana.modelos.AsignarCuentosResponse;
import com.example.lectana.modelos.CrearAulaRequest;
import com.example.lectana.modelos.ModeloAula;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AulasApiService {
    
    /**
     * Obtener todas las aulas del docente autenticado
     */
    @GET("aulas/docente")
    Call<ApiResponse<List<ModeloAula>>> getAulasDocente(@Header("Authorization") String token);
    
    /**
     * Obtener detalles de una aula específica
     */
    @GET("aulas/docente/{id}")
    Call<ApiResponse<ModeloAula>> getAulaDetalle(@Header("Authorization") String token, @Path("id") int aulaId);
    
    /**
     * Crear una nueva aula
     */
    @POST("aulas/docente")
    Call<ApiResponse<ModeloAula>> crearAula(@Header("Authorization") String token, @Body CrearAulaRequest request);
    
    /**
     * Actualizar una aula existente
     */
    @PUT("aulas/docente/{id}")
    Call<ApiResponse<ModeloAula>> actualizarAula(@Header("Authorization") String token, @Path("id") int aulaId, @Body CrearAulaRequest request);
    
    /**
     * Asignar cuentos a una aula
     */
    @PUT("aulas/docente/{id}/cuentos")
    Call<ApiResponse<AsignarCuentosResponse>> asignarCuentosAula(@Header("Authorization") String token, @Path("id") int aulaId, @Body AsignarCuentosRequest request);

    /**
     * Agregar un cuento al aula (incremental)
     */
    @POST("aulas/docente/{id}/cuentos")
    Call<ApiResponse<AsignarCuentosResponse>> agregarCuentoAula(@Header("Authorization") String token, @Path("id") int aulaId, @Body com.example.lectana.modelos.AgregarCuentoRequest request);

    /**
     * Quitar un cuento del aula (incremental)
     */
    @DELETE("aulas/docente/{id}/cuentos/{idCuento}")
    Call<ApiResponse<AsignarCuentosResponse>> quitarCuentoAula(@Header("Authorization") String token, @Path("id") int aulaId, @Path("idCuento") int idCuento);

    /**
     * Quitar por id de asignación (opcional, si backend lo habilita)
     */
    @DELETE("aulas/docente/{id}/cuentos/asignacion/{idAsignacion}")
    Call<ApiResponse<AsignarCuentosResponse>> quitarCuentoAulaPorAsignacion(@Header("Authorization") String token, @Path("id") int aulaId, @Path("idAsignacion") int idAsignacion);
}
