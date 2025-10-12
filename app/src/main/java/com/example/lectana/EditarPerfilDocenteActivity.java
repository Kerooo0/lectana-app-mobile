package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.network.DocenteApiClient;
import com.example.lectana.ui.SuccessModal;

import org.json.JSONObject;

public class EditarPerfilDocenteActivity extends AppCompatActivity {

    // Componentes de la interfaz
    private EditText edtNombreDocente;
    private EditText edtApellidoDocente;
    private EditText edtEmailDocente;
    private EditText edtTelefonoDocente;
    private EditText edtEdadDocente;
    private EditText edtDniDocente;
    private EditText edtInstitucionDocente;
    private EditText edtPaisDocente;
    private EditText edtProvinciaDocente;
    private Spinner spinnerNivelEducativo;
    private Button botonGuardarPerfil;
    private Button botonCancelarEdicion;

    // Gestión de sesión y API
    private SessionManager sessionManager;
    private DocenteApiClient docenteApiClient;
    
    private static final String BASE_URL = "https://lectana-backend.onrender.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_docente);

        // Inicializar gestión de sesión y API
        sessionManager = new SessionManager(this);
        docenteApiClient = new DocenteApiClient(BASE_URL);
        
        // Verificar sesión antes de continuar
        if (!verificarSesion()) {
            return;
        }

        inicializarComponentes();
        cargarDatosUsuario();
        configurarSpinner();
        configurarListeners();
    }

    private void inicializarComponentes() {
        edtNombreDocente = findViewById(R.id.edt_nombre_docente);
        edtApellidoDocente = findViewById(R.id.edt_apellido_docente);
        edtEmailDocente = findViewById(R.id.edt_email_docente);
        edtTelefonoDocente = findViewById(R.id.edt_telefono_docente);
        edtEdadDocente = findViewById(R.id.edt_edad_docente);
        edtDniDocente = findViewById(R.id.edt_dni_docente);
        edtInstitucionDocente = findViewById(R.id.edt_institucion_docente);
        edtPaisDocente = findViewById(R.id.edt_pais_docente);
        edtProvinciaDocente = findViewById(R.id.edt_provincia_docente);
        spinnerNivelEducativo = findViewById(R.id.spinner_nivel_educativo);
        botonGuardarPerfil = findViewById(R.id.boton_guardar_perfil);
        botonCancelarEdicion = findViewById(R.id.boton_cancelar_edicion);

        // Botón volver
        findViewById(R.id.boton_volver_editar).setOnClickListener(v -> finish());
    }

    private void cargarDatosUsuario() {
        try {
            JSONObject user = sessionManager.getUser();
            JSONObject docente = sessionManager.getDocente();
            
            if (user != null) {
                // Cargar datos básicos del usuario
                edtNombreDocente.setText(user.optString("nombre", ""));
                edtApellidoDocente.setText(user.optString("apellido", ""));
                edtEmailDocente.setText(user.optString("email", ""));
                edtEdadDocente.setText(String.valueOf(user.optInt("edad", 0)));
                
                // Cargar datos específicos del docente si están disponibles
                if (docente != null) {
                    edtTelefonoDocente.setText(docente.optString("telefono", ""));
                    edtDniDocente.setText(docente.optString("dni", ""));
                    edtInstitucionDocente.setText(docente.optString("institucion_nombre", ""));
                    edtPaisDocente.setText(docente.optString("institucion_pais", ""));
                    edtProvinciaDocente.setText(docente.optString("institucion_provincia", ""));
                    
                    // Configurar nivel educativo
                    String nivelEducativo = docente.optString("nivel_educativo", "AMBOS");
                    String[] niveles = {"PRIMARIA", "SECUNDARIA", "AMBOS"};
                    for (int i = 0; i < niveles.length; i++) {
                        if (niveles[i].equals(nivelEducativo)) {
                            spinnerNivelEducativo.setSelection(i);
                            break;
                        }
                    }
                } else {
                    // Valores por defecto si no hay datos del docente
                    edtTelefonoDocente.setText("");
                    edtDniDocente.setText("");
                    edtInstitucionDocente.setText("");
                    edtPaisDocente.setText("");
                    edtProvinciaDocente.setText("");
                    spinnerNivelEducativo.setSelection(2); // AMBOS por defecto
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error cargando datos del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarSpinner() {
        String[] nivelesEducativos = {"PRIMARIA", "SECUNDARIA", "AMBOS"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nivelesEducativos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNivelEducativo.setAdapter(adapter);
        
        // Seleccionar "AMBOS" por defecto
        spinnerNivelEducativo.setSelection(2);
    }

    private void configurarListeners() {
        botonGuardarPerfil.setOnClickListener(v -> guardarPerfil());
        botonCancelarEdicion.setOnClickListener(v -> cancelarEdicion());
    }

    private void guardarPerfil() {
        // Validar campos obligatorios
        if (!validarCampos()) {
            return;
        }

        // Mostrar loading
        botonGuardarPerfil.setEnabled(false);
        botonGuardarPerfil.setText("Guardando...");

        // Obtener token de la sesión
        String token = sessionManager.getToken();
        Log.d("EditarPerfil", "Token obtenido: " + (token != null ? "EXISTE" : "NULL"));
        Log.d("EditarPerfil", "Usuario logueado: " + sessionManager.isLoggedIn());
        Log.d("EditarPerfil", "Rol: " + sessionManager.getRole());
        
        if (token == null) {
            Toast.makeText(this, "Sesión expirada - Token no encontrado", Toast.LENGTH_LONG).show();
            irAlLogin();
            return;
        }

        // Crear JSON con los datos del formulario
        try {
            JSONObject datosPerfil = new JSONObject();
            
            // Datos personales (tabla usuario)
            datosPerfil.put("nombre", edtNombreDocente.getText().toString().trim());
            datosPerfil.put("apellido", edtApellidoDocente.getText().toString().trim());
            datosPerfil.put("email", edtEmailDocente.getText().toString().trim());
            datosPerfil.put("edad", Integer.parseInt(edtEdadDocente.getText().toString().trim()));
            
            // Datos profesionales (tabla docente)
            String telefono = edtTelefonoDocente.getText().toString().trim();
            if (!telefono.isEmpty()) {
                datosPerfil.put("telefono", telefono);
            }
            
            String dni = edtDniDocente.getText().toString().trim();
            if (!dni.isEmpty()) {
                datosPerfil.put("dni", dni);
            }
            
            datosPerfil.put("institucion_nombre", edtInstitucionDocente.getText().toString().trim());
            datosPerfil.put("institucion_pais", edtPaisDocente.getText().toString().trim());
            datosPerfil.put("institucion_provincia", edtProvinciaDocente.getText().toString().trim());
            datosPerfil.put("nivel_educativo", spinnerNivelEducativo.getSelectedItem().toString());
            
            // Enviar datos al backend
            docenteApiClient.actualizarPerfilDocente(token, datosPerfil, new DocenteApiClient.UpdateProfileCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    runOnUiThread(() -> {
                        botonGuardarPerfil.setEnabled(true);
                        botonGuardarPerfil.setText("Guardar");
                        
                        Log.d("EditarPerfil", "Respuesta del backend: " + response.toString());
                        
                        // Actualizar datos en la sesión con los cambios realizados
                        actualizarSesionConCambios();
                        
                        // Mostrar modal de éxito
                        SuccessModal successModal = new SuccessModal(EditarPerfilDocenteActivity.this, new SuccessModal.OnSuccessListener() {
                            @Override
                            public void onSuccess() {
                                // Volver al perfil después de cerrar el modal
                                Intent intent = new Intent(EditarPerfilDocenteActivity.this, PerfilDocenteActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        successModal.showModal();
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        botonGuardarPerfil.setEnabled(true);
                        botonGuardarPerfil.setText("Guardar");
                        Toast.makeText(EditarPerfilDocenteActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                    });
                }
            });

        } catch (Exception e) {
            botonGuardarPerfil.setEnabled(true);
            botonGuardarPerfil.setText("Guardar");
            Toast.makeText(this, "Error preparando datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarCampos() {
        boolean esValido = true;

        // Validar nombre (2-50 caracteres)
        String nombre = edtNombreDocente.getText().toString().trim();
        if (nombre.isEmpty()) {
            edtNombreDocente.setError("El nombre es obligatorio");
            esValido = false;
        } else if (nombre.length() < 2 || nombre.length() > 50) {
            edtNombreDocente.setError("El nombre debe tener entre 2 y 50 caracteres");
            esValido = false;
        }

        // Validar apellido (2-50 caracteres)
        String apellido = edtApellidoDocente.getText().toString().trim();
        if (apellido.isEmpty()) {
            edtApellidoDocente.setError("El apellido es obligatorio");
            esValido = false;
        } else if (apellido.length() < 2 || apellido.length() > 50) {
            edtApellidoDocente.setError("El apellido debe tener entre 2 y 50 caracteres");
            esValido = false;
        }

        // Validar email (formato válido)
        String email = edtEmailDocente.getText().toString().trim();
        if (email.isEmpty()) {
            edtEmailDocente.setError("El email es obligatorio");
            esValido = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailDocente.setError("Formato de email inválido");
            esValido = false;
        }

        // Validar edad (18-80 años)
        String edadStr = edtEdadDocente.getText().toString().trim();
        if (edadStr.isEmpty()) {
            edtEdadDocente.setError("La edad es obligatoria");
            esValido = false;
        } else {
            try {
                int edad = Integer.parseInt(edadStr);
                if (edad < 18 || edad > 80) {
                    edtEdadDocente.setError("La edad debe estar entre 18 y 80 años");
                    esValido = false;
                }
            } catch (NumberFormatException e) {
                edtEdadDocente.setError("Formato de edad inválido");
                esValido = false;
            }
        }

        // Validar DNI (7-15 caracteres)
        String dni = edtDniDocente.getText().toString().trim();
        if (dni.isEmpty()) {
            edtDniDocente.setError("El DNI es obligatorio");
            esValido = false;
        } else if (dni.length() < 7 || dni.length() > 15) {
            edtDniDocente.setError("El DNI debe tener entre 7 y 15 caracteres");
            esValido = false;
        }

        // Validar institución (mínimo 1 carácter)
        String institucion = edtInstitucionDocente.getText().toString().trim();
        if (institucion.isEmpty()) {
            edtInstitucionDocente.setError("La institución es obligatoria");
            esValido = false;
        }

        // Validar país (mínimo 1 carácter)
        String pais = edtPaisDocente.getText().toString().trim();
        if (pais.isEmpty()) {
            edtPaisDocente.setError("El país es obligatorio");
            esValido = false;
        }

        // Validar provincia (mínimo 1 carácter)
        String provincia = edtProvinciaDocente.getText().toString().trim();
        if (provincia.isEmpty()) {
            edtProvinciaDocente.setError("La provincia es obligatoria");
            esValido = false;
        }

        return esValido;
    }

    private void cancelarEdicion() {
        // Mostrar diálogo de confirmación
        Toast.makeText(this, "Cambios descartados", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Actualizar la sesión con los cambios realizados en el formulario
     */
    private void actualizarSesionConCambios() {
        try {
            // Obtener datos actuales de la sesión
            JSONObject user = sessionManager.getUser();
            JSONObject docente = sessionManager.getDocente();
            
            if (user != null) {
                // Actualizar datos del usuario
                user.put("nombre", edtNombreDocente.getText().toString().trim());
                user.put("apellido", edtApellidoDocente.getText().toString().trim());
                user.put("email", edtEmailDocente.getText().toString().trim());
                user.put("edad", Integer.parseInt(edtEdadDocente.getText().toString().trim()));
                
                Log.d("EditarPerfil", "Usuario actualizado: " + user.toString());
            }
            
            if (docente != null) {
                // Actualizar datos del docente
                String telefono = edtTelefonoDocente.getText().toString().trim();
                if (!telefono.isEmpty()) {
                    docente.put("telefono", telefono);
                }
                
                String dni = edtDniDocente.getText().toString().trim();
                if (!dni.isEmpty()) {
                    docente.put("dni", dni);
                }
                
                docente.put("institucion_nombre", edtInstitucionDocente.getText().toString().trim());
                docente.put("institucion_pais", edtPaisDocente.getText().toString().trim());
                docente.put("institucion_provincia", edtProvinciaDocente.getText().toString().trim());
                docente.put("nivel_educativo", spinnerNivelEducativo.getSelectedItem().toString());
                
                Log.d("EditarPerfil", "Docente actualizado: " + docente.toString());
            }
            
            // Guardar ambos objetos actualizados en una sola llamada
            sessionManager.saveSession(sessionManager.getToken(), sessionManager.getRole(), user, docente);
            
            Log.d("EditarPerfil", "Sesión actualizada con cambios locales");
            
        } catch (Exception e) {
            Log.e("EditarPerfil", "Error actualizando sesión: " + e.getMessage());
        }
    }

    /**
     * Verificar si hay una sesión válida de docente
     */
    private boolean verificarSesion() {
        Log.d("EditarPerfil", "Verificando sesión...");
        Log.d("EditarPerfil", "isLoggedIn: " + sessionManager.isLoggedIn());
        Log.d("EditarPerfil", "getToken: " + (sessionManager.getToken() != null ? "EXISTE" : "NULL"));
        Log.d("EditarPerfil", "getRole: " + sessionManager.getRole());
        
        if (!sessionManager.isLoggedIn()) {
            Log.e("EditarPerfil", "Usuario no logueado");
            Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            irAlLogin();
            return false;
        }

        if (!sessionManager.isDocente()) {
            Log.e("EditarPerfil", "Usuario no es docente, rol: " + sessionManager.getRole());
            Toast.makeText(this, "Acceso denegado. Esta área es solo para docentes.", Toast.LENGTH_LONG).show();
            irAlLogin();
            return false;
        }

        Log.d("EditarPerfil", "Sesión válida");
        return true;
    }

    /**
     * Redirigir al login y limpiar sesión
     */
    private void irAlLogin() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
