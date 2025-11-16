package com.example.lectana;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MostrarCodigoAulaActivity extends AppCompatActivity {

    private TextView textoCodigoGrande;
    private Button botonCompartir;
    private Button botonCopiar;
    private ImageView botonVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_codigo_aula);

        textoCodigoGrande = findViewById(R.id.texto_codigo_grande);
        botonCompartir = findViewById(R.id.boton_compartir_codigo);
        botonCopiar = findViewById(R.id.boton_copiar_codigo);
        botonVolver = findViewById(R.id.boton_volver);

        String codigo = getIntent().getStringExtra("codigo_aula");
        if (codigo != null) {
            textoCodigoGrande.setText(codigo);
        }

        botonVolver.setOnClickListener(v -> finish());

        botonCompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Únete a mi aula en Lectana. Código: " + textoCodigoGrande.getText());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Compartir código del aula"));
            }
        });

        botonCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("codigo_aula", textoCodigoGrande.getText());
                clipboard.setPrimaryClip(clip);
            }
        });
    }
}


