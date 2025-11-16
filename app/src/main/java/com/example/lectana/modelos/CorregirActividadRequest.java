package com.example.lectana.modelos;

public class CorregirActividadRequest {
    private int id_alumno;
    private int total;
    private int total_correctas;
    private int total_incorrectas;
    private int sin_corregir;
    private String estado;
    private double nota;

    public CorregirActividadRequest() {}

    public CorregirActividadRequest(int id_alumno, int total, int total_correctas, int total_incorrectas, int sin_corregir, String estado, double nota) {
        this.id_alumno = id_alumno;
        this.total = total;
        this.total_correctas = total_correctas;
        this.total_incorrectas = total_incorrectas;
        this.sin_corregir = sin_corregir;
        this.estado = estado;
        this.nota = nota;
    }

    // Getters
    public int getId_alumno() {
        return id_alumno;
    }

    public int getTotal() {
        return total;
    }

    public int getTotal_correctas() {
        return total_correctas;
    }

    public int getTotal_incorrectas() {
        return total_incorrectas;
    }

    public int getSin_corregir() {
        return sin_corregir;
    }

    public String getEstado() {
        return estado;
    }

    public double getNota() {
        return nota;
    }

    // Setters
    public void setId_alumno(int id_alumno) {
        this.id_alumno = id_alumno;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotal_correctas(int total_correctas) {
        this.total_correctas = total_correctas;
    }

    public void setTotal_incorrectas(int total_incorrectas) {
        this.total_incorrectas = total_incorrectas;
    }

    public void setSin_corregir(int sin_corregir) {
        this.sin_corregir = sin_corregir;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}

