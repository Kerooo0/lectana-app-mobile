package com.example.lectana.model;

import java.io.Serializable;

/**
 * Clase para almacenar los datos del formulario de registro de docente
 * Implementa Serializable para poder pasarla entre fragmentos
 */
public class DatosRegistroDocente implements Serializable {
    
    // Datos personales
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String telefono;
    private int edad;
    
    // Datos institucionales
    private String institucionNombre;
    private String institucionPais;
    private String institucionProvincia;
    private String nivelEducativo;
    
    // Datos de acceso
    private String password;
    private String confirmarPassword;
    
    // Constructor vacío
    public DatosRegistroDocente() {}
    
    // Constructor completo
    public DatosRegistroDocente(String nombre, String apellido, String dni, String email, 
                               String telefono, int edad, String institucionNombre, String institucionPais, 
                               String institucionProvincia, String nivelEducativo, 
                               String password, String confirmarPassword) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.edad = edad;
        this.institucionNombre = institucionNombre;
        this.institucionPais = institucionPais;
        this.institucionProvincia = institucionProvincia;
        this.nivelEducativo = nivelEducativo;
        this.password = password;
        this.confirmarPassword = confirmarPassword;
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    
    public String getInstitucionNombre() { return institucionNombre; }
    public void setInstitucionNombre(String institucionNombre) { this.institucionNombre = institucionNombre; }
    
    public String getInstitucionPais() { return institucionPais; }
    public void setInstitucionPais(String institucionPais) { this.institucionPais = institucionPais; }
    
    public String getInstitucionProvincia() { return institucionProvincia; }
    public void setInstitucionProvincia(String institucionProvincia) { this.institucionProvincia = institucionProvincia; }
    
    public String getNivelEducativo() { return nivelEducativo; }
    public void setNivelEducativo(String nivelEducativo) { this.nivelEducativo = nivelEducativo; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getConfirmarPassword() { return confirmarPassword; }
    public void setConfirmarPassword(String confirmarPassword) { this.confirmarPassword = confirmarPassword; }
    
    /**
     * Valida que todos los campos obligatorios estén completos
     */
    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() &&
               apellido != null && !apellido.trim().isEmpty() &&
               dni != null && !dni.trim().isEmpty() &&
               email != null && !email.trim().isEmpty() &&
               edad >= 18 && edad <= 120 &&
               institucionNombre != null && !institucionNombre.trim().isEmpty() &&
               institucionPais != null && !institucionPais.trim().isEmpty() &&
               institucionProvincia != null && !institucionProvincia.trim().isEmpty() &&
               nivelEducativo != null && !nivelEducativo.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               confirmarPassword != null && !confirmarPassword.trim().isEmpty();
    }
    
    /**
     * Valida que las contraseñas coincidan
     */
    public boolean passwordsCoinciden() {
        return password != null && confirmarPassword != null && 
               password.equals(confirmarPassword);
    }
    
    @Override
    public String toString() {
        return "DatosRegistroDocente{" +
                "nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", dni='" + dni + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", edad=" + edad +
                ", institucionNombre='" + institucionNombre + '\'' +
                ", institucionPais='" + institucionPais + '\'' +
                ", institucionProvincia='" + institucionProvincia + '\'' +
                ", nivelEducativo='" + nivelEducativo + '\'' +
                ", password='" + (password != null ? "[PROTEGIDA]" : "null") + '\'' +
                ", confirmarPassword='" + (confirmarPassword != null ? "[PROTEGIDA]" : "null") + '\'' +
                '}';
    }
}
