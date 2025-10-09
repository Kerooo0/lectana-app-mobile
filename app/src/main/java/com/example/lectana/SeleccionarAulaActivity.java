package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorAulasSeleccionar;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.modelos.ModeloCuento;

import java.util.ArrayList;
import java.util.List;

public class SeleccionarAulaActivity extends AppCompatActivity implements AdaptadorAulasSeleccionar.OnAulaSeleccionadaListener {

    private TextView textoCuentosSeleccionados;
    private LinearLayout contenedorCuentosSeleccionados;
    private RecyclerView recyclerViewAulasDisponibles;
    private Button botonAsignarCuentosAula;

    private AdaptadorAulasSeleccionar adaptadorAulas;
    private List<ModeloAula> listaAulas;
    private List<ModeloCuento> cuentosSeleccionados;
    private ModeloAula aulaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_aula);

        inicializarVistas();
        recibirCuentosSeleccionados();
        configurarListeners();
        cargarAulasDisponibles();
        configurarRecyclerView();
        mostrarCuentosSeleccionados();
    }

    private void inicializarVistas() {
        textoCuentosSeleccionados = findViewById(R.id.texto_cuentos_seleccionados);
        contenedorCuentosSeleccionados = findViewById(R.id.contenedor_cuentos_seleccionados);
        recyclerViewAulasDisponibles = findViewById(R.id.recycler_view_aulas_disponibles);
        botonAsignarCuentosAula = findViewById(R.id.boton_asignar_cuentos_aula);
    }

    private void recibirCuentosSeleccionados() {
        // Recibir los cuentos seleccionados desde la actividad anterior
        Intent intent = getIntent();
        if (intent != null) {
            cuentosSeleccionados = new ArrayList<>();
            
            // Por ahora, simular cuentos seleccionados
            // En una implementación real, se recibirían desde la actividad anterior
            cuentosSeleccionados.add(new ModeloCuento(1, "Caperucita Roja", "Hermanos Grimm", "Clásico", 
                "4-6", "4.5★", "", "15 min", "Un cuento clásico sobre una niña que lleva comida a su abuela enferma."));
            cuentosSeleccionados.add(new ModeloCuento(2, "Los Tres Cerditos", "Cuento tradicional", "Clásico", 
                "3-5", "4.2★", "", "12 min", "La historia de tres cerditos que construyen casas de diferentes materiales."));
        }
    }

    private void configurarListeners() {
        findViewById(R.id.boton_volver_seleccionar_aula).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonAsignarCuentosAula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aulaSeleccionada != null) {
                    asignarCuentosAAula();
                } else {
                    android.widget.Toast.makeText(SeleccionarAulaActivity.this, 
                        "Por favor selecciona un aula", 
                        android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cargarAulasDisponibles() {
        listaAulas = new ArrayList<>();
        
        // Datos de ejemplo - aquí se conectaría con la API
        listaAulas.add(new ModeloAula("1", "3°A - Lengua y Literatura", "24", "ABC123", "5"));
        listaAulas.add(new ModeloAula("2", "3°B - Lengua y Literatura", "22", "DEF456", "3"));
        listaAulas.add(new ModeloAula("3", "4°A - Literatura", "26", "GHI789", "7"));
        listaAulas.add(new ModeloAula("4", "4°B - Literatura", "20", "JKL012", "4"));
        listaAulas.add(new ModeloAula("5", "5°A - Lengua", "28", "MNO345", "6"));
    }

    private void configurarRecyclerView() {
        adaptadorAulas = new AdaptadorAulasSeleccionar(listaAulas, this);
        recyclerViewAulasDisponibles.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAulasDisponibles.setAdapter(adaptadorAulas);
    }

    private void mostrarCuentosSeleccionados() {
        if (cuentosSeleccionados != null && !cuentosSeleccionados.isEmpty()) {
            textoCuentosSeleccionados.setText(cuentosSeleccionados.size() + " cuentos seleccionados");
            
            // Limpiar contenedor
            contenedorCuentosSeleccionados.removeAllViews();
            
            // Agregar cada cuento seleccionado
            for (ModeloCuento cuento : cuentosSeleccionados) {
                TextView textViewCuento = new TextView(this);
                textViewCuento.setText("• " + cuento.getTitulo());
                textViewCuento.setTextColor(getResources().getColor(R.color.gris_medio));
                textViewCuento.setTextSize(14);
                textViewCuento.setPadding(0, 0, 0, 4);
                contenedorCuentosSeleccionados.addView(textViewCuento);
            }
        }
    }

    @Override
    public void onAulaSeleccionada(ModeloAula aula) {
        aulaSeleccionada = aula;
        
        // Actualizar el texto del botón
        botonAsignarCuentosAula.setText("Asignar Cuentos a " + aula.getNombre_aula());
    }

    private void asignarCuentosAAula() {
        if (aulaSeleccionada != null && cuentosSeleccionados != null && !cuentosSeleccionados.isEmpty()) {
            // TODO: Implementar la lógica para asignar cuentos al aula
            // Aquí se haría la llamada a la API para guardar la asignación
            
            android.widget.Toast.makeText(this, 
                "¡Cuentos asignados exitosamente a " + aulaSeleccionada.getNombre_aula() + "!", 
                android.widget.Toast.LENGTH_LONG).show();
            
            // Regresar al panel principal
            Intent intent = new Intent(this, com.example.lectana.docente.PantallaPrincipalDocente.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
