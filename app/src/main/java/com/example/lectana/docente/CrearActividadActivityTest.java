package com.example.lectana.docente;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class CrearActividadActivityTest extends Activity {
    private static final String TAG = "CrearActividadTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(TAG, "=== INICIANDO CrearActividadActivityTest ===");
        Log.d(TAG, "Intent extras: " + (getIntent().getExtras() != null ? getIntent().getExtras().toString() : "null"));
        
        try {
            // Crear una vista simple programáticamente
            TextView textView = new TextView(this);
            textView.setText("CrearActividadActivityTest - Funcionando correctamente");
            textView.setTextSize(18);
            textView.setPadding(50, 50, 50, 50);
            setContentView(textView);
            
            Log.d(TAG, "Vista simple creada correctamente");
            
            // Verificar si es modo edición
            Intent intent = getIntent();
            if (intent.hasExtra("modo_edicion") && intent.getBooleanExtra("modo_edicion", false)) {
                int actividadId = intent.getIntExtra("actividad_id", -1);
                Log.d(TAG, "=== MODO EDICIÓN ACTIVADO ===");
                Log.d(TAG, "ID de actividad a editar: " + actividadId);
                Toast.makeText(this, "Modo edición - ID: " + actividadId, Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "=== MODO CREACIÓN ===");
                Toast.makeText(this, "Modo creación", Toast.LENGTH_LONG).show();
            }
            
            Log.d(TAG, "CrearActividadActivityTest inicializada completamente");
            
        } catch (Exception e) {
            Log.e(TAG, "=== ERROR CRÍTICO ===", e);
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
