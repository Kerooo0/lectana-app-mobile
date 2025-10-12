package com.example.lectana.network;

import android.util.Log;

import com.example.lectana.models.DocenteRegistro;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Cliente para el registro de docentes
 * Se conecta directamente con Supabase REST API
 */
public class RegistroDocenteClient {
    
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    
    private static final String SUPABASE_URL = "https://kutpsehgzxmnyrujmnxo.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Imt1dHBzZWhnenhtbnlydWptbnhvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTcyOTg4OTYsImV4cCI6MjA3Mjg3NDg5Nn0.yeO2t5-wEIM4Lbg3jh3RPgshbknrJrZ-JUr2EKyIF-4";
    private static final String TAG = "RegistroDocenteClient";
    
    public RegistroDocenteClient(String baseUrl) {
        // baseUrl ya no se usa, pero mantenemos el constructor para compatibilidad
    }
    
    public interface RegistroCallback {
        void onSuccess(String message, JSONObject user);
        void onError(String message);
    }
    
    /**
     * Registra un nuevo docente directamente en Supabase
     * Paso 1: Crear usuario
     * Paso 2: Crear docente vinculado al usuario
     */
    public void registrarDocente(DocenteRegistro docente, RegistroCallback callback) {
        // Paso 1: Crear usuario en la tabla 'usuario'
        crearUsuario(docente, new RegistroCallback() {
            @Override
            public void onSuccess(String message, JSONObject usuario) {
                try {
                    int idUsuario = usuario.getInt("id_usuario");
                    // Paso 2: Crear docente vinculado al usuario
                    crearDocente(docente, idUsuario, callback);
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
    
    private void crearUsuario(DocenteRegistro docente, RegistroCallback callback) {
        try {
            JSONObject usuarioJson = new JSONObject();
            usuarioJson.put("nombre", docente.getNombre());
            usuarioJson.put("apellido", docente.getApellido());
            usuarioJson.put("email", docente.getEmail());
            usuarioJson.put("password", docente.getPassword()); // En producción debería hashearse
            usuarioJson.put("edad", docente.getEdad());
            usuarioJson.put("activo", true);

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
    
    private void crearDocente(DocenteRegistro docente, int idUsuario, RegistroCallback callback) {
        try {
            JSONObject docenteJson = new JSONObject();
            docenteJson.put("dni", docente.getDni());
            docenteJson.put("telefono", docente.getTelefono() != null ? docente.getTelefono() : JSONObject.NULL);
            docenteJson.put("nivel_educativo", docente.getNivelEducativo());
            docenteJson.put("verificado", false);
            docenteJson.put("usuario_id_usuario", idUsuario);
            docenteJson.put("institucion_nombre", docente.getInstitucionNombre());
            docenteJson.put("institucion_pais", docente.getInstitucionPais());
            docenteJson.put("institucion_provincia", docente.getInstitucionProvincia() != null ? docente.getInstitucionProvincia() : JSONObject.NULL);

            RequestBody body = RequestBody.create(
                docenteJson.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            String url = SUPABASE_URL + "/rest/v1/docente";

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("apikey", SUPABASE_KEY)
                    .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Prefer", "return=representation")
                    .build();

            Log.d(TAG, "Creando docente en Supabase: " + url);

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error creando docente: " + e.getMessage());
                    callback.onError("Error de conexión al crear docente: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    Log.d(TAG, "Respuesta crear docente: " + response.code() + " - " + responseBody);

                    if (response.isSuccessful()) {
                        try {
                            JSONObject result = new JSONObject();
                            result.put("id_usuario", idUsuario);
                            result.put("mensaje", "Docente registrado exitosamente");
                            callback.onSuccess("¡Registro exitoso! Tu cuenta será verificada por un administrador.", result);
                        } catch (JSONException e) {
                            callback.onError("Error interno");
                        }
                    } else {
                        try {
                            JSONObject error = new JSONObject(responseBody);
                            String errorMsg = error.optString("message", "Error desconocido");
                            callback.onError("Error al crear docente: " + errorMsg);
                        } catch (JSONException e) {
                            callback.onError("Error al crear docente. Código: " + response.code());
                        }
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creando JSON de docente: " + e.getMessage());
            callback.onError("Error preparando datos de docente");
        }
    }
}
