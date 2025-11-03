package com.example.lectana.estudiante.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lectana.R;

public class ProgresoEstadisticasFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progreso_estadisticas, container, false);
        
        // El botón de actividades asignadas ya no es necesario aquí porque tenemos un tab dedicado
        View boton = root.findViewById(R.id.boton_ver_actividades_asignadas);
        if (boton != null) {
            boton.setVisibility(View.GONE);
        }
        
        return root;
    }
}
