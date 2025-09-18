package com.example.lectana.registro.alumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


        View opcion1 = view.findViewById(R.id.opcion1);
        ImageView icono1 = opcion1.findViewById(R.id.opcion_icon);
        TextView texto1 = opcion1.findViewById(R.id.opcion_text);
        icono1.setImageResource(R.drawable.monio);
        texto1.setText(getString(R.string.preescolar));
        TextView textoedad1 = opcion1.findViewById(R.id.opcion_edad);
        textoedad1.setText(getString(R.string.edadPreescolar));


        View opcion2 = view.findViewById(R.id.opcion2);
        ImageView icono2 = opcion2.findViewById(R.id.opcion_icon);
        TextView texto2 = opcion2.findViewById(R.id.opcion_text);
        icono2.setImageResource(R.drawable.rompe_cabezas);
        texto2.setText(getString(R.string.primaria));
        TextView textoedad2 = opcion2.findViewById(R.id.opcion_edad);
        textoedad2.setText(getString(R.string.edadPrimaria));


        View opcion3 = view.findViewById(R.id.opcion3);
        ImageView icono3 = opcion3.findViewById(R.id.opcion_icon);
        TextView texto3 = opcion3.findViewById(R.id.opcion_text);
        icono3.setImageResource(R.drawable.mochila);
        texto3.setText(getString(R.string.primariaInicial));
        TextView textoedad3 = opcion3.findViewById(R.id.opcion_edad);
        textoedad3.setText(getString(R.string.edadPrimariaInicial));


        View opcion4 = view.findViewById(R.id.opcion4);
        ImageView icono4 = opcion4.findViewById(R.id.opcion_icon);
        TextView texto4 = opcion4.findViewById(R.id.opcion_text);
        icono4.setImageResource(R.drawable.sombrero);
        texto4.setText(getString(R.string.secundaria));
        TextView textoedad4 = opcion4.findViewById(R.id.opcion_edad);
        textoedad4.setText(getString(R.string.edadSecundaria));

        View opcion5 = view.findViewById(R.id.opcion5);
        ImageView icono5 = opcion5.findViewById(R.id.opcion_icon);
        TextView texto5 = opcion5.findViewById(R.id.opcion_text);
        icono5.setImageResource(R.drawable.libro);
        texto5.setText(getString(R.string.avanzado));
        TextView textoedad5 = opcion5.findViewById(R.id.opcion_edad);
        textoedad5.setText(R.string.edadAvanzado);

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