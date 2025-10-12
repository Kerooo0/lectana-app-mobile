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
import com.example.lectana.model.DatosRegistroDocente;
import com.example.lectana.registro.RegistroActivity;
import com.example.lectana.registro.registro_pregunta;

public class DatosPersonalesDocenteFragment extends Fragment {

    // Campos del formulario
    private EditText edtNombreDocente;
    private EditText edtApellidoDocente;
    private EditText edtDniDocente;
    private EditText edtEmailDocente;
    private EditText edtTelefonoDocente;
    private EditText edtEdadDocente;
    
    // Datos de registro
    private DatosRegistroDocente datosRegistro;

    public DatosPersonalesDocenteFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_datos_personales_docente, container, false);

        // Obtener datos de registro
        if (getArguments() != null) {
            datosRegistro = (DatosRegistroDocente) getArguments().getSerializable("datosRegistro");
        }
        
        if (datosRegistro == null) {
            datosRegistro = new DatosRegistroDocente();
        }

        // Inicializar campos
        inicializarCampos(vista);

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);
        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);
        Button siguiente = vista.findViewById(R.id.btn_siguiente_datos_personales);

        textoRegistro.setText(getString(R.string.registroDocente));

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
                if (validarCampos()) {
                    guardarDatos();
                    navegarSiguiente();
                }
            }
        });

        return vista;
    }
    
    private void inicializarCampos(View vista) {
        edtNombreDocente = vista.findViewById(R.id.editTextNombreDocente);
        edtApellidoDocente = vista.findViewById(R.id.editTextApellidoDocente);
        edtDniDocente = vista.findViewById(R.id.editTextDniDocente);
        edtEmailDocente = vista.findViewById(R.id.editTextEmailDocente);
        edtTelefonoDocente = vista.findViewById(R.id.editTextTelefonoDocente);
        edtEdadDocente = vista.findViewById(R.id.editTextEdadDocente);
    }
    
    private boolean validarCampos() {
        String nombre = edtNombreDocente.getText().toString().trim();
        String apellido = edtApellidoDocente.getText().toString().trim();
        String dni = edtDniDocente.getText().toString().trim();
        String email = edtEmailDocente.getText().toString().trim();
        String edadStr = edtEdadDocente.getText().toString().trim();
        
        if (nombre.isEmpty()) {
            edtNombreDocente.setError("El nombre es obligatorio");
            return false;
        }
        
        if (apellido.isEmpty()) {
            edtApellidoDocente.setError("El apellido es obligatorio");
            return false;
        }
        
        if (dni.isEmpty()) {
            edtDniDocente.setError("El DNI es obligatorio");
            return false;
        }
        
        if (dni.length() < 7 || dni.length() > 15) {
            edtDniDocente.setError("El DNI debe tener entre 7 y 15 caracteres");
            return false;
        }
        
        if (email.isEmpty()) {
            edtEmailDocente.setError("El email es obligatorio");
            return false;
        }
        
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailDocente.setError("Email inválido");
            return false;
        }
        
        if (edadStr.isEmpty()) {
            edtEdadDocente.setError("La edad es obligatoria");
            return false;
        }
        
        try {
            int edad = Integer.parseInt(edadStr);
            if (edad < 18 || edad > 120) {
                edtEdadDocente.setError("La edad debe estar entre 18 y 120 años");
                return false;
            }
        } catch (NumberFormatException e) {
            edtEdadDocente.setError("Ingresa una edad válida");
            return false;
        }
        
        return true;
    }
    
    private void guardarDatos() {
        datosRegistro.setNombre(edtNombreDocente.getText().toString().trim());
        datosRegistro.setApellido(edtApellidoDocente.getText().toString().trim());
        datosRegistro.setDni(edtDniDocente.getText().toString().trim());
        datosRegistro.setEmail(edtEmailDocente.getText().toString().trim());
        datosRegistro.setTelefono(edtTelefonoDocente.getText().toString().trim());
        
        // Guardar edad
        try {
            int edad = Integer.parseInt(edtEdadDocente.getText().toString().trim());
            datosRegistro.setEdad(edad);
        } catch (NumberFormatException e) {
            datosRegistro.setEdad(30); // Valor por defecto
        }
        
        // Actualizar datos en la actividad principal
        if (getActivity() instanceof RegistroActivity) {
            ((RegistroActivity) getActivity()).setDatosRegistroDocente(datosRegistro);
        }
    }
    
    private void navegarSiguiente() {
        DatosInstitucionalesFragment siguiente = new DatosInstitucionalesFragment();
        
        // Pasar datos al siguiente fragmento
        Bundle bundle = new Bundle();
        bundle.putSerializable("datosRegistro", datosRegistro);
        siguiente.setArguments(bundle);
        
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
        cambioDeFragment.replace(R.id.frameLayout, siguiente);
        cambioDeFragment.commit();
    }
}