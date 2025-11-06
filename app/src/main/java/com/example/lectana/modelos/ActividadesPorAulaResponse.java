package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Respuesta del endpoint GET /api/actividades/actividadesPorAula/:id_aula
 */
public class ActividadesPorAulaResponse {
    
    @SerializedName("actividades")
    private List<Actividad> actividades;

    public ActividadesPorAulaResponse() {
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }
}
