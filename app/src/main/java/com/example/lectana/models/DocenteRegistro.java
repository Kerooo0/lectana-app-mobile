package com.example.lectana.models;

/**
 * Modelo para el registro de un docente
 * Coincide con la estructura de la base de datos
 */
public class DocenteRegistro {
    // Datos personales (tabla usuario)
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private int edad;
    
    // Datos específicos del docente (tabla docente)
    private String dni;
    private String telefono;
    private String nivelEducativo; // PRIMARIA, SECUNDARIA, AMBOS
    
    // Datos institucionales
    private String institucionNombre;
    private String institucionPais;
    private String institucionProvincia;
    
    // Constructor vacío
    public DocenteRegistro() {
    }
    
    // Constructor completo
    public DocenteRegistro(String nombre, String apellido, String email, String password, 
                          int edad, String dni, String telefono, String nivelEducativo,
                          String institucionNombre, String institucionPais, String institucionProvincia) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.edad = edad;
        this.dni = dni;
        this.telefono = telefono;
        this.nivelEducativo = nivelEducativo;
        this.institucionNombre = institucionNombre;
        this.institucionPais = institucionPais;
        this.institucionProvincia = institucionProvincia;
    }
    
    // Getters y Setters
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
    
    public void setEdad(int edad) {
        this.edad = edad;
    }
    
    public String getDni() {
        return dni;
    }
    
    public void setDni(String dni) {
        this.dni = dni;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getNivelEducativo() {
        return nivelEducativo;
    }
    
    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }
    
    public String getInstitucionNombre() {
        return institucionNombre;
    }
    
    public void setInstitucionNombre(String institucionNombre) {
        this.institucionNombre = institucionNombre;
    }
    
    public String getInstitucionPais() {
        return institucionPais;
    }
    
    public void setInstitucionPais(String institucionPais) {
        this.institucionPais = institucionPais;
    }
    
    public String getInstitucionProvincia() {
        return institucionProvincia;
    }
    
    public void setInstitucionProvincia(String institucionProvincia) {
        this.institucionProvincia = institucionProvincia;
    }
}
