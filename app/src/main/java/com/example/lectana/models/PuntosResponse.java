package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;

public class PuntosResponse {
    @SerializedName("ok")
    private boolean ok;
    
    @SerializedName("mensaje")
    private String mensaje;
    
    @SerializedName("data")
    private PuntosData data;
    
    public boolean isOk() {
        return ok;
    }
    
    public void setOk(boolean ok) {
        this.ok = ok;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public PuntosData getData() {
        return data;
    }
    
    public void setData(PuntosData data) {
        this.data = data;
    }
    
    public static class PuntosData {
        @SerializedName("puntos")
        private int puntos;
        
        @SerializedName("alumno_id_alumno")
        private int alumnoId;
        
        public int getPuntos() {
            return puntos;
        }
        
        public void setPuntos(int puntos) {
            this.puntos = puntos;
        }
        
        public int getAlumnoId() {
            return alumnoId;
        }
        
        public void setAlumnoId(int alumnoId) {
            this.alumnoId = alumnoId;
        }
    }
}
