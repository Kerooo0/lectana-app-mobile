package com.example.lectana.registro.docente;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.R;


public class DatosInstitucionalesFragment extends Fragment {

    private EditText editNombreInstitucion;
    private Spinner spinnerPais, spinnerProvincia;
    private LinearLayout layoutPrimaria, layoutSecundaria, layoutAmbos;
    private String nivelSeleccionado = "";
    private RegistroDocenteManager registroManager;

    public DatosInstitucionalesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_datos_institucionales, container, false);

        // Inicializar el manager
        registroManager = RegistroDocenteManager.getInstance(requireContext());

        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);
        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);
        barra.setProgress(2);

        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);
        progreso.setText(getString(R.string.pasoDos));

        ImageView volver = vista.findViewById(R.id.flechaVolverRegistro);
        textoRegistro.setText(getString(R.string.registroDocente));

        Button siguiente = vista.findViewById(R.id.botonSiguienteDatosInstitucionales);

        // Referencias a campos
        editNombreInstitucion = vista.findViewById(R.id.editTextNombreInstitucion);
        spinnerPais = vista.findViewById(R.id.spinnerPaisDocente);
        spinnerProvincia = vista.findViewById(R.id.spinnerProvinciaDocente);
        
        // Referencias a los layouts de nivel educativo
        layoutPrimaria = vista.findViewById(R.id.layoutPrimaria);
        layoutSecundaria = vista.findViewById(R.id.layoutSecundaria);
        layoutAmbos = vista.findViewById(R.id.layoutOtro);

        // Configurar spinners
        configurarSpinners();

        // Configurar selección de nivel educativo
        configurarSeleccionNivel();

        // Restaurar datos si existen
        restaurarDatos();

        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment Volver = new DatosPersonalesDocenteFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
                cambioDeFragment.replace(R.id.frameLayout, Volver);
                cambioDeFragment.commit();
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDatos()) {
                    guardarDatos();
                    
                    Fragment Siguiente = new DatosAccesoDocenteFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction cambioDeFragment = fragmentManager.beginTransaction();
                    cambioDeFragment.replace(R.id.frameLayout, Siguiente);
                    cambioDeFragment.commit();
                }
            }
        });

        return vista;
    }

    private void configurarSpinners() {
        // Países de ejemplo (puedes expandir esta lista)
        String[] paises = {"Argentina", "Chile", "México", "España", "Colombia", "Perú", "Uruguay"};
        ArrayAdapter<String> adapterPais = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_spinner_item, paises);
        adapterPais.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPais.setAdapter(adapterPais);

        // Provincias de Argentina (ejemplo)
        String[] provincias = {"Buenos Aires", "Córdoba", "Santa Fe", "Mendoza", "Tucumán", 
                               "Salta", "Entre Ríos", "Misiones", "Chaco", "Otra"};
        ArrayAdapter<String> adapterProvincia = new ArrayAdapter<>(requireContext(), 
            android.R.layout.simple_spinner_item, provincias);
        adapterProvincia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvincia.setAdapter(adapterProvincia);
    }

    private void configurarSeleccionNivel() {
        layoutPrimaria.setOnClickListener(v -> seleccionarNivel("PRIMARIA", layoutPrimaria));
        layoutSecundaria.setOnClickListener(v -> seleccionarNivel("SECUNDARIA", layoutSecundaria));
        layoutAmbos.setOnClickListener(v -> seleccionarNivel("AMBOS", layoutAmbos));
    }

    private void seleccionarNivel(String nivel, LinearLayout layoutSeleccionado) {
        // Resetear todos los layouts
        resetearSeleccion(layoutPrimaria);
        resetearSeleccion(layoutSecundaria);
        resetearSeleccion(layoutAmbos);

        // Marcar el seleccionado
        layoutSeleccionado.setBackground(ContextCompat.getDrawable(requireContext(), 
            R.drawable.boton_rosa_rectangular));
        nivelSeleccionado = nivel;
    }

    private void resetearSeleccion(LinearLayout layout) {
        layout.setBackground(ContextCompat.getDrawable(requireContext(), 
            R.drawable.boton_blanco_rectangular));
    }

    private boolean validarDatos() {
        String nombreInstitucion = editNombreInstitucion.getText().toString().trim();

        if (nombreInstitucion.isEmpty()) {
            editNombreInstitucion.setError("Ingresa el nombre de la institución");
            editNombreInstitucion.requestFocus();
            return false;
        }

        if (nivelSeleccionado.isEmpty()) {
            Toast.makeText(requireContext(), "Selecciona un nivel educativo", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void guardarDatos() {
        String nombreInstitucion = editNombreInstitucion.getText().toString().trim();
        String pais = spinnerPais.getSelectedItem().toString();
        String provincia = spinnerProvincia.getSelectedItem().toString();

        registroManager.guardarDatosInstitucionales(nombreInstitucion, pais, provincia, nivelSeleccionado);
        Toast.makeText(requireContext(), "Datos institucionales guardados", Toast.LENGTH_SHORT).show();
    }

    private void restaurarDatos() {
        if (registroManager.hayDatosGuardados()) {
            registroManager.restaurarDatos();
            editNombreInstitucion.setText(registroManager.getDocenteRegistro().getInstitucionNombre());
            
            String nivelGuardado = registroManager.getDocenteRegistro().getNivelEducativo();
            if (nivelGuardado != null && !nivelGuardado.isEmpty()) {
                nivelSeleccionado = nivelGuardado;
                switch (nivelGuardado) {
                    case "PRIMARIA":
                        seleccionarNivel("PRIMARIA", layoutPrimaria);
                        break;
                    case "SECUNDARIA":
                        seleccionarNivel("SECUNDARIA", layoutSecundaria);
                        break;
                    case "AMBOS":
                        seleccionarNivel("AMBOS", layoutAmbos);
                        break;
                }
            }
        }
    }
}