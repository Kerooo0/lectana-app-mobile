package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para representar una respuesta posible de una pregunta de actividad
 */
public class RespuestaActividad {
    
    @SerializedName("id_respuesta_actividad")
    private int idRespuestaActividad;
    
    @SerializedName("respuestas")
    private String respuestas;
    
    @SerializedName("respuesta_correcta")
    private boolean respuestaCorrecta;
    
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

    public int getPreguntaActividadId() {
        return preguntaActividadId;
    }

    public void setPreguntaActividadId(int preguntaActividadId) {
        this.preguntaActividadId = preguntaActividadId;
    }
}
