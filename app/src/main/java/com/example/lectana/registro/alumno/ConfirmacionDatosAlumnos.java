package com.example.lectana.registro.alumno;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.example.lectana.models.AlumnoRegistro;
import com.example.lectana.network.RegistroAlumnoClient;

import org.json.JSONObject;


public class ConfirmacionDatosAlumnos extends Fragment {

    public ConfirmacionDatosAlumnos() {

    }

    private RegistroAlumnoManager registroManager;
    private RegistroAlumnoClient registroClient;
    private TextView txtNombre, txtEdad, txtGrado,txtEmail,txtApellido;
    private Button btnConfirmar;
    private ProgressBar progressBar;
    
    // URL base de Supabase
    private static final String BASE_URL = "https://kutpsehgzxmnyrujmnxo.supabase.co";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_confirmacion_datos_alumnos, container, false);

        // Inicializar componentes
        registroManager = RegistroAlumnoManager.getInstance(requireContext());
        registroClient = new RegistroAlumnoClient(BASE_URL);

        ProgressBar barraDeProgreso = vista.findViewById(R.id.barraProgreso);
        barraDeProgreso.setProgress(3);

        TextView barraProgresoPaso = vista.findViewById(R.id.barraProgresoPaso);
        barraProgresoPaso.setText(getText(R.string.pasoTres));

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);

        // Referencias a los TextViews de confirmación existentes en el layout
        txtNombre = vista.findViewById(R.id.valorNombreAlumno);
        txtEdad = vista.findViewById(R.id.valorEdadAlumno);
        txtApellido = vista.findViewById(R.id.valorApellidoAlumno);
        txtEmail = vista.findViewById(R.id.valorEmailAlumno);
        txtGrado = vista.findViewById(R.id.valorPaisAlumno);

        // Botones
        btnConfirmar = vista.findViewById(R.id.Confirmar_registro);
        
        // Crear ProgressBar programáticamente (no existe en el layout)
        progressBar = new ProgressBar(requireContext());
        progressBar.setVisibility(View.GONE);

        // Mostrar datos guardados
        mostrarDatosConfirmacion();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment volver = new DatosAccesoAlumno();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();

                cambioDeFragment.replace(R.id.frameLayout, volver);

                cambioDeFragment.commit();

            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (registroManager.datosCompletos()) {
                    realizarRegistro();
                } else {
                    Toast.makeText(requireContext(), 
                        "Por favor completa todos los pasos del registro", 
                        Toast.LENGTH_LONG).show();
                }
            }
        });

        return vista;

    }

    private void mostrarDatosConfirmacion() {
        AlumnoRegistro alumno = registroManager.getAlumnoRegistro();

        txtNombre.setText(alumno.getNombre());
        txtApellido.setText(alumno.getApellido());
        txtEmail.setText(alumno.getEmail());
        txtEdad.setText(alumno.getEdad() + " años");
        txtGrado.setText(alumno.getPais());
    }
    
    private void realizarRegistro() {
        // Deshabilitar botón durante el registro
        btnConfirmar.setEnabled(false);
        btnConfirmar.setText("Registrando...");

        AlumnoRegistro alumno = registroManager.getAlumnoRegistro();
        
        Log.d("ConfirmacionAlumno", "Iniciando registro para: " + alumno.getEmail());
        Log.d("ConfirmacionAlumno", "Datos completos: Nombre=" + alumno.getNombre() + 
              ", Apellido=" + alumno.getApellido() + ", Edad=" + alumno.getEdad() + 
              ", Grado=" + alumno.getGrado() + ", FechaNac=" + alumno.getFechaNacimiento());

        registroClient.registrarAlumno(alumno, new RegistroAlumnoClient.RegistroCallback() {
            @Override
            public void onSuccess(String message, JSONObject user) {
                // Ejecutar en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> {
                    Toast.makeText(requireContext(), 
                        "¡Registro exitoso! " + message, 
                        Toast.LENGTH_LONG).show();

                    Log.d("ConfirmacionAlumno", "✅ REGISTRO EXITOSO: " + message);
                    Log.d("ConfirmacionAlumno", "✅ Usuario creado: " + user.toString());

                    // Limpiar datos guardados
                    registroManager.limpiarDatos();

                    // Ir al fragment de cuenta creada o al login
                    Fragment cuentaCreada = new CuentaCreadaAlumnoFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
                    cambioDeFragment.replace(R.id.frameLayout, cuentaCreada);
                    cambioDeFragment.commit();
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Ejecutar en el hilo principal
                new Handler(Looper.getMainLooper()).post(() -> {
                    btnConfirmar.setEnabled(true);
                    btnConfirmar.setText("Registrarse");
                    
                    // Log completo del error
                    Log.e("ConfirmacionAlumno", "❌ ERROR COMPLETO: " + errorMessage);
                    
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