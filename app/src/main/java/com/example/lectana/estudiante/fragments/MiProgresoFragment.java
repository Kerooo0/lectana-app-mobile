package com.example.lectana.estudiante.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lectana.R;

public class MiProgresoFragment extends Fragment {
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mi_progreso_tabs, container, false);
        
        // Cargar directamente el fragment de actividades
        cargarFragmentActividades();
        
        return root;
    }
    
    private void cargarFragmentActividades() {
        Fragment fragment = new ActividadesEstudianteFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.containerProgreso, fragment);
        transaction.commit();
    }
}


