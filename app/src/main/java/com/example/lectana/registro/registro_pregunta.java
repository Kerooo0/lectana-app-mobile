package com.example.lectana.registro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.lectana.Login;
import com.example.lectana.R;



public class registro_pregunta extends AppCompatActivity {




    String opcionSeleccionada = null;
    CardView cardDocente, cardAlumno;
    ImageView volver;
    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_pregunta);


        cardDocente = findViewById(R.id.cardDocente);
        cardAlumno = findViewById(R.id.cardAlumno);
        volver = findViewById(R.id.flechaRegistro);
        btnContinuar = findViewById(R.id.btnOpcionRegistro);

        if (savedInstanceState != null) {
            opcionSeleccionada = savedInstanceState.getString("opcionSeleccionada");
            if (opcionSeleccionada != null) {
                cambioEstado(opcionSeleccionada);
            }
        }


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

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(registro_pregunta.this,Login.class);

                startActivity(intent);
            }

        });


        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registroOpcion();

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


    private void registroOpcion(){

        if(opcionSeleccionada != null){

            if( opcionSeleccionada.equalsIgnoreCase("docente") ){

                Toast.makeText(registro_pregunta.this, "Vas hacia registro docente", Toast.LENGTH_SHORT).show();

            } else {

                Intent intent = new Intent(registro_pregunta.this, RegistroActivity.class);

                startActivity(intent);

            }

        } else {

            Toast.makeText(registro_pregunta.this, "Debes seleccionar una opción", Toast.LENGTH_SHORT).show();

        }





    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("opcionSeleccionada", opcionSeleccionada);
    }


}