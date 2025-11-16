package com.example.lectana.modelos;

public class ModeloActividadAula {
    private String id;
    private String nombre;
    private String fecha;
    private int progreso;
    private boolean completada;

    public ModeloActividadAula(String id, String nombre, String fecha, int progreso, boolean completada) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.progreso = progreso;
        this.completada = completada;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public int getProgreso() {
        return progreso;
    }

    public boolean isCompletada() {
        return completada;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}
