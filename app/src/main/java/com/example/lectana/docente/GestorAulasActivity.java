package com.example.lectana.docente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.CrearNuevaAulaActivity;
import com.example.lectana.VisualizarAulaActivity;
import com.example.lectana.adaptadores.AdaptadorListaAulas;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.repository.AulasRepository;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class GestorAulasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAulas;
    private ProgressBar progressBar;
    private EditText editBuscarAula;
    private Spinner spinnerOrden;
    private android.widget.TextView textEmpty;

    private AdaptadorListaAulas adapter;
    private List<ModeloAula> aulasDatos;
    private List<ModeloAula> aulasFiltradas;

    private SessionManager sessionManager;
    private AulasRepository aulasRepository;
    private Collator collatorEs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestor_aulas);

        sessionManager = new SessionManager(this);
        if (!sessionManager.isLoggedIn() || !sessionManager.isDocente()) {
            Toast.makeText(this, "Acceso denegado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        aulasRepository = new AulasRepository(sessionManager);

        recyclerViewAulas = findViewById(R.id.recyclerViewAulas);
        progressBar = findViewById(R.id.progressBar);
        editBuscarAula = findViewById(R.id.edit_buscar_aula);
        spinnerOrden = findViewById(R.id.spinner_orden);
        textEmpty = findViewById(R.id.textEmpty);

        android.widget.ImageButton botonVolver = findViewById(R.id.botonVolver);
        if (botonVolver != null) {
            botonVolver.setOnClickListener(v -> finish());
        }

        android.widget.Button botonCrearAula = findViewById(R.id.botonCrearAula);
        if (botonCrearAula != null) {
            botonCrearAula.setOnClickListener(v -> {
                Intent intent = new Intent(GestorAulasActivity.this, CrearNuevaAulaActivity.class);
                startActivity(intent);
            });
        }

        Locale localeEs = new Locale("es", "ES");
        collatorEs = Collator.getInstance(localeEs);
        collatorEs.setStrength(Collator.PRIMARY);

        aulasDatos = new ArrayList<>();
        aulasFiltradas = new ArrayList<>();

        adapter = new AdaptadorListaAulas(aulasFiltradas, new AdaptadorListaAulas.OnClickListenerAula() {
            @Override
            public void onClicAula(ModeloAula aula) {
                Intent i = new Intent(GestorAulasActivity.this, VisualizarAulaActivity.class);
                i.putExtra("aula_id", aula.getId_aula());
                i.putExtra("nombre_aula", aula.getNombre_aula());
                i.putExtra("codigo_aula", aula.getCodigo_acceso());
                startActivity(i);
            }

            @Override
            public void onClicEstadisticas(ModeloAula aula) {
                Toast.makeText(GestorAulasActivity.this, "Estadísticas próximamente", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onClicEliminarAula(ModeloAula aula) {
                eliminarAula(aula);
            }
        });

        recyclerViewAulas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAulas.setAdapter(adapter);
        // Espaciado entre items
        final int spacingPx = (int) (8 * getResources().getDisplayMetrics().density);
        recyclerViewAulas.addItemDecoration(new androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(android.graphics.Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = spacingPx;
                outRect.bottom = spacingPx;
            }
        });

        configurarBuscadorYOrden();
        cargarAulas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar aulas cuando se regrese a esta pantalla (por ejemplo, después de crear una nueva aula)
        cargarAulas();
    }

    private void configurarBuscadorYOrden() {
        if (editBuscarAula != null) {
            editBuscarAula.addTextChangedListener(new android.text.TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) { aplicarFiltrosYOrden(); }
                @Override public void afterTextChanged(android.text.Editable s) {}
            });
        }

        if (spinnerOrden != null) {
            List<String> opciones = Arrays.asList("Nombre (A-Z)", "Nombre (Z-A)", "Más cuentos");
            android.widget.ArrayAdapter<String> ordenAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
            ordenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerOrden.setAdapter(ordenAdapter);
            spinnerOrden.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) { aplicarFiltrosYOrden(); }
                @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });
        }
    }

    private void cargarAulas() {
        mostrarCargando(true);
        aulasRepository.getAulasDocente(new AulasRepository.AulasCallback<List<ModeloAula>>() {
            @Override
            public void onSuccess(List<ModeloAula> result) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    aulasDatos.clear();
                    if (result != null) aulasDatos.addAll(result);
                    aplicarFiltrosYOrden();
                    actualizarEmptyState();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(GestorAulasActivity.this, "Error al cargar aulas: " + message, Toast.LENGTH_LONG).show();
                    actualizarEmptyState();
                });
            }
        });
    }

    private void aplicarFiltrosYOrden() {
        try {
            String query = editBuscarAula != null && editBuscarAula.getText() != null ? editBuscarAula.getText().toString().toLowerCase() : "";
            aulasFiltradas.clear();
            for (ModeloAula aula : aulasDatos) {
                boolean coincideNombre = query.isEmpty() || (aula.getNombre_aula() != null && aula.getNombre_aula().toLowerCase().contains(query));
                if (coincideNombre) aulasFiltradas.add(aula);
            }

            if (spinnerOrden != null && spinnerOrden.getSelectedItem() != null) {
                String orden = spinnerOrden.getSelectedItem().toString();
                if ("Nombre (A-Z)".equals(orden)) {
                    Collections.sort(aulasFiltradas, (a, b) -> collatorEs.compare(safe(a.getNombre_aula()).trim(), safe(b.getNombre_aula()).trim()));
                } else if ("Nombre (Z-A)".equals(orden)) {
                    Collections.sort(aulasFiltradas, (a, b) -> collatorEs.compare(safe(b.getNombre_aula()).trim(), safe(a.getNombre_aula()).trim()));
                } else if ("Más cuentos".equals(orden)) {
                    Collections.sort(aulasFiltradas, (a, b) -> {
                        int tb = totalCuentosForSort(b);
                        int ta = totalCuentosForSort(a);
                        int comp = Integer.compare(tb, ta);
                        if (comp != 0) return comp;
                        // Desempatar por nombre A-Z para orden estable
                        return collatorEs.compare(safe(a.getNombre_aula()).trim(), safe(b.getNombre_aula()).trim());
                    });
                }
            }

            adapter.notifyDataSetChanged();
            actualizarEmptyState();
        } catch (Exception e) {
            Log.e("GestorAulas", "Error filtrando/ordenando: " + e.getMessage(), e);
        }
    }

    private void eliminarAula(ModeloAula aula) {
        mostrarCargando(true);
        aulasRepository.eliminarAula(aula.getId_aula(), new AulasRepository.AulasCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(GestorAulasActivity.this, "Aula eliminada correctamente", Toast.LENGTH_SHORT).show();
                    cargarAulas();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    mostrarCargando(false);
                    Toast.makeText(GestorAulasActivity.this, "Error al eliminar aula: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        if (recyclerViewAulas != null) recyclerViewAulas.setVisibility(mostrar ? View.GONE : View.VISIBLE);
    }

    private String safe(String s) { return s == null ? "" : s; }

    private void actualizarEmptyState() {
        if (textEmpty == null) return;
        boolean vacio = aulasFiltradas == null || aulasFiltradas.isEmpty();
        textEmpty.setVisibility(vacio ? View.VISIBLE : View.GONE);
        recyclerViewAulas.setVisibility(vacio ? View.GONE : View.VISIBLE);
    }

    private int totalCuentosForSort(ModeloAula aula) {
        if (aula == null) return 0;
        int total = aula.getTotal_cuentos();
        if (total > 0) return total;
        // Si el total viene en 0 pero hay lista de cuentos cargada, usar su tamaño
        java.util.List<com.example.lectana.modelos.CuentoApi> lista = aula.getCuentos();
        return (lista != null) ? lista.size() : 0;
    }
}


