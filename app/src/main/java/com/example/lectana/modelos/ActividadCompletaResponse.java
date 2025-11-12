package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Respuesta del endpoint GET /api/actividades/actividadCompleta/:idActividad
 * Backend devuelve solo las preguntas con sus respuestas
 */
public class ActividadCompletaResponse {
    
    @SerializedName("actividadCompleta")
    private List<PreguntaActividad> preguntas;

    public ActividadCompletaResponse() {
    }

    public List<PreguntaActividad> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaActividad> preguntas) {
        this.preguntas = preguntas;
    }
    
    /**
     * Método de compatibilidad - construye un objeto ActividadCompleta
     * a partir de las preguntas devueltas
     */
    public ActividadCompleta getActividad() {
        if (preguntas == null || preguntas.isEmpty()) {
            return null;
        }
        
        // Crear actividad completa a partir de las preguntas
        ActividadCompleta actividad = new ActividadCompleta();
        actividad.setPreguntaActividad(preguntas);
        
        // Obtener id_actividad del primer pregunta
        if (preguntas.get(0) != null) {
            actividad.setIdActividad(preguntas.get(0).getActividadIdActividad());
        }
        
        return actividad;
    }
}

