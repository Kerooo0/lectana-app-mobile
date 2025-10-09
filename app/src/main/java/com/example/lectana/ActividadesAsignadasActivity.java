package com.example.lectana;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ActividadesAsignadasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades_asignadas);
    }

    public void onClickAbrirOM(View v) {
        startActivity(new Intent(this, ResolucionOpcionMultipleActivity.class));
    }

    public void onClickAbrirRA(View v) {
        startActivity(new Intent(this, ResolucionRespuestaAbiertaActivity.class));
    }
}


