package com.example.lectana.modelos;

import java.util.List;

public class AsignarCuentosRequest {
    private List<Integer> cuentos_ids;

    public AsignarCuentosRequest() {
    }

    public AsignarCuentosRequest(List<Integer> cuentos_ids) {
        this.cuentos_ids = cuentos_ids;
    }

    public List<Integer> getCuentos_ids() {
        return cuentos_ids;
    }

    public void setCuentos_ids(List<Integer> cuentos_ids) {
        this.cuentos_ids = cuentos_ids;
    }
}
