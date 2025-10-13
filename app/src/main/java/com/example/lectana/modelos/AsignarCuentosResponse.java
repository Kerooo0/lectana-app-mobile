package com.example.lectana.modelos;

import java.util.List;

public class AsignarCuentosResponse {
    private int aula_id;
    private int cuentos_asignados;
    private List<Integer> cuentos_ids;
    private int docente_id;
    private String message;

    public AsignarCuentosResponse() {
    }

    public int getAula_id() {
        return aula_id;
    }

    public void setAula_id(int aula_id) {
        this.aula_id = aula_id;
    }

    public int getCuentos_asignados() {
        return cuentos_asignados;
    }

    public void setCuentos_asignados(int cuentos_asignados) {
        this.cuentos_asignados = cuentos_asignados;
    }

    public List<Integer> getCuentos_ids() {
        return cuentos_ids;
    }

    public void setCuentos_ids(List<Integer> cuentos_ids) {
        this.cuentos_ids = cuentos_ids;
    }

    public int getDocente_id() {
        return docente_id;
    }

    public void setDocente_id(int docente_id) {
        this.docente_id = docente_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
