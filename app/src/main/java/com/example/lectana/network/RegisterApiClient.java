package com.example.lectana.network;

import android.util.Log;

import com.example.lectana.model.DatosRegistroDocente;

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

public class RegisterApiClient {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final String baseUrl;
    private static final String TAG = "RegisterApiClient";

    public RegisterApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public interface RegisterCallback {
        void onSuccess(JSONObject response);
        void onError(String message);
    }

    /**
     * Registrar docente en el backend
     */
    public void registrarDocente(DatosRegistroDocente datos, RegisterCallback callback) {
        try {
            // Crear JSON con los datos del formulario
            JSONObject datosRegistro = new JSONObject();
            
            // Datos personales
            datosRegistro.put("nombre", datos.getNombre().trim());
            datosRegistro.put("apellido", datos.getApellido().trim());
            datosRegistro.put("email", datos.getEmail().trim());
            datosRegistro.put("edad", datos.getEdad());
            datosRegistro.put("password", datos.getPassword());
            datosRegistro.put("dni", datos.getDni().trim());
            
            // Teléfono es opcional
            if (datos.getTelefono() != null && !datos.getTelefono().trim().isEmpty()) {
                datosRegistro.put("telefono", datos.getTelefono().trim());
            }
            
            // Datos institucionales
            datosRegistro.put("institucion_nombre", datos.getInstitucionNombre().trim());
            datosRegistro.put("institucion_pais", datos.getInstitucionPais().trim());
            datosRegistro.put("institucion_provincia", datos.getInstitucionProvincia().trim());
            
        // Enviar valores en mayúsculas que funcionan con el esquema Zod del backend
        String nivelEducativo;
        switch (datos.getNivelEducativo().toUpperCase()) {
            case "PRIMARIA":
                nivelEducativo = "PRIMARIA";
                break;
            case "SECUNDARIA":
                nivelEducativo = "SECUNDARIA";
                break;
            case "AMBOS":
                nivelEducativo = "AMBOS";
                break;
            default:
                nivelEducativo = "AMBOS"; // Valor por defecto
        }
        Log.d(TAG, "Nivel educativo original: " + datos.getNivelEducativo() + " -> Convertido: " + nivelEducativo);
            datosRegistro.put("nivel_educativo", nivelEducativo);

            String url = baseUrl.endsWith("/") ? baseUrl + "api/auth/registro-form-docente" : baseUrl + "/api/auth/registro-form-docente";

            RequestBody body = RequestBody.create(
                datosRegistro.toString(),
                MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Log.d(TAG, "Enviando registro de docente a: " + url);
            Log.d(TAG, "Datos: " + datosRegistro.toString());

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

                        boolean ok = jsonResponse.optBoolean("ok", false);

                        if (response.isSuccessful() && ok) {
                            Log.d(TAG, "Docente registrado exitosamente");
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
                                        errorMsg.append(error);
                                        callback.onError(errorMsg.toString());
                                        return;
                                    }
                                }
                            } else if (response.code() == 409) {
                                error = "Email ya registrado";
                                Log.e(TAG, "ERROR 409: Email ya existe");
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
            Log.e(TAG, "Error creando JSON o request: " + e.getMessage());
            callback.onError("Error interno al preparar la solicitud");
        }
    }
}
