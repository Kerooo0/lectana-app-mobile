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
    private Spinner spinnerGrado;
    private EditText inputInstitucion;
    private EditText inputCodigoAula;
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
        configurarSpinnerGrado();
        configurarListeners();
        cargarDatosActuales();
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        inputNombre = findViewById(R.id.input_nombre);
        inputApellido = findViewById(R.id.input_apellido);
        inputFechaNacimiento = findViewById(R.id.input_fecha_nacimiento);
        spinnerGrado = findViewById(R.id.spinner_grado);
        inputInstitucion = findViewById(R.id.input_institucion);
        inputCodigoAula = findViewById(R.id.input_codigo_aula);
        botonGuardar = findViewById(R.id.boton_guardar);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void configurarSpinnerGrado() {
        String[] grados = {
                "Selecciona un grado",
                "1° Básico",
                "2° Básico",
                "3° Básico",
                "4° Básico",
                "5° Básico",
                "6° Básico",
                "7° Básico",
                "8° Básico",
                "1° Medio",
                "2° Medio",
                "3° Medio",
                "4° Medio"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                grados
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrado.setAdapter(adapter);
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
            org.json.JSONObject user = sessionManager.getUser();
            if (user == null) {
                Toast.makeText(this, "Error: No hay sesión activa", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Debug: Ver estructura completa del usuario
            Log.d("EditarPerfil", "Usuario completo: " + user.toString());
            Log.d("EditarPerfil", "Role: " + sessionManager.getRole());

            // Intentar con id_estudiante o id_alumno
            int idEstudiante = user.optInt("id_estudiante", 0);
            if (idEstudiante == 0) {
                idEstudiante = user.optInt("id_alumno", 0);
                Log.d("EditarPerfil", "Usando id_alumno: " + idEstudiante);
            } else {
                Log.d("EditarPerfil", "Usando id_estudiante: " + idEstudiante);
            }
            
            String token = "Bearer " + sessionManager.getToken();

            apiService.getPerfilEstudiante(token, idEstudiante).enqueue(new Callback<ApiResponse<EstudiantesApiService.EstudiantePerfilResponse>>() {
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

        } catch (Exception e) {
            mostrarCargando(false);
            Log.e("EditarPerfil", "Error al cargar datos", e);
            cargarDatosDeSesion();
        }
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
                
                // Grado
                String grado = user.optString("grado", "");
                if (!grado.isEmpty() && !grado.equals("null")) {
                    setSpinnerValue(spinnerGrado, grado);
                }
                
                // Institución
                String institucion = user.optString("institucion", "");
                if (!institucion.isEmpty() && !institucion.equals("null")) {
                    inputInstitucion.setText(institucion);
                }
                
                // Código de aula
                String codigoAula = user.optString("codigo_aula", "");
                if (!codigoAula.isEmpty() && !codigoAula.equals("null")) {
                    inputCodigoAula.setText(codigoAula);
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
        
        // Grado (opcional)
        if (perfil.getGrado() != null && !perfil.getGrado().isEmpty()) {
            setSpinnerValue(spinnerGrado, perfil.getGrado());
        }
        
        // Institución (opcional)
        if (perfil.getInstitucion() != null && !perfil.getInstitucion().isEmpty()) {
            inputInstitucion.setText(perfil.getInstitucion());
        }
        
        // Código de aula (opcional)
        if (perfil.getCodigo_aula() != null && !perfil.getCodigo_aula().isEmpty()) {
            inputCodigoAula.setText(perfil.getCodigo_aula());
        }
        
        Log.d("EditarPerfil", "Formulario llenado - Nombre: " + perfil.getNombre() + 
              ", Apellido: " + perfil.getApellido() + 
              ", Fecha: " + perfil.getFecha_nacimiento() + 
              ", Grado: " + perfil.getGrado() + 
              ", Institución: " + perfil.getInstitucion() + 
              ", Código Aula: " + perfil.getCodigo_aula());
    }

    private void setSpinnerValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void guardarCambios() {
        // Validar campos
        String nombre = inputNombre.getText().toString().trim();
        String apellido = inputApellido.getText().toString().trim();
        String fechaNacimiento = inputFechaNacimiento.getText().toString().trim();
        String grado = spinnerGrado.getSelectedItem().toString();
        String institucion = inputInstitucion.getText().toString().trim();
        String codigoAula = inputCodigoAula.getText().toString().trim();

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

        if (grado.equals("Selecciona un grado")) {
            Toast.makeText(this, "Por favor selecciona un grado", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCargando(true);

        try {
            org.json.JSONObject user = sessionManager.getUser();
            
            // Intentar con id_estudiante o id_alumno
            int idEstudiante = user.optInt("id_estudiante", 0);
            if (idEstudiante == 0) {
                idEstudiante = user.optInt("id_alumno", 0);
            }
            
            Log.d("EditarPerfil", "Guardando cambios para ID: " + idEstudiante);
            
            String token = "Bearer " + sessionManager.getToken();

            EstudiantesApiService.ActualizarPerfilRequest request = 
                new EstudiantesApiService.ActualizarPerfilRequest(
                    nombre,
                    apellido,
                    fechaNacimiento,
                    grado,
                    institucion,
                    codigoAula
                );

            apiService.actualizarPerfil(token, idEstudiante, request).enqueue(
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
