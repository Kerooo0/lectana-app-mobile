package com.example.lectana.estudiante.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.lectana.R;

public class MiProgresoFragment extends Fragment {
    
    private LinearLayout btnEstadisticas;
    private LinearLayout btnActividades;
    private TextView textEstadisticas;
    private TextView textActividades;
    private View indicadorEstadisticas;
    private View indicadorActividades;
    
    private String tabActual = "estadisticas";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mi_progreso_tabs, container, false);
        
        inicializarComponentes(root);
        configurarListeners();
        
        // Mostrar fragment de estadísticas por defecto
        cargarFragmentEstadisticas();
        
        return root;
    }
    
    private void inicializarComponentes(View root) {
        btnEstadisticas = root.findViewById(R.id.btnTabEstadisticas);
        btnActividades = root.findViewById(R.id.btnTabActividades);
        textEstadisticas = root.findViewById(R.id.textTabEstadisticas);
        textActividades = root.findViewById(R.id.textTabActividades);
        indicadorEstadisticas = root.findViewById(R.id.indicadorEstadisticas);
        indicadorActividades = root.findViewById(R.id.indicadorActividades);
    }
    
    private void configurarListeners() {
        btnEstadisticas.setOnClickListener(v -> {
            if (!tabActual.equals("estadisticas")) {
                tabActual = "estadisticas";
                cargarFragmentEstadisticas();
                actualizarEstadoTabs();
            }
        });
        
        btnActividades.setOnClickListener(v -> {
            if (!tabActual.equals("actividades")) {
                tabActual = "actividades";
                cargarFragmentActividades();
                actualizarEstadoTabs();
            }
        });
    }
    
    private void cargarFragmentEstadisticas() {
        Fragment fragment = new ProgresoEstadisticasFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.containerProgreso, fragment);
        transaction.commit();
    }
    
    private void cargarFragmentActividades() {
        Fragment fragment = new ActividadesEstudianteFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.containerProgreso, fragment);
        transaction.commit();
    }
    
    private void actualizarEstadoTabs() {
        if (tabActual.equals("estadisticas")) {
            // Tab Estadísticas activo
            btnEstadisticas.setBackgroundResource(R.drawable.boton_pestana_activa);
            textEstadisticas.setTextColor(getResources().getColor(R.color.azul_fuerte, null));
            indicadorEstadisticas.setVisibility(View.VISIBLE);
            
            // Tab Actividades inactivo
            btnActividades.setBackgroundResource(R.drawable.boton_pestana_inactiva);
            textActividades.setTextColor(getResources().getColor(R.color.gris_medio, null));
            indicadorActividades.setVisibility(View.GONE);
        } else {
            // Tab Actividades activo
            btnActividades.setBackgroundResource(R.drawable.boton_pestana_activa);
            textActividades.setTextColor(getResources().getColor(R.color.azul_fuerte, null));
            indicadorActividades.setVisibility(View.VISIBLE);
            
            // Tab Estadísticas inactivo
            btnEstadisticas.setBackgroundResource(R.drawable.boton_pestana_inactiva);
            textEstadisticas.setTextColor(getResources().getColor(R.color.gris_medio, null));
            indicadorEstadisticas.setVisibility(View.GONE);
        }
    }
}


