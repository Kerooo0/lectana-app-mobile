package com.example.lectana.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectionTest {

    private final OkHttpClient httpClient = new OkHttpClient();
    private static final String TAG = "ConnectionTest";

    public interface TestCallback {
        void onSuccess(String message);
        void onError(String message);
    }

    /**
     * Probar conexión con el endpoint /health del backend
     */
    public void testHealthEndpoint(String baseUrl, TestCallback callback) {
        String url = baseUrl.endsWith("/") ? baseUrl + "health" : baseUrl + "/health";
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Log.d(TAG, "Probando conexión con: " + url);

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
                    callback.onSuccess("✅ Backend conectado correctamente\nCódigo: " + response.code() + "\nRespuesta: " + responseBody);
                } else {
                    callback.onError("❌ Error del servidor\nCódigo: " + response.code() + "\nRespuesta: " + responseBody);
                }
            }
        });
    }

    /**
     * Probar conexión con el endpoint /api/auth/login (sin credenciales)
     */
    public void testLoginEndpoint(String baseUrl, TestCallback callback) {
        String url = baseUrl.endsWith("/") ? baseUrl + "api/auth/login" : baseUrl + "/api/auth/login";
        
        Request request = new Request.Builder()
                .url(url)
                .post(okhttp3.RequestBody.create("{}", okhttp3.MediaType.get("application/json")))
                .build();

        Log.d(TAG, "Probando endpoint de login: " + url);

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

                if (response.code() == 400) {
                    // 400 es esperado cuando no enviamos credenciales válidas
                    callback.onSuccess("✅ Endpoint de login accesible\nCódigo: " + response.code() + "\nRespuesta: " + responseBody);
                } else if (response.isSuccessful()) {
                    callback.onSuccess("✅ Endpoint de login accesible\nCódigo: " + response.code() + "\nRespuesta: " + responseBody);
                } else {
                    callback.onError("❌ Error del servidor\nCódigo: " + response.code() + "\nRespuesta: " + responseBody);
                }
            }
        });
    }

    /**
     * Probar conexión básica con el servidor
     */
    public void testBasicConnection(String baseUrl, TestCallback callback) {
        String url = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
        
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Log.d(TAG, "Probando conexión básica con: " + url);

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

                callback.onSuccess("✅ Servidor accesible\nCódigo: " + response.code() + "\nRespuesta: " + responseBody);
            }
        });
    }
}
