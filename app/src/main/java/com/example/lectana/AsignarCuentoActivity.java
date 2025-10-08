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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorCuentosDisponibles;
import com.example.lectana.modelos.ModeloCuento;

import java.util.ArrayList;
import java.util.List;

public class AsignarCuentoActivity extends AppCompatActivity implements AdaptadorCuentosDisponibles.OnCuentoSeleccionadoListener {

    private EditText barraBusquedaCuentos;
    private Button botonFiltroTodos, botonFiltroClasicos, botonFiltroAventura, botonFiltroEdad;
    private RecyclerView recyclerViewCuentosAsignar;
    private Button botonConfirmarAsignaciones;
    private TextView textoNombreAulaAsignarCuento;

    private AdaptadorCuentosDisponibles adaptadorCuentos;
    private List<ModeloCuento> listaCuentosCompleta;
    private List<ModeloCuento> listaCuentosFiltrada;
    private String filtroActual = "todos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_cuento);

        inicializarVistas();
        configurarListeners();
        cargarDatosEjemplo();
        configurarRecyclerView();
        aplicarFiltro("todos");
    }

    private void inicializarVistas() {
        barraBusquedaCuentos = findViewById(R.id.campo_busqueda_cuentos);
        botonFiltroTodos = findViewById(R.id.filtro_todos);
        botonFiltroClasicos = findViewById(R.id.filtro_clasicos);
        botonFiltroAventura = findViewById(R.id.filtro_aventura);
        botonFiltroEdad = findViewById(R.id.filtro_edad);
        recyclerViewCuentosAsignar = findViewById(R.id.recycler_view_cuentos_disponibles);
        botonConfirmarAsignaciones = findViewById(R.id.boton_confirmar_asignaciones);
        textoNombreAulaAsignarCuento = findViewById(R.id.texto_nombre_aula_asignar);
    }

    private void configurarListeners() {
        findViewById(R.id.boton_volver).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Filtros
        botonFiltroTodos.setOnClickListener(v -> aplicarFiltro("todos"));
        botonFiltroClasicos.setOnClickListener(v -> aplicarFiltro("clasicos"));
        botonFiltroAventura.setOnClickListener(v -> aplicarFiltro("aventura"));
        botonFiltroEdad.setOnClickListener(v -> aplicarFiltro("edad"));

        // Búsqueda
        barraBusquedaCuentos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarPorTexto(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Confirmar asignaciones
        botonConfirmarAsignaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar que hay cuentos seleccionados
                List<ModeloCuento> cuentosSeleccionados = new ArrayList<>();
                for (ModeloCuento cuento : listaCuentosCompleta) {
                    if (cuento.isSeleccionado()) {
                        cuentosSeleccionados.add(cuento);
                    }
                }
                
                if (cuentosSeleccionados.isEmpty()) {
                    android.widget.Toast.makeText(AsignarCuentoActivity.this, 
                        "Por favor selecciona al menos un cuento", 
                        android.widget.Toast.LENGTH_SHORT).show();
                    return;
                }
                
                // Navegar a seleccionar aula
                Intent intent = new Intent(AsignarCuentoActivity.this, SeleccionarAulaActivity.class);
                // TODO: Pasar los cuentos seleccionados a la siguiente actividad
                startActivity(intent);
            }
        });
    }

    private void cargarDatosEjemplo() {
        listaCuentosCompleta = new ArrayList<>();
        
        // Cuentos clásicos
        listaCuentosCompleta.add(new ModeloCuento(1, "Caperucita Roja", "Hermanos Grimm", "Clásico", 
            "4-6", "4.5★", "", "15 min", "Un cuento clásico sobre una niña que lleva comida a su abuela enferma."));
        listaCuentosCompleta.add(new ModeloCuento(2, "Los Tres Cerditos", "Cuento tradicional", "Clásico", 
            "3-5", "4.2★", "", "12 min", "La historia de tres cerditos que construyen casas de diferentes materiales."));
        listaCuentosCompleta.add(new ModeloCuento(3, "El Patito Feo", "Hans Christian Andersen", "Clásico", 
            "4-7", "4.7★", "", "18 min", "Un patito diferente que descubre su verdadera identidad."));
        
        // Cuentos de aventura
        listaCuentosCompleta.add(new ModeloCuento(4, "La Isla del Tesoro", "Robert Louis Stevenson", "Aventura", 
            "8-12", "4.3★", "", "45 min", "Una emocionante aventura pirata en busca de un tesoro oculto."));
        listaCuentosCompleta.add(new ModeloCuento(5, "Las Aventuras de Tom Sawyer", "Mark Twain", "Aventura", 
            "9-13", "4.4★", "", "60 min", "Las travesuras y aventuras de un niño travieso en el Mississippi."));
        
        // Cuentos para 8-12 años
        listaCuentosCompleta.add(new ModeloCuento(6, "Matilda", "Roald Dahl", "Fantasía", 
            "8-12", "4.6★", "", "35 min", "Una niña con poderes especiales que ama la lectura."));
        listaCuentosCompleta.add(new ModeloCuento(7, "Charlie y la Fábrica de Chocolate", "Roald Dahl", "Fantasía", 
            "8-12", "4.8★", "", "40 min", "Un niño pobre que gana un tour por la fábrica de chocolate más famosa."));
        
        listaCuentosFiltrada = new ArrayList<>(listaCuentosCompleta);
    }

    private void configurarRecyclerView() {
        adaptadorCuentos = new AdaptadorCuentosDisponibles(listaCuentosFiltrada, this);
        recyclerViewCuentosAsignar.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCuentosAsignar.setAdapter(adaptadorCuentos);
    }

    private void aplicarFiltro(String filtro) {
        filtroActual = filtro;
        actualizarBotonesFiltro();
        
        listaCuentosFiltrada.clear();
        
        switch (filtro) {
            case "todos":
                listaCuentosFiltrada.addAll(listaCuentosCompleta);
                break;
            case "clasicos":
                for (ModeloCuento cuento : listaCuentosCompleta) {
                    if ("Clásico".equals(cuento.getGenero())) {
                        listaCuentosFiltrada.add(cuento);
                    }
                }
                break;
            case "aventura":
                for (ModeloCuento cuento : listaCuentosCompleta) {
                    if ("Aventura".equals(cuento.getGenero())) {
                        listaCuentosFiltrada.add(cuento);
                    }
                }
                break;
            case "edad":
                for (ModeloCuento cuento : listaCuentosCompleta) {
                    if (cuento.getEdadRecomendada().contains("8-12")) {
                        listaCuentosFiltrada.add(cuento);
                    }
                }
                break;
        }
        
        adaptadorCuentos.notifyDataSetChanged();
    }

    private void actualizarBotonesFiltro() {
        // Resetear todos los botones
        botonFiltroTodos.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroTodos.setTextColor(getResources().getColor(R.color.gris_oscuro));
        botonFiltroClasicos.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroClasicos.setTextColor(getResources().getColor(R.color.gris_oscuro));
        botonFiltroAventura.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroAventura.setTextColor(getResources().getColor(R.color.gris_oscuro));
        botonFiltroEdad.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        botonFiltroEdad.setTextColor(getResources().getColor(R.color.gris_oscuro));

        // Aplicar estilo al botón activo
        switch (filtroActual) {
            case "todos":
                botonFiltroTodos.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroTodos.setTextColor(getResources().getColor(R.color.white));
                break;
            case "clasicos":
                botonFiltroClasicos.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroClasicos.setTextColor(getResources().getColor(R.color.white));
                break;
            case "aventura":
                botonFiltroAventura.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroAventura.setTextColor(getResources().getColor(R.color.white));
                break;
            case "edad":
                botonFiltroEdad.setBackgroundResource(R.drawable.boton_verde);
                botonFiltroEdad.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }

    private void filtrarPorTexto(String texto) {
        listaCuentosFiltrada.clear();
        
        for (ModeloCuento cuento : listaCuentosCompleta) {
            if (cuento.getTitulo().toLowerCase().contains(texto.toLowerCase()) ||
                cuento.getAutor().toLowerCase().contains(texto.toLowerCase()) ||
                cuento.getGenero().toLowerCase().contains(texto.toLowerCase())) {
                listaCuentosFiltrada.add(cuento);
            }
        }
        
        adaptadorCuentos.notifyDataSetChanged();
    }

    @Override
    public void onCuentoSeleccionado(ModeloCuento cuento, boolean seleccionado) {
        // Actualizar contador de cuentos seleccionados
        int cuentosSeleccionados = 0;
        for (ModeloCuento c : listaCuentosCompleta) {
            if (c.isSeleccionado()) {
                cuentosSeleccionados++;
            }
        }
        
        // Actualizar texto del botón
        if (cuentosSeleccionados > 0) {
            botonConfirmarAsignaciones.setText("Confirmar Asignaciones (" + cuentosSeleccionados + ")");
        } else {
            botonConfirmarAsignaciones.setText("Confirmar Asignaciones");
        }
    }
}
