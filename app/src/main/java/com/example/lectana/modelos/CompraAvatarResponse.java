package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CompraAvatarResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("compra")
    private CompraInfo compra;

    @SerializedName("item")
    private ItemInfo item;

    @SerializedName("puntosActuales")
    private int puntosActuales;

    @SerializedName("puntosGastados")
    private int puntosGastados;

    @SerializedName("logrosDesbloqueados")
    private List<LogroDesbloqueado> logrosDesbloqueados;

    @SerializedName("mensaje")
    private String mensaje;

    @SerializedName("error")
    private String error;

    // Clases internas
    public static class CompraInfo {
        @SerializedName("alumno_id_alumno")
        public int alumnoId;

        @SerializedName("item_id_item")
        public int itemId;

        @SerializedName("movimiento")
        public String movimiento;

        @SerializedName("fecha_canje")
        public String fechaCanje;
    }

    public static class ItemInfo {
        @SerializedName("id_item")
        public int idItem;

        @SerializedName("nombre")
        public String nombre;

        @SerializedName("precio")
        public int precio;
    }

    public static class LogroDesbloqueado {
        @SerializedName("id_logros")
        public int idLogros;

        @SerializedName("nombre")
        public String nombre;

        @SerializedName("descripcion")
        public String descripcion;
    }

    // Constructores
    public CompraAvatarResponse() {}

    // Getters y Setters
    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public CompraInfo getCompra() {
        return compra;
    }

    public void setCompra(CompraInfo compra) {
        this.compra = compra;
    }

    public ItemInfo getItem() {
        return item;
    }

    public void setItem(ItemInfo item) {
        this.item = item;
    }

    public int getPuntosActuales() {
        return puntosActuales;
    }

    public void setPuntosActuales(int puntosActuales) {
        this.puntosActuales = puntosActuales;
    }

    public int getPuntosGastados() {
        return puntosGastados;
    }

    public void setPuntosGastados(int puntosGastados) {
        this.puntosGastados = puntosGastados;
    }

    public List<LogroDesbloqueado> getLogrosDesbloqueados() {
        return logrosDesbloqueados;
    }

    public void setLogrosDesbloqueados(List<LogroDesbloqueado> logrosDesbloqueados) {
        this.logrosDesbloqueados = logrosDesbloqueados;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
