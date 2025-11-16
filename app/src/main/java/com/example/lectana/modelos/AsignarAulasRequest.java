package com.example.lectana.modelos;

import java.util.List;

public class AsignarAulasRequest {
    private List<Integer> aulas_ids;

    public AsignarAulasRequest() {
    }

    public AsignarAulasRequest(List<Integer> aulas_ids) {
        this.aulas_ids = aulas_ids;
    }

    public List<Integer> getAulas_ids() {
        return aulas_ids;
    }

    public void setAulas_ids(List<Integer> aulas_ids) {
        this.aulas_ids = aulas_ids;
    }
}

