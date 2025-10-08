package com.example.lectana.modelos;

public class ModeloProgresoEstudiante {
    private String idEstudiante;
    private String nombreEstudiante;
    private String tiempoUltimaActividad;
    private int puntuacionActual;
    private int puntuacionTotal;
    private int porcentajeProgreso;
    private String estadoProgreso; // "completado", "en_progreso", "pendiente"

    public ModeloProgresoEstudiante() {}

    public ModeloProgresoEstudiante(String idEstudiante, String nombreEstudiante, 
                                  String tiempoUltimaActividad, int puntuacionActual, 
                                  int puntuacionTotal, int porcentajeProgreso, 
                                  String estadoProgreso) {
        this.idEstudiante = idEstudiante;
        this.nombreEstudiante = nombreEstudiante;
        this.tiempoUltimaActividad = tiempoUltimaActividad;
        this.puntuacionActual = puntuacionActual;
        this.puntuacionTotal = puntuacionTotal;
        this.porcentajeProgreso = porcentajeProgreso;
        this.estadoProgreso = estadoProgreso;
    }

    // Getters y Setters
    public String getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(String idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getTiempoUltimaActividad() {
        return tiempoUltimaActividad;
    }

    public void setTiempoUltimaActividad(String tiempoUltimaActividad) {
        this.tiempoUltimaActividad = tiempoUltimaActividad;
    }

    public int getPuntuacionActual() {
        return puntuacionActual;
    }

    public void setPuntuacionActual(int puntuacionActual) {
        this.puntuacionActual = puntuacionActual;
    }

    public int getPuntuacionTotal() {
        return puntuacionTotal;
    }

    public void setPuntuacionTotal(int puntuacionTotal) {
        this.puntuacionTotal = puntuacionTotal;
    }

    public int getPorcentajeProgreso() {
        return porcentajeProgreso;
    }

    public void setPorcentajeProgreso(int porcentajeProgreso) {
        this.porcentajeProgreso = porcentajeProgreso;
    }

    public String getEstadoProgreso() {
        return estadoProgreso;
    }

    public void setEstadoProgreso(String estadoProgreso) {
        this.estadoProgreso = estadoProgreso;
    }

    public String getPuntuacionFormateada() {
        return puntuacionActual + "/" + puntuacionTotal;
    }

    public String getPorcentajeFormateado() {
        return porcentajeProgreso + "%";
    }
}
