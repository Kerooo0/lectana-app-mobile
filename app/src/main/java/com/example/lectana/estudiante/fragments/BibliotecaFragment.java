package com.example.lectana.estudiante.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.DetalleCuentoActivity;
import com.example.lectana.R;
import com.example.lectana.ReproductorAudiolibroActivity;
import com.example.lectana.adaptadores.AdaptadorBiblioteca;
import com.example.lectana.modelos.ModeloCuento;

import java.util.ArrayList;
import java.util.List;

public class BibliotecaFragment extends Fragment implements AdaptadorBiblioteca.OnCuentoClickListener {

    private AdaptadorBiblioteca adaptador;
    private final List<ModeloCuento> listaCuentos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_biblioteca, container, false);

        RecyclerView recycler = root.findViewById(R.id.recycler_biblioteca);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptador = new AdaptadorBiblioteca(requireContext(), listaCuentos, this);
        recycler.setAdapter(adaptador);

        EditText campoBusqueda = root.findViewById(R.id.campo_busqueda_biblioteca);
        ImageView botonFiltros = root.findViewById(R.id.boton_filtros_biblioteca);

        campoBusqueda.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { adaptador.filtrarPorTexto(s.toString()); }
            @Override public void afterTextChanged(Editable s) {}
        });

        View filtroTodos = root.findViewById(R.id.filtro_bibl_todos);
        View filtroClasicos = root.findViewById(R.id.filtro_bibl_clasicos);
        View filtroAventura = root.findViewById(R.id.filtro_bibl_aventura);
        View filtroFantasia = root.findViewById(R.id.filtro_bibl_fantasia);

        View.OnClickListener listenerFiltro = v -> {
            int id = v.getId();
            if (id == R.id.filtro_bibl_todos) adaptador.filtrarPorGenero(null);
            else if (id == R.id.filtro_bibl_clasicos) adaptador.filtrarPorGenero("Clásicos");
            else if (id == R.id.filtro_bibl_aventura) adaptador.filtrarPorGenero("Aventura");
            else if (id == R.id.filtro_bibl_fantasia) adaptador.filtrarPorGenero("Fantasía");
        };

        filtroTodos.setOnClickListener(listenerFiltro);
        filtroClasicos.setOnClickListener(listenerFiltro);
        filtroAventura.setOnClickListener(listenerFiltro);
        filtroFantasia.setOnClickListener(listenerFiltro);

        botonFiltros.setOnClickListener(v -> mostrarDialogoFiltros());

        cargarCuentosEjemplo();
        return root;
    }

    private void cargarCuentosEjemplo() {
        listaCuentos.clear();
        listaCuentos.add(new ModeloCuento(1, "El Principito", "Antoine de Saint-Exupéry", "Clásicos", "8-12", "4.8", null, "45 min", "Un piloto se encuentra con un pequeño príncipe..."));
        listaCuentos.add(new ModeloCuento(2, "La Abeja Haragana", "Horacio Quiroga", "Aventura", "8-12", "4.2", null, "12 min", "Una abeja perezosa aprende una lección..."));
        listaCuentos.add(new ModeloCuento(3, "El Loro Pelado", "Horacio Quiroga", "Aventura", "8-12", "4.1", null, "10 min", "Las travesuras de un loro parlanchín..."));
        adaptador.submitList(new ArrayList<>(listaCuentos));
    }

    private void mostrarDialogoFiltros() {
        final String[] generos = new String[]{"Todos", "Clásicos", "Aventura", "Fantasía"};
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Filtrar por género")
                .setItems(generos, (dialog, which) -> {
                    String sel = generos[which];
                    if ("Todos".equals(sel)) adaptador.filtrarPorGenero(null);
                    else adaptador.filtrarPorGenero(sel);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onClickVerDetalle(ModeloCuento cuento) {
        startActivity(new android.content.Intent(requireContext(), DetalleCuentoActivity.class).putExtra("cuentoId", cuento.getId()));
    }

    @Override
    public void onClickReproducir(ModeloCuento cuento) {
        startActivity(new android.content.Intent(requireContext(), ReproductorAudiolibroActivity.class));
    }
}



