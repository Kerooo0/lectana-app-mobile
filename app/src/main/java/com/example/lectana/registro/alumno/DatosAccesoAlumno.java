package com.example.lectana.registro.alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lectana.R;


public class DatosAccesoAlumno extends Fragment {


    public DatosAccesoAlumno() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_datos_acceso_alumno, container, false);

        ProgressBar barraDeProgreso = vista.findViewById(R.id.barraProgreso);
        barraDeProgreso.setProgress(2);

        TextView barraProgresoPaso = vista.findViewById(R.id.barraProgresoPaso);
        barraProgresoPaso.setText(getText(R.string.pasoDos));

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment volver = new DatosBasicosAlumno();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, volver);

                cambioDeFragment.commit();

            }
        });

        return vista;
    }
}