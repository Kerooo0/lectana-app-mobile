package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;

public class Avatar {
    @SerializedName("id_item")
    private int idItem;

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
    private String movimiento; // "compra" o "regalo"

    @SerializedName("fecha_compra")
    private String fechaCompra;

    // Constructores
    public Avatar() {}

    public Avatar(int idItem, String nombre, String descripcion, int precio, String urlImagen) {
        this.idItem = idItem;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.disponible = true;
    }

    // Getters y Setters
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
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
