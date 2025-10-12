package com.example.lectana.docente;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.Login;
import com.example.lectana.VisualizarAulaActivity;
import com.example.lectana.CrearNuevaAulaActivity;
import com.example.lectana.AsignarCuentoActivity;
import com.example.lectana.CrearActividadActivity;
import com.example.lectana.PerfilDocenteActivity;
import com.example.lectana.R;
import com.example.lectana.adaptadores.AdaptadorListaAulas;
import com.example.lectana.auth.SessionManager;
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
    
    // Gestión de sesión
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantalla_principal_docente);

        // Inicializar gestión de sesión
        sessionManager = new SessionManager(this);
        
        // Verificar sesión antes de continuar
        if (!verificarSesion()) {
            return;
        }

        inicializar_componentes_interfaz();
        configurar_lista_aulas();
        cargar_datos_aulas();
        configurar_botones_accion();
        personalizar_interfaz_usuario();
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
                // Navegar a visualizar aula
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, VisualizarAulaActivity.class);
                intento_navegacion.putExtra("nombre_aula", aula_seleccionada.getNombre_aula());
                intento_navegacion.putExtra("codigo_aula", aula_seleccionada.getCodigo_aula());
                startActivity(intento_navegacion);
            }

            @Override
            public void onClicEstadisticas(ModeloAula aula_seleccionada) {
                // TODO: Navegar a estadísticas del aula cuando se implemente
                // Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaEstadisticasAula.class);
                // intento_navegacion.putExtra("id_aula", aula_seleccionada.getId_aula());
                // startActivity(intento_navegacion);
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
                // TODO: Navegar a notificaciones cuando se implemente
                // Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PantallaNotificaciones.class);
                // startActivity(intento_navegacion);
            }
        });

            icono_configuracion_ajustes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vista) {
                    // Navegar al perfil del docente
                    Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PerfilDocenteActivity.class);
                    startActivity(intento_navegacion);
                }
            });

        findViewById(R.id.boton_crear_nueva_aula).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                // Navegar a crear aula
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, CrearNuevaAulaActivity.class);
                startActivity(intento_navegacion);
            }
        });

        findViewById(R.id.boton_asignar_cuento_aula).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                // Navegar a asignar cuento
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, AsignarCuentoActivity.class);
                startActivity(intento_navegacion);
            }
        });

        findViewById(R.id.boton_crear_actividad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vista) {
                // Navegar a crear actividad
                Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, CrearActividadActivity.class);
                startActivity(intento_navegacion);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar sesión cada vez que se regrese a esta pantalla
        if (!verificarSesion()) {
            return;
        }
        // Recargar datos cuando se regrese a esta pantalla
        cargar_datos_aulas();
        // Actualizar interfaz con datos actualizados
        personalizar_interfaz_usuario();
    }

    /**
     * Verificar si hay una sesión válida de docente
     */
    private boolean verificarSesion() {
        if (!sessionManager.isLoggedIn()) {
            Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            irAlLogin();
            return false;
        }

        if (!sessionManager.isDocente()) {
            Toast.makeText(this, "Acceso denegado. Esta área es solo para docentes.", Toast.LENGTH_LONG).show();
            irAlLogin();
            return false;
        }

        return true;
    }

    /**
     * Redirigir al login y limpiar sesión
     */
    private void irAlLogin() {
        sessionManager.clearSession();
        Intent intent = new Intent(this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Personalizar interfaz con datos del usuario
     */
    private void personalizar_interfaz_usuario() {
        try {
            // Cambiar título a "Panel Docente"
            texto_titulo_principal.setText("Panel Docente");
            
            // Obtener datos del usuario desde la sesión
            org.json.JSONObject user = sessionManager.getUser();
            if (user != null) {
                String nombre = user.optString("nombre", "Docente");
                String apellido = user.optString("apellido", "");
                
                // Crear mensaje de bienvenida personalizado
                String mensajeBienvenida;
                if (!apellido.isEmpty()) {
                    mensajeBienvenida = "Bienvenido Prof. " + nombre + " " + apellido;
                } else {
                    mensajeBienvenida = "Bienvenido Prof. " + nombre;
                }
                
                texto_bienvenida_docente.setText(mensajeBienvenida);
            } else {
                // Mensaje por defecto si no hay datos del usuario
                texto_bienvenida_docente.setText("Bienvenido Profesor");
            }
        } catch (Exception e) {
            // Mensaje por defecto en caso de error
            texto_titulo_principal.setText("Panel Docente");
            texto_bienvenida_docente.setText("Bienvenido Profesor");
        }
    }

    /**
     * Realizar logout
     */
    public void logout() {
        sessionManager.clearSession();
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        irAlLogin();
    }
}
