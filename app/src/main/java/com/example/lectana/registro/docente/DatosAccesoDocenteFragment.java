package com.example.lectana.registro.docente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lectana.R;
import com.example.lectana.clases.validaciones.ValidacionesPassword;
import com.example.lectana.registro.alumno.DatosBasicosAlumno;

public class DatosAccesoDocenteFragment extends Fragment {


    public DatosAccesoDocenteFragment() {
        // Required empty public constructor
    }
    private String pass1 = "";
    private TextView estadoPassword, coincidenciaPasswordDocente, errorPassword;
    private boolean passwordsValidas = false, esValida = false;
    private Button siguiente;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_datos_acceso_docente, container, false);

        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);

        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);

        barra.setProgress(3);

        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);

        progreso.setText(getString(R.string.pasoTres));

        textoRegistro.setText(getString(R.string.registroDocente));

        EditText password = vista.findViewById(R.id.editTextpasswordDocente);

        estadoPassword = vista.findViewById(R.id.resultadoPassword);

        EditText passwordCoincidencia = vista.findViewById(R.id.editTextRepetirpasswordDocente);

        errorPassword = vista.findViewById(R.id.errorPasswordDocente);

        coincidenciaPasswordDocente = vista.findViewById(R.id.coincidenciaPasswordDocente);

        siguiente = vista.findViewById(R.id.continuarDocente);

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if (passwordsValidas && esValida) {

               // Fragment Confirmar = new ();
               //  FragmentManager fragmentManager = getParentFragmentManager();
              //  FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

              //  cambioDeFragment.replace(R.id.frameLayout, Confirmar);

              //  cambioDeFragment.commit();

                // }

            }
        });


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass1 = password.getText().toString();
                String pass2 = passwordCoincidencia.getText().toString();


                esValida = ValidacionesPassword.esPasswordValida(pass1);
                ValidacionesPassword.mostrarEstadoPassword(estadoPassword, esValida,errorPassword,pass1);


                validarCoincidencia(pass1, pass2);
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override public void afterTextChanged(Editable s) {}
        };


        password.addTextChangedListener(watcher);
        passwordCoincidencia.addTextChangedListener(watcher);

        return vista;
    }


    private void validarCoincidencia(String pass1, String pass2) {
        boolean coinciden = ValidacionesPassword.sonPasswordsIguales(pass1, pass2);
        passwordsValidas = ValidacionesPassword.mostrarCoincidenciaPasswords(
                coincidenciaPasswordDocente,
                coinciden,
                "Coinciden",
                "No coinciden"
        );
    }
}