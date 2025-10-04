package com.example.lectana.registro.docente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lectana.R;
import com.example.lectana.registro.registro_pregunta;

public class DatosPersonalesDocenteFragment extends Fragment {




    public DatosPersonalesDocenteFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_datos_personales_docente, container, false);

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);

        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);

        Button siguiente = vista.findViewById(R.id.btn_siguiente_datos_personales);

        textoRegistro.setText(getString(R.string.registroDocente));

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), registro_pregunta.class);
                startActivity(intent);

            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment Siguiente = new DatosInstitucionalesFragment();
                FragmentManager fragmentManager = getParentFragmentManager();

                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, Siguiente);

                cambioDeFragment.commit();
            }
        });

        return vista;
    }
}