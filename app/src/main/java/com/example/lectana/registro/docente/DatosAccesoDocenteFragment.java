package com.example.lectana.registro.docente;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lectana.R;

public class DatosAccesoDocenteFragment extends Fragment {


    public DatosAccesoDocenteFragment() {
        // Required empty public constructor
    }

    private TextView estadoPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_datos_acceso_docente, container, false);

        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);

        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);

        barra.setProgress(3);

        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);

        progreso.setText(getString(R.string.pasoTres));

        EditText password = vista.findViewById(R.id.editTextpasswordDocente);

        estadoPassword = vista.findViewById(R.id.resultadoPassword);

        password.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String password = s.toString();
                String regex = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=!]).*$";

                int length = s.length();
                if (password.matches(regex)) {
                    estadoPassword.setText(getString(R.string.buena));
                    estadoPassword.setTextColor(Color.GREEN);
                } else {
                    estadoPassword.setText(getString(R.string.malo));
                    estadoPassword.setTextColor(Color.RED);
                }
            }

                @Override
                public void afterTextChanged(Editable s) {

            }
        });

        return vista;
    }
}