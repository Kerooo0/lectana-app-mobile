package com.example.lectana.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lectana.Inicio;
import com.example.lectana.Login;
import com.example.lectana.R;




public class registro_pregunta extends AppCompatActivity {

    String opcionSeleccionada = null;
    CardView cardDocente, cardAlumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_pregunta);


        cardDocente = findViewById(R.id.cardDocente);
        cardAlumno = findViewById(R.id.cardAlumno);


        cardDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opcionSeleccionada = "docente";
                cambioEstado(opcionSeleccionada);
            }

        });


        cardAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opcionSeleccionada = "alumno";
                cambioEstado(opcionSeleccionada);
            }

        });

    }


    private void cambioEstado(String opcion){

      if (opcionSeleccionada.equalsIgnoreCase("docente")){

          cardDocente.setSelected(true);
          cardAlumno.setSelected(false);


      } else {
          cardAlumno.setSelected(true);
          cardDocente.setSelected(false);

      }





    }




}