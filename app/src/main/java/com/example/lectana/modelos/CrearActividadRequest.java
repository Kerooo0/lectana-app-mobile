package com.example.lectana.modelos;

import java.util.List;

public class CrearActividadRequest {
    private String descripcion;
    private String tipo;
    private int cuento_id_cuento;
    private List<Integer> aulas_ids;
    private List<PreguntaRequest> preguntas;

    public CrearActividadRequest() {
    }

    public CrearActividadRequest(String descripcion, String tipo, int cuento_id_cuento, List<Integer> aulas_ids, List<PreguntaRequest> preguntas) {
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.cuento_id_cuento = cuento_id_cuento;
        this.aulas_ids = aulas_ids;
        this.preguntas = preguntas;
    }

    // Getters y Setters
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

    public List<Integer> getAulas_ids() {
        return aulas_ids;
    }

    public void setAulas_ids(List<Integer> aulas_ids) {
        this.aulas_ids = aulas_ids;
    }

    public List<PreguntaRequest> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<PreguntaRequest> preguntas) {
        this.preguntas = preguntas;
    }

    // Clase interna para preguntas
    public static class PreguntaRequest {
        private String enunciado;
        private List<RespuestaRequest> respuestas;

        public PreguntaRequest() {
        }

        public PreguntaRequest(String enunciado, List<RespuestaRequest> respuestas) {
            this.enunciado = enunciado;
            this.respuestas = respuestas;
        }

        public String getEnunciado() {
            return enunciado;
        }

        public void setEnunciado(String enunciado) {
            this.enunciado = enunciado;
        }

        public List<RespuestaRequest> getRespuestas() {
            return respuestas;
        }

        public void setRespuestas(List<RespuestaRequest> respuestas) {
            this.respuestas = respuestas;
        }
    }

    // Clase interna para respuestas
    public static class RespuestaRequest {
        private String respuesta;
        private boolean es_correcta;

        public RespuestaRequest() {
        }

        public RespuestaRequest(String respuesta, boolean es_correcta) {
            this.respuesta = respuesta;
            this.es_correcta = es_correcta;
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
    }
}

