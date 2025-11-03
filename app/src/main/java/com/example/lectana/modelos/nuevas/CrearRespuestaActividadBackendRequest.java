package com.example.lectana.modelos.nuevas;

public class CrearRespuestaActividadBackendRequest {
    private java.util.List<String> respuestas; // la DB espera text[]
    private Integer respuesta_correcta; // DB espera smallint (0/1)

    public CrearRespuestaActividadBackendRequest() {}

    public CrearRespuestaActividadBackendRequest(java.util.List<String> respuestas, Integer respuesta_correcta) {
        this.respuestas = respuestas;
        this.respuesta_correcta = respuesta_correcta;
    }

    public java.util.List<String> getRespuestas() { return respuestas; }
    public void setRespuestas(java.util.List<String> respuestas) { this.respuestas = respuestas; }

    public Integer getRespuesta_correcta() { return respuesta_correcta; }
    public void setRespuesta_correcta(Integer respuesta_correcta) { this.respuesta_correcta = respuesta_correcta; }
}


