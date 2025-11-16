package com.example.lectana.modelos;

import java.util.List;

public class Pregunta {
    private int id_pregunta;
    private String enunciado;
    private int actividad_id_actividad;
    private List<Respuesta> respuestas;

    public Pregunta() {
    }

    public Pregunta(String enunciado) {
        this.enunciado = enunciado;
    }

    // Getters y Setters
    public int getId_pregunta() {
        return id_pregunta;
    }

    public void setId_pregunta(int id_pregunta) {
        this.id_pregunta = id_pregunta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public int getActividad_id_actividad() {
        return actividad_id_actividad;
    }

    public void setActividad_id_actividad(int actividad_id_actividad) {
        this.actividad_id_actividad = actividad_id_actividad;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }

    // Métodos de utilidad
    public int getTotalRespuestas() {
        return respuestas != null ? respuestas.size() : 0;
    }

    public boolean tieneRespuestaCorrecta() {
        if (respuestas == null) return false;
        for (Respuesta respuesta : respuestas) {
            if (respuesta.isEs_correcta()) {
                return true;
            }
        }
        return false;
    }
}

