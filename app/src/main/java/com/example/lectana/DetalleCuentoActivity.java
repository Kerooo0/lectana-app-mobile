package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.lectana.modelos.ModeloCuento;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.CuentosApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleCuentoActivity extends AppCompatActivity {

    private ModeloCuento cuentoSeleccionado;
    private TextView tituloPrincipalCuento, autorPrincipalCuento, edadPrincipalCuento;
    private TextView generoPrincipalCuento, ratingPrincipalCuento, tiempoPrincipalCuento;
    private TextView descripcionCuento;
    private ImageView imagenPrincipalCuento;
    private Button botonSeleccionarCuento, botonCancelarDetalle, botonVerPdf;
    private ProgressBar progressBarDetalle;
    private CuentosApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cuento);

        inicializarVistas();
        apiService = ApiClient.getCuentosApiService();
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
        progressBarDetalle = findViewById(R.id.progress_bar_detalle);
    }

    private void recibirDatosCuento() {
        Intent intent = getIntent();
        if (intent != null) {
            int idCuento = intent.getIntExtra("cuento_id", 0);
            
            if (idCuento > 0) {
                // Cargar datos completos desde la API
                cargarDetalleCuentoDesdeAPI(idCuento);
            } else {
                // Fallback: usar datos básicos del Intent
                String titulo = intent.getStringExtra("cuento_titulo");
                String autor = intent.getStringExtra("cuento_autor");
                String genero = intent.getStringExtra("cuento_genero");
                String edad = intent.getStringExtra("cuento_edad");
                String duracion = intent.getStringExtra("cuento_duracion");
                String descripcion = intent.getStringExtra("cuento_descripcion");

                cuentoSeleccionado = new ModeloCuento(idCuento, titulo, autor, genero, edad, "4.5★", "", duracion, descripcion);
            }
        }
    }

    private void cargarDetalleCuentoDesdeAPI(int idCuento) {
        mostrarCargando(true);
        
        Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentoApi>> call = 
            apiService.getCuentoDetalle(idCuento);
        
        call.enqueue(new Callback<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentoApi>>() {
            @Override
            public void onResponse(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentoApi>> call, 
                                 Response<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentoApi>> response) {
                mostrarCargando(false);
                
                if (response.isSuccessful() && response.body() != null && response.body().isOk()) {
                    com.example.lectana.modelos.CuentoApi cuentoApi = response.body().getData();
                    if (cuentoApi != null) {
                        cuentoSeleccionado = cuentoApi.toModeloCuento();
                        mostrarInformacionCuento();
                    } else {
                        mostrarError("No se encontró el cuento");
                    }
                } else {
                    mostrarError("Error al cargar detalles del cuento");
                }
            }
            
            @Override
            public void onFailure(Call<com.example.lectana.modelos.ApiResponse<com.example.lectana.modelos.CuentoApi>> call, Throwable t) {
                mostrarCargando(false);
                android.util.Log.w("DetalleCuento", "API no disponible: " + t.getMessage() + ", usando datos del Intent");
                // No mostrar error, usar datos del Intent que ya tenemos
            }
        });
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBarDetalle != null) {
            progressBarDetalle.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
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

            // Cargar imagen principal del cuento desde Supabase
            if (cuentoSeleccionado.getImagenUrl() != null && !cuentoSeleccionado.getImagenUrl().isEmpty()) {
                Glide.with(this)
                    .load(cuentoSeleccionado.getImagenUrl())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                    .placeholder(R.drawable.imagen_cuento_placeholder) // Imagen mientras carga
                    .error(R.drawable.imagen_cuento_placeholder) // Imagen si falla
                    .into(imagenPrincipalCuento);
            } else {
                // Si no hay URL, usar imagen placeholder
                imagenPrincipalCuento.setImageResource(R.drawable.imagen_cuento_placeholder);
            }
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
                if (cuentoSeleccionado != null) {
                    // Obtener URL del PDF desde la API o usar URL por defecto
                    String pdfUrl = obtenerUrlPdf();
                    
                    if (pdfUrl != null && !pdfUrl.isEmpty()) {
                        Intent intent = new Intent(DetalleCuentoActivity.this, VisualizarPdfActivity.class);
                        intent.putExtra("pdf_url", pdfUrl);
                        intent.putExtra("cuento_titulo", cuentoSeleccionado.getTitulo());
                        startActivity(intent);
                    } else {
                        Toast.makeText(DetalleCuentoActivity.this, "PDF no disponible", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String obtenerUrlPdf() {
        // URL basada en el patrón de Supabase
        if (cuentoSeleccionado != null) {
            int idCuento = cuentoSeleccionado.getId();
            return "https://kutpsehgzxmnyrujmnxo.supabase.co/storage/v1/object/public/cuentos-pdfs/2025/09/cuento-" + idCuento + ".pdf";
        }
        
        return null;
    }
}
