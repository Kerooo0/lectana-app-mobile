package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CrearNuevaAulaActivity extends AppCompatActivity {

    private EditText campoNombreAula;
    private EditText campoGrado;
    
    private TextView textoVistaPreviaNombre;
    private TextView textoVistaPreviaGrado;
    
    private Button botonCrearAula;
    private Button botonCancelar;
    private ImageView botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_nueva_aula);
        
        inicializarVistas();
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
                navegarASeleccionarCuentos();
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

    private void navegarASeleccionarCuentos() {
        // Crear Intent para pasar datos a la siguiente pantalla
        Intent intent = new Intent(this, SeleccionarCuentosActivity.class);
        
        // Pasar solo los datos necesarios
        intent.putExtra("nombre_aula", campoNombreAula.getText().toString().trim());
        intent.putExtra("grado", campoGrado.getText().toString().trim());
        
        startActivity(intent);
        finish();
    }
}
