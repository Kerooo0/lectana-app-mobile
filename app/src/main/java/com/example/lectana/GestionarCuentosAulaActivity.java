package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorCuentosGestionar;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.repository.AulasRepository;

import java.util.ArrayList;
import java.util.List;

public class GestionarCuentosAulaActivity extends AppCompatActivity {

    private ImageView botonVolver;
    private TextView textoNombreAula;
    private TextView textoTotalCuentos;
    private RecyclerView recyclerViewCuentos;
    private Button botonAgregarCuentos;
    private Button botonGuardarCambios;
    private ProgressBar progressBar;

    // Datos
    private int aulaId;
    private String nombreAula;
    private ModeloAula aulaActual;
    private List<CuentoApi> cuentosActuales;
    private List<CuentoApi> cuentosDisponibles;
    private List<CuentoApi> cuentosSeleccionados;

    // Repositorios
    private AulasRepository aulasRepository;
    private SessionManager sessionManager;

    // Adaptador
    private AdaptadorCuentosGestionar adaptadorCuentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_cuentos_aula);

        recibirDatosAula();
        inicializarVistas();
        inicializarRepositorios();
        configurarListeners();
        cargarDatosAula();
    }

    private void recibirDatosAula() {
        Intent intent = getIntent();
        if (intent != null) {
            aulaId = intent.getIntExtra("aula_id", -1);
            nombreAula = intent.getStringExtra("nombre_aula");
        }
    }

    private void inicializarVistas() {
        botonVolver = findViewById(R.id.boton_volver);
        textoNombreAula = findViewById(R.id.texto_nombre_aula);
        textoTotalCuentos = findViewById(R.id.texto_total_cuentos);
        recyclerViewCuentos = findViewById(R.id.recycler_view_cuentos);
        botonAgregarCuentos = findViewById(R.id.boton_agregar_cuentos);
        botonGuardarCambios = findViewById(R.id.boton_guardar_cambios);
        progressBar = findViewById(R.id.progress_bar);

        recyclerViewCuentos.setLayoutManager(new LinearLayoutManager(this));
    }

    private void inicializarRepositorios() {
        sessionManager = new SessionManager(this);
        aulasRepository = new AulasRepository(sessionManager);
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(v -> finish());

        botonAgregarCuentos.setOnClickListener(v -> {
            // Navegar a seleccionar cuentos disponibles
            Intent intent = new Intent(this, SeleccionarCuentosActivity.class);
            intent.putExtra("aula_id", aulaId);
            intent.putExtra("modo", "gestionar");
            startActivityForResult(intent, 1);
        });

        botonGuardarCambios.setOnClickListener(v -> {
            guardarCambiosCuentos();
        });
    }

    private void cargarDatosAula() {
        if (aulaId == -1) {
            Toast.makeText(this, "Error: ID de aula no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mostrarCargando(true);
        aulasRepository.getAulaDetalle(aulaId, new AulasRepository.AulasCallback<ModeloAula>() {
            @Override
            public void onSuccess(ModeloAula aula) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    aulaActual = aula;
                    actualizarInterfazConDatosReales();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(GestionarCuentosAulaActivity.this, 
                        "Error al cargar aula: " + message, Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        });
    }

    private void actualizarInterfazConDatosReales() {
        if (aulaActual == null) return;

        // Actualizar información básica
        textoNombreAula.setText(aulaActual.getNombre_aula());
        textoTotalCuentos.setText("Cuentos asignados: " + aulaActual.getTotal_cuentos());

        // Cargar cuentos actuales
        cuentosActuales = aulaActual.getCuentos() != null ? aulaActual.getCuentos() : new ArrayList<>();
        cuentosSeleccionados = new ArrayList<>(cuentosActuales);

        // Configurar adaptador
        adaptadorCuentos = new AdaptadorCuentosGestionar(cuentosSeleccionados, new AdaptadorCuentosGestionar.OnCuentoClickListener() {
            @Override
            public void onEliminarCuento(CuentoApi cuento) {
                eliminarCuento(cuento);
            }
        });
        recyclerViewCuentos.setAdapter(adaptadorCuentos);
    }

    private void eliminarCuento(CuentoApi cuento) {
        cuentosSeleccionados.remove(cuento);
        adaptadorCuentos.notifyDataSetChanged();
        textoTotalCuentos.setText("Cuentos asignados: " + cuentosSeleccionados.size());
    }

    private void guardarCambiosCuentos() {
        if (cuentosSeleccionados == null) {
            cuentosSeleccionados = new ArrayList<>();
        }

        // Convertir lista de cuentos a IDs
        List<Integer> cuentosIds = new ArrayList<>();
        for (CuentoApi cuento : cuentosSeleccionados) {
            cuentosIds.add(cuento.getId_cuento());
        }

        mostrarCargando(true);
        aulasRepository.asignarCuentosAula(aulaId, cuentosIds, new AulasRepository.AulasCallback<com.example.lectana.modelos.AsignarCuentosResponse>() {
            @Override
            public void onSuccess(com.example.lectana.modelos.AsignarCuentosResponse response) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(GestionarCuentosAulaActivity.this, 
                        "¡Cuentos actualizados exitosamente!", Toast.LENGTH_LONG).show();
                    
                    // Regresar a la pantalla anterior
                    Intent intent = new Intent();
                    intent.putExtra("cuentos_actualizados", true);
                    setResult(RESULT_OK, intent);
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(GestionarCuentosAulaActivity.this, 
                        "Error al actualizar cuentos: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
        
        // Deshabilitar botones mientras carga
        botonAgregarCuentos.setEnabled(!mostrar);
        botonGuardarCambios.setEnabled(!mostrar);
        recyclerViewCuentos.setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // Recargar datos del aula para obtener los cuentos actualizados
            cargarDatosAula();
        }
    }
}
