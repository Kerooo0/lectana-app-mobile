package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ActividadesDocenteResponse {
    @SerializedName("ok")
    private boolean ok;
    
    @SerializedName("data")
    private List<Actividad> data;
    
    @SerializedName("actividades")
    private List<Actividad> actividades; // Mantener compatibilidad con formato anterior
    
    @SerializedName("total")
    private int total;

    public ActividadesDocenteResponse() {
    }

    public ActividadesDocenteResponse(boolean ok, List<Actividad> actividades, int total) {
        this.ok = ok;
        this.actividades = actividades;
        this.data = actividades; // Sincronizar ambos campos
        this.total = total;
    }

    // Getters y Setters
    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    /**
     * Obtener actividades (compatible con ambos formatos: data o actividades)
     */
    public List<Actividad> getActividades() {
        // Priorizar 'data' si existe, sino usar 'actividades'
        if (data != null && !data.isEmpty()) {
            return data;
        }
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
        this.data = actividades; // Sincronizar ambos campos
    }

    public List<Actividad> getData() {
        return data;
    }

    public void setData(List<Actividad> data) {
        this.data = data;
        this.actividades = data; // Sincronizar ambos campos
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
