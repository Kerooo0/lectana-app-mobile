package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.adaptadores.AdaptadorCuentosDisponibles;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.AsignarCuentosResponse;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.CuentosResponse;
import com.example.lectana.modelos.ModeloCuento;
import com.example.lectana.repository.AulasRepository;
import com.example.lectana.services.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeleccionarCuentosActivity extends AppCompatActivity implements AdaptadorCuentosDisponibles.OnCuentoSeleccionadoListener {

    private TextView textoNombreAulaConfigurar;
    private TextView textoGradoSeccionConfigurar;
    private TextView textoCuentosSeleccionados;
    private RecyclerView recyclerCuentosDisponibles;
    private Button botonFinalizarAula;
    private Button botonCancelar;
    private ImageView botonVolver;
    private ProgressBar progressBar;

    private AdaptadorCuentosDisponibles adaptadorCuentos;
    private List<ModeloCuento> listaCuentos;
    private java.util.Set<Integer> idsAsignadosActuales = new java.util.HashSet<>();
    private List<Integer> cuentosSeleccionadosIds = new ArrayList<>();
    private int cuentosSeleccionados = 0;

    // Datos del aula recibidos del Paso 1
    private int aulaId;
    private String nombreAula;
    private String grado;
    private String codigoAcceso;

    private AulasRepository aulasRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_cuentos);
        
        inicializarRepositorio();
        recibirDatosAula();
        inicializarVistas();
        configurarRecyclerView();
        cargarIdsCuentosAsignados();
        configurarListeners();
        actualizarInformacionAula();
    }

    private void inicializarRepositorio() {
        sessionManager = new SessionManager(this);
        aulasRepository = new AulasRepository(sessionManager);
    }

    private void recibirDatosAula() {
        Intent intent = getIntent();
        aulaId = intent.getIntExtra("aula_id", -1);
        nombreAula = intent.getStringExtra("nombre_aula");
        grado = intent.getStringExtra("grado");
        codigoAcceso = intent.getStringExtra("codigo_acceso");
    }

    private void inicializarVistas() {
        textoNombreAulaConfigurar = findViewById(R.id.texto_nombre_aula_configurar);
        textoGradoSeccionConfigurar = findViewById(R.id.texto_grado_seccion_configurar);
        textoCuentosSeleccionados = findViewById(R.id.texto_cuentos_seleccionados);
        recyclerCuentosDisponibles = findViewById(R.id.recycler_cuentos_disponibles);
        botonFinalizarAula = findViewById(R.id.boton_finalizar_aula);
        botonFinalizarAula.setText("Agregar cuento");
        botonCancelar = findViewById(R.id.boton_cancelar);
        botonVolver = findViewById(R.id.boton_volver);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void configurarRecyclerView() {
        listaCuentos = new ArrayList<>();
        adaptadorCuentos = new AdaptadorCuentosDisponibles(listaCuentos, this, "asignar");
        
        recyclerCuentosDisponibles.setLayoutManager(new LinearLayoutManager(this));
        recyclerCuentosDisponibles.setAdapter(adaptadorCuentos);
    }

    private void cargarCuentosDisponibles() {
        mostrarCargando(true);
        
        Call<ApiResponse<CuentosResponse>> call = ApiClient.getCuentosApiService().getCuentosPublicos(1, 50, null, null, null, null);
        
        call.enqueue(new Callback<ApiResponse<CuentosResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CuentosResponse>> call, Response<ApiResponse<CuentosResponse>> response) {
                mostrarCargando(false);
                
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<CuentosResponse> apiResponse = response.body();
                    if (apiResponse.isOk()) {
                        CuentosResponse cuentosResponse = apiResponse.getData();
                        convertirCuentosApiAModelo(cuentosResponse.getCuentos());
                        adaptadorCuentos.notifyDataSetChanged();
                    } else {
                        Toast.makeText(SeleccionarCuentosActivity.this, "Error al cargar cuentos: " + apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(SeleccionarCuentosActivity.this, "Error del servidor: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CuentosResponse>> call, Throwable t) {
                mostrarCargando(false);
                Toast.makeText(SeleccionarCuentosActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void convertirCuentosApiAModelo(List<CuentoApi> cuentosApi) {
        listaCuentos.clear();
        for (CuentoApi cuentoApi : cuentosApi) {
            if (idsAsignadosActuales.contains(cuentoApi.getId_cuento())) {
                continue; // omitir ya asignados
            }
            ModeloCuento modeloCuento = new ModeloCuento(
                cuentoApi.getId_cuento(),
                cuentoApi.getTitulo(),
                cuentoApi.getAutor().getNombre() + " " + cuentoApi.getAutor().getApellido(),
                cuentoApi.getGenero().getNombre(),
                String.valueOf(cuentoApi.getEdad_publico()),
                "4.5★", // Rating por defecto
                cuentoApi.getUrl_img(),
                cuentoApi.getDuracion() + " min",
                (idsAsignadosActuales.contains(cuentoApi.getId_cuento()) ? "YA_ASIGNADO" : "Cuento disponible para el aula")
            );
            listaCuentos.add(modeloCuento);
        }
    }

    private void mostrarCargando(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        recyclerCuentosDisponibles.setVisibility(mostrar ? View.GONE : View.VISIBLE);
        botonFinalizarAula.setEnabled(!mostrar);
        botonCancelar.setEnabled(!mostrar);
        botonVolver.setEnabled(!mostrar);
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

        // Botón Finalizar Aula (agregar incremental por POST)
        botonFinalizarAula.setOnClickListener(v -> {
            if (cuentosSeleccionados > 0) {
                asignarIncrementalSeleccionados();
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
            cuentosSeleccionadosIds.add(cuento.getId());
        } else {
            cuentosSeleccionados--;
            cuentosSeleccionadosIds.remove(Integer.valueOf(cuento.getId()));
        }
        actualizarContadorCuentos();
    }

    private void asignarIncrementalSeleccionados() {
        if (aulaId == -1) {
            Toast.makeText(this, "Error: ID del aula no válido", Toast.LENGTH_LONG).show();
            return;
        }

        // Filtrar solo los que no están actualmente
        java.util.List<Integer> porAgregar = new java.util.ArrayList<>();
        for (Integer idSel : cuentosSeleccionadosIds) {
            if (!idsAsignadosActuales.contains(idSel)) porAgregar.add(idSel);
        }
        if (porAgregar.isEmpty()) {
            Toast.makeText(this, "No hay nuevos cuentos para agregar", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarCargando(true);
        agregarSecuencial(porAgregar, 0);
    }

    private void agregarSecuencial(java.util.List<Integer> ids, int idx) {
        if (idx >= ids.size()) {
            runOnUiThread(() -> {
                mostrarCargando(false);
                Toast.makeText(SeleccionarCuentosActivity.this, "¡Cuentos asignados exitosamente! Actualizando lista...", Toast.LENGTH_LONG).show();
                
                // Pequeño delay antes de regresar para que el usuario vea el mensaje
                new android.os.Handler().postDelayed(() -> {
                    // Volver al aula que inició el flujo
                    Intent result = new Intent();
                    result.putExtra("cuentos_actualizados", true);
                    result.putExtra("aula_id", aulaId);
                    setResult(RESULT_OK, result);
                    finish();
                }, 1000); // 1 segundo para que vea el mensaje
            });
            return;
        }

        int idCuento = ids.get(idx);
        aulasRepository.agregarCuentoAula(aulaId, idCuento, new AulasRepository.AulasCallback<AsignarCuentosResponse>() {
            @Override
            public void onSuccess(AsignarCuentosResponse response) {
                idsAsignadosActuales.add(idCuento);
                agregarSecuencial(ids, idx + 1);
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> Toast.makeText(SeleccionarCuentosActivity.this, "Error al agregar cuento: " + message, Toast.LENGTH_SHORT).show());
                agregarSecuencial(ids, idx + 1); // continuar con los restantes
            }
        });
    }

    private void cargarIdsCuentosAsignados() {
        if (aulaId == -1) return;
        mostrarCargando(true);
        aulasRepository.getAulaDetalle(aulaId, new AulasRepository.AulasCallback<com.example.lectana.modelos.ModeloAula>() {
            @Override
            public void onSuccess(com.example.lectana.modelos.ModeloAula aula) {
                if (aula != null && aula.getCuentos() != null) {
                    for (CuentoApi c : aula.getCuentos()) {
                        idsAsignadosActuales.add(c.getId_cuento());
                    }
                }
                cargarCuentosDisponibles();
            }

            @Override
            public void onError(String message) {
                // Si falla, igual intentamos cargar y no filtrar
                cargarCuentosDisponibles();
            }
        });
    }
}
