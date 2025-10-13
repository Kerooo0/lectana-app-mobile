package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.network.ConnectionTest;

public class TestConnectionActivity extends AppCompatActivity {

    private TextView textViewResult;
    private Button btnTestHealth;
    private Button btnTestLogin;
    private Button btnTestBasic;
    private ConnectionTest connectionTest;
    
    private static final String BASE_URL = "http://192.168.1.33:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_connection);

        // Inicializar componentes
        textViewResult = findViewById(R.id.textViewResult);
        btnTestHealth = findViewById(R.id.btnTestHealth);
        btnTestLogin = findViewById(R.id.btnTestLogin);
        btnTestBasic = findViewById(R.id.btnTestBasic);
        
        connectionTest = new ConnectionTest();

        // Configurar listeners
        btnTestBasic.setOnClickListener(v -> testBasicConnection());
        btnTestHealth.setOnClickListener(v -> testHealthEndpoint());
        btnTestLogin.setOnClickListener(v -> testLoginEndpoint());
    }

    private void testBasicConnection() {
        textViewResult.setText("Probando conexión básica...");
        btnTestBasic.setEnabled(false);
        
        connectionTest.testBasicConnection(BASE_URL, new ConnectionTest.TestCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    textViewResult.setText(message);
                    btnTestBasic.setEnabled(true);
                    Toast.makeText(TestConnectionActivity.this, "Conexión básica exitosa", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    textViewResult.setText(message);
                    btnTestBasic.setEnabled(true);
                    Toast.makeText(TestConnectionActivity.this, "Error en conexión básica", Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void testHealthEndpoint() {
        textViewResult.setText("Probando endpoint /health...");
        btnTestHealth.setEnabled(false);
        
        connectionTest.testHealthEndpoint(BASE_URL, new ConnectionTest.TestCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    textViewResult.setText(message);
                    btnTestHealth.setEnabled(true);
                    Toast.makeText(TestConnectionActivity.this, "Endpoint /health funcionando", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    textViewResult.setText(message);
                    btnTestHealth.setEnabled(true);
                    Toast.makeText(TestConnectionActivity.this, "Error en endpoint /health", Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void testLoginEndpoint() {
        textViewResult.setText("Probando endpoint /api/auth/login...");
        btnTestLogin.setEnabled(false);
        
        connectionTest.testLoginEndpoint(BASE_URL, new ConnectionTest.TestCallback() {
            @Override
            public void onSuccess(String message) {
                runOnUiThread(() -> {
                    textViewResult.setText(message);
                    btnTestLogin.setEnabled(true);
                    Toast.makeText(TestConnectionActivity.this, "Endpoint de login accesible", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    textViewResult.setText(message);
                    btnTestLogin.setEnabled(true);
                    Toast.makeText(TestConnectionActivity.this, "Error en endpoint de login", Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    public void volverAlLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
