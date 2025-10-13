package com.example.lectana.repository;

import android.util.Log;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.AsignarCuentosRequest;
import com.example.lectana.modelos.AsignarCuentosResponse;
import com.example.lectana.modelos.CrearAulaRequest;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.services.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AulasRepository {
    private static final String TAG = "AulasRepository";
    private final SessionManager sessionManager;

    public AulasRepository(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public interface AulasCallback<T> {
        void onSuccess(T data);
        void onError(String message);
    }

    /**
     * Obtener todas las aulas del docente
     */
    public void getAulasDocente(AulasCallback<List<ModeloAula>> callback) {
        String token = sessionManager.getToken();
        Log.d(TAG, "=== DEBUG GET AULAS DOCENTE ===");
        Log.d(TAG, "Token obtenido: " + (token != null ? "Presente (" + token.length() + " chars)" : "Nulo"));
        Log.d(TAG, "Rol del usuario: " + sessionManager.getRole());
        Log.d(TAG, "¿Está logueado?: " + sessionManager.isLoggedIn());
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token de autenticación no encontrado");
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;
        Log.d(TAG, "Header de autorización: " + authHeader);
        Log.d(TAG, "Token completo: " + token);
        Log.d(TAG, "Longitud del token: " + token.length());
        Call<ApiResponse<List<ModeloAula>>> call = ApiClient.getAulasApiService().getAulasDocente(authHeader);
        
        call.enqueue(new Callback<ApiResponse<List<ModeloAula>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<ModeloAula>>> call, Response<ApiResponse<List<ModeloAula>>> response) {
                Log.d(TAG, "=== RESPUESTA GET AULAS DOCENTE ===");
                Log.d(TAG, "Código de respuesta: " + response.code());
                Log.d(TAG, "¿Respuesta exitosa?: " + response.isSuccessful());
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<ModeloAula>> apiResponse = response.body();
                    Log.d(TAG, "Respuesta API OK: " + apiResponse.isOk());
                    Log.d(TAG, "Mensaje de respuesta: " + apiResponse.getMessage());
                    
                    if (apiResponse.isOk()) {
                        Log.d(TAG, "Aulas obtenidas exitosamente: " + apiResponse.getData().size());
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
            public void onFailure(Call<ApiResponse<List<ModeloAula>>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Obtener detalles de una aula específica
     */
    public void getAulaDetalle(int aulaId, AulasCallback<ModeloAula> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;
        Call<ApiResponse<ModeloAula>> call = ApiClient.getAulasApiService().getAulaDetalle(authHeader, aulaId);
        
        call.enqueue(new Callback<ApiResponse<ModeloAula>>() {
            @Override
            public void onResponse(Call<ApiResponse<ModeloAula>> call, Response<ApiResponse<ModeloAula>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ModeloAula> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido");
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error leyendo respuesta de error", e);
                        }
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ModeloAula>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Crear una nueva aula
     */
    public void crearAula(String nombreAula, String grado, AulasCallback<ModeloAula> callback) {
        String token = sessionManager.getToken();
        Log.d(TAG, "Token obtenido: " + (token != null ? "Presente" : "Nulo"));
        
        if (token == null || token.isEmpty()) {
            Log.e(TAG, "Token de autenticación no encontrado");
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        CrearAulaRequest request = new CrearAulaRequest(nombreAula, grado);
        String authHeader = "Bearer " + token;
        Log.d(TAG, "Creando aula: " + nombreAula + " - " + grado);
        Log.d(TAG, "Header de autorización: " + authHeader);
        
        Call<ApiResponse<ModeloAula>> call = ApiClient.getAulasApiService().crearAula(authHeader, request);
        
        call.enqueue(new Callback<ApiResponse<ModeloAula>>() {
            @Override
            public void onResponse(Call<ApiResponse<ModeloAula>> call, Response<ApiResponse<ModeloAula>> response) {
                Log.d(TAG, "Respuesta del servidor: " + response.code());
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ModeloAula> apiResponse = response.body();
                    Log.d(TAG, "Respuesta API OK: " + apiResponse.isOk());
                    
                    if (apiResponse.isOk()) {
                        Log.d(TAG, "Aula creada exitosamente");
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
                    
                    // Manejar errores específicos
                    if (response.code() == 401) {
                        errorMessage = "Sesión expirada. Inicia sesión nuevamente.";
                    } else if (response.code() == 403) {
                        errorMessage = "Acceso denegado. Verifica tus permisos.";
                    } else if (response.code() == 400) {
                        errorMessage = "Datos inválidos. Verifica la información ingresada.";
                    }
                    
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ModeloAula>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Actualizar una aula existente
     */
    public void actualizarAula(int aulaId, String nombreAula, String grado, AulasCallback<ModeloAula> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        CrearAulaRequest request = new CrearAulaRequest(nombreAula, grado);
        String authHeader = "Bearer " + token;
        Call<ApiResponse<ModeloAula>> call = ApiClient.getAulasApiService().actualizarAula(authHeader, aulaId, request);
        
        call.enqueue(new Callback<ApiResponse<ModeloAula>>() {
            @Override
            public void onResponse(Call<ApiResponse<ModeloAula>> call, Response<ApiResponse<ModeloAula>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ModeloAula> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido");
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error leyendo respuesta de error", e);
                        }
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<ModeloAula>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Asignar cuentos a una aula
     */
    public void asignarCuentosAula(int aulaId, List<Integer> cuentosIds, AulasCallback<AsignarCuentosResponse> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        AsignarCuentosRequest request = new AsignarCuentosRequest(cuentosIds);
        String authHeader = "Bearer " + token;
        Call<ApiResponse<AsignarCuentosResponse>> call = ApiClient.getAulasApiService().asignarCuentosAula(authHeader, aulaId, request);
        
        call.enqueue(new Callback<ApiResponse<AsignarCuentosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AsignarCuentosResponse>> call, Response<ApiResponse<AsignarCuentosResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AsignarCuentosResponse> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido");
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Error leyendo respuesta de error", e);
                        }
                    }
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AsignarCuentosResponse>> call, Throwable t) {
                Log.e(TAG, "Error de conexión", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /** Agregar un solo cuento (incremental) */
    public void agregarCuentoAula(int aulaId, int idCuento, AulasCallback<AsignarCuentosResponse> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }
        String authHeader = "Bearer " + token;
        com.example.lectana.modelos.AgregarCuentoRequest request = new com.example.lectana.modelos.AgregarCuentoRequest(idCuento);
        Call<ApiResponse<AsignarCuentosResponse>> call = ApiClient.getAulasApiService().agregarCuentoAula(authHeader, aulaId, request);
        call.enqueue(new Callback<ApiResponse<AsignarCuentosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AsignarCuentosResponse>> call, Response<ApiResponse<AsignarCuentosResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AsignarCuentosResponse> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido");
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    if (response.errorBody() != null) {
                        try { errorMessage = response.errorBody().string(); } catch (Exception ignored) {}
                    }
                    callback.onError(errorMessage);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<AsignarCuentosResponse>> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /** Quitar un solo cuento (incremental) */
    public void quitarCuentoAula(int aulaId, int idCuento, AulasCallback<AsignarCuentosResponse> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }
        String authHeader = "Bearer " + token;
        Call<ApiResponse<AsignarCuentosResponse>> call = ApiClient.getAulasApiService().quitarCuentoAula(authHeader, aulaId, idCuento);
        call.enqueue(new Callback<ApiResponse<AsignarCuentosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AsignarCuentosResponse>> call, Response<ApiResponse<AsignarCuentosResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AsignarCuentosResponse> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido");
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    if (response.errorBody() != null) {
                        try { errorMessage = response.errorBody().string(); } catch (Exception ignored) {}
                    }
                    callback.onError(errorMessage);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<AsignarCuentosResponse>> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /** Quitar por id de asignación (fallback opcional) */
    public void quitarCuentoAulaPorAsignacion(int aulaId, int idAsignacion, AulasCallback<AsignarCuentosResponse> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }
        String authHeader = "Bearer " + token;
        Call<ApiResponse<AsignarCuentosResponse>> call = ApiClient.getAulasApiService().quitarCuentoAulaPorAsignacion(authHeader, aulaId, idAsignacion);
        call.enqueue(new Callback<ApiResponse<AsignarCuentosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AsignarCuentosResponse>> call, Response<ApiResponse<AsignarCuentosResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<AsignarCuentosResponse> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(apiResponse.getData());
                    } else {
                        callback.onError(apiResponse.getMessage() != null ? apiResponse.getMessage() : "Error desconocido");
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    if (response.errorBody() != null) {
                        try { errorMessage = response.errorBody().string(); } catch (Exception ignored) {}
                    }
                    callback.onError(errorMessage);
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<AsignarCuentosResponse>> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }
}