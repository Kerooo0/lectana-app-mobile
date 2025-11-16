package com.example.lectana.modelos;

import java.util.List;

public class ModeloCuentoConActividades {
    private String id;
    private String nombreCuento;
    private int totalActividades;
    private List<ModeloActividadDetallada> actividades;

    public ModeloCuentoConActividades(String id, String nombreCuento, int totalActividades, List<ModeloActividadDetallada> actividades) {
        this.id = id;
        this.nombreCuento = nombreCuento;
        this.totalActividades = totalActividades;
        this.actividades = actividades;
    }

    // Getters
    public String getId() { return id; }
    public String getNombreCuento() { return nombreCuento; }
    public int getTotalActividades() { return totalActividades; }
    public List<ModeloActividadDetallada> getActividades() { return actividades; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setNombreCuento(String nombreCuento) { this.nombreCuento = nombreCuento; }
    public void setTotalActividades(int totalActividades) { this.totalActividades = totalActividades; }
    public void setActividades(List<ModeloActividadDetallada> actividades) { this.actividades = actividades; }
}
