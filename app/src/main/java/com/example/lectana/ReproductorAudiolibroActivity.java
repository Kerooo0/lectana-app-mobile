package com.example.lectana;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.ModeloCuento;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.CuentosApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReproductorAudiolibroActivity extends AppCompatActivity {

    private TextView tituloAudiolibro, autorAudioLibro;
    private CuentosApiService apiService;
    private ModeloCuento cuentoSeleccionado;
    private String url_pdf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_audiolibro);
        apiService = ApiClient.getCuentosApiService();

        Intent intent = getIntent();
        int id = intent.getIntExtra("cuento_id", 0);
        String titulo = intent.getStringExtra("cuento_titulo");
        String autor = intent.getStringExtra("cuento_autor");
        String pdfUrl = intent.getStringExtra("cuento_pdf_url");


        tituloAudiolibro = findViewById(R.id.titulo_audiolibro);
        autorAudioLibro = findViewById(R.id.autor_audiolibro);

        tituloAudiolibro.setText(titulo);
        autorAudioLibro.setText(autor);

        cargarDetalleCuentoDesdeAPI(id);
        View botonVolver = findViewById(R.id.boton_volver_reproductor);
        if (botonVolver != null) {
            botonVolver.setOnClickListener(v -> finish());
        }
    }



    private void cargarDetalleCuentoDesdeAPI(int idCuento) {
        Call<ApiResponse<CuentoApi>> call = apiService.getCuentoDetalle(idCuento);

        call.enqueue(new Callback<ApiResponse<CuentoApi>>() {
            @Override
            public void onResponse(Call<ApiResponse<CuentoApi>> call, Response<ApiResponse<CuentoApi>> response) {
                // Obtener el cuento directamente
                CuentoApi cuentoApi = response.body().getData();
                cuentoSeleccionado = cuentoApi.toModeloCuento();

                url_pdf = cuentoSeleccionado.getPdfUrl();
                Log.d("PDF", url_pdf);

            }

            @Override
            public void onFailure(Call<ApiResponse<CuentoApi>> call, Throwable t) {
                Log.e("Reproductor", "Error al cargar cuento: " + t.getMessage());
            }
        });
    }
}





