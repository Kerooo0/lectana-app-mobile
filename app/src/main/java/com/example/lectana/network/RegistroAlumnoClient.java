package com.example.lectana.network;

import android.util.Log;

import com.example.lectana.models.AlumnoRegistro;

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
 * Cliente para el registro de alumnos
 * Se conecta con el backend de Render
 */
public class RegistroAlumnoClient {
    
    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    
    private final String baseUrl;
    private static final String TAG = "RegistroAlumnoClient";
    
    public RegistroAlumnoClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }
    
    public interface RegistroCallback {
        void onSuccess(String message, JSONObject user);
        void onError(String message);
    }
    
    /**
     * Registra un nuevo alumno usando el backend de Render
     */
    public void registrarAlumno(AlumnoRegistro alumno, RegistroCallback callback) {
        try {
            JSONObject datosRegistro = new JSONObject();
            
            // Datos de usuario según especificación del backend
            datosRegistro.put("nombre", alumno.getNombre());
            datosRegistro.put("apellido", alumno.getApellido());
            datosRegistro.put("email", alumno.getEmail());
            datosRegistro.put("password", alumno.getPassword()); // El backend se encarga del hash
            datosRegistro.put("edad", alumno.getEdad());
            
            // Campo opcional para vincular al aula
            // datosRegistro.put("codigo_acceso", "ABC123"); // Se puede agregar después

            String url = baseUrl.endsWith("/") ? baseUrl + "api/auth/registro-form-alumno" : baseUrl + "/api/auth/registro-form-alumno";

            RequestBody body = RequestBody.create(
                datosRegistro.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Log.d(TAG, "Enviando registro de alumno a: " + url);
            Log.d(TAG, "Datos: " + datosRegistro.toString());

            httpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Error de conexión: " + e.getMessage());
                    callback.onError("Error de conexión: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    Log.d(TAG, "Respuesta del servidor: " + response.code() + " - " + responseBody);

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            boolean ok = jsonResponse.optBoolean("ok", true);
                            
                            if (ok) {
                                String message = jsonResponse.optString("message", "¡Registro exitoso!");
                                JSONObject user = jsonResponse.optJSONObject("user");
                                if (user == null) {
                                    user = new JSONObject();
                                }
                                callback.onSuccess(message, user);
                            } else {
                                String error = jsonResponse.optString("error", "Error desconocido");
                                callback.onError(error);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parseando respuesta: " + e.getMessage());
                            callback.onError("Error procesando respuesta del servidor");
                        }
                    } else {
                        try {
                            JSONObject error = new JSONObject(responseBody);
                            String errorMsg = error.optString("error", "Error desconocido");
                            callback.onError("Error al registrar alumno: " + errorMsg);
                        } catch (JSONException e) {
                            callback.onError("Error al registrar alumno. Código: " + response.code());
                        }
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creando JSON: " + e.getMessage());
            callback.onError("Error preparando datos de registro");
        }
    }
}
