package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

public class ConfiguracionAulaActivity extends AppCompatActivity {

    private ImageView botonVolverConfiguracion;
    private ImageView botonGuardarConfiguracion;
    private TextView textoNombreActual;
    private TextView textoGradoActual;
    private EditText campoNombreAulaConfigurar;
    private EditText campoGradoAulaConfigurar;
    private TextView textoVistaPreviaNombre;
    private TextView textoVistaPreviaGrado;
    private Button botonCancelarConfiguracion;
    private Button botonGuardarCambios;

    private String nombreAulaOriginal;
    private String gradoAulaOriginal;
    private String codigoAula;
    private int aulaId;
    
    // Repositorio y sesión
    private AulasRepository aulasRepository;
    private SessionManager sessionManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_aula);

        recibirDatosAula();
        inicializarVistas();
        inicializarRepositorio();
        configurarListeners();
        cargarDatosActuales();
    }

    private void recibirDatosAula() {
        Intent intent = getIntent();
        if (intent != null) {
            nombreAulaOriginal = intent.getStringExtra("nombre_aula");
            gradoAulaOriginal = intent.getStringExtra("grado_aula");
            codigoAula = intent.getStringExtra("codigo_aula");
            aulaId = intent.getIntExtra("aula_id", -1);
        }
    }

    private void inicializarVistas() {
        botonVolverConfiguracion = findViewById(R.id.boton_volver_configuracion);
        botonGuardarConfiguracion = findViewById(R.id.boton_guardar_configuracion);
        textoNombreActual = findViewById(R.id.texto_nombre_actual);
        textoGradoActual = findViewById(R.id.texto_grado_actual);
        campoNombreAulaConfigurar = findViewById(R.id.campo_nombre_aula_configurar);
        campoGradoAulaConfigurar = findViewById(R.id.campo_grado_aula_configurar);
        textoVistaPreviaNombre = findViewById(R.id.texto_vista_previa_nombre);
        textoVistaPreviaGrado = findViewById(R.id.texto_vista_previa_grado);
        botonCancelarConfiguracion = findViewById(R.id.boton_cancelar_configuracion);
        botonGuardarCambios = findViewById(R.id.boton_guardar_cambios);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void inicializarRepositorio() {
        sessionManager = new SessionManager(this);
        aulasRepository = new AulasRepository(sessionManager);
    }

    private void configurarListeners() {
        botonVolverConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonGuardarConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        botonCancelarConfiguracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });

        // Atajo desde tuerca: gestionar cuentos
        botonGuardarConfiguracion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(ConfiguracionAulaActivity.this, GestionarCuentosAulaActivity.class);
                intent.putExtra("aula_id", aulaId);
                intent.putExtra("nombre_aula", nombreAulaOriginal);
                startActivity(intent);
                return true;
            }
        });

        // TextWatchers para actualizar la vista previa en tiempo real
        campoNombreAulaConfigurar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarVistaPrevia();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        campoGradoAulaConfigurar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarVistaPrevia();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void cargarDatosActuales() {
        if (nombreAulaOriginal != null) {
            textoNombreActual.setText(nombreAulaOriginal);
            campoNombreAulaConfigurar.setText(nombreAulaOriginal);
            textoVistaPreviaNombre.setText(nombreAulaOriginal);
        }

        if (gradoAulaOriginal != null) {
            textoGradoActual.setText(gradoAulaOriginal);
            campoGradoAulaConfigurar.setText(gradoAulaOriginal);
            textoVistaPreviaGrado.setText("Grado: " + gradoAulaOriginal);
        }
    }

    private void actualizarVistaPrevia() {
        String nuevoNombre = campoNombreAulaConfigurar.getText().toString().trim();
        String nuevoGrado = campoGradoAulaConfigurar.getText().toString().trim();

        if (!nuevoNombre.isEmpty()) {
            textoVistaPreviaNombre.setText(nuevoNombre);
        } else {
            textoVistaPreviaNombre.setText(nombreAulaOriginal);
        }

        if (!nuevoGrado.isEmpty()) {
            textoVistaPreviaGrado.setText("Grado: " + nuevoGrado);
        } else {
            textoVistaPreviaGrado.setText("Grado: " + gradoAulaOriginal);
        }
    }

    private void guardarCambios() {
        String nuevoNombre = campoNombreAulaConfigurar.getText().toString().trim();
        String nuevoGrado = campoGradoAulaConfigurar.getText().toString().trim();

        // Validaciones
        if (nuevoNombre.isEmpty()) {
            Toast.makeText(this, "El nombre del aula no puede estar vacío", 
                Toast.LENGTH_SHORT).show();
            return;
        }

        if (nuevoGrado.isEmpty()) {
            Toast.makeText(this, "El grado no puede estar vacío", 
                Toast.LENGTH_SHORT).show();
            return;
        }

        if (aulaId == -1) {
            Toast.makeText(this, "Error: ID de aula no válido", 
                Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar loading
        mostrarCargando(true);

        // Actualizar aula en el backend
        aulasRepository.actualizarAula(aulaId, nuevoNombre, nuevoGrado, new AulasRepository.AulasCallback<ModeloAula>() {
            @Override
            public void onSuccess(ModeloAula aulaActualizada) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(ConfiguracionAulaActivity.this, 
                        "¡Cambios guardados exitosamente!", 
                        Toast.LENGTH_LONG).show();

                    // Regresar a la pantalla anterior con los datos actualizados
                    Intent intent = new Intent();
                    intent.putExtra("nombre_aula_actualizado", nuevoNombre);
                    intent.putExtra("grado_aula_actualizado", nuevoGrado);
                    setResult(RESULT_OK, intent);
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(ConfiguracionAulaActivity.this, 
                        "Error al guardar cambios: " + message, 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        
        // Deshabilitar botones mientras carga
        botonGuardarCambios.setEnabled(!mostrar);
        botonGuardarConfiguracion.setEnabled(!mostrar);
        campoNombreAulaConfigurar.setEnabled(!mostrar);
        campoGradoAulaConfigurar.setEnabled(!mostrar);
    }
}
