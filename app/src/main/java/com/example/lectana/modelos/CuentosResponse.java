package com.example.lectana.modelos;

import java.util.List;

public class CuentosResponse {
    private List<CuentoApi> cuentos;
    private Paginacion paginacion;

    public List<CuentoApi> getCuentos() {
        return cuentos;
    }

    public void setCuentos(List<CuentoApi> cuentos) {
        this.cuentos = cuentos;
    }

    public Paginacion getPaginacion() {
        return paginacion;
    }

    public void setPaginacion(Paginacion paginacion) {
        this.paginacion = paginacion;
    }
}
