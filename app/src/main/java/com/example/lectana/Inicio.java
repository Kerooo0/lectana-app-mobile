package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lectana.estudiante.PanelEstudianteActivity;
import com.example.lectana.docente.PantallaPrincipalDocente;

public class Inicio extends AppCompatActivity {

    Button boton;
    Button botonIrPanelEstudiante;
    Button botonIrPanelDocente;
    private static final String BASE_URL = "https://lectana-backend.onrender.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);

        boton = findViewById(R.id.boton_inicio);
        botonIrPanelEstudiante = findViewById(R.id.boton_ir_panel_estudiante);
        botonIrPanelDocente = findViewById(R.id.boton_ir_panel_docente);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, Login.class);
                startActivity(intent);
            }
        });

        // Botón temporal: ir directo al Panel Estudiante
        botonIrPanelEstudiante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, PanelEstudianteActivity.class);
                startActivity(intent);
            }
        });

        // Botón temporal: ir directo al Panel Docente
        botonIrPanelDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, PantallaPrincipalDocente.class);
                startActivity(intent);
            }
        });
    }
}