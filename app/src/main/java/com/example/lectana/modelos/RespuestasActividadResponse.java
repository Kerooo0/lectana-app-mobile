package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Respuesta del endpoint GET /api/docentes/actividades/{id}/respuestas
 * Contiene todas las preguntas y respuestas de estudiantes para una actividad
 */
public class RespuestasActividadResponse {
    
    @SerializedName("respuestas")
    private List<PreguntaConRespuestas> respuestas;
    
    @SerializedName("total_preguntas")
    private int totalPreguntas;

    public RespuestasActividadResponse() {
        this.respuestas = new ArrayList<>();
    }

    public List<PreguntaConRespuestas> getRespuestas() {
        if (respuestas == null) {
            respuestas = new ArrayList<>();
        }
        return respuestas;
    }

    public void setRespuestas(List<PreguntaConRespuestas> respuestas) {
        this.respuestas = respuestas;
    }

    public int getTotalPreguntas() {
        return totalPreguntas;
    }

    public void setTotalPreguntas(int totalPreguntas) {
        this.totalPreguntas = totalPreguntas;
    }

    /**
     * Clase interna para representar una pregunta con sus respuestas de estudiantes
     */
    public static class PreguntaConRespuestas {
        
        @SerializedName("id_pregunta_actividad")
        private int idPreguntaActividad;
        
        @SerializedName("enunciado")
        private String enunciado;
        
        @SerializedName("respuesta_usuario")
        private List<RespuestaEstudiante> respuestasEstudiantes;

        public PreguntaConRespuestas() {
            this.respuestasEstudiantes = new ArrayList<>();
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

        public List<RespuestaEstudiante> getRespuestasEstudiantes() {
            if (respuestasEstudiantes == null) {
                respuestasEstudiantes = new ArrayList<>();
            }
            return respuestasEstudiantes;
        }

        public void setRespuestasEstudiantes(List<RespuestaEstudiante> respuestasEstudiantes) {
            this.respuestasEstudiantes = respuestasEstudiantes;
        }
    }

    /**
     * Clase interna para representar la respuesta de un estudiante
     */
    public static class RespuestaEstudiante {
        
        @SerializedName("id_respuesta_usuario")
        private int idRespuestaUsuario;
        
        @SerializedName("respuesta_texto")
        private String respuestaTexto;
        
        @SerializedName("fecha_respuesta")
        private String fechaRespuesta;
        
        @SerializedName("alumno")
        private EstudianteInfo alumno;
        
        @SerializedName("respuesta_actividad")
        private OpcionRespuesta opcionRespuesta;

        public RespuestaEstudiante() {
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

        public EstudianteInfo getAlumno() {
            return alumno;
        }

        public void setAlumno(EstudianteInfo alumno) {
            this.alumno = alumno;
        }

        public OpcionRespuesta getOpcionRespuesta() {
            return opcionRespuesta;
        }

        public void setOpcionRespuesta(OpcionRespuesta opcionRespuesta) {
            this.opcionRespuesta = opcionRespuesta;
        }
    }

    /**
     * Información del estudiante que respondió
     */
    public static class EstudianteInfo {
        
        @SerializedName("id_alumno")
        private int idAlumno;
        
        @SerializedName("usuario")
        private UsuarioInfo usuario;

        public EstudianteInfo() {
        }

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
    }

    /**
     * Información del usuario
     */
    public static class UsuarioInfo {
        
        @SerializedName("nombre")
        private String nombre;
        
        @SerializedName("apellido")
        private String apellido;
        
        @SerializedName("email")
        private String email;

        public UsuarioInfo() {
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
     * Opción de respuesta correcta (para múltiple choice)
     */
    public static class OpcionRespuesta {
        
        @SerializedName("id_respuesta_actividad")
        private int idRespuestaActividad;
        
        @SerializedName("respuestas")
        private String respuestas;
        
        @SerializedName("respuesta_correcta")
        private boolean respuestaCorrecta;

        public OpcionRespuesta() {
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
