package com.example.lectana.registro.alumno;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lectana.R;
import com.example.lectana.registro.RegistroActivity;


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


        return vista;
    }
}