package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;

public class MarcarActividadCompletadaRequest {
    
    @SerializedName("actividadId")
    private int actividadId;

    public MarcarActividadCompletadaRequest() {
    }

    public MarcarActividadCompletadaRequest(int actividadId) {
        this.actividadId = actividadId;
    }

    public int getActividadId() {
        return actividadId;
    }

    public void setActividadId(int actividadId) {
        this.actividadId = actividadId;
    }
}
