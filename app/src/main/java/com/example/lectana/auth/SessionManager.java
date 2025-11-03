package com.example.lectana.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {
    
    private static final String TAG = "SessionManager";
    private static final String PREF_NAME = "LectanaSession";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER = "user";
    private static final String KEY_DOCENTE = "docente";
    private static final String KEY_ROLE = "role";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Context context;
    
    public SessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }
    
    /**
     * Guardar sesión de usuario (igual que localStorage en el frontend web)
     */
    public void saveSession(String token, String role, JSONObject user) {
        try {
            editor.putString(KEY_TOKEN, token);
            editor.putString(KEY_ROLE, role);
            editor.putString(KEY_USER, user.toString());
            
            // Si es alumno, extraer y guardar alumno_id y aula_id
            if ("alumno".equals(role) && user != null) {
                if (user.has("id_alumno")) {
                    int alumnoId = user.getInt("id_alumno");
                    editor.putInt(KEY_ALUMNO_ID, alumnoId);
                    Log.d(TAG, "ID de alumno guardado: " + alumnoId);
                }
                if (user.has("id_aula")) {
                    int aulaId = user.getInt("id_aula");
                    editor.putInt(KEY_AULA_ID, aulaId);
                    Log.d(TAG, "ID de aula guardado: " + aulaId);
                }
            }
            
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.apply();
            
            Log.d(TAG, "Sesión guardada - Role: " + role);
        } catch (Exception e) {
            Log.e(TAG, "Error guardando sesión: " + e.getMessage());
        }
    }

    /**
     * Guardar sesión completa con datos de docente
     */
    public void saveSession(String token, String role, JSONObject user, JSONObject docente) {
        try {
            editor.putString(KEY_TOKEN, token);
            editor.putString(KEY_ROLE, role);
            editor.putString(KEY_USER, user.toString());
            
            // Si es alumno, extraer y guardar alumno_id y aula_id
            if ("alumno".equals(role) && user != null) {
                if (user.has("id_alumno")) {
                    int alumnoId = user.getInt("id_alumno");
                    editor.putInt(KEY_ALUMNO_ID, alumnoId);
                    Log.d(TAG, "ID de alumno guardado: " + alumnoId);
                }
                if (user.has("id_aula")) {
                    int aulaId = user.getInt("id_aula");
                    editor.putInt(KEY_AULA_ID, aulaId);
                    Log.d(TAG, "ID de aula guardado: " + aulaId);
                }
            }
            
            if (docente != null) {
                editor.putString(KEY_DOCENTE, docente.toString());
                Log.d(TAG, "Datos de docente guardados: " + docente.toString());
            } else {
                Log.w(TAG, "Docente es null, no se guardan datos específicos");
            }
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.apply();
            
            Log.d(TAG, "Sesión completa guardada - Role: " + role);
        } catch (Exception e) {
            Log.e(TAG, "Error guardando sesión completa: " + e.getMessage());
        }
    }
    
    /**
     * Verificar si hay una sesión activa
     */
    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false) && 
               prefs.getString(KEY_TOKEN, null) != null;
    }
    
    /**
     * Obtener token guardado
     */
    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }
    
    /**
     * Obtener rol del usuario
     */
    public String getRole() {
        return prefs.getString(KEY_ROLE, null);
    }
    
    /**
     * Obtener datos del usuario
     */
    public JSONObject getUser() {
        try {
            String userStr = prefs.getString(KEY_USER, null);
            return userStr != null ? new JSONObject(userStr) : null;
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtener datos del docente
     */
    public JSONObject getDocente() {
        try {
            String docenteStr = prefs.getString(KEY_DOCENTE, null);
            if (docenteStr != null) {
                JSONObject docente = new JSONObject(docenteStr);
                Log.d(TAG, "Datos de docente recuperados: " + docente.toString());
                return docente;
            } else {
                Log.w(TAG, "No hay datos de docente guardados");
                return null;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parseando docente: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Limpiar sesión (logout)
     */
    public void clearSession() {
        editor.clear();
        editor.apply();
        Log.d(TAG, "Sesión limpiada");
    }
    
    /**
     * Verificar si el usuario es administrador
     */
    public boolean isAdmin() {
        return "administrador".equals(getRole());
    }
    
    /**
     * Verificar si el usuario es docente
     */
    public boolean isDocente() {
        return "docente".equals(getRole());
    }
    
    /**
     * Verificar si el usuario es estudiante
     */
    public boolean isEstudiante() {
        return "alumno".equals(getRole());
    }
    
    /**
     * Verificar si la sesión es válida para docentes
     */
    public boolean isDocenteSessionValid() {
        return isLoggedIn() && isDocente() && getToken() != null;
    }
    
    /**
     * Limpiar sesión y mostrar mensaje de error
     */
    public void clearSessionWithMessage(String message) {
        Log.w(TAG, "Limpiando sesión: " + message);
        clearSession();
    }
    
    // Métodos para alumno y aula
    private static final String KEY_ALUMNO_ID = "alumno_id";
    private static final String KEY_AULA_ID = "aula_id";
    
    /**
     * Guardar ID del alumno
     */
    public void saveAlumnoId(int alumnoId) {
        editor.putInt(KEY_ALUMNO_ID, alumnoId);
        editor.apply();
        Log.d(TAG, "Alumno ID guardado: " + alumnoId);
    }
    
    /**
     * Obtener ID del alumno
     */
    public int getAlumnoId() {
        return prefs.getInt(KEY_ALUMNO_ID, 0);
    }
    
    /**
     * Guardar ID del aula
     */
    public void saveAulaId(int aulaId) {
        editor.putInt(KEY_AULA_ID, aulaId);
        editor.apply();
        Log.d(TAG, "Aula ID guardada: " + aulaId);
    }
    
    /**
     * Obtener ID del aula
     */
    public int getAulaId() {
        return prefs.getInt(KEY_AULA_ID, 0);
    }
    
    /**
     * Guardar datos completos del alumno
     */
    public void saveAlumnoData(int alumnoId, int aulaId) {
        editor.putInt(KEY_ALUMNO_ID, alumnoId);
        editor.putInt(KEY_AULA_ID, aulaId);
        editor.apply();
        Log.d(TAG, "Datos de alumno guardados - Alumno ID: " + alumnoId + ", Aula ID: " + aulaId);
    }
}
