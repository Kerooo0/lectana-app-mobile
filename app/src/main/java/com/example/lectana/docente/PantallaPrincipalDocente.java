package com.example.lectana.docente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

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
import com.example.lectana.BibliotecaCuentosActivity;
import com.example.lectana.docente.CrearActividadActivity;
import com.example.lectana.docente.GestionActividadesActivity;
import com.example.lectana.PerfilDocenteActivity;
import com.example.lectana.R;
import com.example.lectana.adaptadores.AdaptadorListaAulas;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.repository.AulasRepository;

import java.util.ArrayList;
import java.util.List;

public class PantallaPrincipalDocente extends AppCompatActivity {

    // Componentes de la Interfaz
    private TextView texto_titulo_principal, texto_bienvenida_docente;
    private TextView numero_total_aulas, numero_total_estudiantes, numero_cuentos_asignados;
    private ImageView icono_campana_notificaciones, icono_configuracion_ajustes;
    private RecyclerView lista_aulas_docente;
    private AdaptadorListaAulas adaptador_lista_aulas;
    private ProgressBar progressBar;

    // Datos de las Aulas
    private List<ModeloAula> lista_aulas_docente_datos;
    
    // Gestión de sesión y repositorio
    private SessionManager sessionManager;
    private AulasRepository aulasRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pantalla_principal_docente);

        try {
            sessionManager = new SessionManager(this);
            aulasRepository = new AulasRepository(sessionManager);

            if (!verificarSesion()) {
                return;
            }

            inicializar_componentes_interfaz();
            configurar_lista_aulas();
            configurar_botones_accion();
            personalizar_interfaz_usuario();
            
            // Cargar datos de forma segura
            cargar_datos_aulas_seguro();
            
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error en onCreate: " + e.getMessage(), e);
            Toast.makeText(this, "Error al inicializar la aplicación", Toast.LENGTH_LONG).show();
            irAlLogin();
        }
    }

    private void inicializar_componentes_interfaz() {
        try {
            texto_titulo_principal = findViewById(R.id.texto_titulo_principal);
            texto_bienvenida_docente = findViewById(R.id.texto_bienvenida_docente);
            numero_total_aulas = findViewById(R.id.numero_total_aulas);
            numero_total_estudiantes = findViewById(R.id.numero_total_estudiantes);
            numero_cuentos_asignados = findViewById(R.id.numero_cuentos_asignados);
            icono_campana_notificaciones = findViewById(R.id.icono_campana_notificaciones);
            icono_configuracion_ajustes = findViewById(R.id.icono_configuracion_ajustes);
            lista_aulas_docente = findViewById(R.id.lista_aulas_docente);
            progressBar = findViewById(R.id.progress_bar);
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error inicializando componentes: " + e.getMessage(), e);
        }
    }

    private void configurar_lista_aulas() {
        try {
            lista_aulas_docente_datos = new ArrayList<>();
            adaptador_lista_aulas = new AdaptadorListaAulas(lista_aulas_docente_datos, new AdaptadorListaAulas.OnClickListenerAula() {
                @Override
                public void onClicAula(ModeloAula aula_seleccionada) {
                    Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, VisualizarAulaActivity.class);
                    intento_navegacion.putExtra("aula_id", aula_seleccionada.getId_aula());
                    intento_navegacion.putExtra("nombre_aula", aula_seleccionada.getNombre_aula());
                    intento_navegacion.putExtra("codigo_aula", aula_seleccionada.getCodigo_acceso());
                    startActivity(intento_navegacion);
                }

                @Override
                public void onClicEstadisticas(ModeloAula aula_seleccionada) {
                    // TODO: Implementar estadísticas
                    Toast.makeText(PantallaPrincipalDocente.this, "Estadísticas próximamente", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClicEliminarAula(ModeloAula aula_seleccionada) {
                    mostrarDialogoConfirmacionEliminar(aula_seleccionada);
                }
            });

            lista_aulas_docente.setLayoutManager(new LinearLayoutManager(this));
            lista_aulas_docente.setAdapter(adaptador_lista_aulas);
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error configurando lista: " + e.getMessage(), e);
        }
    }

    private void cargar_datos_aulas_seguro() {
        try {
            mostrarCargando(true);
            
            // Verificar sesión antes de cargar
            if (!verificarSesionYRole()) {
                mostrarCargando(false);
                return;
            }
            
            aulasRepository.getAulasDocente(new AulasRepository.AulasCallback<List<ModeloAula>>() {
                @Override
                public void onSuccess(List<ModeloAula> aulas) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        actualizarDatosAulas(aulas);
                    });
                }

                @Override
                public void onError(String message) {
                    runOnUiThread(() -> {
                        mostrarCargando(false);
                        Toast.makeText(PantallaPrincipalDocente.this, "Error al cargar aulas: " + message, Toast.LENGTH_LONG).show();
                        mostrarMensajeSinAulas();
                    });
                }
            });
            
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error cargando aulas: " + e.getMessage(), e);
            mostrarCargando(false);
            mostrarMensajeSinAulas();
        }
    }

    private boolean verificarSesionYRole() {
        try {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Sesión expirada. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
                irAlLogin();
                return false;
            }

            String role = sessionManager.getRole();
            String token = sessionManager.getToken();
            
            Log.d("PantallaPrincipalDocente", "Rol actual: " + role);
            Log.d("PantallaPrincipalDocente", "Token presente: " + (token != null ? "Sí" : "No"));
            
            if (!"docente".equals(role)) {
                Toast.makeText(this, "Acceso denegado. Solo los docentes pueden acceder a esta sección.", Toast.LENGTH_LONG).show();
                irAlLogin();
                return false;
            }

            return true;
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error verificando sesión: " + e.getMessage(), e);
            return false;
        }
    }

    private void actualizarDatosAulas(List<ModeloAula> aulas) {
        try {
            lista_aulas_docente_datos.clear();
            lista_aulas_docente_datos.addAll(aulas);
            adaptador_lista_aulas.notifyDataSetChanged();

            int totalEstudiantes = 0;
            int totalCuentos = 0;

            for (ModeloAula aula : aulas) {
                // Log temporal para debug
                Log.d("PantallaPrincipalDocente", "Aula: " + aula.getNombre_aula() + 
                      " | Estudiantes: " + aula.getTotal_estudiantes() + 
                      " | Cuentos: " + aula.getTotal_cuentos());
                
                totalEstudiantes += aula.getTotal_estudiantes();
                totalCuentos += aula.getTotal_cuentos();
            }

            numero_total_aulas.setText(String.valueOf(aulas.size()));
            numero_total_estudiantes.setText(String.valueOf(totalEstudiantes));
            numero_cuentos_asignados.setText(String.valueOf(totalCuentos));
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error actualizando datos: " + e.getMessage(), e);
        }
    }

    private void mostrarMensajeSinAulas() {
        try {
            lista_aulas_docente_datos.clear();
            adaptador_lista_aulas.notifyDataSetChanged();
            
            numero_total_aulas.setText("0");
            numero_total_estudiantes.setText("0");
            numero_cuentos_asignados.setText("0");
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error mostrando mensaje sin aulas: " + e.getMessage(), e);
        }
    }

    private void mostrarCargando(boolean mostrar) {
        try {
            if (progressBar != null) {
                progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
            }
            if (lista_aulas_docente != null) {
                lista_aulas_docente.setVisibility(mostrar ? View.GONE : View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error mostrando carga: " + e.getMessage(), e);
        }
    }

    private void configurar_botones_accion() {
        try {
            if (icono_campana_notificaciones != null) {
                icono_campana_notificaciones.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        Toast.makeText(PantallaPrincipalDocente.this, "Notificaciones próximamente", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (icono_configuracion_ajustes != null) {
                icono_configuracion_ajustes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View vista) {
                        Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, PerfilDocenteActivity.class);
                        startActivity(intento_navegacion);
                    }
                });
            }

            findViewById(R.id.boton_crear_nueva_aula).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vista) {
                    Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, CrearNuevaAulaActivity.class);
                    startActivity(intento_navegacion);
                }
            });


            findViewById(R.id.boton_crear_actividad).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vista) {
                    Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, CrearActividadActivity.class);
                    startActivity(intento_navegacion);
                }
            });

            findViewById(R.id.boton_gestionar_actividades).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vista) {
                    Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, GestionActividadesActivity.class);
                    startActivity(intento_navegacion);
                }
            });

            findViewById(R.id.boton_explorar_cuentos).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vista) {
                    Intent intento_navegacion = new Intent(PantallaPrincipalDocente.this, BibliotecaCuentosActivity.class);
                    intento_navegacion.putExtra("modo", BibliotecaCuentosActivity.MODO_EXPLORAR);
                    startActivity(intento_navegacion);
                }
            });
            
            // Debug simple - mantener presionado el título
            if (texto_titulo_principal != null) {
                texto_titulo_principal.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View vista) {
                        mostrarDebugSesion();
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error configurando botones: " + e.getMessage(), e);
        }
    }

    private void mostrarDebugSesion() {
        try {
            StringBuilder debug = new StringBuilder();
            debug.append("=== DEBUG SESIÓN ===\n");
            debug.append("¿Logueado?: ").append(sessionManager.isLoggedIn() ? "SÍ" : "NO").append("\n");
            debug.append("Token: ").append(sessionManager.getToken() != null ? "Presente" : "Ausente").append("\n");
            debug.append("Rol: ").append(sessionManager.getRole() != null ? sessionManager.getRole() : "Nulo").append("\n");
            debug.append("¿Es docente?: ").append(sessionManager.isDocente() ? "SÍ" : "NO");
            
            Toast.makeText(this, debug.toString(), Toast.LENGTH_LONG).show();
            Log.d("DebugSesion", debug.toString());
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error en debug: " + e.getMessage(), e);
        }
    }

    private void personalizar_interfaz_usuario() {
        try {
            if (texto_titulo_principal != null) {
                texto_titulo_principal.setText("Panel Docente");
            }
            
            if (texto_bienvenida_docente != null) {
                org.json.JSONObject user = sessionManager.getUser();
                if (user != null) {
                    String nombre = user.optString("nombre", "Docente");
                    String apellido = user.optString("apellido", "");
                    
                    String mensajeBienvenida;
                    if (!apellido.isEmpty()) {
                        mensajeBienvenida = "Bienvenido Prof. " + nombre + " " + apellido;
                    } else {
                        mensajeBienvenida = "Bienvenido Prof. " + nombre;
                    }
                    
                    texto_bienvenida_docente.setText(mensajeBienvenida);
                } else {
                    texto_bienvenida_docente.setText("Bienvenido Profesor");
                }
            }
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error personalizando interfaz: " + e.getMessage(), e);
            if (texto_titulo_principal != null) {
                texto_titulo_principal.setText("Panel Docente");
            }
            if (texto_bienvenida_docente != null) {
                texto_bienvenida_docente.setText("Bienvenido Profesor");
            }
        }
    }

    /**
     * Verificar si hay una sesión válida de docente
     */
    private boolean verificarSesion() {
        try {
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
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error verificando sesión: " + e.getMessage(), e);
            irAlLogin();
            return false;
        }
    }

    /**
     * Redirigir al login y limpiar sesión
     */
    private void irAlLogin() {
        try {
            sessionManager.clearSession();
            Intent intent = new Intent(this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error yendo al login: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (!verificarSesion()) {
                return;
            }
            cargar_datos_aulas_seguro();
        } catch (Exception e) {
            Log.e("PantallaPrincipalDocente", "Error en onResume: " + e.getMessage(), e);
        }
    }

    private void mostrarDialogoConfirmacionEliminar(ModeloAula aula) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Aula")
                .setMessage("¿Estás seguro de que quieres eliminar el aula '" + aula.getNombre_aula() + "'?\n\n" +
                           "Esta acción eliminará:\n" +
                           "• Todos los estudiantes del aula\n" +
                           "• Todas las asignaciones de cuentos\n" +
                           "• Todo el historial de actividades\n\n" +
                           "Esta acción NO se puede deshacer.")
                .setPositiveButton("Eliminar", (dialog, which) -> {
                    eliminarAula(aula);
                })
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void eliminarAula(ModeloAula aula) {
        mostrarCargando(true);
        aulasRepository.eliminarAula(aula.getId_aula(), new AulasRepository.AulasCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(PantallaPrincipalDocente.this, "Aula eliminada correctamente", Toast.LENGTH_SHORT).show();
                    // Recargar la lista de aulas
                    cargar_datos_aulas_seguro();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(PantallaPrincipalDocente.this, "Error al eliminar aula: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * Crear datos de ejemplo para aulas cuando no hay conexión
     */
    private List<ModeloAula> crearAulasEjemplo() {
        List<ModeloAula> aulas = new ArrayList<>();
        
        // Aula 1
        ModeloAula aula1 = new ModeloAula();
        aula1.setId_aula(1);
        aula1.setNombre_aula("Primer Grado A");
        aula1.setGrado("1");
        aula1.setCodigo_acceso("PRIM-A-2024");
        aula1.setTotal_estudiantes(2);
        aula1.setTotal_cuentos(3);
        
        // Crear estudiantes para el aula 1
        List<ModeloAula.Estudiante> estudiantes1 = new ArrayList<>();
        
        ModeloAula.Estudiante est1 = new ModeloAula.Estudiante();
        est1.setId(1);
        ModeloAula.Estudiante.Usuario usuario1 = new ModeloAula.Estudiante.Usuario();
        usuario1.setNombre("Juan");
        usuario1.setApellido("Pérez");
        usuario1.setEmail("juan.perez@ejemplo.com");
        est1.setUsuario(usuario1);
        estudiantes1.add(est1);
        
        ModeloAula.Estudiante est2 = new ModeloAula.Estudiante();
        est2.setId(2);
        ModeloAula.Estudiante.Usuario usuario2 = new ModeloAula.Estudiante.Usuario();
        usuario2.setNombre("María");
        usuario2.setApellido("García");
        usuario2.setEmail("maria.garcia@ejemplo.com");
        est2.setUsuario(usuario2);
        estudiantes1.add(est2);
        
        aula1.setEstudiantes(estudiantes1);
        aulas.add(aula1);
        
        // Aula 2
        ModeloAula aula2 = new ModeloAula();
        aula2.setId_aula(2);
        aula2.setNombre_aula("Segundo Grado B");
        aula2.setGrado("2");
        aula2.setCodigo_acceso("SEG-B-2024");
        aula2.setTotal_estudiantes(1);
        aula2.setTotal_cuentos(2);
        
        // Crear estudiantes para el aula 2
        List<ModeloAula.Estudiante> estudiantes2 = new ArrayList<>();
        
        ModeloAula.Estudiante est3 = new ModeloAula.Estudiante();
        est3.setId(3);
        ModeloAula.Estudiante.Usuario usuario3 = new ModeloAula.Estudiante.Usuario();
        usuario3.setNombre("Carlos");
        usuario3.setApellido("López");
        usuario3.setEmail("carlos.lopez@ejemplo.com");
        est3.setUsuario(usuario3);
        estudiantes2.add(est3);
        
        aula2.setEstudiantes(estudiantes2);
        aulas.add(aula2);
        
        Log.d("PantallaPrincipalDocente", "Creadas " + aulas.size() + " aulas de ejemplo");
        return aulas;
    }
}
