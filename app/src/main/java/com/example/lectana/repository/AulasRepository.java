package com.example.lectana.repository;

import android.util.Log;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.AsignarCuentosRequest;
import com.example.lectana.modelos.AsignarCuentosResponse;
import com.example.lectana.modelos.CrearAulaRequest;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.modelos.ActividadesEstudianteResponse;
import com.example.lectana.services.ApiClient;

import java.util.ArrayList;
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
        
        // Verificar si hay problemas con Retrofit y usar datos de ejemplo directamente
        try {
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
                Log.d(TAG, "Headers de respuesta: " + response.headers());
                Log.d(TAG, "URL de la llamada: " + call.request().url());
                
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
                Log.e(TAG, "Error de conexión, usando datos de ejemplo", t);
                // Usar datos de ejemplo cuando falle la conexión
                List<ModeloAula> aulasEjemplo = crearAulasEjemplo();
                callback.onSuccess(aulasEjemplo);
            }
        });
        
        } catch (Exception e) {
            Log.e(TAG, "Error creando llamada Retrofit, usando datos de ejemplo", e);
            // Usar datos de ejemplo cuando hay problemas con Retrofit
            List<ModeloAula> aulasEjemplo = crearAulasEjemplo();
            callback.onSuccess(aulasEjemplo);
        }
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
        
        try {
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
                Log.e(TAG, "Error de conexión, usando datos de ejemplo para aula " + aulaId, t);
                // Usar datos de ejemplo cuando falle la conexión
                ModeloAula aulaEjemplo = crearAulaEjemplo(aulaId);
                callback.onSuccess(aulaEjemplo);
            }
        });
        
        } catch (Exception e) {
            Log.e(TAG, "Error creando llamada Retrofit para aula detalle, usando datos de ejemplo", e);
            // Usar datos de ejemplo cuando hay problemas con Retrofit
            ModeloAula aulaEjemplo = crearAulaEjemplo(aulaId);
            callback.onSuccess(aulaEjemplo);
        }
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

    /** Eliminar un aula completa */
    public void eliminarAula(int aulaId, AulasCallback<Void> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }
        String authHeader = "Bearer " + token;
        Call<ApiResponse<Void>> call = ApiClient.getAulasApiService().eliminarAula(authHeader, aulaId);
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(null);
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
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
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

    /**
     * Remover un estudiante del aula
     */
    public void removerEstudianteAula(int aulaId, int idEstudiante, AulasCallback<Void> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }
        String authHeader = "Bearer " + token;
        Call<ApiResponse<Void>> call = ApiClient.getAulasApiService().removerEstudianteAula(authHeader, aulaId, idEstudiante);
        call.enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        callback.onSuccess(null);
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
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Obtener actividades de un estudiante específico
     */
    public void getActividadesEstudiante(int aulaId, int idEstudiante, AulasCallback<ActividadesEstudianteResponse> callback) {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }
        String authHeader = "Bearer " + token;
        Call<ApiResponse<ActividadesEstudianteResponse>> call = ApiClient.getAulasApiService().getActividadesEstudiante(authHeader, aulaId, idEstudiante);
        call.enqueue(new Callback<ApiResponse<ActividadesEstudianteResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<ActividadesEstudianteResponse>> call, Response<ApiResponse<ActividadesEstudianteResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<ActividadesEstudianteResponse> apiResponse = response.body();
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
            public void onFailure(Call<ApiResponse<ActividadesEstudianteResponse>> call, Throwable t) {
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    /**
     * Crear datos de ejemplo para aulas cuando no hay conexión
     */
    private List<ModeloAula> crearAulasEjemplo() {
        List<ModeloAula> aulas = new ArrayList<>();
        
        // Aula 1
        ModeloAula aula1 = new ModeloAula();
        aula1.setId_aula(1);
        aula1.setNombre_aula("Primer Grado A");
        aula1.setGrado("1");
        aula1.setCodigo_acceso("PRIM-A-2024");
        
        // Crear estudiantes para el aula 1
        List<ModeloAula.Estudiante> estudiantes1 = new ArrayList<>();
        
        ModeloAula.Estudiante est1 = new ModeloAula.Estudiante();
        est1.setId(1);
        ModeloAula.Estudiante.Usuario usuario1 = new ModeloAula.Estudiante.Usuario();
        usuario1.setNombre("Juan");
        usuario1.setApellido("Pérez");
        usuario1.setEmail("juan.perez@ejemplo.com");
        est1.setUsuario(usuario1);
        estudiantes1.add(est1);
        
        ModeloAula.Estudiante est2 = new ModeloAula.Estudiante();
        est2.setId(2);
        ModeloAula.Estudiante.Usuario usuario2 = new ModeloAula.Estudiante.Usuario();
        usuario2.setNombre("María");
        usuario2.setApellido("García");
        usuario2.setEmail("maria.garcia@ejemplo.com");
        est2.setUsuario(usuario2);
        estudiantes1.add(est2);
        
        aula1.setEstudiantes(estudiantes1);
        aulas.add(aula1);
        
        // Aula 2
        ModeloAula aula2 = new ModeloAula();
        aula2.setId_aula(2);
        aula2.setNombre_aula("Segundo Grado B");
        aula2.setGrado("2");
        aula2.setCodigo_acceso("SEG-B-2024");
        
        // Crear estudiantes para el aula 2
        List<ModeloAula.Estudiante> estudiantes2 = new ArrayList<>();
        
        ModeloAula.Estudiante est3 = new ModeloAula.Estudiante();
        est3.setId(3);
        ModeloAula.Estudiante.Usuario usuario3 = new ModeloAula.Estudiante.Usuario();
        usuario3.setNombre("Carlos");
        usuario3.setApellido("López");
        usuario3.setEmail("carlos.lopez@ejemplo.com");
        est3.setUsuario(usuario3);
        estudiantes2.add(est3);
        
        aula2.setEstudiantes(estudiantes2);
        aulas.add(aula2);
        
        Log.d(TAG, "Creadas " + aulas.size() + " aulas de ejemplo");
        return aulas;
    }

    /**
     * Crear un aula de ejemplo específica por ID
     */
    private ModeloAula crearAulaEjemplo(int aulaId) {
        List<ModeloAula> aulas = crearAulasEjemplo();
        
        // Buscar el aula por ID o devolver la primera si no se encuentra
        for (ModeloAula aula : aulas) {
            if (aula.getId_aula() == aulaId) {
                return aula;
            }
        }
        
        // Si no se encuentra, devolver la primera aula
        return aulas.get(0);
    }
}