package com.example.lectana.modelos;

public class ModeloCuento {
    private int id;
    private String titulo;
    private String autor;
    private String genero;
    private String edadRecomendada;
    private String rating;
    private String imagenUrl;
    private String tiempoLectura;
    private String descripcion;
    private String pdfUrl;
    private String audioUrl;
    private Integer audioDuration;
    private String audioStatus;
    private boolean seleccionado;

    public ModeloCuento() {}

    public ModeloCuento(int id, String titulo, String autor, String genero, 
                       String edadRecomendada, String rating, String imagenUrl, 
                       String tiempoLectura, String descripcion, String pdfUrl,
                       String audioUrl, Integer audioDuration, String audioStatus) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.edadRecomendada = edadRecomendada;
        this.rating = rating;
        this.imagenUrl = imagenUrl;
        this.tiempoLectura = tiempoLectura;
        this.descripcion = descripcion;
        this.pdfUrl = pdfUrl;
        this.audioUrl = audioUrl;
        this.audioDuration = audioDuration;
        this.audioStatus = audioStatus;
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

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEdadRecomendada() {
        return edadRecomendada;
    }

    public void setEdadRecomendada(String edadRecomendada) {
        this.edadRecomendada = edadRecomendada;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getTiempoLectura() {
        return tiempoLectura;
    }

    public void setTiempoLectura(String tiempoLectura) {
        this.tiempoLectura = tiempoLectura;
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

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Integer getAudioDuration() {
        return audioDuration;
    }

    public void setAudioDuration(Integer audioDuration) {
        this.audioDuration = audioDuration;
    }

    public String getAudioStatus() {
        return audioStatus;
    }

    public void setAudioStatus(String audioStatus) {
        this.audioStatus = audioStatus;
    }
}
