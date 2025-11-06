package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

/**
 * Respuesta del endpoint GET /api/actividades/actividadCompleta/:idActividad
 */
public class ActividadCompletaResponse {
    
    @SerializedName("actividad")
    private ActividadCompleta actividad;

    public ActividadCompletaResponse() {
    }

    public ActividadCompleta getActividad() {
        return actividad;
    }

    public void setActividad(ActividadCompleta actividad) {
        this.actividad = actividad;
    }
}
