package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PanelEstudianteActivity extends AppCompatActivity {

    private TextView saludoEstudiante;
    private ImageView botonConfiguracionEstudiante;
    private View cardNovedad;
    private TextView textoNovedad;
    private View botonVerLectura;
    private ImageView imagenLibroActual;
    private TextView tituloLibroActual;
    private TextView capituloLibroActual;
    private View barraProgresoLectura;
    private TextView porcentajeProgresoLectura;
    private ImageView botonReproducirLectura;
    private TextView textoActividadPendiente;
    private TextView nombreActividadPendiente;
    private View botonHacerActividad;
    private TextView textoRachaLectura;
    private TextView textoPuntosSemana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_estudiante);

        inicializarVistas();
        configurarListeners();
        cargarDatosEstudiante();
    }

    private void inicializarVistas() {
        saludoEstudiante = findViewById(R.id.saludo_estudiante);
        botonConfiguracionEstudiante = findViewById(R.id.boton_configuracion_estudiante);
        cardNovedad = findViewById(R.id.card_novedad);
        textoNovedad = findViewById(R.id.texto_novedad);
        botonVerLectura = findViewById(R.id.boton_ver_lectura);
        imagenLibroActual = findViewById(R.id.imagen_libro_actual);
        tituloLibroActual = findViewById(R.id.titulo_libro_actual);
        capituloLibroActual = findViewById(R.id.capitulo_libro_actual);
        barraProgresoLectura = findViewById(R.id.barra_progreso_lectura);
        porcentajeProgresoLectura = findViewById(R.id.porcentaje_progreso_lectura);
        botonReproducirLectura = findViewById(R.id.boton_reproducir_lectura);
        textoActividadPendiente = findViewById(R.id.texto_actividad_pendiente);
        nombreActividadPendiente = findViewById(R.id.nombre_actividad_pendiente);
        botonHacerActividad = findViewById(R.id.boton_hacer_actividad);
        textoRachaLectura = findViewById(R.id.texto_racha_lectura);
        textoPuntosSemana = findViewById(R.id.texto_puntos_semana);
    }

    private void configurarListeners() {
        // Botón configuración
        botonConfiguracionEstudiante.setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilEstudianteActivity.class);
            startActivity(intent);
        });

        // Botón ver lectura (novedad)
        botonVerLectura.setOnClickListener(v -> {
            // TODO: Implementar navegación a la nueva lectura asignada
            Toast.makeText(this, "Ver nueva lectura: El Loro Pelado", Toast.LENGTH_SHORT).show();
        });

        // Botón reproducir lectura actual
        botonReproducirLectura.setOnClickListener(v -> {
            // TODO: Implementar reproductor de audiolibro
            Toast.makeText(this, "Reproducir audiolibro: El Principito", Toast.LENGTH_SHORT).show();
        });

        // Botón hacer actividad pendiente
        botonHacerActividad.setOnClickListener(v -> {
            // TODO: Implementar navegación a actividad pendiente
            Toast.makeText(this, "Realizar actividad: La Abeja Haragana", Toast.LENGTH_SHORT).show();
        });

        // Navegación inferior
        configurarNavegacionInferior();
    }

    private void configurarNavegacionInferior() {
        findViewById(R.id.tab_inicio).setOnClickListener(v -> {
            // Ya estamos en inicio, no hacer nada
        });

        findViewById(R.id.tab_biblioteca).setOnClickListener(v -> {
            // TODO: Implementar navegación a biblioteca
            Toast.makeText(this, "Biblioteca", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.tab_progreso).setOnClickListener(v -> {
            // TODO: Implementar navegación a progreso
            Toast.makeText(this, "Progreso", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.tab_tienda).setOnClickListener(v -> {
            // TODO: Implementar navegación a tienda
            Toast.makeText(this, "Tienda", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.tab_perfil).setOnClickListener(v -> {
            Intent intent = new Intent(this, PerfilEstudianteActivity.class);
            startActivity(intent);
        });
    }

    private void cargarDatosEstudiante() {
        // TODO: Cargar datos reales del estudiante desde la base de datos
        // Por ahora, datos de ejemplo
        saludoEstudiante.setText("¡Hola, Juanito!");
        textoNovedad.setText("Tu docente te asignó una nueva lectura: El Loro Pelado.");
        tituloLibroActual.setText("El Principito");
        capituloLibroActual.setText("Capítulo 3 de 8");
        porcentajeProgresoLectura.setText("37% completado");
        textoActividadPendiente.setText("Tenés 1 actividad pendiente");
        nombreActividadPendiente.setText("La Abeja Haragana");
        textoRachaLectura.setText("Racha de lectura: 3 días seguidos");
        textoPuntosSemana.setText("Puntos ganados esta semana: +25 puntos");

        // TODO: Configurar la barra de progreso visualmente
        // barraProgresoLectura.setProgress(37);
    }
}
