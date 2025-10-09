package com.example.lectana.estudiante.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lectana.Login;
import com.example.lectana.R;
import com.example.lectana.centro_ayuda;

public class PerfilFragment extends Fragment {

    private ImageView fotoPerfilEstudiante;
    private ImageView botonEditarFoto;
    private TextView nombreEstudiantePerfil;
    private TextView nivelEstudiantePerfil;
    private TextView puntosEstudiantePerfil;
    private LinearLayout opcionCambiarPassword;
    private LinearLayout opcionCentroAyuda;
    private LinearLayout opcionCerrarSesion;
    private View botonIrTienda;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        inicializarVistas(view);
        configurarListeners();
        cargarDatosPerfil();
    }

    private void inicializarVistas(View view) {
        fotoPerfilEstudiante = view.findViewById(R.id.foto_perfil_estudiante);
        botonEditarFoto = view.findViewById(R.id.boton_editar_foto);
        nombreEstudiantePerfil = view.findViewById(R.id.nombre_estudiante_perfil);
        nivelEstudiantePerfil = view.findViewById(R.id.nivel_estudiante_perfil);
        puntosEstudiantePerfil = view.findViewById(R.id.puntos_estudiante_perfil);
        opcionCambiarPassword = view.findViewById(R.id.opcion_cambiar_password);
        opcionCentroAyuda = view.findViewById(R.id.opcion_centro_ayuda);
        opcionCerrarSesion = view.findViewById(R.id.opcion_cerrar_sesion);
        botonIrTienda = view.findViewById(R.id.boton_ir_tienda);
    }

    private void configurarListeners() {
        // Botón editar foto
        botonEditarFoto.setOnClickListener(v -> {
            // TODO: Implementar selección de foto de perfil
            Toast.makeText(getContext(), "Seleccionar foto de perfil", Toast.LENGTH_SHORT).show();
        });

        // Opciones de configuración
        opcionCambiarPassword.setOnClickListener(v -> {
            // TODO: Implementar cambio de contraseña
            Toast.makeText(getContext(), "Cambiar contraseña", Toast.LENGTH_SHORT).show();
        });

        opcionCentroAyuda.setOnClickListener(v -> {
            // Navegar al centro de ayuda
            Intent intent = new Intent(getContext(), centro_ayuda.class);
            startActivity(intent);
        });

        opcionCerrarSesion.setOnClickListener(v -> {
            // TODO: Implementar cerrar sesión
            Toast.makeText(getContext(), "Cerrar sesión", Toast.LENGTH_SHORT).show();
            // Por ahora, volver al login
            Intent intent = new Intent(getContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });

        // Botón ir a tienda
        botonIrTienda.setOnClickListener(v -> {
            // TODO: Implementar navegación a tienda
            Toast.makeText(getContext(), "Ir a Tienda", Toast.LENGTH_SHORT).show();
        });
    }

    private void cargarDatosPerfil() {
        // TODO: Cargar datos reales del estudiante desde la base de datos
        // Por ahora, datos de ejemplo
        nombreEstudiantePerfil.setText("Juanito Lector");
        nivelEstudiantePerfil.setText("Lector Avanzado");
        puntosEstudiantePerfil.setText("120 puntos");
    }
}
