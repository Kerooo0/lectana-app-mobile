package com.example.lectana.modelos;

public class AulaAlumnoResponse {
    private int aula_id_aula;
    private AulaInfo aula;

    public int getAula_id_aula() {
        return aula_id_aula;
    }

    public void setAula_id_aula(int aula_id_aula) {
        this.aula_id_aula = aula_id_aula;
    }

    public AulaInfo getAula() {
        return aula;
    }

    public void setAula(AulaInfo aula) {
        this.aula = aula;
    }
}
