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

    private EditText nombre, apellido, email, edad;
    private ImageView FlechaVolver;
    private Button siguiente;
    private Spinner spinnerPais;
    private String nombreAlumno, apellidoAlumno, emailAlumno, paisAlumno;
    private int edadAlumno;
    private RegistroAlumnoManager registroManager;

    public DatosBasicosAlumno() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View vista = inflater.inflate(R.layout.fragment_datos_basicos_alumno, container, false);

        // Inicializar el manager
        registroManager = RegistroAlumnoManager.getInstance(requireContext());

        Bundle args = getArguments();
        if (args != null) {
            String edadSeleccionada = args.getString("edadSeleccionada");
            Log.d("DatosBasicosAlumno", "Edad seleccionada: " + edadSeleccionada);
        }

        FlechaVolver = vista.findViewById(R.id.flechaVolverRegistro);
        siguiente = vista.findViewById(R.id.boton_registrarse);
        nombre = vista.findViewById(R.id.editTextNombreAlumno);
        apellido = vista.findViewById(R.id.editTextApellidoAlumno);
        email = vista.findViewById(R.id.editTextEmailAlumno);
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
                // Guardar datos en el manager
                String grado = calcularGradoPorEdad(edadAlumno);
                String fechaNacimiento = calcularFechaNacimiento(edadAlumno);
                
                registroManager.guardarDatosBasicos(
                    nombreAlumno, 
                    apellidoAlumno, 
                    edadAlumno, 
                    paisAlumno,
                    fechaNacimiento,
                    grado
                );
                
                Log.d("DatosBasicosAlumno", "Datos guardados: " + nombreAlumno + " " + apellidoAlumno + 
                      ", edad: " + edadAlumno + ", email: " + emailAlumno + ", grado: " + grado);

                Fragment avanzar = new DatosAccesoAlumno();
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
        apellidoAlumno = apellido.getText().toString().trim();
        emailAlumno = email.getText().toString().trim();
        String edadAlumnoTexto = edad.getText().toString().trim();
        paisAlumno = spinnerPais.getSelectedItem().toString();

        if(nombreAlumno.isEmpty() || apellidoAlumno.isEmpty() || emailAlumno.isEmpty() || edadAlumnoTexto.isEmpty()) {
            Toast.makeText(getContext(), "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailAlumno).matches()) {
            Toast.makeText(getContext(), "Por favor ingresa un email válido", Toast.LENGTH_SHORT).show();
            return false;
        }

        edadAlumno = Integer.parseInt(edadAlumnoTexto);
        
        // Guardar email en el manager
        registroManager.getAlumnoRegistro().setEmail(emailAlumno);

        return true;
    }
    
    private String calcularGradoPorEdad(int edad) {
        // Calcular grado aproximado basado en edad
        if (edad <= 5) return "Preescolar";
        else if (edad <= 6) return "1°";
        else if (edad <= 7) return "2°";
        else if (edad <= 8) return "3°";
        else if (edad <= 9) return "4°";
        else if (edad <= 10) return "5°";
        else if (edad <= 11) return "6°";
        else if (edad <= 12) return "7°";
        else if (edad <= 13) return "8°";
        else if (edad <= 14) return "9°";
        else if (edad <= 15) return "10°";
        else if (edad <= 16) return "11°";
        else return "12°";
    }
    
    private String calcularFechaNacimiento(int edad) {
        // Calcular fecha de nacimiento aproximada (año actual - edad)
        int anioActual = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int anioNacimiento = anioActual - edad;
        // Retornar en formato YYYY-MM-DD
        return anioNacimiento + "-01-01";
    }

}