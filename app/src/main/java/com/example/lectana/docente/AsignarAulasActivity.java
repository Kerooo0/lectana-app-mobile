package com.example.lectana.docente;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloAula;
import com.example.lectana.repository.ActividadesRepository;
import com.example.lectana.repository.AulasRepository;
import com.example.lectana.auth.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AsignarAulasActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewAulas;
    private Button btnGuardarAsignacion;
    
    private ActividadesRepository actividadesRepository;
    private AulasRepository aulasRepository;
    private SessionManager sessionManager;
    
    private List<ModeloAula> listaAulas;
    private List<ModeloAula> aulasSeleccionadas;
    private AulasSeleccionAdapter adapter;
    
    private int actividadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_aulas);
        
        actividadId = getIntent().getIntExtra("actividad_id", -1);
        
        inicializarComponentes();
        configurarRecyclerView();
        configurarListeners();
        cargarAulas();
    }

    private void inicializarComponentes() {
        recyclerViewAulas = findViewById(R.id.recyclerViewAulas);
        btnGuardarAsignacion = findViewById(R.id.btnGuardarAsignacion);
        
        sessionManager = new SessionManager(this);
        actividadesRepository = new ActividadesRepository(sessionManager);
        aulasRepository = new AulasRepository(sessionManager);
        
        listaAulas = new ArrayList<>();
        aulasSeleccionadas = new ArrayList<>();
    }

    private void configurarRecyclerView() {
        adapter = new AulasSeleccionAdapter(listaAulas, aulasSeleccionadas, new AulasSeleccionAdapter.OnAulaSeleccionListener() {
            @Override
            public void onAulaSeleccionada(ModeloAula aula, boolean seleccionada) {
                if (seleccionada) {
                    if (!aulasSeleccionadas.contains(aula)) {
                        aulasSeleccionadas.add(aula);
                    }
                } else {
                    aulasSeleccionadas.remove(aula);
                }
                adapter.notifyDataSetChanged();
            }
        });
        
        recyclerViewAulas.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAulas.setAdapter(adapter);
    }

    private void configurarListeners() {
        btnGuardarAsignacion.setOnClickListener(v -> guardarAsignacion());
    }

    private void cargarAulas() {
        aulasRepository.getAulasDocente(new AulasRepository.AulasCallback<List<ModeloAula>>() {
            @Override
            public void onSuccess(List<ModeloAula> aulas) {
                runOnUiThread(() -> {
                    listaAulas.clear();
                    listaAulas.addAll(aulas);
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(AsignarAulasActivity.this, "Error al cargar aulas: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void guardarAsignacion() {
        if (aulasSeleccionadas.isEmpty()) {
            Toast.makeText(this, "Debe seleccionar al menos una aula", Toast.LENGTH_SHORT).show();
            return;
        }
        
        List<Integer> aulasIds = new ArrayList<>();
        for (ModeloAula aula : aulasSeleccionadas) {
            aulasIds.add(aula.getId_aula());
        }
        
        actividadesRepository.asignarActividadAulas(actividadId, aulasIds, new ActividadesRepository.ActividadesCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                runOnUiThread(() -> {
                    Toast.makeText(AsignarAulasActivity.this, "Aulas asignadas exitosamente", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(AsignarAulasActivity.this, "Error al asignar aulas: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
}
