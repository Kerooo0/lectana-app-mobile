package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.registro.registro_pregunta;

public class Login extends AppCompatActivity {

    TextView registro;
    Button boton_iniciar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        registro = findViewById(R.id.txtRegister);
        boton_iniciar_sesion = findViewById(R.id.btnLogin);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, registro_pregunta.class);
                startActivity(intent);
            }
        });

        // Botón temporal para probar Panel Estudiante
        boton_iniciar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, PanelEstudianteActivity.class);
                startActivity(intent);
            }
        });
    }
}