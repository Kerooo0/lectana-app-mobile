package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Envolvente para la respuesta del endpoint GET /api/actividades/actividadCompleta/:idActividad
 * Backend devuelve: {"actividadCompleta": [{...preguntas...}]}
 */
public class ActividadCompletaResponseWrapper {
    
    @SerializedName("actividadCompleta")
    private List<PreguntaActividad> actividadCompleta;
    
    public List<PreguntaActividad> getActividadCompleta() {
        return actividadCompleta;
    }
    
    public void setActividadCompleta(List<PreguntaActividad> actividadCompleta) {
        this.actividadCompleta = actividadCompleta;
    }
}
