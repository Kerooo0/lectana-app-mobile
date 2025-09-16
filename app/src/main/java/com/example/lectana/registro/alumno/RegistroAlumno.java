package com.example.lectana.registro.alumno;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lectana.Login;
import com.example.lectana.R;
import com.example.lectana.centro_ayuda;
import com.example.lectana.registro.registro_pregunta;

public class RegistroAlumno extends AppCompatActivity {

    TextView volverLogin, soporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_alumno);


        volverLogin = findViewById(R.id.IniciarSesionRegistro);
        soporte = findViewById(R.id.soporteRegistro);

        volverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroAlumno.this, Login.class);
                startActivity(intent);
            }
        });

        soporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroAlumno.this, centro_ayuda.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new EdadAlumnoFragment())
                    .commit();
        }


    }

}
