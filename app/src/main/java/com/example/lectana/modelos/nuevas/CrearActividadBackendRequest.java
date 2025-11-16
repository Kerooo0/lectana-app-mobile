package com.example.lectana.modelos.nuevas;

public class CrearActividadBackendRequest {
    private String fecha_entrega; // ISO-8601
    private String tipo; // "multiple_choice" | "respuesta_abierta"
    private String descripcion; // opcional pero recomendado
    private int id_cuento; // requerido
    private java.util.List<Integer> aulas_ids; // requerido según nuevo contrato

    public CrearActividadBackendRequest() {}

    public CrearActividadBackendRequest(String fecha_entrega, String tipo, String descripcion, int id_cuento, java.util.List<Integer> aulas_ids) {
        this.fecha_entrega = fecha_entrega;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.id_cuento = id_cuento;
        this.aulas_ids = aulas_ids;
    }

    public String getFecha_entrega() { return fecha_entrega; }
    public void setFecha_entrega(String fecha_entrega) { this.fecha_entrega = fecha_entrega; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getId_cuento() { return id_cuento; }
    public void setId_cuento(int id_cuento) { this.id_cuento = id_cuento; }

    public java.util.List<Integer> getAulas_ids() { return aulas_ids; }
    public void setAulas_ids(java.util.List<Integer> aulas_ids) { this.aulas_ids = aulas_ids; }
}


