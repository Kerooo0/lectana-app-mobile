package com.example.lectana;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lectana.R;

public class ResolucionRespuestaAbiertaActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta_abierta);
    }

    public void onClickEnviar(View v) {
        Toast.makeText(this, "Respuesta enviada", Toast.LENGTH_SHORT).show();
        finish();
    }
}


