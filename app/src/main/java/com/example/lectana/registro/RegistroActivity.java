package com.example.lectana.registro;

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
import com.example.lectana.registro.alumno.EdadAlumnoFragment;
import com.example.lectana.registro.docente.DatosPersonalesDocenteFragment;

public class RegistroActivity extends AppCompatActivity {

    TextView volverLogin, soporte, tenesCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);



        volverLogin = findViewById(R.id.IniciarSesionRegistro);
        soporte = findViewById(R.id.soporteRegistro);
        tenesCuenta = findViewById(R.id.textoTenesCuenta);


        volverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroActivity.this, Login.class);
                startActivity(intent);
            }
        });

        soporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistroActivity.this, centro_ayuda.class);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            String seleccion = getIntent().getStringExtra("seleccion");

            if ("Docente".equalsIgnoreCase(seleccion)) {
                //Fragment de docente
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new DatosPersonalesDocenteFragment())
                        .commit();

            } else {
                //Fragment de alumno
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, new EdadAlumnoFragment())
                        .commit();
            }
        }

    }

    public void setFooterVisibility(boolean mostrarVolver, boolean mostrarTenesCuenta) {
        volverLogin.setVisibility(mostrarVolver ? View.VISIBLE : View.GONE);
        tenesCuenta.setVisibility(mostrarTenesCuenta ? View.VISIBLE : View.GONE);
    }

}