package com.example.lectana.model;

import java.io.Serializable;

public class DatosRegistroAlumno implements Serializable {


    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private int edad;


    private String nacionalidad;
    private Integer aulaId;


    public DatosRegistroAlumno() {}


    public DatosRegistroAlumno(String nombre, String apellido, String email,
                               String password, int edad, String nacionalidad,
                               Integer aulaId) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.edad = edad;
        this.nacionalidad = nacionalidad;
        this.aulaId = aulaId;
    }


    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }

    public Integer getAulaId() { return aulaId; }
    public void setAulaId(Integer aulaId) { this.aulaId = aulaId; }


    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
                apellido != null && !apellido.trim().isEmpty() &&
                email != null && !email.trim().isEmpty() &&
                password != null && !password.trim().isEmpty() &&
                edad > 0 &&
                nacionalidad != null && !nacionalidad.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "DatosRegistroAlumno{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", email='" + email + '\'' +
                ", edad=" + edad +
                ", nacionalidad='" + nacionalidad + '\'' +
                ", aulaId=" + aulaId +
                '}';
    }


}
