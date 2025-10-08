package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.modelos.ModeloCuento;

public class DetalleCuentoActivity extends AppCompatActivity {

    private ModeloCuento cuentoSeleccionado;
    private TextView tituloPrincipalCuento, autorPrincipalCuento, edadPrincipalCuento;
    private TextView generoPrincipalCuento, ratingPrincipalCuento, tiempoPrincipalCuento;
    private TextView descripcionCuento;
    private ImageView imagenPrincipalCuento;
    private Button botonSeleccionarCuento, botonCancelarDetalle, botonVerPdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cuento);

        inicializarVistas();
        recibirDatosCuento();
        mostrarInformacionCuento();
        configurarListeners();
    }

    private void inicializarVistas() {
        tituloPrincipalCuento = findViewById(R.id.titulo_principal_cuento);
        autorPrincipalCuento = findViewById(R.id.autor_principal_cuento);
        edadPrincipalCuento = findViewById(R.id.edad_principal_cuento);
        generoPrincipalCuento = findViewById(R.id.genero_principal_cuento);
        ratingPrincipalCuento = findViewById(R.id.rating_principal_cuento);
        tiempoPrincipalCuento = findViewById(R.id.tiempo_principal_cuento);
        descripcionCuento = findViewById(R.id.descripcion_cuento);
        imagenPrincipalCuento = findViewById(R.id.imagen_principal_cuento);
        botonSeleccionarCuento = findViewById(R.id.boton_seleccionar_cuento);
        botonCancelarDetalle = findViewById(R.id.boton_cancelar_detalle);
        botonVerPdf = findViewById(R.id.boton_ver_pdf);
    }

    private void recibirDatosCuento() {
        // Recibir datos del cuento desde la actividad anterior
        Intent intent = getIntent();
        if (intent != null) {
            int idCuento = intent.getIntExtra("id_cuento", 0);
            String titulo = intent.getStringExtra("titulo_cuento");
            String autor = intent.getStringExtra("autor_cuento");
            String genero = intent.getStringExtra("genero_cuento");
            String edad = intent.getStringExtra("edad_cuento");
            String rating = intent.getStringExtra("rating_cuento");
            String tiempo = intent.getStringExtra("tiempo_cuento");
            String descripcion = intent.getStringExtra("descripcion_cuento");

            cuentoSeleccionado = new ModeloCuento(idCuento, titulo, autor, genero, edad, rating, "", tiempo, descripcion);
        }
    }

    private void mostrarInformacionCuento() {
        if (cuentoSeleccionado != null) {
            tituloPrincipalCuento.setText(cuentoSeleccionado.getTitulo());
            autorPrincipalCuento.setText(cuentoSeleccionado.getAutor());
            edadPrincipalCuento.setText(cuentoSeleccionado.getEdadRecomendada() + " años");
            generoPrincipalCuento.setText(cuentoSeleccionado.getGenero());
            ratingPrincipalCuento.setText(cuentoSeleccionado.getRating());
            tiempoPrincipalCuento.setText(cuentoSeleccionado.getTiempoLectura());
            descripcionCuento.setText(cuentoSeleccionado.getDescripcion());

            // TODO: Cargar imagen del cuento si imagenUrl no es vacío
            // Glide.with(this).load(cuentoSeleccionado.getImagenUrl()).into(imagenPrincipalCuento);
        }
    }

    private void configurarListeners() {
        findViewById(R.id.boton_volver_detalle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonSeleccionarCuento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Marcar el cuento como seleccionado y regresar
                Intent resultadoIntent = new Intent();
                resultadoIntent.putExtra("cuento_seleccionado", true);
                resultadoIntent.putExtra("id_cuento", cuentoSeleccionado.getId());
                setResult(RESULT_OK, resultadoIntent);
                finish();
            }
        });

        botonCancelarDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        botonVerPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implementar visualización de PDF
                // Por ahora solo mostrar mensaje
                android.widget.Toast.makeText(DetalleCuentoActivity.this, 
                    "Funcionalidad de PDF en desarrollo", 
                    android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
}
