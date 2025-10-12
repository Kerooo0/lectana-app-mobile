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
import com.example.lectana.model.DatosRegistroAlumno;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class DatosBasicosAlumno extends Fragment {

    private EditText nombre,edad,apellido,email;
    private ImageView FlechaVolver;
    private Button siguiente;
    private Spinner spinnerPais;
    private  String nombreAlumno,paisAlumno,apellidoAlumno,emailAlumno;
    private int edadAlumno;
    private DatosRegistroAlumno datosRegistro;

    public DatosBasicosAlumno() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View vista = inflater.inflate(R.layout.fragment_datos_basicos_alumno, container, false);

        if (getArguments() != null) {
            datosRegistro = (DatosRegistroAlumno) getArguments().getSerializable("datosAlumno");
        }
        if (datosRegistro == null) {
            datosRegistro = new DatosRegistroAlumno();
        }

        inicializarCampos(vista);


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


        siguiente.setOnClickListener(v -> {
            if (validarDatosEditText()) {
                guardarDatos();

                Fragment avanzar = new DatosAccesoAlumno();
                Bundle bundle = new Bundle();
                bundle.putSerializable("datosAlumno", datosRegistro);
                avanzar.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, avanzar)
                        .commit();
            }
        });


        return vista;
    }

    private void inicializarCampos(View vista) {
        nombre = vista.findViewById(R.id.editTextNombreAlumno);
        edad = vista.findViewById(R.id.editTextEdadAlumno);
        apellido = vista.findViewById(R.id.editTextApellidoAlumno);
        email = vista.findViewById(R.id.editTextEmailAlumno);
        spinnerPais = vista.findViewById(R.id.spinnerPais);
        listaPaises(spinnerPais);
        FlechaVolver = vista.findViewById(R.id.flechaVolverRegistro);
        siguiente = vista.findViewById(R.id.boton_registrarse);
    }

    private void guardarDatos() {
        datosRegistro.setNombre(nombre.getText().toString().trim());
        datosRegistro.setApellido(apellido.getText().toString().trim());
        datosRegistro.setEmail(email.getText().toString().trim());
        datosRegistro.setEdad(Integer.parseInt(edad.getText().toString().trim()));
        datosRegistro.setNacionalidad(spinnerPais.getSelectedItem().toString());
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
        apellidoAlumno = apellido.getText().toString().trim();
        emailAlumno = email.getText().toString().trim();

        if(nombreAlumno.isEmpty() || edadAlumnoTexto.isEmpty() || apellidoAlumno.isEmpty() || emailAlumno.isEmpty()) {
            Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }


        edadAlumno = Integer.parseInt(edadAlumnoTexto);

        return true;
    }


}