package com.example.lectana.registro.docente;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lectana.Login;
import com.example.lectana.R;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.clases.validaciones.ValidacionesPassword;
import com.example.lectana.model.DatosRegistroDocente;
import com.example.lectana.network.RegisterApiClient;
import com.example.lectana.registro.RegistroActivity;
import com.example.lectana.registro.alumno.DatosBasicosAlumno;
import com.example.lectana.ui.SuccessModal;

import org.json.JSONObject;

public class DatosAccesoDocenteFragment extends Fragment {

    // Campos del formulario
    private EditText edtPasswordDocente;
    private EditText edtRepetirPasswordDocente;
    private TextView estadoPassword, coincidenciaPasswordDocente, errorPassword;
    private Button siguiente;
    
    // Datos de registro
    private DatosRegistroDocente datosRegistro;
    
    // Validación de password
    private String pass1 = "";
    private boolean passwordsValidas = false, esValida = false;
    
    // API y sesión
    private RegisterApiClient registerApiClient;
    private SessionManager sessionManager;
    private static final String BASE_URL = "https://lectana-backend.onrender.com";
    private static final String TAG = "DatosAccesoDocente";

    public DatosAccesoDocenteFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_datos_acceso_docente, container, false);

        // Obtener datos de registro
        if (getArguments() != null) {
            datosRegistro = (DatosRegistroDocente) getArguments().getSerializable("datosRegistro");
        }
        
        if (datosRegistro == null) {
            datosRegistro = new DatosRegistroDocente();
        }

        // Inicializar componentes
        inicializarComponentes(vista);
        inicializarAPI();

        ProgressBar barra = vista.findViewById(R.id.barraProgresoDoc);
        TextView textoRegistro = vista.findViewById(R.id.textoRegistro);
        barra.setProgress(3);
        TextView progreso = vista.findViewById(R.id.barraProgresoPaso);
        progreso.setText(getString(R.string.pasoTres));
        textoRegistro.setText(getString(R.string.registroDocente));

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarCampos()) {
                    guardarDatos();
                    registrarDocente();
                }
            }
        });

        // Configurar validación de password en tiempo real
        configurarValidacionPassword();

        return vista;
    }
    
    private void inicializarComponentes(View vista) {
        edtPasswordDocente = vista.findViewById(R.id.editTextpasswordDocente);
        edtRepetirPasswordDocente = vista.findViewById(R.id.editTextRepetirpasswordDocente);
        estadoPassword = vista.findViewById(R.id.resultadoPassword);
        errorPassword = vista.findViewById(R.id.errorPasswordDocente);
        coincidenciaPasswordDocente = vista.findViewById(R.id.coincidenciaPasswordDocente);
        siguiente = vista.findViewById(R.id.continuarDocente);
    }
    
    private void inicializarAPI() {
        registerApiClient = new RegisterApiClient(BASE_URL);
        sessionManager = new SessionManager(getContext());
    }
    
    private void configurarValidacionPassword() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pass1 = edtPasswordDocente.getText().toString();
                String pass2 = edtRepetirPasswordDocente.getText().toString();

                esValida = ValidacionesPassword.esPasswordValida(pass1);
                ValidacionesPassword.mostrarEstadoPassword(estadoPassword, esValida, errorPassword, pass1);

                validarCoincidencia(pass1, pass2);
            }

            @Override 
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override 
            public void afterTextChanged(Editable s) {}
        };

        edtPasswordDocente.addTextChangedListener(watcher);
        edtRepetirPasswordDocente.addTextChangedListener(watcher);
    }
    
    private boolean validarCampos() {
        String password = edtPasswordDocente.getText().toString().trim();
        String confirmarPassword = edtRepetirPasswordDocente.getText().toString().trim();
        
        if (password.isEmpty()) {
            edtPasswordDocente.setError("La contraseña es obligatoria");
            return false;
        }
        
        if (password.length() < 8) {
            edtPasswordDocente.setError("La contraseña debe tener al menos 8 caracteres");
            return false;
        }
        
        if (confirmarPassword.isEmpty()) {
            edtRepetirPasswordDocente.setError("Confirma tu contraseña");
            return false;
        }
        
        if (!password.equals(confirmarPassword)) {
            edtRepetirPasswordDocente.setError("Las contraseñas no coinciden");
            return false;
        }
        
        if (!esValida) {
            Toast.makeText(getContext(), "La contraseña no cumple con los requisitos de seguridad", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
    
    private void guardarDatos() {
        datosRegistro.setPassword(edtPasswordDocente.getText().toString().trim());
        datosRegistro.setConfirmarPassword(edtRepetirPasswordDocente.getText().toString().trim());
        
        // Actualizar datos en la actividad principal
        if (getActivity() instanceof RegistroActivity) {
            ((RegistroActivity) getActivity()).setDatosRegistroDocente(datosRegistro);
        }
    }
    
    private void registrarDocente() {
        // Mostrar loading
        siguiente.setEnabled(false);
        siguiente.setText("Registrando...");
        
        Log.d(TAG, "Iniciando registro de docente con datos: " + datosRegistro.toString());
        
        registerApiClient.registrarDocente(datosRegistro, new RegisterApiClient.RegisterCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                getActivity().runOnUiThread(() -> {
                    siguiente.setEnabled(true);
                    siguiente.setText("Registrar");
                    
                    Log.d(TAG, "Registro exitoso: " + response.toString());
                    
                    try {
                        // Guardar sesión
                        String token = response.getString("token");
                        String role = response.getString("role");
                        JSONObject user = response.getJSONObject("user");
                        JSONObject docente = response.optJSONObject("docente");
                        
                        sessionManager.saveSession(token, role, user, docente);
                        
                        // Mostrar modal de éxito
                        SuccessModal successModal = new SuccessModal(getContext(), new SuccessModal.OnSuccessListener() {
                            @Override
                            public void onSuccess() {
                                // Redirigir al login
                                Intent intent = new Intent(getContext(), Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                        successModal.showModal();
                        
                    } catch (Exception e) {
                        Log.e(TAG, "Error procesando respuesta: " + e.getMessage());
                        Toast.makeText(getContext(), "Error procesando respuesta del servidor", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            
            @Override
            public void onError(String message) {
                getActivity().runOnUiThread(() -> {
                    siguiente.setEnabled(true);
                    siguiente.setText("Registrar");
                    
                    Log.e(TAG, "Error en registro: " + message);
                    Toast.makeText(getContext(), "Error: " + message, Toast.LENGTH_LONG).show();
                });
            }
        });
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