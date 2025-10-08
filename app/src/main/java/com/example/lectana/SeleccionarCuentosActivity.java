package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorCuentosDisponibles;
import com.example.lectana.modelos.ModeloCuento;

import java.util.ArrayList;
import java.util.List;

public class SeleccionarCuentosActivity extends AppCompatActivity implements AdaptadorCuentosDisponibles.OnCuentoSeleccionadoListener {

    private TextView textoNombreAulaConfigurar;
    private TextView textoGradoSeccionConfigurar;
    private TextView textoCuentosSeleccionados;
    private RecyclerView recyclerCuentosDisponibles;
    private Button botonFinalizarAula;
    private Button botonCancelar;
    private ImageView botonVolver;

    private AdaptadorCuentosDisponibles adaptadorCuentos;
    private List<ModeloCuento> listaCuentos;
    private int cuentosSeleccionados = 0;

    // Datos del aula recibidos del Paso 1
    private String nombreAula;
    private String grado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_cuentos);
        
        recibirDatosAula();
        inicializarVistas();
        configurarRecyclerView();
        cargarCuentosDisponibles();
        configurarListeners();
        actualizarInformacionAula();
    }

    private void recibirDatosAula() {
        Intent intent = getIntent();
        nombreAula = intent.getStringExtra("nombre_aula");
        grado = intent.getStringExtra("grado");
    }

    private void inicializarVistas() {
        textoNombreAulaConfigurar = findViewById(R.id.texto_nombre_aula_configurar);
        textoGradoSeccionConfigurar = findViewById(R.id.texto_grado_seccion_configurar);
        textoCuentosSeleccionados = findViewById(R.id.texto_cuentos_seleccionados);
        recyclerCuentosDisponibles = findViewById(R.id.recycler_cuentos_disponibles);
        botonFinalizarAula = findViewById(R.id.boton_finalizar_aula);
        botonCancelar = findViewById(R.id.boton_cancelar);
        botonVolver = findViewById(R.id.boton_volver);
    }

    private void configurarRecyclerView() {
        listaCuentos = new ArrayList<>();
        adaptadorCuentos = new AdaptadorCuentosDisponibles(listaCuentos, this);
        
        recyclerCuentosDisponibles.setLayoutManager(new LinearLayoutManager(this));
        recyclerCuentosDisponibles.setAdapter(adaptadorCuentos);
    }

    private void cargarCuentosDisponibles() {
        // Datos de ejemplo - aquí se conectaría con la API
        listaCuentos.add(new ModeloCuento(1, "El Principito", "Antoine de Saint-Exupéry", "Primaria", "Un clásico de la literatura infantil"));
        listaCuentos.add(new ModeloCuento(2, "Caperucita Roja", "Charles Perrault", "Preescolar", "La historia de la niña y el lobo"));
        listaCuentos.add(new ModeloCuento(3, "Los Tres Cerditos", "Anónimo", "Preescolar", "La fábula de los tres hermanos"));
        listaCuentos.add(new ModeloCuento(4, "Alicia en el País de las Maravillas", "Lewis Carroll", "Primaria", "Las aventuras de Alicia"));
        listaCuentos.add(new ModeloCuento(5, "El Patito Feo", "Hans Christian Andersen", "Preescolar", "La historia del patito diferente"));
        listaCuentos.add(new ModeloCuento(6, "Pinocho", "Carlo Collodi", "Primaria", "Las aventuras del muñeco de madera"));
        listaCuentos.add(new ModeloCuento(7, "La Bella y la Bestia", "Gabrielle-Suzanne Barbot", "Primaria", "Una historia de amor y transformación"));
        listaCuentos.add(new ModeloCuento(8, "Blancanieves", "Hermanos Grimm", "Preescolar", "La historia de la princesa y los siete enanitos"));
        
        adaptadorCuentos.notifyDataSetChanged();
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

        // Botón Finalizar Aula
        botonFinalizarAula.setOnClickListener(v -> {
            if (cuentosSeleccionados > 0) {
                finalizarCreacionAula();
            } else {
                // Mostrar mensaje de que debe seleccionar al menos un cuento
                textoCuentosSeleccionados.setText("Debe seleccionar al menos un cuento");
                textoCuentosSeleccionados.setTextColor(getResources().getColor(R.color.rojo));
            }
        });
    }

    private void actualizarInformacionAula() {
        textoNombreAulaConfigurar.setText(nombreAula);
        textoGradoSeccionConfigurar.setText("Grado " + grado);
        actualizarContadorCuentos();
    }

    private void actualizarContadorCuentos() {
        textoCuentosSeleccionados.setText("Cuentos Seleccionados: " + cuentosSeleccionados);
        textoCuentosSeleccionados.setTextColor(getResources().getColor(R.color.azul_semi_oscuro));
    }

    @Override
    public void onCuentoSeleccionado(ModeloCuento cuento, boolean seleccionado) {
        if (seleccionado) {
            cuentosSeleccionados++;
        } else {
            cuentosSeleccionados--;
        }
        actualizarContadorCuentos();
    }

    private void finalizarCreacionAula() {
        // Aquí se haría la llamada a la API para crear el aula con los cuentos seleccionados
        // Por ahora, solo navegamos de vuelta al Panel Docente
        
        Intent intent = new Intent(this, PantallaPrincipalDocente.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
