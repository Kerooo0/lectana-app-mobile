package com.example.lectana.registro.docente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        textoRegistro.setText(getString(R.string.registroDocente));

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), registro_pregunta.class);
                startActivity(intent);

            }
        });


        return vista;
    }
}