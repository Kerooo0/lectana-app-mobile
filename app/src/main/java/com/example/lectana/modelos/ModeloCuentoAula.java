package com.example.lectana.modelos;

public class ModeloCuentoAula {
    private String id;
    private String titulo;
    private String autor;
    private int progreso;
    private boolean completado;

    public ModeloCuentoAula(String id, String titulo, String autor, int progreso, boolean completado) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.progreso = progreso;
        this.completado = completado;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getProgreso() {
        return progreso;
    }

    public boolean isCompletado() {
        return completado;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setProgreso(int progreso) {
        this.progreso = progreso;
    }

    public void setCompletado(boolean completado) {
        this.completado = completado;
    }
}
