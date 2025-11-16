package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Respuesta del endpoint GET /api/actividades/actividadesPorAula/:id_aula
 * Backend retorna registros de la tabla intermedia actividad_aula con datos nested
 * NOTA: El backend puede retornar { actividades: [...] } o directamente [...]
 */
public class ActividadesPorAulaResponse {
    
    @SerializedName("actividades")
    private List<ActividadAula> actividades;

    public ActividadesPorAulaResponse() {
    }

    public List<ActividadAula> getActividades() {
        if (actividades == null) {
            return new ArrayList<>();
        }
        return actividades;
    }

    public void setActividades(List<ActividadAula> actividades) {
        this.actividades = actividades;
    }
}
