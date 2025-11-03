package com.example.lectana.modelos.nuevas;

import com.example.lectana.modelos.Actividad;
import java.util.List;

public class CrearActividadBackendResponse {
    // El backend devuelve un array con la(s) filas insertadas
    private List<Actividad> actividad;

    public List<Actividad> getActividad() { return actividad; }
    public void setActividad(List<Actividad> actividad) { this.actividad = actividad; }
}


