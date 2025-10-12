package com.example.lectana.registro.alumno;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lectana.models.AlumnoRegistro;

/**
 * Gestor para almacenar temporalmente los datos del registro de alumno
 * mientras el usuario navega entre los diferentes pasos
 */
public class RegistroAlumnoManager {
    
    private static RegistroAlumnoManager instance;
    private AlumnoRegistro alumnoRegistro;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "registro_alumno_temp";
    
    private RegistroAlumnoManager(Context context) {
        alumnoRegistro = new AlumnoRegistro();
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized RegistroAlumnoManager getInstance(Context context) {
        if (instance == null) {
            instance = new RegistroAlumnoManager(context);
        }
        return instance;
    }
    
    public AlumnoRegistro getAlumnoRegistro() {
        return alumnoRegistro;
    }
    
    // Métodos para guardar datos básicos: nombre, apellido, edad, país
    public void guardarDatosBasicos(String nombre, String apellido, int edad, String pais, 
                                   String fechaNacimiento, String grado) {
        alumnoRegistro.setNombre(nombre);
        alumnoRegistro.setApellido(apellido);
        alumnoRegistro.setEdad(edad);
        alumnoRegistro.setFechaNacimiento(fechaNacimiento);
        alumnoRegistro.setGrado(grado);
        
        // Guardar también en SharedPreferences por si la app se cierra
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putInt("edad", edad);
        editor.putString("pais", pais);
        editor.putString("fechaNacimiento", fechaNacimiento);
        editor.putString("grado", grado);
        editor.apply();
    }
    
    // Métodos para guardar datos de acceso: email y password
    public void guardarDatosAcceso(String email, String password) {
        alumnoRegistro.setEmail(email);
        alumnoRegistro.setPassword(password);
        
        // NO guardar la contraseña en SharedPreferences por seguridad
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.apply();
    }
    
    // Método para limpiar todos los datos después del registro exitoso
    public void limpiarDatos() {
        alumnoRegistro = new AlumnoRegistro();
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
    
    // Método para verificar si hay datos guardados
    public boolean hayDatosGuardados() {
        return prefs.contains("nombre");
    }
    
    // Método para restaurar datos desde SharedPreferences
    public void restaurarDatos() {
        if (hayDatosGuardados()) {
            alumnoRegistro.setNombre(prefs.getString("nombre", ""));
            alumnoRegistro.setApellido(prefs.getString("apellido", ""));
            alumnoRegistro.setEdad(prefs.getInt("edad", 0));
            alumnoRegistro.setEmail(prefs.getString("email", ""));
            alumnoRegistro.setFechaNacimiento(prefs.getString("fechaNacimiento", ""));
            alumnoRegistro.setGrado(prefs.getString("grado", ""));
        }
    }
    
    // Método para validar que todos los datos requeridos estén completos
    public boolean datosCompletos() {
        return alumnoRegistro.getNombre() != null && !alumnoRegistro.getNombre().isEmpty()
                && alumnoRegistro.getApellido() != null && !alumnoRegistro.getApellido().isEmpty()
                && alumnoRegistro.getEmail() != null && !alumnoRegistro.getEmail().isEmpty()
                && alumnoRegistro.getPassword() != null && !alumnoRegistro.getPassword().isEmpty()
                && alumnoRegistro.getFechaNacimiento() != null && !alumnoRegistro.getFechaNacimiento().isEmpty()
                && alumnoRegistro.getGrado() != null && !alumnoRegistro.getGrado().isEmpty()
                && alumnoRegistro.getEdad() >= 3 && alumnoRegistro.getEdad() <= 25;
    }
}
