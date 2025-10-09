package com.example.lectana.estudiante.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lectana.R;

public class TiendaFragment extends Fragment {

    private TextView puntosUsuario;
    private LinearLayout contenedorRecompensas;
    
    // Tabs de categorías
    private LinearLayout tabTodos;
    private LinearLayout tabAvatares;
    private LinearLayout tabInsignias;
    private LinearLayout tabMisAvatares;
    
    private String categoriaActual = "todos";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tienda_simple, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        inicializarVistas(view);
        configurarListeners();
        cargarDatosEjemplo();
        actualizarEstadoTabs("todos");
    }

    private void inicializarVistas(View view) {
        puntosUsuario = view.findViewById(R.id.puntos_usuario_tienda);
        contenedorRecompensas = view.findViewById(R.id.contenedor_recompensas);
        
        // Tabs
        tabTodos = view.findViewById(R.id.tab_todos);
        tabAvatares = view.findViewById(R.id.tab_avatares);
        tabInsignias = view.findViewById(R.id.tab_insignias);
        tabMisAvatares = view.findViewById(R.id.tab_mis_avatares);
        
        // Botón información
        Button botonInfoPuntos = view.findViewById(R.id.boton_info_puntos);
        if (botonInfoPuntos != null) {
            botonInfoPuntos.setOnClickListener(v -> mostrarInfoPuntos());
        }
    }

    private void configurarListeners() {
        if (tabTodos != null) {
            tabTodos.setOnClickListener(v -> {
                filtrarPorCategoria("todos");
                actualizarEstadoTabs("todos");
            });
        }
        
        if (tabAvatares != null) {
            tabAvatares.setOnClickListener(v -> {
                filtrarPorCategoria("avatares");
                actualizarEstadoTabs("avatares");
            });
        }
        
        if (tabInsignias != null) {
            tabInsignias.setOnClickListener(v -> {
                filtrarPorCategoria("insignias");
                actualizarEstadoTabs("insignias");
            });
        }
        
        if (tabMisAvatares != null) {
            tabMisAvatares.setOnClickListener(v -> {
                filtrarPorCategoria("mis_avatares");
                actualizarEstadoTabs("mis_avatares");
            });
        }
    }

    private void cargarDatosEjemplo() {
        if (contenedorRecompensas != null) {
            contenedorRecompensas.removeAllViews();
            
            // Crear elementos de ejemplo
            crearItemRecompensa("Astronauta", "avatares", 50, "Equipar Avatar");
            crearItemRecompensa("Explorador", "avatares", 70, "Equipar Avatar");
            crearItemRecompensa("Mago", "avatares", 60, "Equipar Avatar");
            crearItemRecompensa("Pirata", "avatares", 80, "Equipar Avatar");
            
            crearItemRecompensa("Súper Lector", "insignias", 30, "Canjear");
            crearItemRecompensa("Mago de Cuentos", "insignias", 40, "Canjear");
            crearItemRecompensa("Coleccionista", "insignias", 35, "Canjear");
            crearItemRecompensa("Velocidad", "insignias", 45, "Canjear");
            
            crearItemRecompensa("Astronauta", "mis_avatares", 0, "Equipado");
            crearItemRecompensa("Explorador", "mis_avatares", 0, "Equipado");
        }
        
        if (puntosUsuario != null) {
            puntosUsuario.setText("120");
        }
    }
    
    private void crearItemRecompensa(String nombre, String categoria, int costo, String accion) {
        if (contenedorRecompensas == null) return;
        
        // Crear LinearLayout para el item
        LinearLayout itemLayout = new LinearLayout(getContext());
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setPadding(16, 16, 16, 16);
        itemLayout.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        itemLayout.setLayoutParams(params);
        
        // Texto del nombre
        TextView nombreText = new TextView(getContext());
        nombreText.setText(nombre);
        nombreText.setTextSize(16);
        nombreText.setTextColor(getResources().getColor(R.color.gris_oscuro));
        nombreText.setPadding(0, 0, 0, 8);
        
        // Texto del costo
        TextView costoText = new TextView(getContext());
        costoText.setText("Costo: " + costo + " puntos");
        costoText.setTextSize(14);
        costoText.setTextColor(getResources().getColor(R.color.gris_medio));
        costoText.setPadding(0, 0, 0, 8);
        
        // Botón de acción
        Button botonAccion = new Button(getContext());
        botonAccion.setText(accion);
        botonAccion.setTextSize(12);
        botonAccion.setBackgroundResource(R.drawable.boton_verde_rectangular);
        botonAccion.setTextColor(getResources().getColor(android.R.color.white));
        
        botonAccion.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Click en " + nombre, Toast.LENGTH_SHORT).show();
        });
        
        // Agregar elementos al layout
        itemLayout.addView(nombreText);
        itemLayout.addView(costoText);
        itemLayout.addView(botonAccion);
        
        // Agregar al contenedor
        contenedorRecompensas.addView(itemLayout);
    }

    private void filtrarPorCategoria(String categoria) {
        categoriaActual = categoria;
        cargarDatosEjemplo(); // Recargar datos
    }

    private void actualizarEstadoTabs(String tabSeleccionado) {
        // Resetear todos los tabs
        resetearTabs();
        
        // Activar el tab seleccionado
        switch (tabSeleccionado) {
            case "todos":
                if (tabTodos != null) {
                    tabTodos.setBackgroundColor(getResources().getColor(R.color.azul_claro));
                }
                break;
            case "avatares":
                if (tabAvatares != null) {
                    tabAvatares.setBackgroundColor(getResources().getColor(R.color.azul_claro));
                }
                break;
            case "insignias":
                if (tabInsignias != null) {
                    tabInsignias.setBackgroundColor(getResources().getColor(R.color.azul_claro));
                }
                break;
            case "mis_avatares":
                if (tabMisAvatares != null) {
                    tabMisAvatares.setBackgroundColor(getResources().getColor(R.color.azul_claro));
                }
                break;
        }
    }
    
    private void resetearTabs() {
        if (tabTodos != null) {
            tabTodos.setBackgroundColor(getResources().getColor(R.color.fondo_claro));
        }
        if (tabAvatares != null) {
            tabAvatares.setBackgroundColor(getResources().getColor(R.color.fondo_claro));
        }
        if (tabInsignias != null) {
            tabInsignias.setBackgroundColor(getResources().getColor(R.color.fondo_claro));
        }
        if (tabMisAvatares != null) {
            tabMisAvatares.setBackgroundColor(getResources().getColor(R.color.fondo_claro));
        }
    }

    private void mostrarInfoPuntos() {
        Toast.makeText(getContext(), 
            "Los puntos te permiten:\n" +
            "• Desbloquear avatares\n" +
            "• Conseguir insignias\n" +
            "• Personalizar tu perfil\n" +
            "• Mostrar tus logros",
            Toast.LENGTH_LONG).show();
    }
}