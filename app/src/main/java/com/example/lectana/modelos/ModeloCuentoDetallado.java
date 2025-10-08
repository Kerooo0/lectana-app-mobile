package com.example.lectana.modelos;

public class ModeloCuentoDetallado {
    private String id;
    private String titulo;
    private String subtitulo;
    private String imagenUrl;
    private int estudiantesLeidos;
    private String actividad1;
    private int completadas1;
    private String actividad2;
    private int completadas2;

    public ModeloCuentoDetallado(String id, String titulo, String subtitulo, String imagenUrl, 
                                 int estudiantesLeidos, String actividad1, int completadas1, 
                                 String actividad2, int completadas2) {
        this.id = id;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.imagenUrl = imagenUrl;
        this.estudiantesLeidos = estudiantesLeidos;
        this.actividad1 = actividad1;
        this.completadas1 = completadas1;
        this.actividad2 = actividad2;
        this.completadas2 = completadas2;
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getSubtitulo() { return subtitulo; }
    public String getImagenUrl() { return imagenUrl; }
    public int getEstudiantesLeidos() { return estudiantesLeidos; }
    public String getActividad1() { return actividad1; }
    public int getCompletadas1() { return completadas1; }
    public String getActividad2() { return actividad2; }
    public int getCompletadas2() { return completadas2; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
    public void setEstudiantesLeidos(int estudiantesLeidos) { this.estudiantesLeidos = estudiantesLeidos; }
    public void setActividad1(String actividad1) { this.actividad1 = actividad1; }
    public void setCompletadas1(int completadas1) { this.completadas1 = completadas1; }
    public void setActividad2(String actividad2) { this.actividad2 = actividad2; }
    public void setCompletadas2(int completadas2) { this.completadas2 = completadas2; }
}
