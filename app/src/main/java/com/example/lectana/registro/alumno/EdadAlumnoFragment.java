package com.example.lectana.registro.alumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.R;
import com.example.lectana.registro.registro_pregunta;


public class EdadAlumnoFragment extends Fragment {

    private String seleccionadaNombre = "";
    private final int[] opcionIds = {R.id.preescolar, R.id.primariaInicial, R.id.primaria, R.id.secundaria, R.id.avanzado};
    private final int[] ids = {R.id.preescolar, R.id.primariaInicial, R.id.primaria, R.id.secundaria, R.id.avanzado};
    public EdadAlumnoFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edades_alumno, container, false);

        ImageView flechaVolver = view.findViewById(R.id.flechaVolverRegistro);

        Button siguiente = view.findViewById(R.id.boton_registrarse);

        crearCards(view);

        for (int id : ids) {
            View opcion = view.findViewById(id);
            opcion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cambioEstado(opcion);
                }

            });
        }


        flechaVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), registro_pregunta.class);
                startActivity(intent);

            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (seleccionadaNombre.equalsIgnoreCase("")){
                    Toast.makeText(getContext(), "Debes seleccionar una opción", Toast.LENGTH_SHORT).show();
                    // Intent intent = new Intent(getActivity(), registro_pregunta.class);
                    // startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Vas hacia la siguiente pantalla "+seleccionadaNombre, Toast.LENGTH_SHORT).show();

                }


            }
        });


        return view;
    }


    private void  crearCards(View view){


        int[] iconos = {R.drawable.monio, R.drawable.rompe_cabezas, R.drawable.mochila, R.drawable.sombrero, R.drawable.libro};
        int[] textos = {R.string.preescolar, R.string.primariaInicial, R.string.primaria, R.string.secundaria, R.string.avanzado};
        int[] edades = {R.string.edadPreescolar, R.string.primariaInicial, R.string.edadPrimaria, R.string.edadSecundaria, R.string.edadAvanzado};

        for (int i = 0; i < opcionIds.length; i++) {
            View opcion = view.findViewById(opcionIds[i]);
            ((ImageView) opcion.findViewById(R.id.opcion_icon)).setImageResource(iconos[i]);
            ((TextView) opcion.findViewById(R.id.opcion_text)).setText(getString(textos[i]));
            ((TextView) opcion.findViewById(R.id.opcion_edad)).setText(getString(edades[i]));
        }


    }

    private void cambioEstado(View opcion){

        seleccionadaNombre = "";

        for (int id : ids) {
            View v = getView().findViewById(id);
            v.setSelected(false);
            v.animate().scaleY(1f).setDuration(200).start();
        }

        opcion.animate().scaleY(1.13f).setDuration(200).start();
        opcion.setSelected(true);
        seleccionadaNombre = opcion.getResources().getResourceEntryName(opcion.getId());
        Log.d("Seleccion", "Seleccionada Nombre: " + seleccionadaNombre);



    }

}