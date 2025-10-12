package com.example.lectana.registro.docente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.R;
import com.example.lectana.registro.registro_pregunta;

public class DatosPersonalesDocenteFragment extends Fragment {

    private EditText editNombre, editApellido, editDni, editEmail, editTelefono;
    private RegistroDocenteManager registroManager;

    public DatosPersonalesDocenteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_datos_personales_docente, container, false);

        // Inicializar el manager
        registroManager = RegistroDocenteManager.getInstance(requireContext());

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);
        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);
        Button siguiente = vista.findViewById(R.id.btn_siguiente_datos_personales);

        // Obtener referencias a los EditText
        editNombre = vista.findViewById(R.id.editTextNombreDocente);
        editApellido = vista.findViewById(R.id.editTextApellidoDocente);
        editDni = vista.findViewById(R.id.editTextDniDocente);
        editEmail = vista.findViewById(R.id.editTextEmailDocente);
        editTelefono = vista.findViewById(R.id.editTextTelefonoDocente);

        textoRegistro.setText(getString(R.string.registroDocente));

        // Restaurar datos si existen
        restaurarDatos();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), registro_pregunta.class);
                startActivity(intent);
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()) {
                    guardarDatos();
                    
                    Fragment Siguiente = new DatosInstitucionalesFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
                    cambioDeFragment.replace(R.id.frameLayout, Siguiente);
                    cambioDeFragment.commit();
                }
            }
        });

        return vista;
    }

    private boolean validarDatos() {
        String nombre = editNombre.getText().toString().trim();
        String apellido = editApellido.getText().toString().trim();
        String dni = editDni.getText().toString().trim();
        String email = editEmail.getText().toString().trim();

        if (nombre.isEmpty()) {
            editNombre.setError("Ingresa tu nombre");
            editNombre.requestFocus();
            return false;
        }

        if (apellido.isEmpty()) {
            editApellido.setError("Ingresa tu apellido");
            editApellido.requestFocus();
            return false;
        }

        if (dni.isEmpty()) {
            editDni.setError("Ingresa tu DNI");
            editDni.requestFocus();
            return false;
        }

        if (!dni.matches("\\d+")) {
            editDni.setError("El DNI solo debe contener números");
            editDni.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            editEmail.setError("Ingresa tu email");
            editEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Ingresa un email válido");
            editEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void guardarDatos() {
        String nombre = editNombre.getText().toString().trim();
        String apellido = editApellido.getText().toString().trim();
        String dni = editDni.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String telefono = editTelefono.getText().toString().trim();

        registroManager.guardarDatosPersonales(nombre, apellido, dni, email, telefono);
        Toast.makeText(requireContext(), "Datos personales guardados", Toast.LENGTH_SHORT).show();
    }

    private void restaurarDatos() {
        if (registroManager.hayDatosGuardados()) {
            registroManager.restaurarDatos();
            editNombre.setText(registroManager.getDocenteRegistro().getNombre());
            editApellido.setText(registroManager.getDocenteRegistro().getApellido());
            editDni.setText(registroManager.getDocenteRegistro().getDni());
            editEmail.setText(registroManager.getDocenteRegistro().getEmail());
            editTelefono.setText(registroManager.getDocenteRegistro().getTelefono());
        }
    }
}