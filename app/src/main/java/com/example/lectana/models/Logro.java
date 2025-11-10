package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;

public class Logro {
    @SerializedName("id_logros")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("url_imagen")
    private String urlImagen;

    @SerializedName("progreso")
    private int progreso; // 0-100

    @SerializedName("desbloqueado")
    private boolean desbloqueado;

    // Constructores
    public Logro() {}

    public Logro(int id, String nombre, String descripcion, String urlImagen, int progreso, boolean desbloqueado) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.urlImagen = urlImagen;
        this.progreso = progreso;
        this.desbloqueado = desbloqueado;
    }

    // Getters y Setters
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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public boolean isDesbloqueado() {
        return desbloqueado;
    }

    public void setDesbloqueado(boolean desbloqueado) {
        this.desbloqueado = desbloqueado;
    }
}
