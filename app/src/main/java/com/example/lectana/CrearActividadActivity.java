package com.example.lectana;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CrearActividadActivity extends AppCompatActivity {

    private Button botonOpcionMultiple;
    private Button botonRespuestaAbierta;
    private LinearLayout contenedorOpcionesRespuesta;
    private EditText campoPregunta;
    private EditText campoOpcionA, campoOpcionB, campoOpcionC;
    private CheckBox checkboxOpcionA, checkboxOpcionB, checkboxOpcionC;
    private boolean esOpcionMultiple = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actividad);

        inicializarVistas();
        configurarListeners();
        mostrarOpcionesRespuesta(esOpcionMultiple);
    }

    private void inicializarVistas() {
        botonOpcionMultiple = findViewById(R.id.boton_opcion_multiple);
        botonRespuestaAbierta = findViewById(R.id.boton_respuesta_abierta);
        contenedorOpcionesRespuesta = findViewById(R.id.contenedor_opciones_respuesta);
        campoPregunta = findViewById(R.id.campo_pregunta);
        campoOpcionA = findViewById(R.id.campo_opcion_a);
        campoOpcionB = findViewById(R.id.campo_opcion_b);
        campoOpcionC = findViewById(R.id.campo_opcion_c);
        checkboxOpcionA = findViewById(R.id.checkbox_opcion_a);
        checkboxOpcionB = findViewById(R.id.checkbox_opcion_b);
        checkboxOpcionC = findViewById(R.id.checkbox_opcion_c);
    }

    private void configurarListeners() {
        // Botón volver
        findViewById(R.id.boton_volver).setOnClickListener(v -> finish());

        // Botones de tipo de actividad
        botonOpcionMultiple.setOnClickListener(v -> {
            esOpcionMultiple = true;
            actualizarBotonesTipo();
            mostrarOpcionesRespuesta(true);
        });

        botonRespuestaAbierta.setOnClickListener(v -> {
            esOpcionMultiple = false;
            actualizarBotonesTipo();
            mostrarOpcionesRespuesta(false);
        });

        // Botón crear actividad
        findViewById(R.id.boton_crear_actividad).setOnClickListener(v -> crearActividad());

        // Botón vista previa
        findViewById(R.id.boton_vista_previa).setOnClickListener(v -> mostrarVistaPrevia());
    }

    private void actualizarBotonesTipo() {
        if (esOpcionMultiple) {
            botonOpcionMultiple.setBackgroundResource(R.drawable.boton_tipo_actividad_seleccionado);
            botonOpcionMultiple.setTextColor(getResources().getColor(R.color.naranjaPastel));
            botonRespuestaAbierta.setBackgroundResource(R.drawable.boton_tipo_actividad_no_seleccionado);
            botonRespuestaAbierta.setTextColor(getResources().getColor(R.color.gris_medio));
        } else {
            botonRespuestaAbierta.setBackgroundResource(R.drawable.boton_tipo_actividad_seleccionado);
            botonRespuestaAbierta.setTextColor(getResources().getColor(R.color.naranjaPastel));
            botonOpcionMultiple.setBackgroundResource(R.drawable.boton_tipo_actividad_no_seleccionado);
            botonOpcionMultiple.setTextColor(getResources().getColor(R.color.gris_medio));
        }
    }

    private void mostrarOpcionesRespuesta(boolean mostrar) {
        contenedorOpcionesRespuesta.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    private void crearActividad() {
        // Implementar lógica para crear actividad
        // Validar campos, guardar en base de datos, etc.
    }

    private void mostrarVistaPrevia() {
        // Implementar vista previa de la actividad
    }
}
