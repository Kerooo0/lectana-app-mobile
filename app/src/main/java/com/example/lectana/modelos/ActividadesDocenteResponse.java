package com.example.lectana.modelos;

import java.util.List;

public class ActividadesDocenteResponse {
    private boolean ok;
    private List<Actividad> actividades;
    private int total;

    public ActividadesDocenteResponse() {
    }

    public ActividadesDocenteResponse(boolean ok, List<Actividad> actividades, int total) {
        this.ok = ok;
        this.actividades = actividades;
        this.total = total;
    }

    // Getters y Setters
    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
