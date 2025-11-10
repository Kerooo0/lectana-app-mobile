package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;

public class EstadisticasLogrosResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("data")
    private EstadisticasLogros data;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public EstadisticasLogros getData() {
        return data;
    }

    public void setData(EstadisticasLogros data) {
        this.data = data;
    }

    public static class EstadisticasLogros {
        @SerializedName("total_logros")
        private int totalLogros;

        @SerializedName("desbloqueados")
        private int desbloqueados;

        @SerializedName("en_progreso")
        private int enProgreso;

        @SerializedName("progreso_promedio")
        private double progresoPromedio;

        public int getTotalLogros() {
            return totalLogros;
        }

        public void setTotalLogros(int totalLogros) {
            this.totalLogros = totalLogros;
        }

        public int getDesbloqueados() {
            return desbloqueados;
        }

        public void setDesbloqueados(int desbloqueados) {
            this.desbloqueados = desbloqueados;
        }

        public int getEnProgreso() {
            return enProgreso;
        }

        public void setEnProgreso(int enProgreso) {
            this.enProgreso = enProgreso;
        }

        public double getProgresoPromedio() {
            return progresoPromedio;
        }

        public void setProgresoPromedio(double progresoPromedio) {
            this.progresoPromedio = progresoPromedio;
        }
    }
}
