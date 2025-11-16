package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Respuesta del endpoint GET /api/actividades/actividadCompleta/:idActividad
 * Backend devuelve la actividad completa con todas sus preguntas
 * NOTA: El backend retorna directamente ActividadCompleta (sin envolver)
 */
public class ActividadCompletaResponse extends ActividadCompleta {
    
    /**
     * Obtener las preguntas de la actividad
     * (compatibilidad con código que espera getPreguntas)
     */
    public List<PreguntaActividad> getPreguntas() {
        return this.getPreguntaActividad();
    }

    public void setPreguntas(List<PreguntaActividad> preguntas) {
        this.setPreguntaActividad(preguntas);
    }
}

