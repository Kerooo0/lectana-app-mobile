package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("id_item")
    private String id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("tipo")
    private String tipo; // avatar, marco, fondo, badge, accesorio

    @SerializedName("categoria")
    private String categoria; // superheroe, animal, fantasia, deportes, musica, arte, naturaleza

    @SerializedName("precio_puntos")
    private int precioPuntos;

    @SerializedName("url_imagen")
    private String urlImagen;

    @SerializedName("activo")
    private boolean activo;

    @SerializedName("desbloqueado")
    private boolean desbloqueado;

    // Constructores
    public Item() {}

    public Item(String id, String nombre, String descripcion, String tipo, String categoria, 
                int precioPuntos, String urlImagen, boolean activo, boolean desbloqueado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.precioPuntos = precioPuntos;
        this.urlImagen = urlImagen;
        this.activo = activo;
        this.desbloqueado = desbloqueado;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPrecioPuntos() {
        return precioPuntos;
    }

    public void setPrecioPuntos(int precioPuntos) {
        this.precioPuntos = precioPuntos;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isDesbloqueado() {
        return desbloqueado;
    }

    public void setDesbloqueado(boolean desbloqueado) {
        this.desbloqueado = desbloqueado;
    }
}
