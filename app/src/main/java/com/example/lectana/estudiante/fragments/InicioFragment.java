package com.example.lectana.estudiante.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lectana.R;
import com.example.lectana.ReproductorAudiolibroActivity;

public class InicioFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_inicio, container, false);

        View botonPlay = root.findViewById(R.id.boton_reproducir_lectura);
        if (botonPlay != null) {
            botonPlay.setOnClickListener(v -> {
                if (getContext() != null) {
                    startActivity(new Intent(getContext(), ReproductorAudiolibroActivity.class));
                }
            });
        }

        return root;
    }
}


