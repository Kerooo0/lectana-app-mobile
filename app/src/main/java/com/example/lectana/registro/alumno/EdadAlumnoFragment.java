package com.example.lectana.registro.alumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
    Button siguiente;
    CardView cardAdvertencia;
    TextView importante, advertencia;
    public EdadAlumnoFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_edades_alumno, container, false);

        cardAdvertencia = view.findViewById(R.id.cardAdvertenciaAlumno);

        ImageView flechaVolver = view.findViewById(R.id.flechaVolverRegistro);

        siguiente = view.findViewById(R.id.boton_registrarse);

        importante = view.findViewById(R.id.textoImportanteAlumno);

        advertencia = view.findViewById(R.id.textoAdvertenciaAlumno);

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
        int color;
        int colorTexto;
        seleccionadaNombre = "";


        for (int id : ids) {
            View v = getView().findViewById(id);
            v.setSelected(false);
            v.animate().scaleY(1f).setDuration(200).start();
        }

        opcion.animate().scaleY(1.13f).setDuration(200).start();
        opcion.setSelected(true);
        seleccionadaNombre = opcion.getResources().getResourceEntryName(opcion.getId());

        if (seleccionadaNombre.equalsIgnoreCase("preescolar") || seleccionadaNombre.equalsIgnoreCase("primariaInicial")){


            color = ContextCompat.getColor(requireContext(),R.color.fondo_beige);
            cardAdvertencia.setBackgroundColor(color);
            colorTexto = ContextCompat.getColor(requireContext(),R.color.textoRojo);
            importante.setTextColor(colorTexto);
            advertencia.setTextColor(colorTexto);
            advertencia.setText(getString(R.string.mensajeAdvertenciaPreescolar));
            cardAdvertencia.setVisibility(View.VISIBLE);
            siguiente.setText(getString(R.string.estoyConAdulto));

        } else if (seleccionadaNombre.equalsIgnoreCase("primaria")) {

            color = ContextCompat.getColor(requireContext(),R.color.celeste_claro);
            cardAdvertencia.setBackgroundColor(color);
            colorTexto = ContextCompat.getColor(requireContext(),R.color.azul_fuerte);
            importante.setTextColor(colorTexto);
            advertencia.setTextColor(colorTexto);
            advertencia.setText(getString(R.string.mensajeAdvertenciaPrimario));
            if (cardAdvertencia.getVisibility() != View.VISIBLE){
                cardAdvertencia.setVisibility(View.VISIBLE);
            }


            siguiente.setText(getString(R.string.estoyConAdulto));

        } else {

            cardAdvertencia.setVisibility(View.GONE);
            siguiente.setText(getString(R.string.continuar));

        }

        Log.d("Seleccion", "Seleccionada Nombre: " + seleccionadaNombre);



    }

}