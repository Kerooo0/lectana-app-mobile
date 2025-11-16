package com.example.lectana.modelos.nuevas;

import com.example.lectana.modelos.Actividad;

public class CrearActividadBackendResponse {
    // Nuevo contrato: un objeto actividad y (opcional) asignaciones
    private Actividad actividad;

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }
}


