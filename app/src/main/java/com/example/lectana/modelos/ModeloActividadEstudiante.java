package com.example.lectana.modelos;

public class ModeloActividadEstudiante {
    private String titulo;
    private String estado;
    private String fecha;
    private int progreso;
    private boolean completada;

    public ModeloActividadEstudiante(String titulo, String estado, String fecha, int progreso, boolean completada) {
        this.titulo = titulo;
        this.estado = estado;
        this.fecha = fecha;
        this.progreso = progreso;
        this.completada = completada;
    }

    // Getters
    public String getTitulo() {
        return titulo;
    }

    public String getEstado() {
        return estado;
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
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
