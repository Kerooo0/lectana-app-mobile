package com.example.lectana.registro.alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lectana.R;


public class ConfirmacionDatosAlumnos extends Fragment {

    public ConfirmacionDatosAlumnos() {

    }

    private String nombre,pais,password;
    private int edad=0;
    private TextView valorEmail,valorEdad,valorPais;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_confirmacion_datos_alumnos, container, false);

        Bundle args = getArguments();
        if (args != null) {
            nombre = args.getString("nombreAlumno");
            edad = args.getInt("edadAlumno");
            pais = args.getString("paisAlumno");
            password = args.getString("passwordAlumno");
        }

        ProgressBar barraDeProgreso = vista.findViewById(R.id.barraProgreso);
        barraDeProgreso.setProgress(3);

        TextView barraProgresoPaso = vista.findViewById(R.id.barraProgresoPaso);
        barraProgresoPaso.setText(getText(R.string.pasoTres));

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);

        Button siguiente = vista.findViewById(R.id.boton_registrarse);

        valorEmail = vista.findViewById(R.id.valorEmailAlumno);

        valorEdad = vista.findViewById(R.id.valorEdadAlumno);

        valorPais = vista.findViewById(R.id.valorPaisAlumno);

        datosAlumno(valorEmail,valorEdad,valorPais);

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment volver = new DatosAccesoAlumno();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, volver);

                cambioDeFragment.commit();

            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment crear = new CuentaCreadaAlumnoFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, crear);

                cambioDeFragment.commit();

            }
        });

        return vista;

    }

    private void datosAlumno(TextView valorEmail, TextView valorEdad, TextView valorPais){

        valorEmail.setText(nombre);

        valorEdad.setText(String.valueOf(edad));

        valorPais.setText(pais);
    }

}