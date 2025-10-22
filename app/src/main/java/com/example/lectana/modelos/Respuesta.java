package com.example.lectana.modelos;

public class Respuesta {
    private int id_respuesta;
    private String respuesta;
    private boolean es_correcta;
    private int pregunta_id_pregunta;

    public Respuesta() {
    }

    public Respuesta(String respuesta, boolean es_correcta) {
        this.respuesta = respuesta;
        this.es_correcta = es_correcta;
    }

    // Getters y Setters
    public int getId_respuesta() {
        return id_respuesta;
    }

    public void setId_respuesta(int id_respuesta) {
        this.id_respuesta = id_respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isEs_correcta() {
        return es_correcta;
    }

    public void setEs_correcta(boolean es_correcta) {
        this.es_correcta = es_correcta;
    }

    public int getPregunta_id_pregunta() {
        return pregunta_id_pregunta;
    }

    public void setPregunta_id_pregunta(int pregunta_id_pregunta) {
        this.pregunta_id_pregunta = pregunta_id_pregunta;
    }
}

