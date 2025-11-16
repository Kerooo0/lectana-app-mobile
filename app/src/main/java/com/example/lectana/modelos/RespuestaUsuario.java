package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

public class RespuestaUsuario {
    
    @SerializedName("id_respuesta_usuario")
    private int idRespuestaUsuario;
    
    @SerializedName("respuesta_texto")
    private String respuestaTexto;
    
    @SerializedName("fecha_respuesta")
    private String fechaRespuesta;
    
    @SerializedName("pregunta_actividad_id_pregunta_actividad")
    private int preguntaActividadId;
    
    @SerializedName("alumno_id_alumno")
    private int alumnoIdAlumno;
    
    @SerializedName("respuesta_actividad_id_respuesta_actividad")
    private Integer respuestaActividadIdRespuestaActividad;
    
    @SerializedName("alumno")
    private AlumnoInfo alumno;
    
    @SerializedName("respuesta_actividad")
    private RespuestaActividadInfo respuestaActividad;

    // Constructor vacío
    public RespuestaUsuario() {
    }
    
    // Clases internas para objetos anidados
    public static class AlumnoInfo {
        @SerializedName("id_alumno")
        private int idAlumno;
        
        @SerializedName("usuario")
        private UsuarioInfo usuario;
        
        public AlumnoInfo() {}
        
        public int getIdAlumno() { return idAlumno; }
        public void setIdAlumno(int idAlumno) { this.idAlumno = idAlumno; }
        
        public UsuarioInfo getUsuario() { return usuario; }
        public void setUsuario(UsuarioInfo usuario) { this.usuario = usuario; }
    }
    
    public static class UsuarioInfo {
        @SerializedName("nombre")
        private String nombre;
        
        @SerializedName("apellido")
        private String apellido;
        
        public UsuarioInfo() {}
        
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        
        public String getApellido() { return apellido; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        
        public String getNombreCompleto() {
            return (nombre != null ? nombre : "") + " " + (apellido != null ? apellido : "");
        }
    }
    
    public static class RespuestaActividadInfo {
        @SerializedName("id_respuesta_actividad")
        private int idRespuestaActividad;
        
        @SerializedName("respuesta")
        private String respuesta;
        
        @SerializedName("es_correcta")
        private boolean esCorrecta;
        
        public RespuestaActividadInfo() {}
        
        public int getIdRespuestaActividad() { return idRespuestaActividad; }
        public void setIdRespuestaActividad(int idRespuestaActividad) { this.idRespuestaActividad = idRespuestaActividad; }
        
        public String getRespuesta() { return respuesta; }
        public void setRespuesta(String respuesta) { this.respuesta = respuesta; }
        
        public boolean isEsCorrecta() { return esCorrecta; }
        public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }
    }

    // Getters y Setters
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

    public int getPreguntaActividadId() {
        return preguntaActividadId;
    }

    public void setPreguntaActividadId(int preguntaActividadId) {
        this.preguntaActividadId = preguntaActividadId;
    }

    public int getAlumnoIdAlumno() {
        return alumnoIdAlumno;
    }

    public void setAlumnoIdAlumno(int alumnoIdAlumno) {
        this.alumnoIdAlumno = alumnoIdAlumno;
    }

    public Integer getRespuestaActividadIdRespuestaActividad() {
        return respuestaActividadIdRespuestaActividad;
    }

    public void setRespuestaActividadIdRespuestaActividad(Integer respuestaActividadIdRespuestaActividad) {
        this.respuestaActividadIdRespuestaActividad = respuestaActividadIdRespuestaActividad;
    }

    public AlumnoInfo getAlumno() {
        return alumno;
    }

    public void setAlumno(AlumnoInfo alumno) {
        this.alumno = alumno;
    }

    public RespuestaActividadInfo getRespuestaActividad() {
        return respuestaActividad;
    }

    public void setRespuestaActividad(RespuestaActividadInfo respuestaActividad) {
        this.respuestaActividad = respuestaActividad;
    }

    // Clase interna para el request al endpoint POST
    public static class Request {
        @SerializedName("respuesta_texto")
        private String respuestaTexto;
        
        @SerializedName("pregunta_actividad_id_pregunta_actividad")
        private int preguntaActividadId;
        
        @SerializedName("alumno_id_alumno")
        private int alumnoIdAlumno;
        
        @SerializedName("respuesta_actividad_id_respuesta_actividad")
        private Integer respuestaActividadIdRespuestaActividad;

        public Request() {
        }

        public String getRespuestaTexto() {
            return respuestaTexto;
        }

        public void setRespuestaTexto(String respuestaTexto) {
            this.respuestaTexto = respuestaTexto;
        }

        public int getPreguntaActividadId() {
            return preguntaActividadId;
        }

        public void setPreguntaActividadId(int preguntaActividadId) {
            this.preguntaActividadId = preguntaActividadId;
        }

        public int getAlumnoIdAlumno() {
            return alumnoIdAlumno;
        }

        public void setAlumnoIdAlumno(int alumnoIdAlumno) {
            this.alumnoIdAlumno = alumnoIdAlumno;
        }

        public Integer getRespuestaActividadIdRespuestaActividad() {
            return respuestaActividadIdRespuestaActividad;
        }

        public void setRespuestaActividadIdRespuestaActividad(Integer respuestaActividadIdRespuestaActividad) {
            this.respuestaActividadIdRespuestaActividad = respuestaActividadIdRespuestaActividad;
        }
    }
}
