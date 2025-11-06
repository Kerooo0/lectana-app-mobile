package com.example.lectana.services;

import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.ActividadesDocenteResponse;
import com.example.lectana.modelos.ActividadesPorAulaResponse;
import com.example.lectana.modelos.ActividadCompletaResponse;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.AsignarAulasRequest;
import com.example.lectana.modelos.CrearActividadRequest;
import com.example.lectana.modelos.nuevas.CrearActividadNuevaRequest;
import com.example.lectana.modelos.nuevas.CrearActividadBackendRequest;
import com.example.lectana.modelos.nuevas.CrearActividadBackendResponse;
import com.example.lectana.modelos.nuevas.CrearPreguntaActividadRequest;
import com.example.lectana.modelos.nuevas.CrearRespuestaActividadRequest;
import com.example.lectana.modelos.nuevas.CrearRespuestaActividadBackendRequest;

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

    // ============================================
    // ENDPOINTS PARA ALUMNOS
    // ============================================
    
    /**
     * Obtener actividades de un aula específica
     * GET /api/actividades/actividadesPorAula/:id_aula
     * Rol: alumno, docente, administrador
     * Devuelve: { actividades: [...] }
     */
    @GET("actividades/actividadesPorAula/{id_aula}")
    Call<ActividadesPorAulaResponse> getActividadesPorAula(
            @Header("Authorization") String token,
            @Path("id_aula") int aulaId
    );

    /**
     * Obtener detalles completos de una actividad (con preguntas y respuestas)
     * GET /api/actividades/actividadCompleta/:idActividad
     * Rol: alumno, docente, administrador
     * Devuelve: { actividadCompleta: {...} }
     */
    @GET("actividades/actividadCompleta/{idActividad}")
    Call<ActividadCompletaResponse> getActividadCompleta(
            @Header("Authorization") String token,
            @Path("idActividad") int idActividad
    );

    // ============================================
    // ENDPOINTS PARA DOCENTES
    // ============================================

    // NUEVO: crear actividad (mínimo viable con nuevas rutas)
    // POST /api/actividades/crearActividad
    @POST("actividades/crearActividad")
    Call<CrearActividadBackendResponse> crearActividadNueva(
            @Header("Authorization") String token,
            @Body CrearActividadBackendRequest request
    );

    // Listar actividades del docente (se mantiene mientras no haya nueva ruta definida)
    @GET("docentes/actividades")
    Call<ActividadesDocenteResponse> getActividadesDocente(
            @Header("Authorization") String token
    );

    // Ver actividad específica (se mantiene ruta actual)
    @GET("docentes/actividades/{id}")
    Call<ApiResponse<Actividad>> getActividadDetalle(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    // Actualizar actividad (ruta actual)
    @PUT("docentes/actividades/{id}")
    Call<ApiResponse<Actividad>> actualizarActividad(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body CrearActividadRequest request
    );

    // Eliminar actividad (ruta actual - legacy)
    @DELETE("docentes/actividades/{id}")
    Call<ApiResponse<Void>> eliminarActividadLegacy(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    // Eliminar actividad (ruta nueva sugerida por backend)
    // DELETE /api/actividades/:id_actividad → { ok: true/false, mensaje|error }
    @DELETE("actividades/{id}")
    Call<okhttp3.ResponseBody> eliminarActividad(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    // Asignar actividad a aulas (ruta actual)
    @PUT("docentes/actividades/{id}/asignar-aulas")
    Call<ApiResponse<Void>> asignarActividadAulas(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body AsignarAulasRequest request
    );

    // Ver actividades de un aula específica (ruta actual)
    // Tolerante: puede devolver array plano o un objeto con data
    @GET("docentes/actividades/aula/{id}")
    Call<okhttp3.ResponseBody> getActividadesAula(
            @Header("Authorization") String token,
            @Path("id") int aulaId
    );

    // NUEVOS: crear pregunta en actividad
    // POST /api/preguntas/crearPreguntaActividad/:id_actividad
    @POST("preguntas/crearPreguntaActividad/{id_actividad}")
    Call<okhttp3.ResponseBody> crearPreguntaActividad(
            @Header("Authorization") String token,
            @Path("id_actividad") int idActividad,
            @Body CrearPreguntaActividadRequest request
    );

    // NUEVOS: crear respuesta (opción) para pregunta multiple choice
    // POST /api/respuestas/crearRespuestaActividad/:id_pregunta
    @POST("respuestas/crearRespuestaActividad/{id_pregunta}")
    Call<okhttp3.ResponseBody> crearRespuestaActividad(
            @Header("Authorization") String token,
            @Path("id_pregunta") int idPregunta,
            @Body CrearRespuestaActividadBackendRequest request
    );
    
    // Obtener respuestas de un alumno para una actividad específica
    @GET("respuestas-usuario/alumno/{alumno_id}/actividad/{actividad_id}")
    Call<ApiResponse<List<com.example.lectana.modelos.RespuestaUsuario>>> getRespuestasAlumnoActividad(
            @Header("Authorization") String token,
            @Path("alumno_id") int alumnoId,
            @Path("actividad_id") int actividadId
    );
}

