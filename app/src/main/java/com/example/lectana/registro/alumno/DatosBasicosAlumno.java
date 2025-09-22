package com.example.lectana.registro.alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.R;


public class DatosBasicosAlumno extends Fragment {

    private EditText nombre,edad;

    public DatosBasicosAlumno() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_datos_basicos_alumno, container, false);


        ImageView FlechaVolver = vista.findViewById(R.id.flechaVolverRegistro);

        Button siguiente = vista.findViewById(R.id.boton_registrarse);

        nombre = vista.findViewById(R.id.editTextNombreAlumno);

        edad = vista.findViewById(R.id.editTextEdadAlumno);

        FlechaVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment volver = new EdadAlumnoFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, volver);

                cambioDeFragment.commit();

            }
        });


        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment avanzar = new DatosAccesoAlumno();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, avanzar);

                cambioDeFragment.commit();

            }
        });


        return vista;
    }


}