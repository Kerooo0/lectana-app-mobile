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

    /**
     * Obtener perfil del alumno autenticado
     * GET /api/alumnos/obtener-perfil-alumno
     * Rol: alumno
     */
    @GET("alumnos/obtener-perfil-alumno")
    Call<PerfilAlumnoResponse> obtenerPerfilAlumno(
            @Header("Authorization") String token
    );

    /**
     * Actualizar perfil del alumno
     * PUT /api/alumnos/actualizar-perfil-alumno
     * Rol: alumno
     */
    @PUT("alumnos/actualizar-perfil-alumno")
    Call<PerfilAlumnoResponse> actualizarPerfilAlumno(
            @Header("Authorization") String token,
            @Body ActualizarPerfilRequest request
    );

    /**
     * Unirse a un aula mediante código de acceso
     * POST /alumnos/unirse-aula
     * Rol: alumno
     */
    @POST("alumnos/unirse-aula")
    Call<ApiResponse<UnirseAulaResponse>> unirseAula(
            @Header("Authorization") String token,
            @Body UnirseAulaRequest request
    );

    /**
     * Salir del aula actual
     * POST /alumnos/salir-aula
     * Rol: alumno
     */
    @POST("alumnos/salir-aula")
    Call<ApiResponse<SalirAulaResponse>> salirAula(
            @Header("Authorization") String token
    );

    /**
     * Cambiar de aula del alumno
     * POST /api/alumnos/cambiarAula
     * Rol: alumno
     */
    @POST("alumnos/cambiarAula")
    Call<ApiResponse<CambiarAulaResponse>> cambiarAula(
            @Header("Authorization") String token,
            @Body CambiarAulaRequest request
    );

    // ============================================
    // MODELOS DE REQUEST/RESPONSE
    // ============================================

    /**
     * Request para responder una pregunta
     * - Para preguntas de opción múltiple: {"respuesta": "ID_como_string"}
     * - Para preguntas abiertas: {"respuesta": "texto de la respuesta"}
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
        @SerializedName("respuestaPregunta")
        private java.util.List<RespuestaUsuario> respuestaPregunta;

        public java.util.List<RespuestaUsuario> getRespuestaPregunta() {
            return respuestaPregunta;
        }

        public void setRespuestaPregunta(java.util.List<RespuestaUsuario> respuestaPregunta) {
            this.respuestaPregunta = respuestaPregunta;
        }

        /**
         * Modelo de respuesta individual del usuario
         */
        public static class RespuestaUsuario {
            @SerializedName("id_respuesta_usuario")
            private int idRespuestaUsuario;

            @SerializedName("respuesta_texto")
            private String respuestaTexto;

            @SerializedName("fecha_respuesta")
            private String fechaRespuesta;

            @SerializedName("pregunta_actividad_id_pregunta_actividad")
            private int preguntaActividadIdPreguntaActividad;

            @SerializedName("alumno_id_alumno")
            private int alumnoIdAlumno;

            public int getIdRespuestaUsuario() {
                return idRespuestaUsuario;
            }

            public void setIdRespuestaUsuario(int idRespuestaUsuario) {
                this.idRespuestaUsuario = idRespuestaUsuario;
            }

            public String getRespuestaTexto() {
                return respuestaTexto;
            }

            public void setRespuestaTexto(String respuestaTexto) {
                this.respuestaTexto = respuestaTexto;
            }

            public String getFechaRespuesta() {
                return fechaRespuesta;
            }

            public void setFechaRespuesta(String fechaRespuesta) {
                this.fechaRespuesta = fechaRespuesta;
            }

            public int getPreguntaActividadIdPreguntaActividad() {
                return preguntaActividadIdPreguntaActividad;
            }

            public void setPreguntaActividadIdPreguntaActividad(int preguntaActividadIdPreguntaActividad) {
                this.preguntaActividadIdPreguntaActividad = preguntaActividadIdPreguntaActividad;
            }

            public int getAlumnoIdAlumno() {
                return alumnoIdAlumno;
            }

            public void setAlumnoIdAlumno(int alumnoIdAlumno) {
                this.alumnoIdAlumno = alumnoIdAlumno;
            }
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

    /**
     * Request para actualizar perfil
     */
    class ActualizarPerfilRequest {
        @SerializedName("nombre")
        private String nombre;

        @SerializedName("apellido")
        private String apellido;

        @SerializedName("email")
        private String email;

        @SerializedName("edad")
        private Integer edad;

        @SerializedName("nacionalidad")
        private String nacionalidad;

        @SerializedName("aula_id_aula")
        private Integer aulaIdAula;

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellido() {
            return apellido;
        }

        public void setApellido(String apellido) {
            this.apellido = apellido;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Integer getEdad() {
            return edad;
        }

        public void setEdad(Integer edad) {
            this.edad = edad;
        }

        public String getNacionalidad() {
            return nacionalidad;
        }

        public void setNacionalidad(String nacionalidad) {
            this.nacionalidad = nacionalidad;
        }

        public Integer getAulaIdAula() {
            return aulaIdAula;
        }

        public void setAulaIdAula(Integer aulaIdAula) {
            this.aulaIdAula = aulaIdAula;
        }
    }

    /**
     * Response de perfil de alumno
     */
    class PerfilAlumnoResponse {
        @SerializedName("ok")
        private boolean ok;

        @SerializedName("data")
        private PerfilData data;

        public boolean isOk() {
            return ok;
        }

        public void setOk(boolean ok) {
            this.ok = ok;
        }

        public PerfilData getData() {
            return data;
        }

        public void setData(PerfilData data) {
            this.data = data;
        }

        public static class PerfilData {
            @SerializedName("id_alumno")
            private int idAlumno;

            @SerializedName("usuario")
            private UsuarioInfo usuario;

            @SerializedName("aula_id_aula")
            private Integer aulaIdAula;

            @SerializedName("nacionalidad")
            private String nacionalidad;

            public int getIdAlumno() {
                return idAlumno;
            }

            public void setIdAlumno(int idAlumno) {
                this.idAlumno = idAlumno;
            }

            public UsuarioInfo getUsuario() {
                return usuario;
            }

            public void setUsuario(UsuarioInfo usuario) {
                this.usuario = usuario;
            }

            public Integer getAulaIdAula() {
                return aulaIdAula;
            }

            public void setAulaIdAula(Integer aulaIdAula) {
                this.aulaIdAula = aulaIdAula;
            }

            public String getNacionalidad() {
                return nacionalidad;
            }

            public void setNacionalidad(String nacionalidad) {
                this.nacionalidad = nacionalidad;
            }

            public static class UsuarioInfo {
                @SerializedName("id_usuario")
                private int idUsuario;

                @SerializedName("nombre")
                private String nombre;

                @SerializedName("apellido")
                private String apellido;

                @SerializedName("email")
                private String email;

                @SerializedName("edad")
                private int edad;

                public int getIdUsuario() {
                    return idUsuario;
                }

                public void setIdUsuario(int idUsuario) {
                    this.idUsuario = idUsuario;
                }

                public String getNombre() {
                    return nombre;
                }

                public void setNombre(String nombre) {
                    this.nombre = nombre;
                }

                public String getApellido() {
                    return apellido;
                }

                public void setApellido(String apellido) {
                    this.apellido = apellido;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public int getEdad() {
                    return edad;
                }

                public void setEdad(int edad) {
                    this.edad = edad;
                }
            }
        }
    }

    /**
     * Request para unirse a un aula
     */
    class UnirseAulaRequest {
        @SerializedName("codigo_acceso")
        private String codigoAcceso;

        public UnirseAulaRequest(String codigoAcceso) {
            this.codigoAcceso = codigoAcceso;
        }

        public String getCodigoAcceso() {
            return codigoAcceso;
        }

        public void setCodigoAcceso(String codigoAcceso) {
            this.codigoAcceso = codigoAcceso;
        }
    }

    /**
     * Response al unirse a un aula
     */
    class UnirseAulaResponse {
        @SerializedName("mensaje")
        private String mensaje;

        @SerializedName("aula")
        private Aula aula;

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public Aula getAula() {
            return aula;
        }

        public void setAula(Aula aula) {
            this.aula = aula;
        }

        public static class Aula {
            @SerializedName("id_aula")
            private int idAula;

            @SerializedName("nombre_aula")
            private String nombreAula;

            @SerializedName("grado")
            private String grado;

            @SerializedName("codigo_acceso")
            private String codigoAcceso;

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

            public String getGrado() {
                return grado;
            }

            public void setGrado(String grado) {
                this.grado = grado;
            }

            public String getCodigoAcceso() {
                return codigoAcceso;
            }

            public void setCodigoAcceso(String codigoAcceso) {
                this.codigoAcceso = codigoAcceso;
            }
        }
    }

    /**
     * Response al salir de un aula
     */
    class SalirAulaResponse {
        @SerializedName("mensaje")
        private String mensaje;

        @SerializedName("aula_anterior")
        private String aulaAnterior;

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getAulaAnterior() {
            return aulaAnterior;
        }

        public void setAulaAnterior(String aulaAnterior) {
            this.aulaAnterior = aulaAnterior;
        }
    }

    /**
     * Request para cambiar de aula
     */
    class CambiarAulaRequest {
        @SerializedName("aulaId")
        private int aulaId;

        public CambiarAulaRequest(int aulaId) {
            this.aulaId = aulaId;
        }

        public int getAulaId() {
            return aulaId;
        }

        public void setAulaId(int aulaId) {
            this.aulaId = aulaId;
        }
    }

    /**
     * Response al cambiar de aula
     */
    class CambiarAulaResponse {
        @SerializedName("cambioAula")
        private java.util.List<CambioAulaItem> cambioAula;

        public java.util.List<CambioAulaItem> getCambioAula() {
            return cambioAula;
        }

        public void setCambioAula(java.util.List<CambioAulaItem> cambioAula) {
            this.cambioAula = cambioAula;
        }

        public static class CambioAulaItem {
            @SerializedName("alumno_id_alumno")
            private int alumnoIdAlumno;

            @SerializedName("aula_id_aula")
            private int aulaIdAula;

            public int getAlumnoIdAlumno() {
                return alumnoIdAlumno;
            }

            public void setAlumnoIdAlumno(int alumnoIdAlumno) {
                this.alumnoIdAlumno = alumnoIdAlumno;
            }

            public int getAulaIdAula() {
                return aulaIdAula;
            }

            public void setAulaIdAula(int aulaIdAula) {
                this.aulaIdAula = aulaIdAula;
            }
        }
    }
}
