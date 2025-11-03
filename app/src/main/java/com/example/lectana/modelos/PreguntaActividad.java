package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PreguntaActividad {
    
    @SerializedName("id_pregunta_actividad")
    private int idPreguntaActividad;
    
    @SerializedName("enunciado")
    private String enunciado;
    
    @SerializedName("actividad_id_actividad")
    private int actividadIdActividad;
    
    @SerializedName("respuestas")
    private List<RespuestaActividad> respuestas;

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

    public List<RespuestaActividad> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<RespuestaActividad> respuestas) {
        this.respuestas = respuestas;
    }
}
