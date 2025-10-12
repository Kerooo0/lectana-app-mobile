package com.example.lectana.registro.alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lectana.R;
import com.example.lectana.clases.validaciones.ValidacionesPassword;
import com.example.lectana.model.DatosRegistroAlumno;


public class DatosAccesoAlumno extends Fragment {


    public DatosAccesoAlumno() {
        // Required empty public constructor
    }

    private String pass1 = "",nombreAlumno = "",paisAlumno = "",emailAlumno = "", apellidoAlumno = "";
    private int edadAlumno = 0;
    private TextView estadoPassword,passwordCoincidencia, errorPassword;
    private boolean passwordsValidas = false, esValida = false;
    private DatosRegistroAlumno datosRegistro = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_datos_acceso_alumno, container, false);


        if (getArguments() != null) {
            datosRegistro = (DatosRegistroAlumno) getArguments().getSerializable("datosAlumno");
        }
        if (datosRegistro == null) {
            datosRegistro = new DatosRegistroAlumno();
        }

        ProgressBar barraDeProgreso = vista.findViewById(R.id.barraProgreso);
        barraDeProgreso.setProgress(2);

        TextView barraProgresoPaso = vista.findViewById(R.id.barraProgresoPaso);
        barraProgresoPaso.setText(getText(R.string.pasoDos));

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);

        Button siguiente = vista.findViewById(R.id.boton_registrarse);

        EditText password = vista.findViewById(R.id.passwordAlumno);

        errorPassword = vista.findViewById(R.id.errorPasswordEstudiante);

        estadoPassword = vista.findViewById(R.id.estadoPasswordAlumno);

        passwordCoincidencia = vista.findViewById(R.id.repetirPasswordIncorrectaAlumno);

        EditText coincidenciaPasswordAlumno = vista.findViewById(R.id.editTextRepetirPasswordAlumno);

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

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (passwordsValidas && esValida) {
                    datosRegistro.setPassword(pass1);

                    confirmacionDatosAlumno(datosRegistro);
                }

            }
        });



        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass1 = password.getText().toString();
                String pass2 = coincidenciaPasswordAlumno.getText().toString();


                esValida = ValidacionesPassword.esPasswordValida(pass1);


                ValidacionesPassword.mostrarEstadoPassword(estadoPassword, esValida, errorPassword, pass1);


                validarCoincidencia(pass1, pass2);
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void afterTextChanged(Editable s) {}
        };

        password.addTextChangedListener(watcher);
        coincidenciaPasswordAlumno.addTextChangedListener(watcher);

        return vista;
    }

    private void validarCoincidencia(String pass1, String pass2) {
        boolean coinciden = ValidacionesPassword.sonPasswordsIguales(pass1, pass2);
        passwordsValidas =  ValidacionesPassword.mostrarCoincidenciaPasswords(
                passwordCoincidencia,
                coinciden,
                "Coinciden",
                "No coinciden"
        );
    }

    private void confirmacionDatosAlumno(DatosRegistroAlumno datosRegistro) {
        Fragment siguiente = new ConfirmacionDatosAlumnos();
        Bundle bundle = new Bundle();
        bundle.putSerializable("datosAlumno", datosRegistro);
        siguiente.setArguments(bundle);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
        cambioDeFragment.replace(R.id.frameLayout, siguiente);
        cambioDeFragment.commit();
    }
}