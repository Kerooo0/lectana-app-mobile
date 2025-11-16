package com.example.lectana.modelos;

public class EstadisticasActividad {
    private int total_estudiantes;
    private int completadas;
    private int corregidas;
    private int pendientes;
    private double nota_promedio;
    private double nota_maxima;
    private double nota_minima;

    public EstadisticasActividad() {}

    public EstadisticasActividad(int total_estudiantes, int completadas, int corregidas, int pendientes, double nota_promedio, double nota_maxima, double nota_minima) {
        this.total_estudiantes = total_estudiantes;
        this.completadas = completadas;
        this.corregidas = corregidas;
        this.pendientes = pendientes;
        this.nota_promedio = nota_promedio;
        this.nota_maxima = nota_maxima;
        this.nota_minima = nota_minima;
    }

    // Getters
    public int getTotal_estudiantes() {
        return total_estudiantes;
    }

    public int getCompletadas() {
        return completadas;
    }

    public int getCorregidas() {
        return corregidas;
    }

    public int getPendientes() {
        return pendientes;
    }

    public double getNota_promedio() {
        return nota_promedio;
    }

    public double getNota_maxima() {
        return nota_maxima;
    }

    public double getNota_minima() {
        return nota_minima;
    }

    // Setters
    public void setTotal_estudiantes(int total_estudiantes) {
        this.total_estudiantes = total_estudiantes;
    }

    public void setCompletadas(int completadas) {
        this.completadas = completadas;
    }

    public void setCorregidas(int corregidas) {
        this.corregidas = corregidas;
    }

    public void setPendientes(int pendientes) {
        this.pendientes = pendientes;
    }

    public void setNota_promedio(double nota_promedio) {
        this.nota_promedio = nota_promedio;
    }

    public void setNota_maxima(double nota_maxima) {
        this.nota_maxima = nota_maxima;
    }

    public void setNota_minima(double nota_minima) {
        this.nota_minima = nota_minima;
    }
}

