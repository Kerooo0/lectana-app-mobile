package com.example.lectana.estudiante;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.models.Avatar;
import com.example.lectana.services.AvatarService;
import com.example.lectana.auth.SessionManager;
import com.example.lectana.estudiante.adapters.TiendaAvatarAdapter;

import java.util.List;

public class TiendaActivity extends AppCompatActivity {

    private RecyclerView rvAvatares;
    private ProgressBar pbCargando;
    private TextView tvPuntosActuales;
    private TextView tvTituloTienda;
    private TiendaAvatarAdapter adapter;
    private AvatarService avatarService;
    private SessionManager sessionManager;
    private SharedPreferences prefs;

    private int puntosActuales = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tienda);

        inicializarComponentes();
        cargarAvataresDIsponibles();
    }

    private void inicializarComponentes() {
        rvAvatares = findViewById(R.id.rvAvataresTienda);
        pbCargando = findViewById(R.id.pbCargandoTienda);
        tvPuntosActuales = findViewById(R.id.tvPuntosActuales);
        tvTituloTienda = findViewById(R.id.tvTituloTienda);

        sessionManager = new SessionManager(this);
        avatarService = AvatarService.getInstance();
        prefs = getSharedPreferences("avatar_prefs", MODE_PRIVATE);

        // Configurar RecyclerView con GridLayout de 2 columnas
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvAvatares.setLayoutManager(layoutManager);

        adapter = new TiendaAvatarAdapter(avatar -> comprarAvatar(avatar));
        rvAvatares.setAdapter(adapter);
    }

    private void cargarAvataresDIsponibles() {
        pbCargando.setVisibility(View.VISIBLE);
        String token = sessionManager.getToken();

        if (token == null) {
            Toast.makeText(this, "No autenticado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Cargar perfil para obtener puntos actuales
        avatarService.obtenerPerfil(token, new AvatarService.OnPerfilListener() {
            @Override
            public void onSuccess(String nombre, String apellido, String email, int edad, String avatarActual, int puntos) {
                puntosActuales = puntos;
                tvPuntosActuales.setText("Puntos disponibles: " + puntos);
                adapter.setPuntosActuales(puntos);

                // Cargar avatares disponibles (no comprados)
                cargarAvataresPorComprar(token);
            }

            @Override
            public void onError(String error) {
                pbCargando.setVisibility(View.GONE);
                Toast.makeText(TiendaActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarAvataresPorComprar(String token) {
        avatarService.obtenerAvatareDisponibles(token, new AvatarService.OnAvatarListListener() {
            @Override
            public void onSuccess(List<Avatar> avatares) {
                pbCargando.setVisibility(View.GONE);
                adapter.setAvatares(avatares);
            }

            @Override
            public void onError(String error) {
                pbCargando.setVisibility(View.GONE);
                Toast.makeText(TiendaActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void comprarAvatar(Avatar avatar) {
        if (puntosActuales < avatar.getPrecio()) {
            Toast.makeText(this, "Puntos insuficientes", Toast.LENGTH_SHORT).show();
            return;
        }

        pbCargando.setVisibility(View.VISIBLE);
        String token = sessionManager.getToken();

        avatarService.comprarAvatar(token, avatar.getIdItem(), new AvatarService.OnCompraListener() {
            @Override
            public void onSuccess(com.example.lectana.models.CompraAvatarResponse response) {
                pbCargando.setVisibility(View.GONE);
                puntosActuales = response.getPuntosActuales();
                tvPuntosActuales.setText("Puntos disponibles: " + puntosActuales);

                Toast.makeText(TiendaActivity.this,
                        "¡Avatar comprado! -" + response.getPuntosGastados() + " puntos",
                        Toast.LENGTH_SHORT).show();

                // Recargar lista de avatares disponibles
                cargarAvataresPorComprar(token);
            }

            @Override
            public void onError(String error) {
                pbCargando.setVisibility(View.GONE);
                Toast.makeText(TiendaActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
