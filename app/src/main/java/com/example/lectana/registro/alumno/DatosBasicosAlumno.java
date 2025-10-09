package com.example.lectana.registro.alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
    private ImageView FlechaVolver;
    private Button siguiente;
    private Spinner spinnerPais;
    private  String nombreAlumno,paisAlumno;
    private int edadAlumno;

    public DatosBasicosAlumno() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View vista = inflater.inflate(R.layout.fragment_datos_basicos_alumno, container, false);

        Bundle args = getArguments();
        if (args != null) {
            String edadSeleccionada = args.getString("edadSeleccionada");
            Log.d("DatosBasicosAlumno", "Edad seleccionada: " + edadSeleccionada);
        }



        FlechaVolver = vista.findViewById(R.id.flechaVolverRegistro);

        siguiente = vista.findViewById(R.id.boton_registrarse);

        nombre = vista.findViewById(R.id.editTextNombreAlumno);

        edad = vista.findViewById(R.id.editTextEdadAlumno);

        spinnerPais = vista.findViewById(R.id.spinnerPais);

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

            boolean resultado = validarDatosEditText();

            if (resultado){

                Bundle bundle = new Bundle();
                bundle.putString("nombreAlumno", nombreAlumno);
                bundle.putInt("edadAlumno", edadAlumno);
                bundle.putString("paisAlumno", paisAlumno);

                Fragment avanzar = new DatosAccesoAlumno();

                avanzar.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, avanzar);

                cambioDeFragment.commit();
                }
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

    private boolean validarDatosEditText(){
        nombreAlumno = nombre.getText().toString().trim();
        String edadAlumnoTexto =  edad.getText().toString().trim();
        paisAlumno = spinnerPais.getSelectedItem().toString();



        if(nombreAlumno.isEmpty() || edadAlumnoTexto.isEmpty()) {
            Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }


        edadAlumno = Integer.parseInt(edadAlumnoTexto);

        return true;
    }


}