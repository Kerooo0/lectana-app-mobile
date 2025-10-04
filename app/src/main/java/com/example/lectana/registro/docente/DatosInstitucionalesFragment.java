package com.example.lectana.registro.docente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lectana.R;


public class DatosInstitucionalesFragment extends Fragment {



    public DatosInstitucionalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_datos_institucionales, container, false);

        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);

        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);

        barra.setProgress(2);

        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);

        progreso.setText(getString(R.string.pasoDos));

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);

        textoRegistro.setText(getString(R.string.registroDocente));

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment Volver = new DatosPersonalesDocenteFragment();
                FragmentManager fragmentManager = getParentFragmentManager();

                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, Volver);

                cambioDeFragment.commit();

            }
        });

        return vista;
    }
}