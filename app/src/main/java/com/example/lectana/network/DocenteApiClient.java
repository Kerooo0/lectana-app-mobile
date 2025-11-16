package com.example.lectana.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DocenteApiClient {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String baseUrl;
    private static final String TAG = "DocenteApiClient";

    public DocenteApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public interface UpdateProfileCallback {
        void onSuccess(JSONObject response);
        void onError(String message);
    }

    /**
     * Actualizar perfil del docente
     */
    public void actualizarPerfilDocente(String token, JSONObject datosPerfil, UpdateProfileCallback callback) {
        try {
            String url = baseUrl.endsWith("/") ? baseUrl + "api/docentes/actualizar-perfil-docente" : baseUrl + "/api/docentes/actualizar-perfil-docente";

            RequestBody body = RequestBody.create(
                datosPerfil.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("Authorization", "Bearer " + token)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Log.d(TAG, "Enviando actualización de perfil a: " + url);
            Log.d(TAG, "Token enviado: " + token);
            Log.d(TAG, "Header Authorization: Bearer " + token);
            Log.d(TAG, "Datos: " + datosPerfil.toString());

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

                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        
                        Log.d(TAG, "Código de respuesta: " + response.code());
                        Log.d(TAG, "Respuesta completa: " + responseBody);
                        
                        if (response.isSuccessful() && jsonResponse.getBoolean("ok")) {
                            Log.d(TAG, "Perfil actualizado exitosamente");
                            callback.onSuccess(jsonResponse);
                        } else {
                            String error = jsonResponse.optString("error", "Error desconocido");
                            
                            Log.e(TAG, "Error del servidor - Código: " + response.code() + ", Mensaje: " + error);
                            
                            // Manejar errores específicos
                            if (response.code() == 400) {
                                JSONObject detalles = jsonResponse.optJSONObject("detalles");
                                if (detalles != null) {
                                    JSONObject fieldErrors = detalles.optJSONObject("fieldErrors");
                                    if (fieldErrors != null) {
                                        StringBuilder errorMsg = new StringBuilder("Errores de validación:\n");
                                        // Procesar errores de campos específicos
                                        // Por ahora, mostrar error genérico
                                        errorMsg.append(error);
                                        callback.onError(errorMsg.toString());
                                        return;
                                    }
                                }
                            } else if (response.code() == 401) {
                                error = "Sesión expirada. Inicia sesión nuevamente.";
                                Log.e(TAG, "ERROR 401: Token inválido o expirado");
                            } else if (response.code() == 403) {
                                error = "Cuenta de docente no verificada";
                                Log.e(TAG, "ERROR 403: Cuenta no verificada");
                            } else if (response.code() == 404) {
                                error = "Docente no encontrado";
                                Log.e(TAG, "ERROR 404: Docente no encontrado");
                            }
                            
                            Log.e(TAG, "Error final: " + error);
                            callback.onError(error);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parseando respuesta: " + e.getMessage());
                        callback.onError("Error procesando respuesta del servidor");
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error creando request: " + e.getMessage());
            callback.onError("Error interno");
        }
    }
}
