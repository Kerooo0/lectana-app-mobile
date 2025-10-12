package com.example.lectana.registro.docente;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.Login;
import com.example.lectana.R;
import com.example.lectana.models.DocenteRegistro;
import com.example.lectana.network.RegistroDocenteClient;

import org.json.JSONObject;

public class ConfirmacionDatosDocenteFragment extends Fragment {

    private RegistroDocenteManager registroManager;
    private RegistroDocenteClient registroClient;
    private TextView txtNombre, txtEmail, txtDni, txtInstitucion, txtPais, txtNivel;
    private Button btnConfirmar, btnEditar;
    private ProgressBar progressBar;
    
    // URL del backend (mismo que en Login)
    private static final String BASE_URL = "https://lectana-backend.onrender.com";

    public ConfirmacionDatosDocenteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_confirmacion_datos_docente, container, false);

        // Inicializar componentes
        registroManager = RegistroDocenteManager.getInstance(requireContext());
        registroClient = new RegistroDocenteClient(BASE_URL);

        // Referencias del header y barra de progreso
        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);
        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);
        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);
        
        barra.setProgress(4);
        progreso.setText("Paso 4 de 4");
        textoRegistro.setText(getString(R.string.registroDocente));

        // Referencias a los TextViews de confirmación
        txtNombre = vista.findViewById(R.id.txtConfirmNombre);
        txtEmail = vista.findViewById(R.id.txtConfirmEmail);
        txtDni = vista.findViewById(R.id.txtConfirmDni);
        txtInstitucion = vista.findViewById(R.id.txtConfirmInstitucion);
        txtPais = vista.findViewById(R.id.txtConfirmPais);
        txtNivel = vista.findViewById(R.id.txtConfirmNivel);

        // Botones
        btnConfirmar = vista.findViewById(R.id.btnConfirmarRegistro);
        btnEditar = vista.findViewById(R.id.btnEditarDatos);
        progressBar = vista.findViewById(R.id.progressBarRegistro);

        // Botón volver
        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);
        volver.setOnClickListener(v -> {
            Fragment Volver = new DatosAccesoDocenteFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
            cambioDeFragment.replace(R.id.frameLayout, Volver);
            cambioDeFragment.commit();
        });

        // Mostrar datos guardados
        mostrarDatosConfirmacion();

        // Configurar botones
        btnEditar.setOnClickListener(v -> {
            // Volver al primer paso para editar
            Fragment PrimerPaso = new DatosPersonalesDocenteFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
            cambioDeFragment.replace(R.id.frameLayout, PrimerPaso);
            cambioDeFragment.commit();
        });

        btnConfirmar.setOnClickListener(v -> {
            if (registroManager.datosCompletos()) {
                realizarRegistro();
            } else {
                Toast.makeText(requireContext(), 
                    "Por favor completa todos los pasos del registro", 
                    Toast.LENGTH_LONG).show();
            }
        });

        return vista;
    }

    private void mostrarDatosConfirmacion() {
        DocenteRegistro docente = registroManager.getDocenteRegistro();
        
        txtNombre.setText(docente.getNombre() + " " + docente.getApellido());
        txtEmail.setText(docente.getEmail());
        txtDni.setText(docente.getDni());
        txtInstitucion.setText(docente.getInstitucionNombre());
        txtPais.setText(docente.getInstitucionPais() + 
            (docente.getInstitucionProvincia() != null ? ", " + docente.getInstitucionProvincia() : ""));
        txtNivel.setText(docente.getNivelEducativo());
    }

    private void realizarRegistro() {
        // Mostrar loading
        btnConfirmar.setEnabled(false);
        btnEditar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        DocenteRegistro docente = registroManager.getDocenteRegistro();

        registroClient.registrarDocente(docente, new RegistroDocenteClient.RegistroCallback() {
            @Override
            public void onSuccess(String message, JSONObject user) {
                // Ejecutar en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireContext(), 
                        "¡Registro exitoso! " + message, 
                        Toast.LENGTH_LONG).show();

                    // Limpiar datos guardados
                    registroManager.limpiarDatos();

                    // Ir al login después de 2 segundos
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        Intent intent = new Intent(requireActivity(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                    }, 2000);
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Ejecutar en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> {
                    progressBar.setVisibility(View.GONE);
                    btnConfirmar.setEnabled(true);
                    btnEditar.setEnabled(true);
                    
                    // Log completo del error
                    Log.e("ConfirmacionDocente", "ERROR COMPLETO: " + errorMessage);
                    
                    // Mostrar diálogo con el error completo
                    new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        .setTitle("Error en el registro")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", null)
                        .show();
                });
            }
        });
    }
}
