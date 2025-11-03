package com.example.lectana.services;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // URL de desarrollo local para emulador Android
    // private static final String BASE_URL = "http://10.0.2.2:3000/api/";
    
    // URL de producción en Render
    private static final String BASE_URL = "https://lectana-backend.onrender.com/api/";
    
    private static Retrofit retrofit;
    private static CuentosApiService cuentosApiService;
    private static AulasApiService aulasApiService;
    private static ActividadesApiService actividadesApiService;
    private static EstudiantesApiService estudiantesApiService;
    private static AuthApiService authApiService;

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(45, TimeUnit.SECONDS)
                .writeTimeout(45, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
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

    public static EstudiantesApiService getEstudiantesApiService() {
        if (estudiantesApiService == null) {
            estudiantesApiService = getRetrofitInstance().create(EstudiantesApiService.class);
        }
        return estudiantesApiService;
    }
    
    public static AuthApiService getAuthApiService() {
        if (authApiService == null) {
            authApiService = getRetrofitInstance().create(AuthApiService.class);
        }
        return authApiService;
    }
    
    // Alias para compatibilidad
    public static Retrofit getClient() {
        return getRetrofitInstance();
    }
}
