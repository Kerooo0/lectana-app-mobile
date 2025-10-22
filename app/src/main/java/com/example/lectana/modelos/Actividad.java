package com.example.lectana.modelos;

import java.util.List;

public class Actividad {
    private int id_actividad;
    private String descripcion;
    private String tipo;
    private int cuento_id_cuento;
    private int docente_id_docente;
    private String fecha_creacion;
    private List<Pregunta> preguntas;
    private List<Integer> aulas_ids;
    private CuentoApi cuento;

    public Actividad() {
    }

    public Actividad(int id_actividad, String descripcion, String tipo, int cuento_id_cuento, int docente_id_docente) {
        this.id_actividad = id_actividad;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.cuento_id_cuento = cuento_id_cuento;
        this.docente_id_docente = docente_id_docente;
    }

    // Getters y Setters
    public int getId_actividad() {
        return id_actividad;
    }

    public void setId_actividad(int id_actividad) {
        this.id_actividad = id_actividad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCuento_id_cuento() {
        return cuento_id_cuento;
    }

    public void setCuento_id_cuento(int cuento_id_cuento) {
        this.cuento_id_cuento = cuento_id_cuento;
    }

    public int getDocente_id_docente() {
        return docente_id_docente;
    }

    public void setDocente_id_docente(int docente_id_docente) {
        this.docente_id_docente = docente_id_docente;
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    public List<Integer> getAulas_ids() {
        return aulas_ids;
    }

    public void setAulas_ids(List<Integer> aulas_ids) {
        this.aulas_ids = aulas_ids;
    }

    public CuentoApi getCuento() {
        return cuento;
    }

    public void setCuento(CuentoApi cuento) {
        this.cuento = cuento;
    }

    // Métodos de utilidad
    public String getTipoDisplay() {
        switch (tipo) {
            case "multiple_choice":
                return "Opción Múltiple";
            case "respuesta_abierta":
                return "Respuesta Abierta";
            default:
                return tipo;
        }
    }

    public int getTotalPreguntas() {
        return preguntas != null ? preguntas.size() : 0;
    }

    public int getTotalAulas() {
        return aulas_ids != null ? aulas_ids.size() : 0;
    }
}

