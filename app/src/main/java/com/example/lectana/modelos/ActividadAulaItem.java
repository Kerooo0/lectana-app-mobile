package com.example.lectana.modelos;

public class ActividadAulaItem {
    private int actividad_id_actividad;
    private int aula_id_aula;
    private Actividad actividad; // objeto anidado con los datos

    public int getActividad_id_actividad() { return actividad_id_actividad; }
    public void setActividad_id_actividad(int actividad_id_actividad) { this.actividad_id_actividad = actividad_id_actividad; }

    public int getAula_id_aula() { return aula_id_aula; }
    public void setAula_id_aula(int aula_id_aula) { this.aula_id_aula = aula_id_aula; }

    public Actividad getActividad() { return actividad; }
    public void setActividad(Actividad actividad) { this.actividad = actividad; }
}


