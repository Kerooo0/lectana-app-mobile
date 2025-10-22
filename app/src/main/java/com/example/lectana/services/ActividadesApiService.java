package com.example.lectana.services;

import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.ActividadesDocenteResponse;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.AsignarAulasRequest;
import com.example.lectana.modelos.CrearActividadRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ActividadesApiService {

    /**
     * Crear actividad completa
     * POST /api/docentes/actividades
     */
    @POST("docentes/actividades")
    Call<ApiResponse<Actividad>> crearActividad(
            @Header("Authorization") String token,
            @Body CrearActividadRequest request
    );

    /**
     * Listar actividades del docente
     * GET /api/docentes/actividades
     */
    @GET("docentes/actividades")
    Call<ActividadesDocenteResponse> getActividadesDocente(
            @Header("Authorization") String token
    );

    /**
     * Ver actividad específica
     * GET /api/docentes/actividades/:id
     */
    @GET("docentes/actividades/{id}")
    Call<ApiResponse<Actividad>> getActividadDetalle(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    /**
     * Actualizar actividad
     * PUT /api/docentes/actividades/:id
     */
    @PUT("docentes/actividades/{id}")
    Call<ApiResponse<Actividad>> actualizarActividad(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body CrearActividadRequest request
    );

    /**
     * Eliminar actividad
     * DELETE /api/docentes/actividades/:id
     */
    @DELETE("docentes/actividades/{id}")
    Call<ApiResponse<Void>> eliminarActividad(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    /**
     * Asignar actividad a aulas
     * PUT /api/docentes/actividades/:id/asignar-aulas
     */
    @PUT("docentes/actividades/{id}/asignar-aulas")
    Call<ApiResponse<Void>> asignarActividadAulas(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body AsignarAulasRequest request
    );

    /**
     * Ver actividades de un aula específica
     * GET /api/docentes/actividades/aula/:id
     */
    @GET("docentes/actividades/aula/{id}")
    Call<ApiResponse<List<Actividad>>> getActividadesAula(
            @Header("Authorization") String token,
            @Path("id") int aulaId
    );
}

