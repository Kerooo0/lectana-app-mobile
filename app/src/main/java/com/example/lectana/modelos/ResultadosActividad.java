package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

/**
 * Modelo para representar los resultados de una actividad
 */
public class ResultadosActividad {
    
    @SerializedName("sin_corregir")
    private int sin_corregir;
    
    @SerializedName("total_estudiantes")
    private int total_estudiantes;
    
    @SerializedName("completadas")
    private int completadas;
    
    @SerializedName("corregidas")
    private int corregidas;
    
    @SerializedName("nota_promedio")
    private double nota_promedio;

    public ResultadosActividad() {}

    public ResultadosActividad(int sin_corregir, int total_estudiantes, int completadas, int corregidas, double nota_promedio) {
        this.sin_corregir = sin_corregir;
        this.total_estudiantes = total_estudiantes;
        this.completadas = completadas;
        this.corregidas = corregidas;
        this.nota_promedio = nota_promedio;
    }

    // Getters
    public int getSin_corregir() {
        return sin_corregir;
    }

    public int getTotal_estudiantes() {
        return total_estudiantes;
    }

    public int getCompletadas() {
        return completadas;
    }

    public int getCorregidas() {
        return corregidas;
    }

    public double getNota_promedio() {
        return nota_promedio;
    }

    // Setters
    public void setSin_corregir(int sin_corregir) {
        this.sin_corregir = sin_corregir;
    }

    public void setTotal_estudiantes(int total_estudiantes) {
        this.total_estudiantes = total_estudiantes;
    }

    public void setCompletadas(int completadas) {
        this.completadas = completadas;
    }

    public void setCorregidas(int corregidas) {
        this.corregidas = corregidas;
    }

    public void setNota_promedio(double nota_promedio) {
        this.nota_promedio = nota_promedio;
    }
}

