package com.example.lectana;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.lectana.models.Avatar;
import com.example.lectana.services.AvatarService;
import com.example.lectana.auth.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class PerfilAlumnoActivity extends AppCompatActivity {
    private static final String TAG = "PerfilAlumno";
    private EditText etNombre, etApellido, etEmail, etEdad;
    private ImageView ivAvatarActual;
    private Spinner spinnerAvatares;
    private ProgressBar pbCargando;
    private SessionManager sessionManager;
    private AvatarService avatarService;
    private List<Avatar> misAvatares = new ArrayList<>();
    private ArrayAdapter<String> avatarAdapter;
    private int avatarActualId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_alumno);

        inicializarComponentes();
        inicializarServicios();
        cargarDatos();
    }

    private void inicializarComponentes() {
        etNombre = findViewById(R.id.etNombrePerfil);
        etApellido = findViewById(R.id.etApellidoPerfil);
        etEmail = findViewById(R.id.etEmailPerfil);
        etEdad = findViewById(R.id.etEdadPerfil);
        ivAvatarActual = findViewById(R.id.ivAvatarActual);
        spinnerAvatares = findViewById(R.id.spinnerAvatares);
        pbCargando = findViewById(R.id.pbCargando);
    }

    private void inicializarServicios() {
        sessionManager = new SessionManager(this);
        avatarService = AvatarService.getInstance();
    }

    private void cargarDatos() {
        pbCargando.setVisibility(View.VISIBLE);
        String token = sessionManager.getToken();
        
        if (token == null || token.isEmpty()) {
            mostrarError("Token no disponible");
            return;
        }

        // Cargar perfil del alumno
        avatarService.obtenerPerfil(token, new AvatarService.OnPerfilListener() {
            @Override
            public void onSuccess(String nombre, String apellido, String email, int edad, String avatarActual, int puntos) {
                etNombre.setText(nombre);
                etApellido.setText(apellido);
                etEmail.setText(email);
                etEdad.setText(String.valueOf(edad));
            }

            @Override
            public void onError(String error) {
                mostrarError("Error al cargar perfil: " + error);
            }
        });

        // Cargar avatares comprados
        avatarService.obtenerMisAvatares(token, new AvatarService.OnAvatarListListener() {
            @Override
            public void onSuccess(List<Avatar> avatares) {
                misAvatares = avatares;
                configurarSpinnerAvatares();
                pbCargando.setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {
                mostrarError("Error al cargar avatares: " + error);
                pbCargando.setVisibility(View.GONE);
            }
        });
    }

    private void configurarSpinnerAvatares() {
        List<String> nombresAvatares = new ArrayList<>();
        
        // Si no hay avatares, mostrar mensaje
        if (misAvatares.isEmpty()) {
            nombresAvatares.add("No tienes avatares. ¡Visita la tienda!");
        } else {
            for (Avatar avatar : misAvatares) {
                nombresAvatares.add(avatar.getNombre() + " (" + avatar.getPrecio() + " pts)");
            }
        }

        avatarAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, 
            nombresAvatares);
        avatarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAvatares.setAdapter(avatarAdapter);

        // Cargar avatar actual desde SharedPreferences
        cargarAvatarActual();

        spinnerAvatares.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Solo cambiar avatar si hay avatares disponibles
                if (!misAvatares.isEmpty()) {
                    Avatar avatarSeleccionado = misAvatares.get(position);
                    cambiarAvatar(avatarSeleccionado);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void cargarAvatarActual() {
        SharedPreferences prefs = getSharedPreferences("avatar_prefs", Context.MODE_PRIVATE);
        avatarActualId = prefs.getInt("avatar_actual_id", -1);

        if (avatarActualId != -1) {
            for (int i = 0; i < misAvatares.size(); i++) {
                if (misAvatares.get(i).getIdItem() == avatarActualId) {
                    spinnerAvatares.setSelection(i);
                    mostrarAvatarEnImageView(misAvatares.get(i));
                    break;
                }
            }
        } else if (!misAvatares.isEmpty()) {
            // Si no hay avatar guardado, mostrar el primero
            spinnerAvatares.setSelection(0);
            mostrarAvatarEnImageView(misAvatares.get(0));
        }
    }

    private void cambiarAvatar(Avatar avatar) {
        avatarActualId = avatar.getIdItem();
        mostrarAvatarEnImageView(avatar);
        
        // Guardar en SharedPreferences
        SharedPreferences prefs = getSharedPreferences("avatar_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("avatar_actual_id", avatarActualId);
        editor.putString("avatar_nombre", avatar.getNombre());
        editor.putString("avatar_url", avatar.getUrlImagen());
        editor.apply();

        Toast.makeText(this, "Avatar cambio a: " + avatar.getNombre(), Toast.LENGTH_SHORT).show();
    }

    private void mostrarAvatarEnImageView(Avatar avatar) {
        if (avatar.getUrlImagen() != null && !avatar.getUrlImagen().isEmpty()) {
            Glide.with(this)
                .load(avatar.getUrlImagen())
                .placeholder(R.drawable.ic_default_avatar)
                .into(ivAvatarActual);
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        Log.e(TAG, mensaje);
        pbCargando.setVisibility(View.GONE);
    }
}
