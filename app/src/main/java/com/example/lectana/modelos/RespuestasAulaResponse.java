package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Respuesta del endpoint GET /api/docentes/aula/{aulaId}/respuestas
 * Contiene todas las respuestas de estudiantes para todas las actividades de un aula
 */
public class RespuestasAulaResponse {
    
    @SerializedName("respuestas")
    private List<RespuestaAulaItem> respuestas;
    
    @SerializedName("total")
    private int total;

    public RespuestasAulaResponse() {
        this.respuestas = new ArrayList<>();
    }

    public List<RespuestaAulaItem> getRespuestas() {
        if (respuestas == null) {
            respuestas = new ArrayList<>();
        }
        return respuestas;
    }

    public void setRespuestas(List<RespuestaAulaItem> respuestas) {
        this.respuestas = respuestas;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Clase interna para representar una respuesta en el contexto del aula
     */
    public static class RespuestaAulaItem {
        
        @SerializedName("id_respuesta_usuario")
        private int idRespuestaUsuario;
        
        @SerializedName("respuesta_texto")
        private String respuestaTexto;
        
        @SerializedName("fecha_respuesta")
        private String fechaRespuesta;
        
        @SerializedName("alumno")
        private EstudianteAulaInfo alumno;
        
        @SerializedName("pregunta_actividad")
        private PreguntaAulaInfo pregunta;
        
        @SerializedName("respuesta_actividad")
        private OpcionRespuestaAula opcionRespuesta;

        public RespuestaAulaItem() {
        }

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

        public EstudianteAulaInfo getAlumno() {
            return alumno;
        }

        public void setAlumno(EstudianteAulaInfo alumno) {
            this.alumno = alumno;
        }

        public PreguntaAulaInfo getPregunta() {
            return pregunta;
        }

        public void setPregunta(PreguntaAulaInfo pregunta) {
            this.pregunta = pregunta;
        }

        public OpcionRespuestaAula getOpcionRespuesta() {
            return opcionRespuesta;
        }

        public void setOpcionRespuesta(OpcionRespuestaAula opcionRespuesta) {
            this.opcionRespuesta = opcionRespuesta;
        }
    }

    /**
     * Información del estudiante en contexto de aula
     */
    public static class EstudianteAulaInfo {
        
        @SerializedName("id_alumno")
        private int idAlumno;
        
        @SerializedName("usuario")
        private UsuarioAulaInfo usuario;

        public EstudianteAulaInfo() {
        }

        public int getIdAlumno() {
            return idAlumno;
        }

        public void setIdAlumno(int idAlumno) {
            this.idAlumno = idAlumno;
        }

        public UsuarioAulaInfo getUsuario() {
            return usuario;
        }

        public void setUsuario(UsuarioAulaInfo usuario) {
            this.usuario = usuario;
        }
    }

    /**
     * Información del usuario en contexto de aula
     */
    public static class UsuarioAulaInfo {
        
        @SerializedName("nombre")
        private String nombre;
        
        @SerializedName("apellido")
        private String apellido;
        
        @SerializedName("email")
        private String email;

        public UsuarioAulaInfo() {
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
    }

    /**
     * Información de la pregunta en contexto de aula
     */
    public static class PreguntaAulaInfo {
        
        @SerializedName("id_pregunta_actividad")
        private int idPreguntaActividad;
        
        @SerializedName("enunciado")
        private String enunciado;
        
        @SerializedName("actividad_id_actividad")
        private int actividadIdActividad;

        public PreguntaAulaInfo() {
        }

        public int getIdPreguntaActividad() {
            return idPreguntaActividad;
        }

        public void setIdPreguntaActividad(int idPreguntaActividad) {
            this.idPreguntaActividad = idPreguntaActividad;
        }

        public String getEnunciado() {
            return enunciado;
        }

        public void setEnunciado(String enunciado) {
            this.enunciado = enunciado;
        }

        public int getActividadIdActividad() {
            return actividadIdActividad;
        }

        public void setActividadIdActividad(int actividadIdActividad) {
            this.actividadIdActividad = actividadIdActividad;
        }
    }

    /**
     * Opción de respuesta en contexto de aula
     */
    public static class OpcionRespuestaAula {
        
        @SerializedName("id_respuesta_actividad")
        private int idRespuestaActividad;
        
        @SerializedName("respuestas")
        private String respuestas;
        
        @SerializedName("respuesta_correcta")
        private boolean respuestaCorrecta;

        public OpcionRespuestaAula() {
        }

        public int getIdRespuestaActividad() {
            return idRespuestaActividad;
        }

        public void setIdRespuestaActividad(int idRespuestaActividad) {
            this.idRespuestaActividad = idRespuestaActividad;
        }

        public String getRespuestas() {
            return respuestas;
        }

        public void setRespuestas(String respuestas) {
            this.respuestas = respuestas;
        }

        public boolean isRespuestaCorrecta() {
            return respuestaCorrecta;
        }

        public void setRespuestaCorrecta(boolean respuestaCorrecta) {
            this.respuestaCorrecta = respuestaCorrecta;
        }
    }
}
