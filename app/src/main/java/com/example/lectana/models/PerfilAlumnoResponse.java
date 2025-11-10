package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;

public class PerfilAlumnoResponse {
    @SerializedName("ok")
    private boolean ok;
    
    @SerializedName("data")
    private DataPerfil data;
    
    public boolean isOk() {
        return ok;
    }
    
    public void setOk(boolean ok) {
        this.ok = ok;
    }
    
    public DataPerfil getData() {
        return data;
    }
    
    public void setData(DataPerfil data) {
        this.data = data;
    }
    
    public static class DataPerfil {
        @SerializedName("usuario")
        private Usuario usuario;
        
        @SerializedName("puntos")
        private int puntos;
        
        @SerializedName("nacionalidad")
        private String nacionalidad;
        
        @SerializedName("alumno_col")
        private String alumnoCol;
        
        @SerializedName("aula_id_aula")
        private Integer aulaId;
        
        public Usuario getUsuario() {
            return usuario;
        }
        
        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
        
        public int getPuntos() {
            return puntos;
        }
        
        public void setPuntos(int puntos) {
            this.puntos = puntos;
        }
        
        public String getNacionalidad() {
            return nacionalidad;
        }
        
        public void setNacionalidad(String nacionalidad) {
            this.nacionalidad = nacionalidad;
        }
        
        public String getAlumnoCol() {
            return alumnoCol;
        }
        
        public void setAlumnoCol(String alumnoCol) {
            this.alumnoCol = alumnoCol;
        }
        
        public Integer getAulaId() {
            return aulaId;
        }
        
        public void setAulaId(Integer aulaId) {
            this.aulaId = aulaId;
        }
    }
    
    public static class Usuario {
        @SerializedName("nombre")
        private String nombre;
        
        @SerializedName("apellido")
        private String apellido;
        
        @SerializedName("email")
        private String email;
        
        @SerializedName("edad")
        private int edad;
        
        @SerializedName("rol")
        private String rol;
        
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
        
        public int getEdad() {
            return edad;
        }
        
        public void setEdad(int edad) {
            this.edad = edad;
        }
        
        public String getRol() {
            return rol;
        }
        
        public void setRol(String rol) {
            this.rol = rol;
        }
        
        public String getNombreCompleto() {
            return nombre + (apellido != null && !apellido.isEmpty() ? " " + apellido : "");
        }
    }
}
