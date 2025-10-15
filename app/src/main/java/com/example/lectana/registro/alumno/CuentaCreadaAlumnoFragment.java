package com.example.lectana.registro.alumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lectana.R;
import com.example.lectana.registro.RegistroActivity;
import com.example.lectana.estudiante.PanelEstudianteActivity;


public class CuentaCreadaAlumnoFragment extends Fragment {

    public CuentaCreadaAlumnoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_cuenta_creada_alumno, container, false);

        // Cambiar visibilidad de footer al entrar en este fragment
        if (getActivity() instanceof RegistroActivity) {
            ((RegistroActivity) getActivity()).setFooterVisibility(false, false);
        }

        // Configurar botón "Explorar Biblioteca"
        Button btnExplorarBiblioteca = vista.findViewById(R.id.button);
        btnExplorarBiblioteca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar al Panel Principal del Estudiante
                Intent intent = new Intent(getActivity(), PanelEstudianteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                
                // Cerrar la actividad de registro
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });

        return vista;
    }
}