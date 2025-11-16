package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para representar una respuesta posible de una pregunta de actividad
 */
public class RespuestaActividad {
    
    @SerializedName("id_respuesta_actividad")
    private int idRespuestaActividad;
    
    @SerializedName("respuestas")
    private List<String> respuestas;
    
    // Backend sends 0 or 1, not boolean
    @SerializedName("respuesta_correcta")
    private int respuestaCorrecta;
    
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

    public List<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<String> respuestas) {
        this.respuestas = respuestas;
    }

    public boolean isRespuestaCorrecta() {
        return respuestaCorrecta == 1;
    }

    public void setRespuestaCorrecta(int respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }
    
    // Helper method for compatibility
    public void setRespuestaCorrecta(boolean respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta ? 1 : 0;
    }

    public int getPreguntaActividadId() {
        return preguntaActividadId;
    }

    public void setPreguntaActividadId(int preguntaActividadId) {
        this.preguntaActividadId = preguntaActividadId;
    }
}
