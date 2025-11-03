package com.example.lectana.modelos.nuevas;

public class CrearPreguntaActividadRequest {
    private String enunciado;
    private String tipo; // "multiple_choice" | "abierta"
    private Integer puntaje; // opcional
    private Integer orden;   // opcional
    private Boolean obligatoria; // opcional

    public CrearPreguntaActividadRequest() {}

    public CrearPreguntaActividadRequest(String enunciado, String tipo, Integer puntaje, Integer orden, Boolean obligatoria) {
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.puntaje = puntaje;
        this.orden = orden;
        this.obligatoria = obligatoria;
    }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Boolean getObligatoria() { return obligatoria; }
    public void setObligatoria(Boolean obligatoria) { this.obligatoria = obligatoria; }
}


