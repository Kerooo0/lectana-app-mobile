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

public class AuthClient {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String baseUrl;
    private static final String TAG = "AuthClient";

    public AuthClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public interface LoginCallback {
        void onSuccess(String token, String role, JSONObject user);
        void onError(String message);
    }

    public void login(String email, String password, LoginCallback callback) {
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            String url = baseUrl.endsWith("/") ? baseUrl + "api/auth/login" : baseUrl + "/api/auth/login";

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            Log.d(TAG, "Enviando login a: " + url);

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
                        
                        // Verificar si la respuesta es exitosa (igual que en el frontend web)
                        boolean ok = jsonResponse.optBoolean("ok", true);
                        
                        if (response.isSuccessful() && ok) {
                            String token = jsonResponse.getString("token");
                            String role = jsonResponse.getString("role");
                            JSONObject user = jsonResponse.getJSONObject("user");
                            
                            Log.d(TAG, "Login exitoso - Role: " + role + ", User: " + user.toString());
                            callback.onSuccess(token, role, user);
                        } else {
                            String error = jsonResponse.optString("error", "Error desconocido");
                            Log.e(TAG, "Error del servidor: " + error);
                            callback.onError(error);
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "Error parseando respuesta: " + e.getMessage());
                        callback.onError("Error procesando respuesta del servidor");
                    }
                }
            });

        } catch (JSONException e) {
            Log.e(TAG, "Error creando JSON: " + e.getMessage());
            callback.onError("Error interno");
        }
    }
}
