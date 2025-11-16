package com.example.lectana.registro.docente;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.lectana.models.DocenteRegistro;

/**
 * Gestor para almacenar temporalmente los datos del registro
 * mientras el usuario navega entre los diferentes pasos
 */
public class RegistroDocenteManager {
    
    private static RegistroDocenteManager instance;
    private DocenteRegistro docenteRegistro;
    private SharedPreferences prefs;
    private static final String PREFS_NAME = "registro_docente_temp";
    
    private RegistroDocenteManager(Context context) {
        docenteRegistro = new DocenteRegistro();
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }
    
    public static synchronized RegistroDocenteManager getInstance(Context context) {
        if (instance == null) {
            instance = new RegistroDocenteManager(context);
        }
        return instance;
    }
    
    public DocenteRegistro getDocenteRegistro() {
        return docenteRegistro;
    }
    
    // Métodos para guardar datos del Paso 1: Datos Personales
    public void guardarDatosPersonales(String nombre, String apellido, String dni, 
                                       String email, String telefono) {
        docenteRegistro.setNombre(nombre);
        docenteRegistro.setApellido(apellido);
        docenteRegistro.setDni(dni);
        docenteRegistro.setEmail(email);
        docenteRegistro.setTelefono(telefono);
        
        // Guardar también en SharedPreferences por si la app se cierra
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("dni", dni);
        editor.putString("email", email);
        editor.putString("telefono", telefono);
        editor.apply();
    }
    
    // Métodos para guardar datos del Paso 2: Datos Institucionales
    public void guardarDatosInstitucionales(String nombreInstitucion, String pais, 
                                           String provincia, String nivelEducativo) {
        docenteRegistro.setInstitucionNombre(nombreInstitucion);
        docenteRegistro.setInstitucionPais(pais);
        docenteRegistro.setInstitucionProvincia(provincia);
        docenteRegistro.setNivelEducativo(nivelEducativo);
        
        // Guardar en SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("institucionNombre", nombreInstitucion);
        editor.putString("institucionPais", pais);
        editor.putString("institucionProvincia", provincia);
        editor.putString("nivelEducativo", nivelEducativo);
        editor.apply();
    }
    
    // Métodos para guardar datos del Paso 3: Datos de Acceso
    public void guardarDatosAcceso(String password, int edad) {
        docenteRegistro.setPassword(password);
        docenteRegistro.setEdad(edad);
        
        // NO guardar la contraseña en SharedPreferences por seguridad
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("edad", edad);
        editor.apply();
    }
    
    // Método para limpiar todos los datos después del registro exitoso
    public void limpiarDatos() {
        docenteRegistro = new DocenteRegistro();
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
            docenteRegistro.setNombre(prefs.getString("nombre", ""));
            docenteRegistro.setApellido(prefs.getString("apellido", ""));
            docenteRegistro.setDni(prefs.getString("dni", ""));
            docenteRegistro.setEmail(prefs.getString("email", ""));
            docenteRegistro.setTelefono(prefs.getString("telefono", ""));
            docenteRegistro.setInstitucionNombre(prefs.getString("institucionNombre", ""));
            docenteRegistro.setInstitucionPais(prefs.getString("institucionPais", ""));
            docenteRegistro.setInstitucionProvincia(prefs.getString("institucionProvincia", ""));
            docenteRegistro.setNivelEducativo(prefs.getString("nivelEducativo", ""));
            docenteRegistro.setEdad(prefs.getInt("edad", 0));
        }
    }
    
    // Método para validar que todos los datos requeridos estén completos
    public boolean datosCompletos() {
        return docenteRegistro.getNombre() != null && !docenteRegistro.getNombre().isEmpty()
                && docenteRegistro.getApellido() != null && !docenteRegistro.getApellido().isEmpty()
                && docenteRegistro.getDni() != null && !docenteRegistro.getDni().isEmpty()
                && docenteRegistro.getEmail() != null && !docenteRegistro.getEmail().isEmpty()
                && docenteRegistro.getPassword() != null && !docenteRegistro.getPassword().isEmpty()
                && docenteRegistro.getNivelEducativo() != null && !docenteRegistro.getNivelEducativo().isEmpty()
                && docenteRegistro.getInstitucionNombre() != null && !docenteRegistro.getInstitucionNombre().isEmpty()
                && docenteRegistro.getInstitucionPais() != null && !docenteRegistro.getInstitucionPais().isEmpty()
                && docenteRegistro.getEdad() >= 18 && docenteRegistro.getEdad() <= 120;
    }
}
