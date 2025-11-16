package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.repository.AulasRepository;

public class CrearNuevaAulaActivity extends AppCompatActivity {

    private EditText campoNombreAula;
    private EditText campoGrado;
    
    private TextView textoVistaPreviaNombre;
    private TextView textoVistaPreviaGrado;
    
    private Button botonCrearAula;
    private Button botonCancelar;
    private ImageView botonVolver;
    private ProgressBar progressBar;

    private AulasRepository aulasRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nueva_aula);
        
        inicializarVistas();
        inicializarRepositorio();
        configurarListeners();
        actualizarVistaPrevia();
    }

    private void inicializarVistas() {
        campoNombreAula = findViewById(R.id.campo_nombre_aula);
        campoGrado = findViewById(R.id.campo_grado);
        
        textoVistaPreviaNombre = findViewById(R.id.texto_vista_previa_nombre);
        textoVistaPreviaGrado = findViewById(R.id.texto_vista_previa_grado);
        
        botonCrearAula = findViewById(R.id.boton_crear_aula);
        botonCancelar = findViewById(R.id.boton_cancelar);
        botonVolver = findViewById(R.id.boton_volver);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void inicializarRepositorio() {
        sessionManager = new SessionManager(this);
        aulasRepository = new AulasRepository(sessionManager);
    }

    private void configurarListeners() {
        // Botón Volver
        botonVolver.setOnClickListener(v -> {
            finish();
        });

        // Botón Cancelar
        botonCancelar.setOnClickListener(v -> {
            finish();
        });

        // Botón Crear Aula
        botonCrearAula.setOnClickListener(v -> {
            if (validarCampos()) {
                crearAula();
            }
        });

        // Listeners para actualizar vista previa
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarVistaPrevia();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        campoNombreAula.addTextChangedListener(textWatcher);
        campoGrado.addTextChangedListener(textWatcher);
    }

    private void actualizarVistaPrevia() {
        String nombreAula = campoNombreAula.getText().toString().trim();
        String grado = campoGrado.getText().toString().trim();

        // Actualizar nombre en vista previa
        if (!nombreAula.isEmpty()) {
            textoVistaPreviaNombre.setText(nombreAula);
        } else {
            textoVistaPreviaNombre.setText("3°A - Lengua");
        }

        // Actualizar grado en vista previa
        if (!grado.isEmpty()) {
            textoVistaPreviaGrado.setText("Grado " + grado);
        } else {
            textoVistaPreviaGrado.setText("Grado 3°");
        }
    }

    private boolean validarCampos() {
        boolean esValido = true;

        // Validar Nombre del Aula (obligatorio)
        if (campoNombreAula.getText().toString().trim().isEmpty()) {
            campoNombreAula.setError("El nombre del aula es obligatorio");
            esValido = false;
        }

        // Validar Grado (obligatorio)
        if (campoGrado.getText().toString().trim().isEmpty()) {
            campoGrado.setError("El grado es obligatorio");
            esValido = false;
        }

        return esValido;
    }

    private void crearAula() {
        String nombreAula = campoNombreAula.getText().toString().trim();
        String grado = campoGrado.getText().toString().trim();

        // Verificar sesión y rol antes de crear aula
        if (!verificarSesionYRole()) {
            return;
        }

        mostrarCargando(true);

        aulasRepository.crearAula(nombreAula, grado, new AulasRepository.AulasCallback<ModeloAula>() {
            @Override
            public void onSuccess(ModeloAula aula) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(CrearNuevaAulaActivity.this, "¡Aula creada exitosamente!", Toast.LENGTH_SHORT).show();
                    
                    // Navegar a seleccionar cuentos con los datos del aula creada
                    navegarASeleccionarCuentos(aula);
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(CrearNuevaAulaActivity.this, "Error al crear aula: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private boolean verificarSesionYRole() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        String role = sessionManager.getRole();
        String token = sessionManager.getToken();
        
        android.util.Log.d("CrearNuevaAula", "Rol actual: " + role);
        android.util.Log.d("CrearNuevaAula", "Token presente: " + (token != null ? "Sí" : "No"));
        
        if (!"docente".equals(role)) {
            Toast.makeText(this, "Acceso denegado. Solo los docentes pueden crear aulas.", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }

        return true;
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        botonCrearAula.setEnabled(!mostrar);
        botonCancelar.setEnabled(!mostrar);
        botonVolver.setEnabled(!mostrar);
    }

    private void navegarASeleccionarCuentos(ModeloAula aula) {
        Intent intent = new Intent(this, SeleccionarCuentosActivity.class);
        intent.putExtra("aula_id", aula.getId_aula());
        intent.putExtra("nombre_aula", aula.getNombre_aula());
        intent.putExtra("grado", aula.getGrado());
        intent.putExtra("codigo_acceso", aula.getCodigo_acceso());
        startActivity(intent);
        finish();
    }
}
