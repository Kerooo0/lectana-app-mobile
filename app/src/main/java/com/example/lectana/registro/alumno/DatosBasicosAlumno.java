package com.example.lectana.registro.alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


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

        Spinner spinnerPais = vista.findViewById(R.id.spinnerPais);

        listaPaises(spinnerPais);

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

    private void listaPaises(Spinner spinner) {
        String[] paises = Locale.getISOCountries();
        List<String> nombrePaises = new ArrayList<>();

        for (String codigoPais : paises) {
            Locale locale = new Locale("", codigoPais);
            String nombrePais = locale.getDisplayCountry(new Locale("es", "ES"));
            nombrePaises.add(nombrePais);
        }

        Collections.sort(nombrePaises);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                nombrePaises
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


}