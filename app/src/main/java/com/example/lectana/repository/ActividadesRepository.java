package com.example.lectana.repository;

import android.util.Log;

import com.example.lectana.modelos.Actividad;
import com.example.lectana.modelos.ActividadesDocenteResponse;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.AsignarAulasRequest;
import com.example.lectana.modelos.CrearActividadRequest;
import com.example.lectana.services.ActividadesApiService;
import com.example.lectana.services.ApiClient;
import com.example.lectana.auth.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActividadesRepository {
    private static final String TAG = "ActividadesRepository";
    private SessionManager sessionManager;

    public ActividadesRepository(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public interface ActividadesCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    /**
     * Crear actividad completa
     */
    public void crearActividad(CrearActividadRequest request, ActividadesCallback<Actividad> callback) {
        String token = sessionManager.getToken();
        Log.d(TAG, "=== CREAR ACTIVIDAD ===");
        Log.d(TAG, "Token obtenido: " + (token != null ? "Presente (" + token.length() + " chars)" : "Nulo"));

        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token de autenticación no encontrado");
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;
        Log.d(TAG, "Header de autorización: " + authHeader);
        Log.d(TAG, "Descripción: " + request.getDescripcion());
        Log.d(TAG, "Tipo: " + request.getTipo());
        Log.d(TAG, "Cuento ID: " + request.getCuento_id_cuento());
        Log.d(TAG, "Aulas IDs: " + request.getAulas_ids());
        Log.d(TAG, "Total preguntas: " + (request.getPreguntas() != null ? request.getPreguntas().size() : 0));

        Call<ApiResponse<Actividad>> call = ApiClient.getActividadesApiService().crearActividad(authHeader, request);

        call.enqueue(new Callback<ApiResponse<Actividad>>() {
            @Override
            public void onResponse(Call<ApiResponse<Actividad>> call, Response<ApiResponse<Actividad>> response) {
                Log.d(TAG, "=== RESPUESTA CREAR ACTIVIDAD ===");
                Log.d(TAG, "Código de respuesta: " + response.code());
                Log.d(TAG, "¿Respuesta exitosa?: " + response.isSuccessful());
                Log.d(TAG, "URL de la llamada: " + call.request().url());

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Actividad> apiResponse = response.body();
                    Log.d(TAG, "Respuesta API OK: " + apiResponse.isOk());
                    Log.d(TAG, "Mensaje de respuesta: " + apiResponse.getMessage());

                    if (apiResponse.isOk()) {
                        Log.d(TAG, "Actividad creada exitosamente: " + apiResponse.getData().getId_actividad());
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido";
                        Log.e(TAG, "Error de API: " + errorMsg);
                        callback.onError(errorMsg);
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    Log.e(TAG, "Error HTTP: " + response.code());

                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "Cuerpo del error: " + errorBody);
                            errorMessage = errorBody;
                        } catch (Exception e) {
                            Log.e(TAG, "Error leyendo respuesta de error", e);
                        }
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Actividad>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al crear actividad", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Obtener actividades del docente
     */
    public void getActividadesDocente(ActividadesCallback<List<Actividad>> callback) {
        Log.d(TAG, "=== INICIANDO getActividadesDocente ===");
        
        try {
            String token = sessionManager.getToken();
            Log.d(TAG, "=== GET ACTIVIDADES DOCENTE ===");
            Log.d(TAG, "Token obtenido: " + (token != null ? "Presente (" + token.length() + " chars)" : "Nulo"));

            if (token == null || token.isEmpty()) {
                Log.e(TAG, "Token de autenticación no encontrado");
                callback.onError("Token de autenticación no encontrado");
                return;
            }

            String authHeader = "Bearer " + token;
            Log.d(TAG, "Header de autorización: " + authHeader);

            Log.d(TAG, "Obteniendo ActividadesApiService...");
            ActividadesApiService apiService = ApiClient.getActividadesApiService();
            Log.d(TAG, "ActividadesApiService obtenido: " + (apiService != null ? "OK" : "NULL"));

            Log.d(TAG, "Creando llamada HTTP...");
            Call<ActividadesDocenteResponse> call = apiService.getActividadesDocente(authHeader);
            Log.d(TAG, "Llamada HTTP creada: " + (call != null ? "OK" : "NULL"));

            Log.d(TAG, "Encolando llamada HTTP...");

            call.enqueue(new Callback<ActividadesDocenteResponse>() {
                @Override
                public void onResponse(Call<ActividadesDocenteResponse> call, Response<ActividadesDocenteResponse> response) {
                    Log.d(TAG, "=== RESPUESTA GET ACTIVIDADES DOCENTE ===");
                    Log.d(TAG, "Código de respuesta: " + response.code());
                    Log.d(TAG, "¿Respuesta exitosa?: " + response.isSuccessful());
                    Log.d(TAG, "URL de la llamada: " + call.request().url());

                    if (response.isSuccessful() && response.body() != null) {
                        ActividadesDocenteResponse apiResponse = response.body();
                        Log.d(TAG, "Respuesta API OK: " + apiResponse.isOk());
                        Log.d(TAG, "Total de actividades: " + apiResponse.getTotal());

                        if (apiResponse.isOk()) {
                            Log.d(TAG, "Actividades obtenidas exitosamente: " + (apiResponse.getActividades() != null ? apiResponse.getActividades().size() : 0));
                            callback.onSuccess(apiResponse.getActividades());
                        } else {
                            Log.e(TAG, "Error de API: respuesta no exitosa");
                            callback.onError("Error de API: respuesta no exitosa");
                        }
                    } else {
                        String errorMessage = "Error del servidor: " + response.code();
                        Log.e(TAG, "Error HTTP: " + response.code());

                        if (response.errorBody() != null) {
                            try {
                                String errorBody = response.errorBody().string();
                                Log.e(TAG, "Cuerpo del error: " + errorBody);
                                errorMessage = errorBody;
                            } catch (Exception e) {
                                Log.e(TAG, "Error leyendo respuesta de error", e);
                            }
                        }
                        callback.onError(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<ActividadesDocenteResponse> call, Throwable t) {
                    Log.e(TAG, "Error de conexión, usando datos de ejemplo", t);
                    // Usar datos de ejemplo cuando falle la conexión
                    List<Actividad> actividadesEjemplo = crearActividadesEjemplo();
                    callback.onSuccess(actividadesEjemplo);
                }
            });
            
            Log.d(TAG, "Llamada HTTP encolada correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error crítico en getActividadesDocente", e);
            callback.onError("Error crítico: " + e.getMessage());
        }
    }

    /**
     * Obtener detalle de actividad específica
     */
    public void getActividadDetalle(int actividadId, ActividadesCallback<Actividad> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;

        try {
            Call<ApiResponse<Actividad>> call = ApiClient.getActividadesApiService().getActividadDetalle(authHeader, actividadId);

            call.enqueue(new Callback<ApiResponse<Actividad>>() {
                @Override
                public void onResponse(Call<ApiResponse<Actividad>> call, Response<ApiResponse<Actividad>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse<Actividad> apiResponse = response.body();
                        if (apiResponse.isOk()) {
                            callback.onSuccess(apiResponse.getData());
                        } else {
                            String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido";
                            callback.onError(errorMsg);
                        }
                    } else {
                        String errorMessage = "Error del servidor: " + response.code();
                        callback.onError(errorMessage);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse<Actividad>> call, Throwable t) {
                    Log.e(TAG, "Error de conexión, usando datos de ejemplo para actividad " + actividadId, t);
                    // Usar datos de ejemplo cuando falle la conexión
                    Actividad actividadEjemplo = crearActividadEjemplo(actividadId);
                    callback.onSuccess(actividadEjemplo);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error creando llamada Retrofit para actividad detalle, usando datos de ejemplo", e);
            // Usar datos de ejemplo cuando hay problemas con Retrofit
            Actividad actividadEjemplo = crearActividadEjemplo(actividadId);
            callback.onSuccess(actividadEjemplo);
        }
    }

    /**
     * Actualizar actividad
     */
    public void actualizarActividad(int actividadId, CrearActividadRequest request, ActividadesCallback<Actividad> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;

        Call<ApiResponse<Actividad>> call = ApiClient.getActividadesApiService().actualizarActividad(authHeader, actividadId, request);

        call.enqueue(new Callback<ApiResponse<Actividad>>() {
            @Override
            public void onResponse(Call<ApiResponse<Actividad>> call, Response<ApiResponse<Actividad>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Actividad> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido";
                        callback.onError(errorMsg);
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Actividad>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al actualizar actividad", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Eliminar actividad
     */
    public void eliminarActividad(int actividadId, ActividadesCallback<Void> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;

        Call<ApiResponse<Void>> call = ApiClient.getActividadesApiService().eliminarActividad(authHeader, actividadId);

        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(null);
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido";
                        callback.onError(errorMsg);
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al eliminar actividad", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Asignar actividad a aulas
     */
    public void asignarActividadAulas(int actividadId, List<Integer> aulasIds, ActividadesCallback<Void> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;
        AsignarAulasRequest request = new AsignarAulasRequest(aulasIds);

        Call<ApiResponse<Void>> call = ApiClient.getActividadesApiService().asignarActividadAulas(authHeader, actividadId, request);

        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(null);
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido";
                        callback.onError(errorMsg);
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al asignar actividad a aulas", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Obtener actividades de un aula específica
     */
    public void getActividadesAula(int aulaId, ActividadesCallback<List<Actividad>> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;

        Call<ApiResponse<List<Actividad>>> call = ApiClient.getActividadesApiService().getActividadesAula(authHeader, aulaId);

        call.enqueue(new Callback<ApiResponse<List<Actividad>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Actividad>>> call, Response<ApiResponse<List<Actividad>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Actividad>> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        String errorMsg = apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido";
                        callback.onError(errorMsg);
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Actividad>>> call, Throwable t) {
                Log.e(TAG, "Error de conexión al obtener actividades del aula", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    // Métodos para datos de ejemplo
    private List<Actividad> crearActividadesEjemplo() {
        List<Actividad> actividades = new ArrayList<>();
        
        // Actividad 1 - Opción Múltiple
        Actividad actividad1 = new Actividad();
        actividad1.setId_actividad(1);
        actividad1.setDescripcion("Comprensión lectora - El Gato Con Botas");
        actividad1.setTipo("multiple_choice");
        actividad1.setCuento_id_cuento(6);
        actividad1.setDocente_id_docente(1);
        actividad1.setFecha_creacion("2024-01-15");
        
        List<Integer> aulas1 = new ArrayList<>();
        aulas1.add(20);
        actividad1.setAulas_ids(aulas1);
        
        actividades.add(actividad1);
        
        // Actividad 2 - Respuesta Abierta
        Actividad actividad2 = new Actividad();
        actividad2.setId_actividad(2);
        actividad2.setDescripcion("Reflexión sobre valores");
        actividad2.setTipo("respuesta_abierta");
        actividad2.setCuento_id_cuento(6);
        actividad2.setDocente_id_docente(1);
        actividad2.setFecha_creacion("2024-01-16");
        
        List<Integer> aulas2 = new ArrayList<>();
        aulas2.add(20);
        actividad2.setAulas_ids(aulas2);
        
        actividades.add(actividad2);
        
        return actividades;
    }

    private Actividad crearActividadEjemplo(int actividadId) {
        Actividad actividad = new Actividad();
        actividad.setId_actividad(actividadId);
        actividad.setDescripcion("Actividad de ejemplo - ID " + actividadId);
        actividad.setTipo("multiple_choice");
        actividad.setCuento_id_cuento(6);
        actividad.setDocente_id_docente(1);
        actividad.setFecha_creacion("2024-01-15");
        
        List<Integer> aulas = new ArrayList<>();
        aulas.add(20);
        actividad.setAulas_ids(aulas);
        
        return actividad;
    }
}
