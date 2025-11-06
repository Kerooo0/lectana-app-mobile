package com.example.lectana.services;

import com.example.lectana.modelos.ApiResponse;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.PUT;

/**
 * Servicio API para operaciones relacionadas con alumnos
 */
public interface AlumnoApiService {
    
    /**
     * Responder a una pregunta de actividad
     * POST /api/alumnos/responder-pregunta/:id_pregunta
     * Rol: alumno
     */
    @POST("alumnos/responder-pregunta/{id_pregunta}")
    Call<ApiResponse<RespuestaPreguntaResponse>> responderPregunta(
            @Header("Authorization") String token,
            @Path("id_pregunta") int idPregunta,
            @Body ResponderPreguntaRequest request
    );

    /**
     * Obtener aula del alumno
     * GET /api/alumnos/obtenerAula
     * Rol: alumno
     */
    @GET("alumnos/obtenerAula")
    Call<ApiResponse<AulaAlumnoResponse>> obtenerAulaAlumno(
            @Header("Authorization") String token
    );

    /**
     * Cambiar contraseña del alumno (preparado para cuando esté disponible)
     * PUT /api/alumnos/cambiar-password
     * Rol: alumno
     */
    @PUT("alumnos/cambiar-password")
    Call<ApiResponse<CambioPasswordResponse>> cambiarPassword(
            @Header("Authorization") String token,
            @Body CambiarPasswordRequest request
    );

    // ============================================
    // MODELOS DE REQUEST/RESPONSE
    // ============================================

    /**
     * Request para responder una pregunta
     */
    class ResponderPreguntaRequest {
        @SerializedName("respuesta")
        private String respuesta;

        public ResponderPreguntaRequest(String respuesta) {
            this.respuesta = respuesta;
        }

        public String getRespuesta() {
            return respuesta;
        }

        public void setRespuesta(String respuesta) {
            this.respuesta = respuesta;
        }
    }

    /**
     * Response de responder pregunta
     */
    class RespuestaPreguntaResponse {
        @SerializedName("id_respuesta")
        private int idRespuesta;

        @SerializedName("mensaje")
        private String mensaje;

        @SerializedName("es_correcta")
        private Boolean esCorrecta;

        public int getIdRespuesta() {
            return idRespuesta;
        }

        public void setIdRespuesta(int idRespuesta) {
            this.idRespuesta = idRespuesta;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public Boolean getEsCorrecta() {
            return esCorrecta;
        }

        public void setEsCorrecta(Boolean esCorrecta) {
            this.esCorrecta = esCorrecta;
        }
    }

    /**
     * Response de obtener aula del alumno
     */
    class AulaAlumnoResponse {
        @SerializedName("aula")
        private AulaInfo aula;

        public AulaInfo getAula() {
            return aula;
        }

        public void setAula(AulaInfo aula) {
            this.aula = aula;
        }

        public static class AulaInfo {
            @SerializedName("id_aula")
            private int idAula;

            @SerializedName("nombre_aula")
            private String nombreAula;

            @SerializedName("codigo_aula")
            private String codigoAula;

            public int getIdAula() {
                return idAula;
            }

            public void setIdAula(int idAula) {
                this.idAula = idAula;
            }

            public String getNombreAula() {
                return nombreAula;
            }

            public void setNombreAula(String nombreAula) {
                this.nombreAula = nombreAula;
            }

            public String getCodigoAula() {
                return codigoAula;
            }

            public void setCodigoAula(String codigoAula) {
                this.codigoAula = codigoAula;
            }
        }
    }

    /**
     * Request para cambiar contraseña
     */
    class CambiarPasswordRequest {
        @SerializedName("password_actual")
        private String passwordActual;

        @SerializedName("password_nueva")
        private String passwordNueva;

        public CambiarPasswordRequest(String passwordActual, String passwordNueva) {
            this.passwordActual = passwordActual;
            this.passwordNueva = passwordNueva;
        }

        public String getPasswordActual() {
            return passwordActual;
        }

        public void setPasswordActual(String passwordActual) {
            this.passwordActual = passwordActual;
        }

        public String getPasswordNueva() {
            return passwordNueva;
        }

        public void setPasswordNueva(String passwordNueva) {
            this.passwordNueva = passwordNueva;
        }
    }

    /**
     * Response de cambiar contraseña
     */
    class CambioPasswordResponse {
        @SerializedName("mensaje")
        private String mensaje;

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
