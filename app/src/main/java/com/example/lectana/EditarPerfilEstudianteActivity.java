package com.example.lectana;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.EstudiantesApiService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPerfilEstudianteActivity extends AppCompatActivity {

    private ImageView botonVolver;
    private EditText inputNombre;
    private EditText inputApellido;
    private EditText inputFechaNacimiento;
    private Button botonGuardar;
    private ProgressBar progressBar;

    private SessionManager sessionManager;
    private EstudiantesApiService apiService;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_estudiante);

        sessionManager = new SessionManager(this);
        apiService = ApiClient.getEstudiantesApiService();
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        inicializarVistas();
        configurarListeners();
        cargarDatosActuales();
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        inputNombre = findViewById(R.id.input_nombre);
        inputApellido = findViewById(R.id.input_apellido);
        inputFechaNacimiento = findViewById(R.id.input_fecha_nacimiento);
        botonGuardar = findViewById(R.id.boton_guardar);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(v -> finish());

        // DatePicker para fecha de nacimiento
        inputFechaNacimiento.setOnClickListener(v -> mostrarDatePicker());

        botonGuardar.setOnClickListener(v -> guardarCambios());
    }

    private void mostrarDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    inputFechaNacimiento.setText(dateFormatter.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Establecer fecha máxima (hoy)
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void cargarDatosActuales() {
        mostrarCargando(true);

        try {
            String token = "Bearer " + sessionManager.getToken();
            
            // Primero obtener datos desde /api/auth/me para tener el id_alumno correcto
            com.example.lectana.services.AuthApiService authApiService = 
                com.example.lectana.services.ApiClient.getAuthApiService();
            
            authApiService.obtenerDatosUsuario(token).enqueue(new Callback<com.example.lectana.modelos.ApiResponse<com.example.lectana.services.AuthApiService.MeResponse>>() {
                @Override
                public void onResponse(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.services.AuthApiService.MeResponse>> call,
                                     Response<com.example.lectana.modelos.ApiResponse<com.example.lectana.services.AuthApiService.MeResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        com.example.lectana.modelos.ApiResponse<com.example.lectana.services.AuthApiService.MeResponse> apiResponse = response.body();
                        
                        // Extraer el id_alumno de la respuesta
                        if (apiResponse.isOk() && apiResponse.getData() != null) {
                            com.example.lectana.services.AuthApiService.MeResponse meData = apiResponse.getData();
                            com.example.lectana.services.AuthApiService.Alumno alumno = meData.getAlumno();
                            
                            if (alumno != null) {
                                int idAlumno = alumno.getIdAlumno();
                                Log.d("EditarPerfil", "ID Alumno obtenido: " + idAlumno);
                                
                                // Guardar el id_alumno y aula_id en SessionManager para uso futuro
                                Integer aulaId = alumno.getAulaIdAula();
                                if (aulaId != null && aulaId > 0) {
                                    sessionManager.saveAlumnoData(idAlumno, aulaId);
                                } else {
                                    sessionManager.saveAlumnoId(idAlumno);
                                }
                                
                                // Ahora cargar el perfil con el id_alumno correcto
                                cargarPerfilEstudiante(idAlumno);
                            } else {
                                Log.e("EditarPerfil", "No se encontró información del alumno en /me");
                                cargarDatosDeSesion();
                                mostrarCargando(false);
                            }
                        } else {
                            Log.e("EditarPerfil", "Respuesta de /me no válida");
                            cargarDatosDeSesion();
                            mostrarCargando(false);
                        }
                    } else {
                        Log.e("EditarPerfil", "Error al obtener datos de /me: " + response.code());
                        cargarDatosDeSesion();
                        mostrarCargando(false);
                    }
                }
                
                @Override
                public void onFailure(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.services.AuthApiService.MeResponse>> call, Throwable t) {
                    Log.e("EditarPerfil", "Error de conexión con /me", t);
                    cargarDatosDeSesion();
                    mostrarCargando(false);
                }
            });

        } catch (Exception e) {
            mostrarCargando(false);
            Log.e("EditarPerfil", "Error al cargar datos", e);
            cargarDatosDeSesion();
        }
    }
    
    private void cargarPerfilEstudiante(int idAlumno) {
        String token = "Bearer " + sessionManager.getToken();
        
        apiService.getPerfilEstudiante(token, idAlumno).enqueue(new Callback<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>> call, 
                                 Response<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>> response) {
                mostrarCargando(false);

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    EstudiantesApiService.EstudiantePerfilResponse perfil = response.body().getData();
                    llenarFormulario(perfil);
                } else {
                    // Cargar datos de la sesión como fallback
                    cargarDatosDeSesion();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>> call, Throwable t) {
                mostrarCargando(false);
                Log.e("EditarPerfil", "Error al cargar perfil", t);
                cargarDatosDeSesion();
            }
        });
    }

    private void cargarDatosDeSesion() {
        try {
            org.json.JSONObject user = sessionManager.getUser();
            if (user != null) {
                Log.d("EditarPerfil", "Cargando datos de sesión: " + user.toString());
                
                // Nombre
                String nombre = user.optString("nombre", "");
                if (!nombre.isEmpty()) {
                    inputNombre.setText(nombre);
                }
                
                // Apellido
                String apellido = user.optString("apellido", "");
                if (!apellido.isEmpty()) {
                    inputApellido.setText(apellido);
                }
                
                // Fecha de nacimiento
                String fechaNac = user.optString("fecha_nacimiento", "");
                if (!fechaNac.isEmpty() && !fechaNac.equals("null")) {
                    inputFechaNacimiento.setText(fechaNac);
                }
            } else {
                Log.w("EditarPerfil", "No hay usuario en sesión");
            }
        } catch (Exception e) {
            Log.e("EditarPerfil", "Error al cargar datos de sesión", e);
        }
    }

    private void llenarFormulario(EstudiantesApiService.EstudiantePerfilResponse perfil) {
        // Nombre (siempre debe tener valor)
        if (perfil.getNombre() != null && !perfil.getNombre().isEmpty()) {
            inputNombre.setText(perfil.getNombre());
        }
        
        // Apellido (siempre debe tener valor)
        if (perfil.getApellido() != null && !perfil.getApellido().isEmpty()) {
            inputApellido.setText(perfil.getApellido());
        }
        
        // Fecha de nacimiento (opcional)
        if (perfil.getFecha_nacimiento() != null && !perfil.getFecha_nacimiento().isEmpty()) {
            inputFechaNacimiento.setText(perfil.getFecha_nacimiento());
        }
        
        Log.d("EditarPerfil", "Formulario llenado - Nombre: " + perfil.getNombre() + 
              ", Apellido: " + perfil.getApellido() + 
              ", Fecha: " + perfil.getFecha_nacimiento());
    }

    private void guardarCambios() {
        // Validar campos
        String nombre = inputNombre.getText().toString().trim();
        String apellido = inputApellido.getText().toString().trim();
        String fechaNacimiento = inputFechaNacimiento.getText().toString().trim();

        if (nombre.isEmpty()) {
            inputNombre.setError("El nombre es requerido");
            inputNombre.requestFocus();
            return;
        }

        if (apellido.isEmpty()) {
            inputApellido.setError("El apellido es requerido");
            inputApellido.requestFocus();
            return;
        }

        mostrarCargando(true);

        try {
            // Usar el id_alumno guardado en SessionManager
            int idAlumno = sessionManager.getAlumnoId();
            
            if (idAlumno == 0) {
                Toast.makeText(this, "Error: ID de alumno no encontrado", Toast.LENGTH_SHORT).show();
                mostrarCargando(false);
                return;
            }
            
            Log.d("EditarPerfil", "Guardando cambios para ID Alumno: " + idAlumno);
            
            String token = "Bearer " + sessionManager.getToken();

            // El backend solo necesita: nombre, apellido, fecha_nacimiento
            EstudiantesApiService.ActualizarPerfilRequest request = 
                new EstudiantesApiService.ActualizarPerfilRequest(
                    nombre,
                    apellido,
                    fechaNacimiento,
                    null,  // grado - no se actualiza desde aquí
                    null,  // institucion - no se actualiza desde aquí
                    null   // codigo_aula - no se actualiza desde aquí
                );

            apiService.actualizarPerfil(token, idAlumno, request).enqueue(
                new Callback<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>> call, 
                                         Response<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>> response) {
                        mostrarCargando(false);

                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(EditarPerfilEstudianteActivity.this, 
                                "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                            
                            // Actualizar datos en la sesión
                            actualizarSesion(response.body().getData());
                            
                            finish();
                        } else {
                            Toast.makeText(EditarPerfilEstudianteActivity.this, 
                                "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>> call, Throwable t) {
                        mostrarCargando(false);
                        Log.e("EditarPerfil", "Error al actualizar perfil", t);
                        Toast.makeText(EditarPerfilEstudianteActivity.this, 
                            "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            );

        } catch (Exception e) {
            mostrarCargando(false);
            Log.e("EditarPerfil", "Error al guardar cambios", e);
            Toast.makeText(this, "Error al procesar la solicitud", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarSesion(EstudiantesApiService.EstudiantePerfilResponse perfil) {
        try {
            org.json.JSONObject user = sessionManager.getUser();
            if (user != null) {
                user.put("nombre", perfil.getNombre());
                user.put("apellido", perfil.getApellido());
                user.put("fecha_nacimiento", perfil.getFecha_nacimiento());
                user.put("edad", perfil.getEdad());
                user.put("grado", perfil.getGrado());
                user.put("institucion", perfil.getInstitucion());
                user.put("codigo_aula", perfil.getCodigo_aula());
                
                sessionManager.saveSession(sessionManager.getToken(), sessionManager.getRole(), user);
            }
        } catch (Exception e) {
            Log.e("EditarPerfil", "Error al actualizar sesión", e);
        }
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        botonGuardar.setEnabled(!mostrar);
    }
}
