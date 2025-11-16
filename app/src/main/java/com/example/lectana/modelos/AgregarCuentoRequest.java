package com.example.lectana.modelos;

public class AgregarCuentoRequest {
    private int id_cuento;

    public AgregarCuentoRequest(int id_cuento) {
        this.id_cuento = id_cuento;
    }

    public int getId_cuento() {
        return id_cuento;
    }

    public void setId_cuento(int id_cuento) {
        this.id_cuento = id_cuento;
    }
}


