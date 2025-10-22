package com.example.lectana.docente;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.R;
import com.example.lectana.modelos.Actividad;

public class DetalleActividadActivity extends AppCompatActivity {
    private static final String TAG = "DetalleActividad";
    
    private TextView textDescripcion;
    private TextView textTipo;
    private TextView textFechaCreacion;
    private TextView textCuento;
    private TextView textTotalPreguntas;
    private TextView textTotalAulas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "=== INICIANDO DetalleActividadActivity ===");
        Log.d(TAG, "Intent extras: " + (getIntent().getExtras() != null ? getIntent().getExtras().toString() : "null"));
        
        try {
            Log.d(TAG, "Intentando cargar layout activity_detalle_actividad...");
            setContentView(R.layout.activity_detalle_actividad);
            Log.d(TAG, "Layout activity_detalle_actividad cargado correctamente");
        } catch (Exception e) {
            Log.e(TAG, "=== ERROR CRÍTICO CARGANDO LAYOUT ===", e);
            Log.e(TAG, "Tipo de error: " + e.getClass().getSimpleName());
            Log.e(TAG, "Mensaje: " + e.getMessage());
            if (e.getCause() != null) {
                Log.e(TAG, "Causa: " + e.getCause().getMessage());
            }
            finish();
            return;
        }
        
        try {
            Log.d(TAG, "Inicializando componentes...");
            inicializarComponentes();
            
            Log.d(TAG, "Cargando datos...");
            cargarDatos();
            
            Log.d(TAG, "DetalleActividadActivity inicializada completamente");
        } catch (Exception e) {
            Log.e(TAG, "Error durante la inicialización", e);
            finish();
        }
    }

    private void inicializarComponentes() {
        Log.d(TAG, "Inicializando componentes de UI...");
        
        try {
            textDescripcion = findViewById(R.id.textDescripcion);
            Log.d(TAG, "textDescripcion: " + (textDescripcion != null ? "OK" : "NULL"));
            
            textTipo = findViewById(R.id.textTipo);
            Log.d(TAG, "textTipo: " + (textTipo != null ? "OK" : "NULL"));
            
            textFechaCreacion = findViewById(R.id.textFechaCreacion);
            Log.d(TAG, "textFechaCreacion: " + (textFechaCreacion != null ? "OK" : "NULL"));
            
            textCuento = findViewById(R.id.textCuento);
            Log.d(TAG, "textCuento: " + (textCuento != null ? "OK" : "NULL"));
            
            textTotalPreguntas = findViewById(R.id.textTotalPreguntas);
            Log.d(TAG, "textTotalPreguntas: " + (textTotalPreguntas != null ? "OK" : "NULL"));
            
            textTotalAulas = findViewById(R.id.textTotalAulas);
            Log.d(TAG, "textTotalAulas: " + (textTotalAulas != null ? "OK" : "NULL"));
            
            Log.d(TAG, "Componentes inicializados correctamente");
        } catch (Exception e) {
            Log.e(TAG, "Error inicializando componentes", e);
            throw e;
        }
    }

    private void cargarDatos() {
        Log.d(TAG, "Cargando datos de la actividad...");
        
        try {
            int actividadId = getIntent().getIntExtra("actividad_id", -1);
            Log.d(TAG, "ID de actividad recibido: " + actividadId);
            
            if (actividadId != -1) {
                Log.d(TAG, "Cargando datos de ejemplo para actividad ID: " + actividadId);
                
                // Aquí cargarías los datos de la actividad desde el backend
                // Por ahora mostrar datos de ejemplo
                if (textDescripcion != null) textDescripcion.setText("Actividad de comprensión lectora");
                if (textTipo != null) textTipo.setText("Opción Múltiple");
                if (textFechaCreacion != null) textFechaCreacion.setText("15/01/2024");
                if (textCuento != null) textCuento.setText("El Gato Con Botas");
                if (textTotalPreguntas != null) textTotalPreguntas.setText("3 preguntas");
                if (textTotalAulas != null) textTotalAulas.setText("1 aula asignada");
                
                Log.d(TAG, "Datos cargados correctamente");
            } else {
                Log.e(TAG, "Error: No se recibió ID de actividad válido");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error cargando datos", e);
            throw e;
        }
    }
}

