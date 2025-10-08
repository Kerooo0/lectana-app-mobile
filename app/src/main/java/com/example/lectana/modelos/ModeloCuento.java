package com.example.lectana.modelos;

public class ModeloCuento {
    private int id;
    private String titulo;
    private String autor;
    private String nivel;
    private String descripcion;
    private boolean seleccionado;

    public ModeloCuento() {}

    public ModeloCuento(int id, String titulo, String autor, String nivel, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.nivel = nivel;
        this.descripcion = descripcion;
        this.seleccionado = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(boolean seleccionado) {
        this.seleccionado = seleccionado;
    }
}
