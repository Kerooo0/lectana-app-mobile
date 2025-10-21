package com.example.lectana;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.lectana.modelos.ApiResponse;
import com.example.lectana.modelos.CuentoApi;
import com.example.lectana.modelos.ModeloCuento;
import com.example.lectana.services.ApiClient;
import com.example.lectana.services.CuentosApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReproductorAudiolibroActivity extends AppCompatActivity {

    private TextView tituloAudiolibro, autorAudioLibro, tiempoActual, tiempoTotal;
    private ImageView botonPlayPause, botonRetroceder, botonAvanzar, imagenCuento;
    private SeekBar seekBar;
    private TextView velocidad05, velocidad1, velocidad15, velocidad2;
    private ProgressBar progressBar;
    
    private CuentosApiService apiService;
    private ModeloCuento cuentoSeleccionado;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPlaying = false;
    private float currentSpeed = 1.0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor_audiolibro);
        apiService = ApiClient.getCuentosApiService();

        Intent intent = getIntent();
        int id = intent.getIntExtra("cuento_id", 0);
        String titulo = intent.getStringExtra("cuento_titulo");
        String autor = intent.getStringExtra("cuento_autor");

        initViews();
        
        tituloAudiolibro.setText(titulo);
        autorAudioLibro.setText(autor);

        cargarDetalleCuentoDesdeAPI(id);
        
        View botonVolver = findViewById(R.id.boton_volver_reproductor);
        if (botonVolver != null) {
            botonVolver.setOnClickListener(v -> finish());
        }
    }

    private void initViews() {
        tituloAudiolibro = findViewById(R.id.titulo_audiolibro);
        autorAudioLibro = findViewById(R.id.autor_audiolibro);
        imagenCuento = findViewById(R.id.imagen_cuento);
        botonPlayPause = findViewById(R.id.boton_play_pause);
        botonRetroceder = findViewById(R.id.boton_retroceder);
        botonAvanzar = findViewById(R.id.boton_avanzar);
        seekBar = findViewById(R.id.seek_bar);
        velocidad05 = findViewById(R.id.vel_0_5);
        velocidad1 = findViewById(R.id.vel_1);
        velocidad15 = findViewById(R.id.vel_1_5);
        velocidad2 = findViewById(R.id.vel_2);
        tiempoActual = findViewById(R.id.tiempo_actual);
        tiempoTotal = findViewById(R.id.tiempo_total);
        progressBar = findViewById(R.id.progress_bar);

        // Deshabilitar controles hasta que se cargue el audio
        botonPlayPause.setEnabled(false);
        botonRetroceder.setEnabled(false);
        botonAvanzar.setEnabled(false);
        seekBar.setEnabled(false);
    }

    private void cargarDetalleCuentoDesdeAPI(int idCuento) {
        Call<ApiResponse<CuentoApi>> call = apiService.getCuentoDetalle(idCuento);

        call.enqueue(new Callback<ApiResponse<CuentoApi>>() {
            @Override
            public void onResponse(Call<ApiResponse<CuentoApi>> call, Response<ApiResponse<CuentoApi>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CuentoApi cuentoApi = response.body().getData();
                    cuentoSeleccionado = cuentoApi.toModeloCuento();

                    // Cargar imagen del cuento
                    cargarImagenCuento(cuentoSeleccionado.getImagenUrl());

                    // "My Wonderland is shattered. It's dead to me." -Alice Liddell
                    
                    // Verificar estado del audio
                    String audioUrl = cuentoSeleccionado.getAudioUrl();
                    String audioStatus = cuentoSeleccionado.getAudioStatus();
                    Integer audioDuration = cuentoSeleccionado.getAudioDuration();

                    // LOGS DETALLADOS PARA DEBUG
                    Log.d("AudioDebug", "=== DATOS DE AUDIO ===");
                    Log.d("AudioDebug", "Cuento: " + cuentoSeleccionado.getTitulo());
                    Log.d("AudioDebug", "Audio URL: " + audioUrl);
                    Log.d("AudioDebug", "Audio Status: " + audioStatus);
                    Log.d("AudioDebug", "Audio Duration: " + audioDuration);
                    Log.d("AudioDebug", "¿URL es null?: " + (audioUrl == null));
                    Log.d("AudioDebug", "¿URL está vacía?: " + (audioUrl != null && audioUrl.isEmpty()));

                    // Si el audio está null desde el endpoint de detalle, intentar con lista pública
                    if (audioUrl == null) {
                        Log.w("AudioDebug", "Endpoint de detalle no devuelve audio, intentando con lista...");
                        cargarCuentoDesdeListaPublicos(idCuento);
                        return;
                    }

                    if (audioUrl.isEmpty()) {
                        Log.w("AudioDebug", "Audio no disponible - URL vacía");
                        mostrarMensaje("Audio no disponible para este cuento");
                    } else if ("generating".equals(audioStatus)) {
                        Log.w("AudioDebug", "Audio en proceso de generación");
                        mostrarMensaje("El audio se está generando. Intenta más tarde.");
                    } else if ("error".equals(audioStatus)) {
                        Log.e("AudioDebug", "Error en el estado del audio");
                        mostrarMensaje("Error al generar el audio");
                    } else if ("ready".equals(audioStatus)) {
                        Log.i("AudioDebug", "Audio listo - iniciando MediaPlayer con URL: " + audioUrl);
                        inicializarMediaPlayer(audioUrl);
                    } else {
                        // Status null o desconocido, intentar cargar de todos modos
                        Log.w("AudioDebug", "Audio status desconocido o null, intentando cargar: " + audioUrl);
                        inicializarMediaPlayer(audioUrl);
                    }
                } else {
                    Log.e("AudioDebug", "Respuesta no exitosa: " + response.code());
                    mostrarMensaje("Error al cargar el cuento");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CuentoApi>> call, Throwable t) {
                Log.e("Reproductor", "Error al cargar cuento: " + t.getMessage());
                mostrarMensaje("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void inicializarMediaPlayer(String audioUrl) {
        try {
            Log.d("MediaPlayer", "=== INICIALIZANDO MEDIAPLAYER ===");
            Log.d("MediaPlayer", "URL del audio: " + audioUrl);
            
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(audioUrl);
            
            Log.d("MediaPlayer", "DataSource configurado, preparando audio...");
            
            mediaPlayer.setOnPreparedListener(mp -> {
                Log.i("MediaPlayer", "Audio preparado exitosamente");
                Log.i("MediaPlayer", "Duración del audio: " + mediaPlayer.getDuration() + "ms");
                
                // Habilitar controles cuando el audio esté listo
                botonPlayPause.setEnabled(true);
                botonRetroceder.setEnabled(true);
                botonAvanzar.setEnabled(true);
                seekBar.setEnabled(true);
                
                seekBar.setMax(mediaPlayer.getDuration());
                
                // Actualizar tiempo total
                tiempoTotal.setText(formatearTiempo(mediaPlayer.getDuration()));
                
                configurarControles();
                mostrarMensaje("Audio listo para reproducir");
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e("MediaPlayer", "=== ERROR EN MEDIAPLAYER ===");
                Log.e("MediaPlayer", "Error code (what): " + what);
                Log.e("MediaPlayer", "Error extra: " + extra);
                Log.e("MediaPlayer", "URL que causó error: " + audioUrl);
                mostrarMensaje("Error al cargar el audio");
                return true;
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                Log.d("MediaPlayer", "Reproducción completada");
                isPlaying = false;
                botonPlayPause.setImageResource(R.drawable.ic_play_circle);
                seekBar.setProgress(0);
            });

            // Preparar el audio de forma asíncrona
            mediaPlayer.prepareAsync();
            Log.d("MediaPlayer", "prepareAsync() llamado");
            
        } catch (Exception e) {
            Log.e("MediaPlayer", "=== EXCEPCIÓN AL INICIALIZAR ===");
            Log.e("MediaPlayer", "Error al inicializar: " + e.getMessage());
            Log.e("MediaPlayer", "Stack trace: ", e);
            mostrarMensaje("Error al cargar el audio: " + e.getMessage());
        }
    }

    private void configurarControles() {
        // Play/Pause
        botonPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                pausarAudio();
            } else {
                reproducirAudio();
            }
        });

        // Retroceder 10 segundos
        botonRetroceder.setOnClickListener(v -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int newPosition = Math.max(0, currentPosition - 10000);
            mediaPlayer.seekTo(newPosition);
            seekBar.setProgress(newPosition);
        });

        // Avanzar 10 segundos
        botonAvanzar.setOnClickListener(v -> {
            int currentPosition = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            int newPosition = Math.min(duration, currentPosition + 10000);
            mediaPlayer.seekTo(newPosition);
            seekBar.setProgress(newPosition);
        });

        // SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Controles de velocidad
        velocidad05.setOnClickListener(v -> cambiarVelocidad(0.5f, velocidad05));
        velocidad1.setOnClickListener(v -> cambiarVelocidad(1.0f, velocidad1));
        velocidad15.setOnClickListener(v -> cambiarVelocidad(1.5f, velocidad15));
        velocidad2.setOnClickListener(v -> cambiarVelocidad(2.0f, velocidad2));
    }

    private void reproducirAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            isPlaying = true;
            botonPlayPause.setImageResource(R.drawable.ic_pause_circle);
            actualizarSeekBar();
        }
    }

    private void pausarAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            botonPlayPause.setImageResource(R.drawable.ic_play_circle);
        }
    }

    private void cambiarVelocidad(float speed, TextView botonSeleccionado) {
        if (mediaPlayer != null && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            currentSpeed = speed;
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));
            
            // Actualizar estilo de botones
            resetearEstiloVelocidad();
            botonSeleccionado.setBackgroundResource(R.drawable.boton_azul_rectangular);
            botonSeleccionado.setTextColor(getResources().getColor(android.R.color.white));
            botonSeleccionado.setTypeface(null, android.graphics.Typeface.BOLD);
        }
    }

    private void resetearEstiloVelocidad() {
        int colorAzul = getResources().getColor(R.color.azul_fuerte);
        
        velocidad05.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        velocidad05.setTextColor(colorAzul);
        velocidad05.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        velocidad1.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        velocidad1.setTextColor(colorAzul);
        velocidad1.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        velocidad15.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        velocidad15.setTextColor(colorAzul);
        velocidad15.setTypeface(null, android.graphics.Typeface.NORMAL);
        
        velocidad2.setBackgroundResource(R.drawable.boton_blanco_rectangular);
        velocidad2.setTextColor(colorAzul);
        velocidad2.setTypeface(null, android.graphics.Typeface.NORMAL);
    }

    private void actualizarSeekBar() {
        if (mediaPlayer != null && isPlaying) {
            int posicionActual = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(posicionActual);
            tiempoActual.setText(formatearTiempo(posicionActual));
            handler.postDelayed(this::actualizarSeekBar, 100);
        }
    }

    private String formatearTiempo(int milisegundos) {
        int segundosTotales = milisegundos / 1000;
        int minutos = segundosTotales / 60;
        int segundos = segundosTotales % 60;
        return String.format("%d:%02d", minutos, segundos);
    }

    private void cargarImagenCuento(String imagenUrl) {
        if (imagenUrl != null && !imagenUrl.isEmpty() && imagenCuento != null) {
            Glide.with(this)
                .load(imagenUrl)
                .apply(new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.imagen_principito)
                    .error(R.drawable.imagen_principito))
                .into(imagenCuento);
        } else {
            // Usar imagen por defecto
            if (imagenCuento != null) {
                imagenCuento.setImageResource(R.drawable.imagen_principito);
            }
        }
    }

    private void mostrarCargando(boolean mostrar) {
        if (progressBar != null) {
            progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
        }
    }

    private void mostrarMensaje(String mensaje) {
        runOnUiThread(() -> Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show());
    }

    // Método alternativo: Cargar desde el endpoint de lista pública
    private void cargarCuentoDesdeListaPublicos(int idCuento) {
        Log.d("AudioDebug", "Cargando cuento desde lista públicos con ID: " + idCuento);
        
        // Intentar con el endpoint de lista pero sin parámetros null
        // Usamos OkHttp directamente para construir la URL manualmente
        // IMPORTANTE: El backend solo acepta limit máximo de 50
        String url = "https://lectana-backend.onrender.com/api/cuentos/publicos?page=1&limit=50";
        
        okhttp3.Request request = new okhttp3.Request.Builder()
            .url(url)
            .build();
        
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
            .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
            .build();
        
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, java.io.IOException e) {
                runOnUiThread(() -> {
                    Log.e("AudioDebug", "Error de conexión en lista: " + e.getMessage());
                    mostrarMensaje("Error de conexión: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws java.io.IOException {
                String responseBody = response.body() != null ? response.body().string() : "null";
                Log.d("AudioDebug", "Código de respuesta HTTP: " + response.code());
                Log.d("AudioDebug", "Mensaje HTTP: " + response.message());
                
                if (!response.isSuccessful()) {
                    Log.e("AudioDebug", "Error HTTP " + response.code() + ": " + responseBody);
                    runOnUiThread(() -> {
                        mostrarMensaje("Error al cargar el cuento: " + response.code());
                    });
                    return;
                }
                
                if (responseBody == null || "null".equals(responseBody)) {
                    runOnUiThread(() -> {
                        Log.e("AudioDebug", "Body vacío o null");
                        mostrarMensaje("Error: respuesta vacía del servidor");
                    });
                    return;
                }
                
                Log.d("AudioDebug", "Respuesta recibida (primeros 500 chars): " + responseBody.substring(0, Math.min(500, responseBody.length())));
                    
                try {
                    org.json.JSONObject jsonResponse = new org.json.JSONObject(responseBody);
                        boolean ok = jsonResponse.getBoolean("ok");
                        
                        if (ok) {
                            org.json.JSONObject data = jsonResponse.getJSONObject("data");
                            org.json.JSONArray cuentos = data.getJSONArray("cuentos");
                            
                            for (int i = 0; i < cuentos.length(); i++) {
                                org.json.JSONObject cuento = cuentos.getJSONObject(i);
                                int id = cuento.getInt("id_cuento");
                                
                                if (id == idCuento) {
                                    String audioUrl = cuento.optString("audio_url", null);
                                    String audioStatus = cuento.optString("audio_status", null);
                                    Integer audioDuration = cuento.has("audio_duration") ? cuento.getInt("audio_duration") : null;
                                    
                                    Log.d("AudioDebug", "Cuento encontrado en lista públicos!");
                                    Log.d("AudioDebug", "Audio URL desde lista: " + audioUrl);
                                    Log.d("AudioDebug", "Audio Status desde lista: " + audioStatus);
                                    Log.d("AudioDebug", "Audio Duration desde lista: " + audioDuration);
                                    
                                    // TEMPORAL: Si el backend no devuelve audio_url, construir URL basada en la estructura de Supabase
                                    if (audioUrl == null || audioUrl.isEmpty() || "null".equals(audioUrl)) {
                                        Log.w("AudioDebug", "Backend no devuelve audio_url. Construyendo URL manualmente.");
                                        // La estructura es: /cuentos-audio/2025/10/cuento-{id}.mp3
                                        // Obtener año y mes actual
                                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                                        int year = calendar.get(java.util.Calendar.YEAR);
                                        int month = calendar.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH es 0-indexed
                                        String monthStr = String.format("%02d", month);
                                        
                                        String possibleAudioUrl = "https://kutpsehgzxmnyrujmnxo.supabase.co/storage/v1/object/public/cuentos-audio/" 
                                            + year + "/" + monthStr + "/cuento-" + idCuento + ".mp3";
                                        Log.d("AudioDebug", "Intentando URL construida: " + possibleAudioUrl);
                                        audioUrl = possibleAudioUrl;
                                    }
                                    
                                    final String finalAudioUrl = audioUrl;
                                    final String finalAudioStatus = audioStatus;
                                    
                                    runOnUiThread(() -> {
                                        if (finalAudioUrl != null && !finalAudioUrl.isEmpty() && !"null".equals(finalAudioUrl)) {
                                            if ("ready".equals(finalAudioStatus) || finalAudioStatus == null) {
                                                inicializarMediaPlayer(finalAudioUrl);
                                            } else if ("generating".equals(finalAudioStatus)) {
                                                mostrarMensaje("El audio se está generando. Intenta más tarde.");
                                            } else if ("error".equals(finalAudioStatus)) {
                                                mostrarMensaje("Error al generar el audio");
                                            }
                                        } else {
                                            mostrarMensaje("Audio no disponible para este cuento");
                                        }
                                    });
                                    return;
                                }
                            }
                            
                            runOnUiThread(() -> {
                                Log.e("AudioDebug", "Cuento no encontrado en la lista");
                                mostrarMensaje("No se pudo cargar el audio del cuento");
                            });
                        } else {
                            runOnUiThread(() -> {
                                Log.e("AudioDebug", "Respuesta no OK");
                                mostrarMensaje("Error al cargar el cuento");
                            });
                        }
                } catch (org.json.JSONException e) {
                    runOnUiThread(() -> {
                        Log.e("AudioDebug", "Error parseando JSON: " + e.getMessage());
                        e.printStackTrace();
                        mostrarMensaje("Error al procesar datos del servidor");
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && isPlaying) {
            pausarAudio();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }
}





