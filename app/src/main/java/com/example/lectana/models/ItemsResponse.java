package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ItemsResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("data")
    private List<Item> data;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }

    public static class Item {
        @SerializedName("id_item")
        private int id;

        @SerializedName("nombre")
        private String nombre;

        @SerializedName("descripcion")
        private String descripcion;

        @SerializedName("precio")
        private int precio;

        @SerializedName("url_imagen")
        private String urlImagen;

        @SerializedName("disponible")
        private boolean disponible;

        @SerializedName("movimiento")
        private String movimiento;

        @SerializedName("fecha_compra")
        private String fechaCompra;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public int getPrecio() {
            return precio;
        }

        public void setPrecio(int precio) {
            this.precio = precio;
        }

        public String getUrlImagen() {
            return urlImagen;
        }

        public void setUrlImagen(String urlImagen) {
            this.urlImagen = urlImagen;
        }

        public boolean isDisponible() {
            return disponible;
        }

        public void setDisponible(boolean disponible) {
            this.disponible = disponible;
        }

        public String getMovimiento() {
            return movimiento;
        }

        public void setMovimiento(String movimiento) {
            this.movimiento = movimiento;
        }

        public String getFechaCompra() {
            return fechaCompra;
        }

        public void setFechaCompra(String fechaCompra) {
            this.fechaCompra = fechaCompra;
        }
    }
}
