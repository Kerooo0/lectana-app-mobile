package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo que representa un registro de la tabla intermedia actividad_aula
 * Esta tabla relaciona actividades con aulas
 */
public class ActividadAula {
    
    @SerializedName("id_actividad_aula")
    private int idActividadAula;
    
    @SerializedName("actividad_id_actividad")
    private int actividadIdActividad;
    
    @SerializedName("aula_id_aula")
    private int aulaIdAula;
    
    @SerializedName("fecha_asignacion")
    private String fechaAsignacion;
    
    /**
     * Objeto actividad nested que viene del JOIN en el backend
     * Contiene todos los datos de la actividad
     */
    @SerializedName("actividad")
    private Actividad actividad;

    // Constructor vacío
    public ActividadAula() {
    }

    // Getters y Setters
    public int getIdActividadAula() {
        return idActividadAula;
    }

    public void setIdActividadAula(int idActividadAula) {
        this.idActividadAula = idActividadAula;
    }

    public int getActividadIdActividad() {
        return actividadIdActividad;
    }

    public void setActividadIdActividad(int actividadIdActividad) {
        this.actividadIdActividad = actividadIdActividad;
    }

    public int getAulaIdAula() {
        return aulaIdAula;
    }

    public void setAulaIdAula(int aulaIdAula) {
        this.aulaIdAula = aulaIdAula;
    }

    public String getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(String fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}
