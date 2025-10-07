package com.example.lectana.modelos;

public class ModeloAula {
    private String id_aula;
    private String nombre_aula;
    private String numero_estudiantes_aula;
    private String codigo_aula;
    private String estudiantes_activos_hoy;

    public ModeloAula() {
    }

    public ModeloAula(String id_aula, String nombre_aula, String numero_estudiantes_aula, String codigo_aula, String estudiantes_activos_hoy) {
        this.id_aula = id_aula;
        this.nombre_aula = nombre_aula;
        this.numero_estudiantes_aula = numero_estudiantes_aula;
        this.codigo_aula = codigo_aula;
        this.estudiantes_activos_hoy = estudiantes_activos_hoy;
    }

    // Métodos para obtener datos
    public String getId_aula() {
        return id_aula;
    }

    public void setId_aula(String id_aula) {
        this.id_aula = id_aula;
    }

    public String getNombre_aula() {
        return nombre_aula;
    }

    public void setNombre_aula(String nombre_aula) {
        this.nombre_aula = nombre_aula;
    }

    public String getNumero_estudiantes_aula() {
        return numero_estudiantes_aula;
    }

    public void setNumero_estudiantes_aula(String numero_estudiantes_aula) {
        this.numero_estudiantes_aula = numero_estudiantes_aula;
    }

    public String getCodigo_aula() {
        return codigo_aula;
    }

    public void setCodigo_aula(String codigo_aula) {
        this.codigo_aula = codigo_aula;
    }

    public String getEstudiantes_activos_hoy() {
        return estudiantes_activos_hoy;
    }

    public void setEstudiantes_activos_hoy(String estudiantes_activos_hoy) {
        this.estudiantes_activos_hoy = estudiantes_activos_hoy;
    }
}
