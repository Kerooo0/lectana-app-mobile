package com.example.lectana.network;

import android.util.Log;

import com.example.lectana.models.AlumnoRegistro;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Cliente para el registro de alumnos
 * Se conecta directamente con Supabase REST API
 * Mismo patrón que RegistroDocenteClient
 */
public class RegistroAlumnoClient {
    
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    
    private static final String SUPABASE_URL = "https://kutpsehgzxmnyrujmnxo.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt1dHBzZWhnenhtbnlydWptbnhvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTcyOTg4OTYsImV4cCI6MjA3Mjg3NDg5Nn0.yeO2t5-wEIM4Lbg3jh3RPgshbknrJrZ-JUr2EKyIF-4";
    private static final String TAG = "RegistroAlumnoClient";
    
    public RegistroAlumnoClient(String baseUrl) {
        // baseUrl ya no se usa, pero mantenemos el constructor para compatibilidad
    }
    
    public interface RegistroCallback {
        void onSuccess(String message, JSONObject user);
        void onError(String message);
    }
    
    /**
     * Registra un nuevo alumno directamente en Supabase
     * Paso 1: Crear usuario
     * Paso 2: Crear alumno vinculado al usuario
     */
    public void registrarAlumno(AlumnoRegistro alumno, RegistroCallback callback) {
        // Paso 1: Crear usuario en la tabla 'usuario'
        crearUsuario(alumno, new RegistroCallback() {
            @Override
            public void onSuccess(String message, JSONObject usuario) {
                try {
                    int idUsuario = usuario.getInt("id_usuario");
                    // Paso 2: Crear alumno vinculado al usuario
                    crearAlumno(alumno, idUsuario, callback);
                } catch (JSONException e) {
                    callback.onError("Error obteniendo ID de usuario: " + e.getMessage());
                }
            }

            @Override
            public void onError(String message) {
                callback.onError(message);
            }
        });
    }
    
    /**
     * PASO 1: Crea el usuario en la tabla 'usuario'
     * Idéntico al de RegistroDocenteClient
     */
    private void crearUsuario(AlumnoRegistro alumno, RegistroCallback callback) {
        try {
            // Hashear la contraseña con BCrypt antes de enviarla
            String hashedPassword = BCrypt.hashpw(alumno.getPassword(), BCrypt.gensalt(12));
            
            JSONObject usuarioJson = new JSONObject();
            usuarioJson.put("nombre", alumno.getNombre());
            usuarioJson.put("apellido", alumno.getApellido());
            usuarioJson.put("email", alumno.getEmail());
            usuarioJson.put("password", hashedPassword); // Contraseña hasheada
            usuarioJson.put("edad", alumno.getEdad());
            usuarioJson.put("activo", true);
            
            Log.d(TAG, "Contraseña hasheada exitosamente");

            RequestBody body = RequestBody.create(
                usuarioJson.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            String url = SUPABASE_URL + "/rest/v1/usuario";

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=representation")
                    .build();

            Log.d(TAG, "Creando usuario en Supabase: " + url);

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error creando usuario: " + e.getMessage());
                    callback.onError("Error de conexión al crear usuario: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    Log.d(TAG, "Respuesta crear usuario: " + response.code() + " - " + responseBody);

                    if (response.isSuccessful()) {
                        try {
                            // Supabase devuelve un array, tomamos el primer elemento
                            if (responseBody.startsWith("[")) {
                                org.json.JSONArray array = new org.json.JSONArray(responseBody);
                                if (array.length() > 0) {
                                    JSONObject usuario = array.getJSONObject(0);
                                    callback.onSuccess("Usuario creado", usuario);
                                } else {
                                    callback.onError("No se recibió el usuario creado");
                                }
                            } else {
                                JSONObject usuario = new JSONObject(responseBody);
                                callback.onSuccess("Usuario creado", usuario);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parseando usuario: " + e.getMessage());
                            callback.onError("Error procesando respuesta: " + e.getMessage());
                        }
                    } else {
                        try {
                            JSONObject error = new JSONObject(responseBody);
                            String errorMsg = error.optString("message", "Error desconocido");
                            callback.onError("Error al crear usuario: " + errorMsg);
                        } catch (JSONException e) {
                            callback.onError("Error al crear usuario. Código: " + response.code());
                        }
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creando JSON de usuario: " + e.getMessage());
            callback.onError("Error preparando datos de usuario");
        }
    }
    
    /**
     * PASO 2: Crea el alumno en la tabla 'alumno'
     * Campos REALES según esquema: usuario_id_usuario, nacionalidad, alumno_col, aula_id_aula
     */
    private void crearAlumno(AlumnoRegistro alumno, int idUsuario, RegistroCallback callback) {
        try {
            JSONObject alumnoJson = new JSONObject();
            alumnoJson.put("usuario_id_usuario", idUsuario);
            
            // Campos opcionales - usar valores por defecto si no existen
            if (alumno.getPais() != null && !alumno.getPais().isEmpty()) {
                alumnoJson.put("nacionalidad", alumno.getPais()); // Usando grado temporalmente
            }
            // alumno_col y aula_id_aula son opcionales (nullable), no los enviamos por ahora

            RequestBody body = RequestBody.create(
                alumnoJson.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            String url = SUPABASE_URL + "/rest/v1/alumno";

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=representation")
                    .build();

            Log.d(TAG, "Creando alumno en Supabase: " + url);

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error creando alumno: " + e.getMessage());
                    callback.onError("Error de conexión al crear alumno: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    Log.d(TAG, "Respuesta crear alumno: " + response.code() + " - " + responseBody);

                    if (response.isSuccessful()) {
                        try {
                            JSONObject result = new JSONObject();
                            result.put("id_usuario", idUsuario);
                            result.put("mensaje", "Alumno registrado exitosamente");
                            callback.onSuccess("¡Registro exitoso! Ya puedes iniciar sesión.", result);
                        } catch (JSONException e) {
                            callback.onError("Error interno");
                        }
                    } else {
                        try {
                            JSONObject error = new JSONObject(responseBody);
                            String errorMsg = error.optString("message", "Error desconocido");
                            callback.onError("Error al crear alumno: " + errorMsg);
                        } catch (JSONException e) {
                            callback.onError("Error al crear alumno. Código: " + response.code());
                        }
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creando JSON de alumno: " + e.getMessage());
            callback.onError("Error preparando datos de alumno");
        }
    }
}
