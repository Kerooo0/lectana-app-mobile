package com.example.lectana.network;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HealthClient {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String baseUrl;

    public HealthClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public interface HealthCallback {
        void onSuccess(String body);
        void onError(String message);
    }

    public void checkHealth(HealthCallback callback) {
        String url = baseUrl.endsWith("/") ? baseUrl + "health" : baseUrl + "/health";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onError("HTTP " + response.code());
                    return;
                }
                String body = response.body() != null ? response.body().string() : "";
                callback.onSuccess(body);
            }
        });
    }
}


