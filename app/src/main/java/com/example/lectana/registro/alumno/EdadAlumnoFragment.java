package com.example.lectana.registro.alumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lectana.Login;
import com.example.lectana.R;
import com.example.lectana.registro.registro_pregunta;


public class EdadAlumnoFragment extends Fragment {

    private ImageView flechaVolver;

    public EdadAlumnoFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edad_alumno, container, false);

        flechaVolver = view.findViewById(R.id.flechaVolverRegistro);

        flechaVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), registro_pregunta.class);
                startActivity(intent);

            }
        });

        return view;
    }
}