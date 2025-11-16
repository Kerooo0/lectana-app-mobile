package com.example.lectana.modelos;

public class ModeloEstudianteAula {
    private String id;
    private String nombre;
    private String ultimaActividad;
    private int progreso;
    private boolean activo;

    public ModeloEstudianteAula(String id, String nombre, String ultimaActividad, int progreso, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.ultimaActividad = ultimaActividad;
        this.progreso = progreso;
        this.activo = activo;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUltimaActividad() {
        return ultimaActividad;
    }

    public int getProgreso() {
        return progreso;
    }

    public boolean isActivo() {
        return activo;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUltimaActividad(String ultimaActividad) {
        this.ultimaActividad = ultimaActividad;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
