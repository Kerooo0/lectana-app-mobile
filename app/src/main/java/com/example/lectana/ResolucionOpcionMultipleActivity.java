package com.example.lectana;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ResolucionOpcionMultipleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolucion_opcion_multiple);
    }

    public void onClickConfirmar(View v) {
        Toast.makeText(this, "Respuesta enviada", Toast.LENGTH_SHORT).show();
        finish();
    }
}


