package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para representar una pregunta de actividad con sus respuestas posibles
 */
public class PreguntaActividad {
    
    @SerializedName("id_pregunta_actividad")
    private int idPreguntaActividad;
    
    @SerializedName("enunciado")
    private String enunciado;
    
    @SerializedName("actividad_id_actividad")
    private int actividadIdActividad;
    
    @SerializedName("respuesta_actividad")
    private List<RespuestaActividad> respuestaActividad;
    
    // Respuesta del usuario (local, no viene del servidor)
    private String respuestaUsuario;
    private int respuestaSeleccionadaId;

    // Constructor vacío
    public PreguntaActividad() {
    }

    // Getters y Setters
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

    public List<RespuestaActividad> getRespuestaActividad() {
        return respuestaActividad;
    }

    public void setRespuestaActividad(List<RespuestaActividad> respuestaActividad) {
        this.respuestaActividad = respuestaActividad;
    }

    public String getRespuestaUsuario() {
        return respuestaUsuario;
    }

    public void setRespuestaUsuario(String respuestaUsuario) {
        this.respuestaUsuario = respuestaUsuario;
    }

    public int getRespuestaSeleccionadaId() {
        return respuestaSeleccionadaId;
    }

    public void setRespuestaSeleccionadaId(int respuestaSeleccionadaId) {
        this.respuestaSeleccionadaId = respuestaSeleccionadaId;
    }

    // Métodos de utilidad
    public boolean tieneRespuestas() {
        return respuestaActividad != null && !respuestaActividad.isEmpty();
    }

    public int getTotalRespuestas() {
        return respuestaActividad != null ? respuestaActividad.size() : 0;
    }

    public RespuestaActividad getRespuestaCorrecta() {
        if (respuestaActividad != null) {
            for (RespuestaActividad respuesta : respuestaActividad) {
                if (respuesta.isRespuestaCorrecta()) {
                    return respuesta;
                }
            }
        }
        return null;
    }
}
