package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorListaAulas;
import com.example.lectana.modelos.ModeloAula;

import java.util.ArrayList;
import java.util.List;

public class PantallaPrincipalDocente extends AppCompatActivity {

    // Componentes de la Interfaz
    private TextView texto_titulo_principal, texto_bienvenida_docente;
    private TextView numero_total_aulas, numero_total_estudiantes, numero_cuentos_asignados;
    private ImageView icono_campana_notificaciones, icono_configuracion_ajustes;
    private RecyclerView lista_aulas_docente;
    private AdaptadorListaAulas adaptador_lista_aulas;

    // Datos de las Aulas
    private List<ModeloAula> lista_aulas_docente_datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantalla_principal_docente);

        inicializar_componentes_interfaz();
        configurar_lista_aulas();
        cargar_datos_aulas();
        configurar_botones_accion();
    }

    private void inicializar_componentes_interfaz() {
        texto_titulo_principal = findViewById(R.id.texto_titulo_principal);
        texto_bienvenida_docente = findViewById(R.id.texto_bienvenida_docente);
        numero_total_aulas = findViewById(R.id.numero_total_aulas);
        numero_total_estudiantes = findViewById(R.id.numero_total_estudiantes);
        numero_cuentos_asignados = findViewById(R.id.numero_cuentos_asignados);
        icono_campana_notificaciones = findViewById(R.id.icono_campana_notificaciones);
        icono_configuracion_ajustes = findViewById(R.id.icono_configuracion_ajustes);
        lista_aulas_docente = findViewById(R.id.lista_aulas_docente);
    }

    private void configurar_lista_aulas() {
        lista_aulas_docente_datos = new ArrayList<>();
        adaptador_lista_aulas = new AdaptadorListaAulas(lista_aulas_docente_datos, new AdaptadorListaAulas.OnClickListenerAula() {
            @Override
            public void onClicAula(ModeloAula aula_seleccionada) {
                // Navegar a detalles del aula
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaDetallesAula.class);
                intento_navegacion.putExtra("id_aula", aula_seleccionada.getId_aula());
                startActivity(intento_navegacion);
            }

            @Override
            public void onClicEstadisticas(ModeloAula aula_seleccionada) {
                // Navegar a estadísticas del aula
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaEstadisticasAula.class);
                intento_navegacion.putExtra("id_aula", aula_seleccionada.getId_aula());
                startActivity(intento_navegacion);
            }
        });

        lista_aulas_docente.setLayoutManager(new LinearLayoutManager(this));
        lista_aulas_docente.setAdapter(adaptador_lista_aulas);
    }

    private void cargar_datos_aulas() {
        // TODO: Cargar datos desde la base de datos
        // Por ahora datos de ejemplo
        cargar_datos_ejemplo();
    }

    private void cargar_datos_ejemplo() {
        // Datos de ejemplo - reemplazar con llamadas a la base de datos
        numero_total_aulas.setText("3");
        numero_total_estudiantes.setText("72");
        numero_cuentos_asignados.setText("15");

        // Aulas de ejemplo
        lista_aulas_docente_datos.clear();
        lista_aulas_docente_datos.add(new ModeloAula("1", "3°A - Lengua y Literatura", "24", "ABC123", "18"));
        lista_aulas_docente_datos.add(new ModeloAula("2", "3°B - Lengua y Literatura", "22", "DEF456", "15"));
        lista_aulas_docente_datos.add(new ModeloAula("3", "4°A - Literatura", "26", "GHI789", "20"));
        
        adaptador_lista_aulas.notifyDataSetChanged();
    }

    private void configurar_botones_accion() {
        icono_campana_notificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                // Navegar a notificaciones
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaNotificaciones.class);
                startActivity(intento_navegacion);
            }
        });

        icono_configuracion_ajustes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                // Navegar a configuración
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaConfiguracion.class);
                startActivity(intento_navegacion);
            }
        });

        findViewById(R.id.boton_crear_nueva_aula).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                // Navegar a crear aula
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaCrearAula.class);
                startActivity(intento_navegacion);
            }
        });

        findViewById(R.id.boton_asignar_cuento_aula).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                // Navegar a asignar cuento
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaAsignarCuento.class);
                startActivity(intento_navegacion);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar datos cuando se regrese a esta pantalla
        cargar_datos_aulas();
    }
}
