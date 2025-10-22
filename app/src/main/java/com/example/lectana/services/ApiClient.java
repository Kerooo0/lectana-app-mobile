package com.example.lectana.services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:3000/api/";
    private static Retrofit retrofit;
    private static CuentosApiService cuentosApiService;
    private static AulasApiService aulasApiService;
    private static ActividadesApiService actividadesApiService;

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static CuentosApiService getCuentosApiService() {
        if (cuentosApiService == null) {
            cuentosApiService = getRetrofitInstance().create(CuentosApiService.class);
        }
        return cuentosApiService;
    }

    public static AulasApiService getAulasApiService() {
        if (aulasApiService == null) {
            aulasApiService = getRetrofitInstance().create(AulasApiService.class);
        }
        return aulasApiService;
    }

    public static ActividadesApiService getActividadesApiService() {
        if (actividadesApiService == null) {
            actividadesApiService = getRetrofitInstance().create(ActividadesApiService.class);
        }
        return actividadesApiService;
    }
}
