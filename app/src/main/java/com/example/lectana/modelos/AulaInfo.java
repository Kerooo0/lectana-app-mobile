package com.example.lectana.modelos;

public class AulaInfo {
    private int id_aula;
    private String nombre_aula;
    private String grado;
    private String codigo_acceso;

    public int getId_aula() {
        return id_aula;
    }

    public void setId_aula(int id_aula) {
        this.id_aula = id_aula;
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

    public String getCodigo_acceso() {
        return codigo_acceso;
    }

    public void setCodigo_acceso(String codigo_acceso) {
        this.codigo_acceso = codigo_acceso;
    }
}
