package com.example.lectana.models;

/**
 * Modelo para el registro de alumnos
 * Basado en el esquema: alumno (id_alumno, fecha_nacimiento, grado, usuario_id_usuario)
 */
public class AlumnoRegistro {
    // Datos de usuario
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private int edad;
    
    // Datos específicos de alumno
    private String fechaNacimiento;  // Formato: "YYYY-MM-DD" o "DD/MM/YYYY"
    private String grado;  // Ej: "1", "2", "3", "4", "5", "6" (primaria) o "1", "2", "3", "4", "5" (secundaria)
    private String nacionalidad;

    public AlumnoRegistro() {
    }

    // Getters y Setters - Usuario
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getEdad() {
        return edad;
    }

    public void setNacionalidad(String nacionalidad){this.nacionalidad = nacionalidad;}

    public String getNacionalidad(){return nacionalidad;}

    public void setEdad(int edad) {
        this.edad = edad;
    }

    // Getters y Setters - Alumno
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }
}
