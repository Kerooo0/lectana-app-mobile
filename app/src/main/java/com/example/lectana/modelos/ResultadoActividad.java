package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

public class ResultadoActividad {
    
    @SerializedName("id_resultado_actividad")
    private int idResultadoActividad;
    
    @SerializedName("id_alumno")
    private int idAlumno;
    
    @SerializedName("id_actividad")
    private int idActividad;
    
    @SerializedName("estado")
    private String estado;
    
    @SerializedName("sin_corregir")
    private int sinCorregir;
    
    @SerializedName("porcentaje")
    private double porcentaje;
    
    @SerializedName("fecha_envio")
    private String fechaEnvio;
    
    @SerializedName("fecha_correccion")
    private String fechaCorreccion;

    public ResultadoActividad() {
    }

    public int getIdResultadoActividad() {
        return idResultadoActividad;
    }

    public void setIdResultadoActividad(int idResultadoActividad) {
        this.idResultadoActividad = idResultadoActividad;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getSinCorregir() {
        return sinCorregir;
    }

    public void setSinCorregir(int sinCorregir) {
        this.sinCorregir = sinCorregir;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(String fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getFechaCorreccion() {
        return fechaCorreccion;
    }

    public void setFechaCorreccion(String fechaCorreccion) {
        this.fechaCorreccion = fechaCorreccion;
    }
}
