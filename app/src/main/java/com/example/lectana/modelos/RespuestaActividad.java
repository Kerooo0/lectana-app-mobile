package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

public class RespuestaActividad {
    
    @SerializedName("id_respuesta_actividad")
    private int idRespuestaActividad;
    
    @SerializedName("respuesta")
    private String respuesta;
    
    @SerializedName("es_correcta")
    private boolean esCorrecta;
    
    @SerializedName("pregunta_actividad_id_pregunta_actividad")
    private int preguntaActividadId;

    // Constructor vacío
    public RespuestaActividad() {
    }

    // Getters y Setters
    public int getIdRespuestaActividad() {
        return idRespuestaActividad;
    }

    public void setIdRespuestaActividad(int idRespuestaActividad) {
        this.idRespuestaActividad = idRespuestaActividad;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public int getPreguntaActividadId() {
        return preguntaActividadId;
    }

    public void setPreguntaActividadId(int preguntaActividadId) {
        this.preguntaActividadId = preguntaActividadId;
    }
}
