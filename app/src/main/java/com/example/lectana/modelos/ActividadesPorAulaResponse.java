package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Respuesta del endpoint GET /api/actividades/actividadesPorAula/:id_aula
 * Backend devuelve registros de la tabla intermedia actividad_aula con datos nested
 */
public class ActividadesPorAulaResponse {
    
    @SerializedName("actividades")
    private List<ActividadAula> actividades;

    public ActividadesPorAulaResponse() {
    }

    public List<ActividadAula> getActividades() {
        return actividades;
    }

    public void setActividades(List<ActividadAula> actividades) {
        this.actividades = actividades;
    }
}
