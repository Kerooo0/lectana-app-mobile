package com.example.lectana.services;

import com.example.lectana.modelos.ApiResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Servicio API para operaciones relacionadas con estudiantes
 */
public interface EstudiantesApiService {

    /**
     * Obtener datos del perfil del estudiante
     */
    @GET("alumnos/obtener-perfil-alumno")
    Call<ApiResponse<EstudiantePerfilResponse>> getPerfilEstudiante(
            @Header("Authorization") String token
    );

    /**
     * Actualizar datos personales del estudiante
     */
    @PUT("alumnos/actualizar-perfil-alumno")
    Call<ApiResponse<EstudiantePerfilResponse>> actualizarPerfil(
            @Header("Authorization") String token,
            @Body ActualizarPerfilRequest request
    );

    /**
     * Cambiar contraseña del estudiante
     */
    @PUT("estudiantes/{id}/password")
    Call<ApiResponse<Void>> cambiarPassword(
            @Header("Authorization") String token,
            @Path("id") int idEstudiante,
            @Body CambiarPasswordRequest request
    );

    /**
     * Solicitar recuperación de contraseña
     */
    @POST("auth/recuperar-password")
    Call<ApiResponse<Void>> solicitarRecuperacionPassword(
            @Body RecuperarPasswordRequest request
    );

    /**
     * Clase para la respuesta del perfil del estudiante
     */
    class EstudiantePerfilResponse {
        private int id_estudiante;
        private String nombre;
        private String apellido;
        private String email;
        private String fecha_nacimiento;
        private int edad;
        private String grado;
        private String institucion;
        private String codigo_aula;

        // Getters y Setters
        public int getId_estudiante() { return id_estudiante; }
        public void setId_estudiante(int id_estudiante) { this.id_estudiante = id_estudiante; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getFecha_nacimiento() { return fecha_nacimiento; }
        public void setFecha_nacimiento(String fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

        public int getEdad() { return edad; }
        public void setEdad(int edad) { this.edad = edad; }

        public String getGrado() { return grado; }
        public void setGrado(String grado) { this.grado = grado; }

        public String getInstitucion() { return institucion; }
        public void setInstitucion(String institucion) { this.institucion = institucion; }

        public String getCodigo_aula() { return codigo_aula; }
        public void setCodigo_aula(String codigo_aula) { this.codigo_aula = codigo_aula; }
    }

    /**
     * Clase para la solicitud de actualización de perfil
     */
    class ActualizarPerfilRequest {
        private String nombre;
        private String apellido;
        private String fecha_nacimiento;
        private String grado;
        private String institucion;
        private String codigo_aula;

        public ActualizarPerfilRequest(String nombre, String apellido, String fecha_nacimiento, 
                                      String grado, String institucion, String codigo_aula) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.fecha_nacimiento = fecha_nacimiento;
            this.grado = grado;
            this.institucion = institucion;
            this.codigo_aula = codigo_aula;
        }

        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }

        public String getFecha_nacimiento() { return fecha_nacimiento; }
        public void setFecha_nacimiento(String fecha_nacimiento) { this.fecha_nacimiento = fecha_nacimiento; }

        public String getGrado() { return grado; }
        public void setGrado(String grado) { this.grado = grado; }

        public String getInstitucion() { return institucion; }
        public void setInstitucion(String institucion) { this.institucion = institucion; }

        public String getCodigo_aula() { return codigo_aula; }
        public void setCodigo_aula(String codigo_aula) { this.codigo_aula = codigo_aula; }
    }

    /**
     * Clase para la solicitud de cambio de contraseña
     */
    class CambiarPasswordRequest {
        private String password_actual;
        private String password_nueva;

        public CambiarPasswordRequest(String passwordActual, String passwordNueva) {
            this.password_actual = passwordActual;
            this.password_nueva = passwordNueva;
        }

        public String getPassword_actual() { return password_actual; }
        public void setPassword_actual(String password_actual) { this.password_actual = password_actual; }

        public String getPassword_nueva() { return password_nueva; }
        public void setPassword_nueva(String password_nueva) { this.password_nueva = password_nueva; }
    }

    /**
     * Clase para la solicitud de recuperación de contraseña
     */
    class RecuperarPasswordRequest {
        private String email;

        public RecuperarPasswordRequest(String email) {
            this.email = email;
        }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
