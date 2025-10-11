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
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.apply();
            
            Log.d(TAG, "Sesión guardada - Role: " + role);
        } catch (Exception e) {
            Log.e(TAG, "Error guardando sesión: " + e.getMessage());
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
}
