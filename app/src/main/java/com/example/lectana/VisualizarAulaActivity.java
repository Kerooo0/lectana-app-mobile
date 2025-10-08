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

import com.example.lectana.adaptadores.AdaptadorActividadesPorCuento;
import com.example.lectana.adaptadores.AdaptadorCuentosDetallados;
import com.example.lectana.adaptadores.AdaptadorEstudiantesAula;
import com.example.lectana.modelos.ModeloActividadDetallada;
import com.example.lectana.modelos.ModeloCuentoConActividades;
import com.example.lectana.modelos.ModeloCuentoDetallado;
import com.example.lectana.modelos.ModeloEstudianteAula;

import java.util.ArrayList;
import java.util.List;

public class VisualizarAulaActivity extends AppCompatActivity {

    // Componentes de la Interfaz
    private ImageView botonVolver;
    private TextView textoNombreAula;
    private TextView textoCodigoAula;
    private ImageView botonConfiguracionAula;
    
    // Estadísticas
    private TextView numeroEstudiantesAula;
    private TextView numeroCuentosAula;
    private TextView numeroActividadesAula;
    
    // Pestañas
    private Button botonPestanaEstudiantes;
    private Button botonPestanaCuentos;
    private Button botonPestanaActividades;
    
    // RecyclerView
    private RecyclerView recyclerViewContenido;
    
    // Adaptadores
    private AdaptadorEstudiantesAula adaptadorEstudiantes;
    private AdaptadorCuentosDetallados adaptadorCuentosDetallados;
    private AdaptadorActividadesPorCuento adaptadorActividadesPorCuento;
    
    // Datos
    private List<ModeloEstudianteAula> listaEstudiantes;
    private List<ModeloCuentoDetallado> listaCuentosDetallados;
    private List<ModeloCuentoConActividades> listaCuentosConActividades;
    
    // Estado actual de la pestaña
    private String pestanaActual = "estudiantes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_aula);

        inicializarVistas();
        configurarListeners();
        cargarDatosEjemplo();
        mostrarPestanaEstudiantes();
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        textoNombreAula = findViewById(R.id.texto_nombre_aula);
        textoCodigoAula = findViewById(R.id.texto_codigo_aula);
        botonConfiguracionAula = findViewById(R.id.boton_configuracion_aula);
        
        numeroEstudiantesAula = findViewById(R.id.numero_estudiantes_aula);
        numeroCuentosAula = findViewById(R.id.numero_cuentos_aula);
        numeroActividadesAula = findViewById(R.id.numero_actividades_aula);
        
        botonPestanaEstudiantes = findViewById(R.id.boton_pestana_estudiantes);
        botonPestanaCuentos = findViewById(R.id.boton_pestana_cuentos);
        botonPestanaActividades = findViewById(R.id.boton_pestana_actividades);
        
        recyclerViewContenido = findViewById(R.id.recycler_view_contenido);
        recyclerViewContenido.setLayoutManager(new LinearLayoutManager(this));
        
        // Inicializar listas
        listaEstudiantes = new ArrayList<>();
        listaCuentosDetallados = new ArrayList<>();
        listaCuentosConActividades = new ArrayList<>();
        
        // Inicializar adaptadores
        adaptadorEstudiantes = new AdaptadorEstudiantesAula(listaEstudiantes);
        adaptadorCuentosDetallados = new AdaptadorCuentosDetallados(listaCuentosDetallados);
        adaptadorActividadesPorCuento = new AdaptadorActividadesPorCuento(listaCuentosConActividades);
    }

    private void configurarListeners() {
        // Botón Volver
        botonVolver.setOnClickListener(v -> finish());
        
        // Botón Configuración
        botonConfiguracionAula.setOnClickListener(v -> {
            // Navegar a configuración del aula
            Intent intent = new Intent(VisualizarAulaActivity.this, ConfiguracionAulaActivity.class);
            intent.putExtra("nombre_aula", textoNombreAula.getText().toString());
            intent.putExtra("codigo_aula", textoCodigoAula.getText().toString().replace("Código: ", ""));
            // Extraer el grado del nombre del aula (ej: "3°A - Lengua" -> "3°")
            String nombreCompleto = textoNombreAula.getText().toString();
            String grado = nombreCompleto.split("°")[0] + "°";
            intent.putExtra("grado_aula", grado);
            startActivityForResult(intent, 1);
        });
        
        // Pestañas
        botonPestanaEstudiantes.setOnClickListener(v -> mostrarPestanaEstudiantes());
        botonPestanaCuentos.setOnClickListener(v -> mostrarPestanaCuentos());
        botonPestanaActividades.setOnClickListener(v -> mostrarPestanaActividades());
    }

    private void cargarDatosEjemplo() {
        // Datos del aula (recibidos del Intent)
        Intent intent = getIntent();
        String nombreAula = intent.getStringExtra("nombre_aula");
        String codigoAula = intent.getStringExtra("codigo_aula");
        
        if (nombreAula != null) {
            textoNombreAula.setText(nombreAula);
        }
        if (codigoAula != null) {
            textoCodigoAula.setText("Código: " + codigoAula);
        }
        
        // Cargar estudiantes de ejemplo
        listaEstudiantes.clear();
        listaEstudiantes.add(new ModeloEstudianteAula("1", "Ana García", "Hace 2 min", 85, true));
        listaEstudiantes.add(new ModeloEstudianteAula("2", "Carlos López", "Hace 1 hora", 72, false));
        listaEstudiantes.add(new ModeloEstudianteAula("3", "María Rodríguez", "Activo ahora", 91, true));
        listaEstudiantes.add(new ModeloEstudianteAula("4", "Juan Pérez", "Hace 2 días", 45, false));
        listaEstudiantes.add(new ModeloEstudianteAula("5", "Sofía Martínez", "Hace 30 min", 68, true));
        
        // Cargar cuentos detallados de ejemplo
        listaCuentosDetallados.clear();
        listaCuentosDetallados.add(new ModeloCuentoDetallado("1", "Caperucita Roja", "Cuento tradicional europeo", "", 19, "Comprensión", 2, "Vocabulario", 8));
        listaCuentosDetallados.add(new ModeloCuentoDetallado("2", "Los Duendes y el Zapatero", "Hermanos Grimm", "", 13, "Comprensión", 6, "Vocabulario", 4));
        listaCuentosDetallados.add(new ModeloCuentoDetallado("3", "El Gato con Botas", "Charles Perrault", "", 15, "Comprensión", 3, "Vocabulario", 7));
        listaCuentosDetallados.add(new ModeloCuentoDetallado("4", "Hansel y Gretel", "Hermanos Grimm", "", 11, "Comprensión", 5, "Vocabulario", 9));
        listaCuentosDetallados.add(new ModeloCuentoDetallado("5", "Ricitos de Oro", "Robert Southey", "", 17, "Comprensión", 4, "Vocabulario", 6));
        
        // Cargar actividades organizadas por cuento
        listaCuentosConActividades.clear();
        
        // Caperucita Roja
        List<ModeloActividadDetallada> actividadesCaperucita = new ArrayList<>();
        actividadesCaperucita.add(new ModeloActividadDetallada("1", "Caperucita Roja", 
            "Comprensión - Capítulo 1", "Opción Múltiple", 15, "Activa",
            "Vocabulario - Capítulo 1", "Respuesta Abierta", 12, "Activa"));
        listaCuentosConActividades.add(new ModeloCuentoConActividades("1", "Caperucita Roja", 2, actividadesCaperucita));
        
        // Los Duendes y el Zapatero
        List<ModeloActividadDetallada> actividadesDuendes = new ArrayList<>();
        actividadesDuendes.add(new ModeloActividadDetallada("2", "Los Duendes y el Zapatero", 
            "Comprensión - Capítulo 1", "Opción Múltiple", 15, "Activa",
            "Vocabulario - Capítulo 1", "Respuesta Abierta", 12, "Activa"));
        listaCuentosConActividades.add(new ModeloCuentoConActividades("2", "Los Duendes y el Zapatero", 2, actividadesDuendes));
        
        // El Gato con Botas
        List<ModeloActividadDetallada> actividadesGato = new ArrayList<>();
        actividadesGato.add(new ModeloActividadDetallada("3", "El Gato con Botas", 
            "Comprensión - Capítulo 1", "Opción Múltiple", 15, "Activa",
            "Vocabulario - Capítulo 1", "Respuesta Abierta", 12, "Activa"));
        listaCuentosConActividades.add(new ModeloCuentoConActividades("3", "El Gato con Botas", 2, actividadesGato));
        
        // Actualizar estadísticas
        numeroEstudiantesAula.setText(String.valueOf(listaEstudiantes.size()));
        numeroCuentosAula.setText(String.valueOf(listaCuentosDetallados.size()));
        numeroActividadesAula.setText(String.valueOf(listaCuentosConActividades.size() * 2)); // 2 actividades por cuento
    }

    private void mostrarPestanaEstudiantes() {
        pestanaActual = "estudiantes";
        actualizarBotonesPestanas();
        recyclerViewContenido.setAdapter(adaptadorEstudiantes);
        adaptadorEstudiantes.notifyDataSetChanged();
    }

    private void mostrarPestanaCuentos() {
        pestanaActual = "cuentos";
        actualizarBotonesPestanas();
        recyclerViewContenido.setAdapter(adaptadorCuentosDetallados);
        adaptadorCuentosDetallados.notifyDataSetChanged();
    }

    private void mostrarPestanaActividades() {
        pestanaActual = "actividades";
        actualizarBotonesPestanas();
        recyclerViewContenido.setAdapter(adaptadorActividadesPorCuento);
        adaptadorActividadesPorCuento.notifyDataSetChanged();
    }

    private void actualizarBotonesPestanas() {
        // Resetear todos los botones
        botonPestanaEstudiantes.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        botonPestanaEstudiantes.setTextColor(getResources().getColor(R.color.gris_medio));
        
        botonPestanaCuentos.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        botonPestanaCuentos.setTextColor(getResources().getColor(R.color.gris_medio));
        
        botonPestanaActividades.setBackgroundResource(R.drawable.boton_pestana_inactiva);
        botonPestanaActividades.setTextColor(getResources().getColor(R.color.gris_medio));
        
        // Activar el botón correspondiente
        switch (pestanaActual) {
            case "estudiantes":
                botonPestanaEstudiantes.setBackgroundResource(R.drawable.boton_pestana_activa);
                botonPestanaEstudiantes.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
            case "cuentos":
                botonPestanaCuentos.setBackgroundResource(R.drawable.boton_pestana_activa);
                botonPestanaCuentos.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
            case "actividades":
                botonPestanaActividades.setBackgroundResource(R.drawable.boton_pestana_activa);
                botonPestanaActividades.setTextColor(getResources().getColor(R.color.gris_oscuro));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Actualizar los datos del aula con los cambios guardados
            String nombreActualizado = data.getStringExtra("nombre_aula_actualizado");
            String gradoActualizado = data.getStringExtra("grado_aula_actualizado");
            
            if (nombreActualizado != null) {
                textoNombreAula.setText(nombreActualizado);
            }
            
            if (gradoActualizado != null) {
                // Actualizar el código del aula si es necesario
                // Por ahora solo mostramos un mensaje de éxito
                android.widget.Toast.makeText(this, 
                    "¡Información del aula actualizada!", 
                    android.widget.Toast.LENGTH_SHORT).show();
            }
        }
    }
}
