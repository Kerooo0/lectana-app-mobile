package com.example.lectana.modelos.nuevas;

public class CrearRespuestaActividadRequest {
    private String texto;
    private Boolean esCorrecta;
    private String retroalimentacion; // opcional
    private Integer orden; // opcional

    public CrearRespuestaActividadRequest() {}

    public CrearRespuestaActividadRequest(String texto, Boolean esCorrecta, String retroalimentacion, Integer orden) {
        this.texto = texto;
        this.esCorrecta = esCorrecta;
        this.retroalimentacion = retroalimentacion;
        this.orden = orden;
    }

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Boolean getEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(Boolean esCorrecta) { this.esCorrecta = esCorrecta; }

    public String getRetroalimentacion() { return retroalimentacion; }
    public void setRetroalimentacion(String retroalimentacion) { this.retroalimentacion = retroalimentacion; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
}


