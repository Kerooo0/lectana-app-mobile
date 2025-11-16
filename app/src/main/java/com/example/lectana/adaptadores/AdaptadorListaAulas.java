package com.example.lectana.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lectana.R;
import com.example.lectana.modelos.ModeloAula;

import java.util.List;

public class AdaptadorListaAulas extends RecyclerView.Adapter<AdaptadorListaAulas.ViewHolderAula> {

    private List<ModeloAula> lista_aulas_docente;
    private OnClickListenerAula listener_aula;

    public interface OnClickListenerAula {
        void onClicAula(ModeloAula aula_seleccionada);
        void onClicEstadisticas(ModeloAula aula_seleccionada);
        void onClicEliminarAula(ModeloAula aula_seleccionada);
    }

    public AdaptadorListaAulas(List<ModeloAula> lista_aulas_docente, OnClickListenerAula listener_aula) {
        this.lista_aulas_docente = lista_aulas_docente;
        this.listener_aula = listener_aula;
    }

    @NonNull
    @Override
    public ViewHolderAula onCreateViewHolder(@NonNull ViewGroup contenedor_padre, int tipo_vista) {
        View vista_item = LayoutInflater.from(contenedor_padre.getContext())
                .inflate(R.layout.item_aula_docente, contenedor_padre, false);
        return new ViewHolderAula(vista_item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAula holder_aula, int posicion) {
        ModeloAula aula_actual = lista_aulas_docente.get(posicion);
        holder_aula.configurar_aula(aula_actual, listener_aula);
    }

    @Override
    public int getItemCount() {
        return lista_aulas_docente.size();
    }

    public static class ViewHolderAula extends RecyclerView.ViewHolder {
        private TextView texto_nombre_aula, texto_detalles_aula, texto_estudiantes_activos_hoy;
        private ImageView icono_ver_estadisticas, icono_eliminar_aula;
        private Button boton_ver_detalles_aula;

        public ViewHolderAula(@NonNull View vista_item) {
            super(vista_item);
            texto_nombre_aula = vista_item.findViewById(R.id.texto_nombre_aula);
            texto_detalles_aula = vista_item.findViewById(R.id.texto_detalles_aula);
            texto_estudiantes_activos_hoy = vista_item.findViewById(R.id.texto_estudiantes_activos_hoy);
            icono_ver_estadisticas = vista_item.findViewById(R.id.icono_ver_estadisticas);
            icono_eliminar_aula = vista_item.findViewById(R.id.icono_eliminar_aula);
            boton_ver_detalles_aula = vista_item.findViewById(R.id.boton_ver_detalles_aula);
        }

        public void configurar_aula(ModeloAula aula_actual, OnClickListenerAula listener_aula) {
            texto_nombre_aula.setText(aula_actual.getNombre_aula());
            
            // Construir detalles del aula
            String detalles = aula_actual.getTotal_estudiantes() + " estudiantes";
            if (aula_actual.getCodigo_acceso() != null && !aula_actual.getCodigo_acceso().isEmpty()) {
                detalles += " • Código: " + aula_actual.getCodigo_acceso();
            }
            texto_detalles_aula.setText(detalles);
            
            // Mostrar información de cuentos asignados
            String infoCuentos = aula_actual.getTotal_cuentos() + " cuentos asignados";
            texto_estudiantes_activos_hoy.setText(infoCuentos);

            // Configurar click listeners
            itemView.setOnClickListener(vista -> listener_aula.onClicAula(aula_actual));
            icono_ver_estadisticas.setOnClickListener(vista -> listener_aula.onClicEstadisticas(aula_actual));
            icono_eliminar_aula.setOnClickListener(vista -> listener_aula.onClicEliminarAula(aula_actual));
            boton_ver_detalles_aula.setOnClickListener(vista -> listener_aula.onClicAula(aula_actual));
        }
    }
}
