package com.example.lectana.registro.docente;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.R;
import com.example.lectana.clases.validaciones.ValidacionesPassword;

public class DatosAccesoDocenteFragment extends Fragment {

    private String pass1 = "";
    private TextView estadoPassword, coincidenciaPasswordDocente, errorPassword;
    private boolean passwordsValidas = false, esValida = false;
    private Button siguiente;
    private EditText password, passwordCoincidencia, editEdad;
    private RegistroDocenteManager registroManager;

    public DatosAccesoDocenteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_datos_acceso_docente, container, false);

        // Inicializar el manager
        registroManager = RegistroDocenteManager.getInstance(requireContext());

        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);
        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);
        barra.setProgress(3);

        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);
        progreso.setText(getString(R.string.pasoTres));
        textoRegistro.setText(getString(R.string.registroDocente));

        password = vista.findViewById(R.id.editTextpasswordDocente);
        estadoPassword = vista.findViewById(R.id.resultadoPassword);
        passwordCoincidencia = vista.findViewById(R.id.editTextRepetirpasswordDocente);
        errorPassword = vista.findViewById(R.id.errorPasswordDocente);
        coincidenciaPasswordDocente = vista.findViewById(R.id.coincidenciaPasswordDocente);
        editEdad = vista.findViewById(R.id.editTextEdadDocente);
        siguiente = vista.findViewById(R.id.continuarDocente);

        // Botón volver
        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);
        volver.setOnClickListener(v -> {
            Fragment Volver = new DatosInstitucionalesFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
            cambioDeFragment.replace(R.id.frameLayout, Volver);
            cambioDeFragment.commit();
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()) {
                    guardarDatos();
                    
                    // Ir al fragment de confirmación
                    Fragment Confirmar = new ConfirmacionDatosDocenteFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
                    cambioDeFragment.replace(R.id.frameLayout, Confirmar);
                    cambioDeFragment.commit();
                }
            }
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass1 = password.getText().toString();
                String pass2 = passwordCoincidencia.getText().toString();

                esValida = ValidacionesPassword.esPasswordValida(pass1);
                ValidacionesPassword.mostrarEstadoPassword(estadoPassword, esValida, errorPassword, pass1);

                validarCoincidencia(pass1, pass2);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        password.addTextChangedListener(watcher);
        passwordCoincidencia.addTextChangedListener(watcher);

        return vista;
    }

    private boolean validarDatos() {
        String edadStr = editEdad.getText().toString().trim();

        if (edadStr.isEmpty()) {
            editEdad.setError("Ingresa tu edad");
            editEdad.requestFocus();
            return false;
        }

        int edad = Integer.parseInt(edadStr);
        if (edad < 18 || edad > 120) {
            editEdad.setError("La edad debe estar entre 18 y 120 años");
            editEdad.requestFocus();
            return false;
        }

        if (pass1.isEmpty()) {
            password.setError("Ingresa una contraseña");
            password.requestFocus();
            return false;
        }

        if (!esValida) {
            Toast.makeText(requireContext(), "La contraseña no cumple los requisitos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!passwordsValidas) {
            Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void guardarDatos() {
        int edad = Integer.parseInt(editEdad.getText().toString().trim());
        registroManager.guardarDatosAcceso(pass1, edad);
        Toast.makeText(requireContext(), "Datos de acceso guardados", Toast.LENGTH_SHORT).show();
    }

    private void validarCoincidencia(String pass1, String pass2) {
        boolean coinciden = ValidacionesPassword.sonPasswordsIguales(pass1, pass2);
        passwordsValidas = ValidacionesPassword.mostrarCoincidenciaPasswords(
                coincidenciaPasswordDocente,
                coinciden,
                "Coinciden",
                "No coinciden"
        );
    }
}