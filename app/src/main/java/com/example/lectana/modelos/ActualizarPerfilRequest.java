package com.example.lectana.modelos;

public class ActualizarPerfilRequest {
    private String nombre;
    private String apellido;
    private String email;
    private Integer edad;
    private String nacionalidad;
    private Integer aula_id_aula;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    public Integer getAula_id_aula() {
        return aula_id_aula;
    }

    public void setAula_id_aula(Integer aula_id_aula) {
        this.aula_id_aula = aula_id_aula;
    }
}
