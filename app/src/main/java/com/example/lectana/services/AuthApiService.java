package com.example.lectana.services;

import com.example.lectana.modelos.ApiResponse;
import com.google.gson.annotations.SerializedName;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

/**
 * Servicio API para operaciones de autenticación
 */
public interface AuthApiService {
    
    /**
     * Obtener información completa del usuario autenticado
     * Incluye datos específicos según el rol (alumno, docente, administrador)
     */
    @GET("auth/me")
    Call<MeResponse> obtenerDatosUsuario(
            @Header("Authorization") String token
    );
    
    /**
     * Respuesta del endpoint /api/auth/me
     */
    class MeResponse {
        @SerializedName("ok")
        private boolean ok;
        
        @SerializedName("user")
        private Usuario user;
        
        @SerializedName("role")
        private String role;
        
        @SerializedName("verificado")
        private boolean verificado;
        
        @SerializedName("alumno")
        private Alumno alumno;
        
        @SerializedName("docente")
        private Docente docente;
        
        @SerializedName("administrador")
        private Administrador administrador;
        
        // Getters
        public boolean isOk() { return ok; }
        public Usuario getUser() { return user; }
        public String getRole() { return role; }
        public boolean isVerificado() { return verificado; }
        public Alumno getAlumno() { return alumno; }
        public Docente getDocente() { return docente; }
        public Administrador getAdministrador() { return administrador; }
    }
    
    /**
     * Datos del usuario
     */
    class Usuario {
        @SerializedName("id_usuario")
        private int idUsuario;
        
        @SerializedName("nombre")
        private String nombre;
        
        @SerializedName("apellido")
        private String apellido;
        
        @SerializedName("email")
        private String email;
        
        @SerializedName("edad")
        private Integer edad;
        
        @SerializedName("activo")
        private boolean activo;
        
        // Getters
        public int getIdUsuario() { return idUsuario; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getEmail() { return email; }
        public Integer getEdad() { return edad; }
        public boolean isActivo() { return activo; }
    }
    
    /**
     * Datos específicos del alumno
     */
    class Alumno {
        @SerializedName("id_alumno")
        private int idAlumno;
        
        @SerializedName("usuario_id_usuario")
        private int usuarioIdUsuario;
        
        @SerializedName("nacionalidad")
        private String nacionalidad;
        
        @SerializedName("alumno_col")
        private String alumnoCol;
        
        @SerializedName("aula_id_aula")
        private Integer aulaIdAula;
        
        // Getters
        public int getIdAlumno() { return idAlumno; }
        public int getUsuarioIdUsuario() { return usuarioIdUsuario; }
        public String getNacionalidad() { return nacionalidad; }
        public String getAlumnoCol() { return alumnoCol; }
        public Integer getAulaIdAula() { return aulaIdAula; }
    }
    
    /**
     * Datos específicos del docente
     */
    class Docente {
        @SerializedName("id_docente")
        private int idDocente;
        
        @SerializedName("usuario_id_usuario")
        private int usuarioIdUsuario;
        
        @SerializedName("telefono")
        private String telefono;
        
        @SerializedName("nivel_educativo")
        private String nivelEducativo;
        
        @SerializedName("institucion_nombre")
        private String institucionNombre;
        
        @SerializedName("institucion_pais")
        private String institucionPais;
        
        @SerializedName("institucion_provincia")
        private String institucionProvincia;
        
        // Getters
        public int getIdDocente() { return idDocente; }
        public int getUsuarioIdUsuario() { return usuarioIdUsuario; }
        public String getTelefono() { return telefono; }
        public String getNivelEducativo() { return nivelEducativo; }
        public String getInstitucionNombre() { return institucionNombre; }
        public String getInstitucionPais() { return institucionPais; }
        public String getInstitucionProvincia() { return institucionProvincia; }
    }
    
    /**
     * Datos específicos del administrador
     */
    class Administrador {
        @SerializedName("id_administrador")
        private int idAdministrador;
        
        @SerializedName("usuario_id_usuario")
        private int usuarioIdUsuario;
        
        // Getters
        public int getIdAdministrador() { return idAdministrador; }
        public int getUsuarioIdUsuario() { return usuarioIdUsuario; }
    }
}
