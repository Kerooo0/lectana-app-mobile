package com.example.lectana.modelos;

public class ModeloActividadDetallada {
    private String id;
    private String tituloHistoria;
    private String nombreActividad1;
    private String tipoActividad1;
    private int estudiantesActividad1;
    private String estadoActividad1;
    private String nombreActividad2;
    private String tipoActividad2;
    private int estudiantesActividad2;
    private String estadoActividad2;

    public ModeloActividadDetallada(String id, String tituloHistoria, 
                                   String nombreActividad1, String tipoActividad1, int estudiantesActividad1, String estadoActividad1,
                                   String nombreActividad2, String tipoActividad2, int estudiantesActividad2, String estadoActividad2) {
        this.id = id;
        this.tituloHistoria = tituloHistoria;
        this.nombreActividad1 = nombreActividad1;
        this.tipoActividad1 = tipoActividad1;
        this.estudiantesActividad1 = estudiantesActividad1;
        this.estadoActividad1 = estadoActividad1;
        this.nombreActividad2 = nombreActividad2;
        this.tipoActividad2 = tipoActividad2;
        this.estudiantesActividad2 = estudiantesActividad2;
        this.estadoActividad2 = estadoActividad2;
    }

    // Getters
    public String getId() { return id; }
    public String getTituloHistoria() { return tituloHistoria; }
    public String getNombreActividad1() { return nombreActividad1; }
    public String getTipoActividad1() { return tipoActividad1; }
    public int getEstudiantesActividad1() { return estudiantesActividad1; }
    public String getEstadoActividad1() { return estadoActividad1; }
    public String getNombreActividad2() { return nombreActividad2; }
    public String getTipoActividad2() { return tipoActividad2; }
    public int getEstudiantesActividad2() { return estudiantesActividad2; }
    public String getEstadoActividad2() { return estadoActividad2; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTituloHistoria(String tituloHistoria) { this.tituloHistoria = tituloHistoria; }
    public void setNombreActividad1(String nombreActividad1) { this.nombreActividad1 = nombreActividad1; }
    public void setTipoActividad1(String tipoActividad1) { this.tipoActividad1 = tipoActividad1; }
    public void setEstudiantesActividad1(int estudiantesActividad1) { this.estudiantesActividad1 = estudiantesActividad1; }
    public void setEstadoActividad1(String estadoActividad1) { this.estadoActividad1 = estadoActividad1; }
    public void setNombreActividad2(String nombreActividad2) { this.nombreActividad2 = nombreActividad2; }
    public void setTipoActividad2(String tipoActividad2) { this.tipoActividad2 = tipoActividad2; }
    public void setEstudiantesActividad2(int estudiantesActividad2) { this.estudiantesActividad2 = estudiantesActividad2; }
    public void setEstadoActividad2(String estadoActividad2) { this.estadoActividad2 = estadoActividad2; }
}
