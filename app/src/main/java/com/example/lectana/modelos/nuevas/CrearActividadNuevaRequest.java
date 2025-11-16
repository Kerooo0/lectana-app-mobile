package com.example.lectana.modelos.nuevas;

public class CrearActividadNuevaRequest {
    private String titulo;
    private String descripcion;
    private String fechaInicio; // ISO-8601 opcional
    private String fechaFin;    // ISO-8601 opcional
    private Integer cursoId;    // o grupoId, según backend
    private Integer grupoId;    // opcional
    private String visibilidad; // opcional: "publico" | "privado"

    public CrearActividadNuevaRequest() {}

    public CrearActividadNuevaRequest(String titulo, String descripcion, String fechaInicio, String fechaFin, Integer cursoId) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cursoId = cursoId;
    }

    public CrearActividadNuevaRequest(String titulo, String descripcion, String fechaInicio, String fechaFin, Integer cursoId, Integer grupoId, String visibilidad) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cursoId = cursoId;
        this.grupoId = grupoId;
        this.visibilidad = visibilidad;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }

    public Integer getGrupoId() { return grupoId; }
    public void setGrupoId(Integer grupoId) { this.grupoId = grupoId; }

    public String getVisibilidad() { return visibilidad; }
    public void setVisibilidad(String visibilidad) { this.visibilidad = visibilidad; }
}


