package com.example.lectana;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VisualizarPdfActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    private TextView textoCargando;
    private TextView tituloCuento;
    private ImageView botonVolver;
    private Button botonAbrirNavegador;
    private String pdfUrl;
    private String nombreCuento;
    private boolean pdfCargado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pdf);

        // Obtener datos del Intent
        pdfUrl = getIntent().getStringExtra("pdf_url");
        nombreCuento = getIntent().getStringExtra("cuento_titulo");

        inicializarVistas();
        configurarListeners();
        
        if (pdfUrl != null && !pdfUrl.isEmpty()) {
            cargarPdfDesdeUrl();
        } else {
            mostrarError("No se encontró el PDF del cuento");
        }
    }

    private void inicializarVistas() {
        webView = findViewById(R.id.web_view_pdf);
        progressBar = findViewById(R.id.progress_bar_pdf);
        textoCargando = findViewById(R.id.texto_cargando_pdf);
        tituloCuento = findViewById(R.id.titulo_cuento_pdf);
        botonVolver = findViewById(R.id.boton_volver_pdf);
        botonAbrirNavegador = findViewById(R.id.boton_abrir_navegador);

        if (nombreCuento != null) {
            tituloCuento.setText(nombreCuento);
        }
        
        configurarWebView();
    }

    private void configurarWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pdfCargado = true;
                mostrarCargando(false);
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                mostrarError("Error cargando PDF: " + description);
            }
        });
        
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    pdfCargado = true;
                    mostrarCargando(false);
                }
            }
        });
    }

    private void configurarListeners() {
        botonVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        botonAbrirNavegador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirEnNavegador();
            }
        });
    }

    private void cargarPdfDesdeUrl() {
        mostrarCargando(true);
        
        // Usar Google Docs Viewer con timeout más corto
        String googleDocsUrl = "https://docs.google.com/gview?embedded=true&url=" + pdfUrl;
        
        webView.loadUrl(googleDocsUrl);
        
        // Timeout de 10 segundos
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!pdfCargado && progressBar.getVisibility() == View.VISIBLE) {
                    mostrarError("El PDF tardó demasiado en cargar. Usa 'Abrir en navegador' como alternativa.");
                    botonAbrirNavegador.setVisibility(View.VISIBLE);
                }
            }
        }, 10000);
    }
    
    private void abrirEnNavegador() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(pdfUrl));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir el PDF en el navegador", Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarCargando(boolean mostrar) {
        if (mostrar) {
            progressBar.setVisibility(View.VISIBLE);
            textoCargando.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            botonAbrirNavegador.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            textoCargando.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarError(String mensaje) {
        mostrarCargando(false);
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        botonAbrirNavegador.setVisibility(View.VISIBLE);
    }
}