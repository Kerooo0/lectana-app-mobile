package com.example.lectana.estudiante.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lectana.R;

public class MiProgresoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mi_progreso, container, false);
        View boton = root.findViewById(R.id.boton_ver_actividades_asignadas);
        if (boton != null) {
            boton.setOnClickListener(v -> startActivity(new android.content.Intent(requireContext(), com.example.lectana.ActividadesAsignadasActivity.class)));
        }
        return root;
    }
}


