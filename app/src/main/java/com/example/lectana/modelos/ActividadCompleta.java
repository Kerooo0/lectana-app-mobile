package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Modelo para representar la respuesta completa de una actividad desde el backend
 * Incluye todos los detalles: actividad, cuento, preguntas y respuestas
 */
public class ActividadCompleta {
    
    @SerializedName("id_actividad")
    private int idActividad;
    
    @SerializedName("descripcion")
    private String descripcion;
    
    @SerializedName("tipo")
    private String tipo;
    
    @SerializedName("fecha_entrega")
    private String fechaEntrega;
    
    @SerializedName("fecha_publicacion")
    private String fechaPublicacion;
    
    @SerializedName("cuento_id_cuento")
    private int cuentoIdCuento;
    
    @SerializedName("cuento")
    private CuentoApi cuento;
    
    @SerializedName("pregunta_actividad")
    private List<PreguntaActividad> preguntaActividad;
    
    @SerializedName("deleted_at")
    private String deletedAt;

    public ActividadCompleta() {
    }

    // Getters y Setters
    public int getIdActividad() {
        return idActividad;
    }

    public void setIdActividad(int idActividad) {
        this.idActividad = idActividad;
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

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public int getCuentoIdCuento() {
        return cuentoIdCuento;
    }

    public void setCuentoIdCuento(int cuentoIdCuento) {
        this.cuentoIdCuento = cuentoIdCuento;
    }

    public CuentoApi getCuento() {
        return cuento;
    }

    public void setCuento(CuentoApi cuento) {
        this.cuento = cuento;
    }

    public List<PreguntaActividad> getPreguntaActividad() {
        return preguntaActividad;
    }

    public void setPreguntaActividad(List<PreguntaActividad> preguntaActividad) {
        this.preguntaActividad = preguntaActividad;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
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
        return preguntaActividad != null ? preguntaActividad.size() : 0;
    }

    public boolean tienePreguntas() {
        return preguntaActividad != null && !preguntaActividad.isEmpty();
    }

    public boolean tieneCuento() {
        return cuento != null;
    }
}
