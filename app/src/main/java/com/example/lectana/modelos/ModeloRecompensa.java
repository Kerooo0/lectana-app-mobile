package com.example.lectana.modelos;

public class ModeloRecompensa {
    private String nombre;
    private String categoria;
    private int costo;
    private int imagen;
    private boolean comprado;
    private String accion;

    public ModeloRecompensa(String nombre, String categoria, int costo, int imagen, boolean comprado, String accion) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.costo = costo;
        this.imagen = imagen;
        this.comprado = comprado;
        this.accion = accion;
    }

    // Getters
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public int getCosto() { return costo; }
    public int getImagen() { return imagen; }
    public boolean isComprado() { return comprado; }
    public String getAccion() { return accion; }

    // Setters
    public void setComprado(boolean comprado) { this.comprado = comprado; }
    public void setAccion(String accion) { this.accion = accion; }
}
