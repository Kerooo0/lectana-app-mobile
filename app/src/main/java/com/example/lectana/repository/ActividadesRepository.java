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
import com.example.lectana.modelos.nuevas.CrearActividadNuevaRequest;
import com.example.lectana.modelos.nuevas.CrearActividadBackendRequest;
import com.example.lectana.modelos.nuevas.CrearPreguntaActividadRequest;
import com.example.lectana.modelos.nuevas.CrearRespuestaActividadRequest;

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
     * Crear actividad con nuevas rutas: 1) actividad 2) preguntas 3) respuestas
     */
    public void crearActividad(CrearActividadRequest request, ActividadesCallback<Actividad> callback) {
        String token = sessionManager.getToken();
        Log.d(TAG, "=== CREAR ACTIVIDAD (NUEVAS RUTAS) ===");
        if (token == null || token.isEmpty()) {
            callback.onError("Token de autenticación no encontrado");
            return;
        }

        String authHeader = "Bearer " + token;
        ActividadesApiService api = ApiClient.getActividadesApiService();

        // Paso 1: crear actividad mínima (mapeo from UI)
        String titulo = request.getDescripcion(); // hasta tener campo específico
        Integer cursoId = (request.getAulas_ids() != null && !request.getAulas_ids().isEmpty()) ? request.getAulas_ids().get(0) : null;
        String fechaInicio = null;
        String fechaFin = null;
        try {
            java.time.OffsetDateTime now = java.time.OffsetDateTime.now();
            fechaInicio = now.toString();
            fechaFin = now.plusDays(7).toString();
        } catch (Throwable ignored) {}

        // DTO exacto del backend (incluye aulas_ids)
        java.util.List<Integer> aulasIds = new java.util.ArrayList<>();
        if (request.getAulas_ids() != null) aulasIds.addAll(request.getAulas_ids());
        CrearActividadBackendRequest crearReq = new CrearActividadBackendRequest(
                fechaFin != null ? fechaFin : fechaInicio, // fecha_entrega
                request.getTipo(),
                request.getDescripcion(),
                request.getCuento_id_cuento(),
                aulasIds
        );

        try {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            Log.d(TAG, "Payload crearActividadNueva JSON => " + gson.toJson(crearReq));
        } catch (Throwable ignored) {}

        api.crearActividadNueva(authHeader, crearReq).enqueue(new Callback<com.example.lectana.modelos.nuevas.CrearActividadBackendResponse>() {
            @Override
            public void onResponse(Call<com.example.lectana.modelos.nuevas.CrearActividadBackendResponse> call, Response<com.example.lectana.modelos.nuevas.CrearActividadBackendResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().getActividad() == null) {
                    String msg = null;
                    try { msg = response.errorBody() != null ? response.errorBody().string() : null; } catch (Exception ignored) {}
                    if (msg == null) msg = "HTTP " + response.code();
                    callback.onError("Error creando actividad: " + msg);
                    return;
                }
                Actividad actividadCreada = response.body().getActividad();
                int actividadId = actividadCreada.getId_actividad();

                // Si no hay preguntas, terminamos acá
                if (request.getPreguntas() == null || request.getPreguntas().isEmpty()) {
                    callback.onSuccess(actividadCreada);
                    return;
                }

                // Paso 2: crear preguntas en serie y sus respuestas (si múltiple choice)
                final String tipoGlobal = request.getTipo();
                crearPreguntasYRespuestasEnCadena(api, authHeader, actividadId, tipoGlobal, request, new ActividadesCallback<Void>() {
                    @Override
                    public void onSuccess(Void data) {
                        callback.onSuccess(actividadCreada);
                    }

                    @Override
                    public void onError(String message) {
                        callback.onError(message);
                    }
                });
            }

            @Override
            public void onFailure(Call<com.example.lectana.modelos.nuevas.CrearActividadBackendResponse> call, Throwable t) {
                callback.onError("Error de conexión al crear actividad: " + t.getMessage());
            }
        });
    }

    private void crearPreguntasYRespuestasEnCadena(ActividadesApiService api, String authHeader, int actividadId, String tipoGlobal, CrearActividadRequest request, ActividadesCallback<Void> callback) {
        List<CrearActividadRequest.PreguntaRequest> preguntas = request.getPreguntas();
        if (preguntas == null || preguntas.isEmpty()) {
            callback.onSuccess(null);
            return;
        }

        crearPreguntaRecursiva(api, authHeader, actividadId, tipoGlobal, preguntas, 0, callback);
    }

    private void crearPreguntaRecursiva(ActividadesApiService api, String authHeader, int actividadId, String tipoGlobal, List<CrearActividadRequest.PreguntaRequest> preguntas, int index, ActividadesCallback<Void> callback) {
        if (index >= preguntas.size()) {
            callback.onSuccess(null);
            return;
        }

        CrearActividadRequest.PreguntaRequest p = preguntas.get(index);
        CrearPreguntaActividadRequest reqPregunta = new CrearPreguntaActividadRequest(
                p.getEnunciado(),
                mapTipoParaPregunta(tipoGlobal),
                1,
                index + 1,
                true
        );

        api.crearPreguntaActividad(authHeader, actividadId, reqPregunta).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    String err = null; try { err = response.errorBody() != null ? response.errorBody().string() : null; } catch (Exception ignored) {}
                    callback.onError("Error creando pregunta: " + (err != null ? err : ("HTTP " + response.code())));
                    return;
                }
                int idPregunta = -1;
                try {
                    String json = response.body().string();
                    com.google.gson.JsonElement root = new com.google.gson.JsonParser().parse(json);
                    Log.d(TAG, "Respuesta crearPregunta raw: " + json);
                    // Buscar id_pregunta en distintas formas
                    if (root.isJsonObject() && root.getAsJsonObject().has("id_pregunta")) {
                        idPregunta = root.getAsJsonObject().get("id_pregunta").getAsInt();
                    } else if (root.isJsonObject() && root.getAsJsonObject().has("id")) {
                        idPregunta = root.getAsJsonObject().get("id").getAsInt();
                    } else if (root.isJsonObject() && root.getAsJsonObject().has("pregunta_id")) {
                        idPregunta = root.getAsJsonObject().get("pregunta_id").getAsInt();
                    } else if (root.isJsonObject() && root.getAsJsonObject().has("preguntaActividad")) {
                        com.google.gson.JsonElement arr = root.getAsJsonObject().get("preguntaActividad");
                        if (arr.isJsonArray() && arr.getAsJsonArray().size() > 0) {
                            com.google.gson.JsonElement first = arr.getAsJsonArray().get(0);
                            if (first.isJsonObject() && first.getAsJsonObject().has("id_pregunta_actividad")) {
                                idPregunta = first.getAsJsonObject().get("id_pregunta_actividad").getAsInt();
                            }
                        }
                    } else if (root.isJsonObject() && root.getAsJsonObject().has("pregunta")) {
                        com.google.gson.JsonElement p = root.getAsJsonObject().get("pregunta");
                        if (p.isJsonObject() && p.getAsJsonObject().has("id_pregunta")) {
                            idPregunta = p.getAsJsonObject().get("id_pregunta").getAsInt();
                        } else if (p.isJsonObject() && p.getAsJsonObject().has("id")) {
                            idPregunta = p.getAsJsonObject().get("id").getAsInt();
                        }
                    } else if (root.isJsonArray() && root.getAsJsonArray().size() > 0) {
                        com.google.gson.JsonElement first = root.getAsJsonArray().get(0);
                        if (first.isJsonObject() && first.getAsJsonObject().has("id_pregunta")) {
                            idPregunta = first.getAsJsonObject().get("id_pregunta").getAsInt();
                        } else if (first.isJsonObject() && first.getAsJsonObject().has("id")) {
                            idPregunta = first.getAsJsonObject().get("id").getAsInt();
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error parseando respuesta de crearPregunta", e);
                }
                if (idPregunta <= 0) {
                    // Intento final: extraer primer entero del body
                    try {
                        String all = response.body() != null ? response.body().string() : null;
                        if (all != null) {
                            java.util.regex.Matcher m = java.util.regex.Pattern.compile("\\d+").matcher(all);
                            if (m.find()) {
                                idPregunta = Integer.parseInt(m.group());
                            }
                        }
                    } catch (Exception ignored) {}
                }
                if (idPregunta <= 0) {
                    callback.onError("Error creando pregunta: id_pregunta no recibido");
                    return;
                }

                // Si es multiple choice, crear respuestas
                if ("multiple_choice".equals(mapTipo(tipoGlobal)) && p.getRespuestas() != null && !p.getRespuestas().isEmpty()) {
                    crearRespuestasEnCadena(api, authHeader, idPregunta, p.getRespuestas(), 0, new ActividadesCallback<Void>() {
                        @Override
                        public void onSuccess(Void data) {
                            crearPreguntaRecursiva(api, authHeader, actividadId, tipoGlobal, preguntas, index + 1, callback);
                        }

                        @Override
                        public void onError(String message) {
                            callback.onError(message);
                        }
                    });
                } else {
                    crearPreguntaRecursiva(api, authHeader, actividadId, tipoGlobal, preguntas, index + 1, callback);
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                callback.onError("Error de conexión creando pregunta: " + t.getMessage());
            }
        });
    }

    private void crearRespuestasEnCadena(ActividadesApiService api, String authHeader, int idPregunta, List<CrearActividadRequest.RespuestaRequest> respuestas, int index, ActividadesCallback<Void> callback) {
        if (index >= respuestas.size()) {
            callback.onSuccess(null);
            return;
        }

        CrearActividadRequest.RespuestaRequest r = respuestas.get(index);
        java.util.List<String> respuestasText = new java.util.ArrayList<>();
        if (r.getRespuesta() != null) respuestasText.add(r.getRespuesta());
        com.example.lectana.modelos.nuevas.CrearRespuestaActividadBackendRequest reqRespuesta =
                new com.example.lectana.modelos.nuevas.CrearRespuestaActividadBackendRequest(
                        respuestasText,
                        r.isEs_correcta() ? 1 : 0
                );

        api.crearRespuestaActividad(authHeader, idPregunta, reqRespuesta).enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (!response.isSuccessful()) {
                    String err = null; try { err = response.errorBody() != null ? response.errorBody().string() : null; } catch (Exception ignored) {}
                    callback.onError("Error creando respuesta: " + (err != null ? err : ("HTTP " + response.code())));
                    return;
                }
                crearRespuestasEnCadena(api, authHeader, idPregunta, respuestas, index + 1, callback);
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                callback.onError("Error de conexión creando respuesta: " + t.getMessage());
            }
        });
    }

    private String mapTipo(String tipoGlobal) {
        if (tipoGlobal == null) return "multiple_choice";
        return tipoGlobal;
    }

    private String mapTipoParaPregunta(String tipoGlobal) {
        if (tipoGlobal == null) return "multiple_choice";
        if ("respuesta_abierta".equals(tipoGlobal)) return "abierta";
        return tipoGlobal;
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

        // Preferir ruta nueva /actividades/{id}
        Call<okhttp3.ResponseBody> call = ApiClient.getActividadesApiService().eliminarActividad(authHeader, actividadId);

        call.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 200 OK: actividad eliminada
                    callback.onSuccess(null);
                    return;
                }

                // Manejo de 404/400/500 con cuerpo { ok: false, error: "..." }
                String mensaje = "Error del servidor: " + response.code();
                try {
                    if (response.errorBody() != null) {
                        String errJson = response.errorBody().string();
                        com.google.gson.JsonElement root = new com.google.gson.JsonParser().parse(errJson);
                        if (root.isJsonObject()) {
                            com.google.gson.JsonObject obj = root.getAsJsonObject();
                            if (obj.has("mensaje")) mensaje = obj.get("mensaje").getAsString();
                            if (obj.has("error")) mensaje = obj.get("error").getAsString();
                        }
                    }
                } catch (Exception ignored) {}
                callback.onError(mensaje);
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
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

        Call<okhttp3.ResponseBody> call = ApiClient.getActividadesApiService().getActividadesAula(authHeader, aulaId);

        call.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, Response<okhttp3.ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String json = response.body().string();
                        com.google.gson.JsonElement root = new com.google.gson.JsonParser().parse(json);
                        List<Actividad> actividades = new ArrayList<>();

                        if (root.isJsonArray()) {
                            for (com.google.gson.JsonElement el : root.getAsJsonArray()) {
                                Actividad act = extraerActividadDeItem(el);
                                if (act != null) actividades.add(act);
                            }
                        } else if (root.isJsonObject()) {
                            com.google.gson.JsonObject obj = root.getAsJsonObject();
                            // Intentar data, items, resultados, etc.
                            String[] posibles = new String[]{"data", "items", "result", "actividades"};
                            boolean mapeado = false;
                            for (String key : posibles) {
                                if (obj.has(key) && obj.get(key).isJsonArray()) {
                                    for (com.google.gson.JsonElement el : obj.get(key).getAsJsonArray()) {
                                        Actividad act = extraerActividadDeItem(el);
                                        if (act != null) actividades.add(act);
                                    }
                                    mapeado = true;
                                    break;
                                }
                            }
                            if (!mapeado) {
                                // Caso objeto único
                                Actividad act = extraerActividadDeItem(obj);
                                if (act != null) actividades.add(act);
                            }
                        }
                        callback.onSuccess(actividades);
                    } catch (Exception e) {
                        Log.e(TAG, "Error parseando actividades del aula", e);
                        callback.onError("Error de parseo: " + e.getMessage());
                    }
                } else {
                    String errorMessage = "Error del servidor: " + response.code();
                    callback.onError(errorMessage);
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
                Log.e(TAG, "Error de conexión al obtener actividades del aula", t);
                callback.onError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private Actividad extraerActividadDeItem(com.google.gson.JsonElement el) {
        try {
            if (el == null || !el.isJsonObject()) return null;
            com.google.gson.JsonObject obj = el.getAsJsonObject();
            // Preferir campo 'actividad' anidado
            if (obj.has("actividad") && obj.get("actividad").isJsonObject()) {
                com.google.gson.Gson g = new com.google.gson.Gson();
                return g.fromJson(obj.get("actividad"), Actividad.class);
            }
            // Si el item ya es una actividad
            if (obj.has("id_actividad") && obj.has("tipo")) {
                com.google.gson.Gson g = new com.google.gson.Gson();
                return g.fromJson(obj, Actividad.class);
            }
        } catch (Exception ignored) {}
        return null;
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
