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

    // Constructor vacío
    public RespuestaUsuario() {
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
