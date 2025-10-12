package com.example.lectana.registro.docente;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.R;
import com.example.lectana.model.DatosRegistroDocente;
import com.example.lectana.registro.RegistroActivity;


public class DatosInstitucionalesFragment extends Fragment {

    // Campos del formulario
    private EditText edtNombreInstitucion;
    private Spinner spinnerPaisDocente;
    private Spinner spinnerProvinciaDocente;
    private LinearLayout layoutPrimaria;
    private LinearLayout layoutSecundaria;
    private LinearLayout layoutOtro;
    
    // Datos de registro
    private DatosRegistroDocente datosRegistro;
    private String nivelEducativoSeleccionado = "AMBOS";

    public DatosInstitucionalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_datos_institucionales, container, false);

        // Obtener datos de registro
        if (getArguments() != null) {
            datosRegistro = (DatosRegistroDocente) getArguments().getSerializable("datosRegistro");
        }
        
        if (datosRegistro == null) {
            datosRegistro = new DatosRegistroDocente();
        }

        // Inicializar campos
        inicializarCampos(vista);

        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);
        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);
        barra.setProgress(2);
        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);
        progreso.setText(getString(R.string.pasoDos));
        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);
        textoRegistro.setText(getString(R.string.registroDocente));
        Button siguiente = vista.findViewById(R.id.botonSiguienteDatosInstitucionales);

        // Configurar listeners para nivel educativo
        configurarNivelEducativo();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navegarAnterior();
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
        edtNombreInstitucion = vista.findViewById(R.id.editTextNombreInstitucion);
        spinnerPaisDocente = vista.findViewById(R.id.spinnerPaisDocente);
        spinnerProvinciaDocente = vista.findViewById(R.id.spinnerProvinciaDocente);
        layoutPrimaria = vista.findViewById(R.id.layoutPrimaria);
        layoutSecundaria = vista.findViewById(R.id.layoutSecundaria);
        layoutOtro = vista.findViewById(R.id.layoutOtro);
        
        // Configurar spinners con datos básicos
        configurarSpinners();
    }
    
    private void configurarSpinners() {
        // Configurar spinner de países
        String[] paises = {"Argentina", "Brasil", "Chile", "Colombia", "México", "Perú", "Uruguay"};
        android.widget.ArrayAdapter<String> adapterPaises = new android.widget.ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, paises);
        adapterPaises.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaisDocente.setAdapter(adapterPaises);
        
        // Configurar spinner de provincias (Argentina por defecto)
        String[] provincias = {"Buenos Aires", "CABA", "Córdoba", "Santa Fe", "Mendoza", "Tucumán", "Salta", "Entre Ríos", "Misiones", "Corrientes"};
        android.widget.ArrayAdapter<String> adapterProvincias = new android.widget.ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, provincias);
        adapterProvincias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvinciaDocente.setAdapter(adapterProvincias);
    }
    
    private void configurarNivelEducativo() {
        layoutPrimaria.setOnClickListener(v -> seleccionarNivelEducativo("PRIMARIA"));
        layoutSecundaria.setOnClickListener(v -> seleccionarNivelEducativo("SECUNDARIA"));
        layoutOtro.setOnClickListener(v -> seleccionarNivelEducativo("AMBOS"));
        
        // Seleccionar "AMBOS" por defecto
        seleccionarNivelEducativo("AMBOS");
    }
    
    private void seleccionarNivelEducativo(String nivel) {
        nivelEducativoSeleccionado = nivel;
        
        // Resetear todos los layouts
        layoutPrimaria.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        layoutSecundaria.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        layoutOtro.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        
        // Resaltar el seleccionado
        switch (nivel) {
            case "PRIMARIA":
                layoutPrimaria.setBackgroundResource(R.drawable.boton_rosa_rectangular);
                break;
            case "SECUNDARIA":
                layoutSecundaria.setBackgroundResource(R.drawable.boton_rosa_rectangular);
                break;
            case "AMBOS":
                layoutOtro.setBackgroundResource(R.drawable.boton_rosa_rectangular);
                break;
        }
    }
    
    private boolean validarCampos() {
        String institucion = edtNombreInstitucion.getText().toString().trim();
        
        if (institucion.isEmpty()) {
            edtNombreInstitucion.setError("El nombre de la institución es obligatorio");
            return false;
        }
        
        return true;
    }
    
    private void guardarDatos() {
        datosRegistro.setInstitucionNombre(edtNombreInstitucion.getText().toString().trim());
        datosRegistro.setInstitucionPais(spinnerPaisDocente.getSelectedItem().toString());
        datosRegistro.setInstitucionProvincia(spinnerProvinciaDocente.getSelectedItem().toString());
        datosRegistro.setNivelEducativo(nivelEducativoSeleccionado);
        
        // Actualizar datos en la actividad principal
        if (getActivity() instanceof RegistroActivity) {
            ((RegistroActivity) getActivity()).setDatosRegistroDocente(datosRegistro);
        }
    }
    
    private void navegarAnterior() {
        DatosPersonalesDocenteFragment anterior = new DatosPersonalesDocenteFragment();
        
        // Pasar datos al fragmento anterior
        Bundle bundle = new Bundle();
        bundle.putSerializable("datosRegistro", datosRegistro);
        anterior.setArguments(bundle);
        
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
        cambioDeFragment.replace(R.id.frameLayout, anterior);
        cambioDeFragment.commit();
    }
    
    private void navegarSiguiente() {
        DatosAccesoDocenteFragment siguiente = new DatosAccesoDocenteFragment();
        
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