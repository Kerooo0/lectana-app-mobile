package com.example.lectana.modelos;

public class CrearAulaRequest {
    private String nombre_aula;
    private String grado;

    public CrearAulaRequest() {
    }

    public CrearAulaRequest(String nombre_aula, String grado) {
        this.nombre_aula = nombre_aula;
        this.grado = grado;
    }

    public String getNombre_aula() {
        return nombre_aula;
    }

    public void setNombre_aula(String nombre_aula) {
        this.nombre_aula = nombre_aula;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }
}
