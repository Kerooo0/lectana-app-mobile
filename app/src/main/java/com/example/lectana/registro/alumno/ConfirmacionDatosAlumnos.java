package com.example.lectana.registro.alumno;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lectana.R;
import com.example.lectana.model.DatosRegistroAlumno;

public class ConfirmacionDatosAlumnos extends Fragment {

    private DatosRegistroAlumno datosRegistro;

    private TextView valorEmail, valorEdad, valorPais, valorNombre, valorApellido;

    public ConfirmacionDatosAlumnos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_confirmacion_datos_alumnos, container, false);


        if (getArguments() != null) {
            datosRegistro = (DatosRegistroAlumno) getArguments().getSerializable("datosAlumno");
        }


        if (datosRegistro == null) {
            datosRegistro = new DatosRegistroAlumno();
        }


        ProgressBar barraDeProgreso = vista.findViewById(R.id.barraProgreso);
        barraDeProgreso.setProgress(3);

        TextView barraProgresoPaso = vista.findViewById(R.id.barraProgresoPaso);
        barraProgresoPaso.setText(getText(R.string.pasoTres));

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);
        Button siguiente = vista.findViewById(R.id.Confirmar_registro);


        valorEmail = vista.findViewById(R.id.valorEmailAlumno);
        valorEdad = vista.findViewById(R.id.valorEdadAlumno);
        valorPais = vista.findViewById(R.id.valorPaisAlumno);
        valorNombre = vista.findViewById(R.id.valorNombreAlumno);
        valorApellido = vista.findViewById(R.id.valorApellidoAlumno);


        mostrarDatosAlumno();


        volver.setOnClickListener(v -> {
            Fragment volverFragment = new DatosAccesoAlumno();
            Bundle bundle = new Bundle();
            bundle.putSerializable("datosAlumno", datosRegistro); // ← Se mantiene el progreso
            volverFragment.setArguments(bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
            cambioDeFragment.replace(R.id.frameLayout, volverFragment);
            cambioDeFragment.commit();
        });


        siguiente.setOnClickListener(v -> {
            Fragment crear = new CuentaCreadaAlumnoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("datosAlumno", datosRegistro);
            crear.setArguments(bundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
            cambioDeFragment.replace(R.id.frameLayout, crear);
            cambioDeFragment.commit();
        });

        return vista;
    }



    private void mostrarDatosAlumno() {
        valorNombre.setText(datosRegistro.getNombre());
        valorApellido.setText(datosRegistro.getApellido());
        valorEmail.setText(datosRegistro.getEmail());
        valorEdad.setText(String.valueOf(datosRegistro.getEdad()));
        valorPais.setText(datosRegistro.getNacionalidad());
    }
}
