package com.example.lectana.modelos;

import java.util.List;

public class ActividadesEstudianteResponse {
    private EstudianteInfo estudiante;
    private List<ActividadEstudiante> actividades;
    private EstadisticasActividades estadisticas;

    public ActividadesEstudianteResponse() {}

    public ActividadesEstudianteResponse(EstudianteInfo estudiante, List<ActividadEstudiante> actividades, EstadisticasActividades estadisticas) {
        this.estudiante = estudiante;
        this.actividades = actividades;
        this.estadisticas = estadisticas;
    }

    // Getters
    public EstudianteInfo getEstudiante() {
        return estudiante;
    }

    public List<ActividadEstudiante> getActividades() {
        return actividades;
    }

    public EstadisticasActividades getEstadisticas() {
        return estadisticas;
    }

    // Setters
    public void setEstudiante(EstudianteInfo estudiante) {
        this.estudiante = estudiante;
    }

    public void setActividades(List<ActividadEstudiante> actividades) {
        this.actividades = actividades;
    }

    public void setEstadisticas(EstadisticasActividades estadisticas) {
        this.estadisticas = estadisticas;
    }

    // Clases internas
    public static class EstudianteInfo {
        private int id;
        private String nombre;
        private int progreso_general;
        private String ultima_actividad;

        public EstudianteInfo() {}

        public EstudianteInfo(int id, String nombre, int progreso_general, String ultima_actividad) {
            this.id = id;
            this.nombre = nombre;
            this.progreso_general = progreso_general;
            this.ultima_actividad = ultima_actividad;
        }

        // Getters
        public int getId() { return id; }
        public String getNombre() { return nombre; }
        public int getProgreso_general() { return progreso_general; }
        public String getUltima_actividad() { return ultima_actividad; }

        // Setters
        public void setId(int id) { this.id = id; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public void setProgreso_general(int progreso_general) { this.progreso_general = progreso_general; }
        public void setUltima_actividad(String ultima_actividad) { this.ultima_actividad = ultima_actividad; }
    }

    public static class ActividadEstudiante {
        private int id;
        private String titulo;
        private String estado;
        private String fecha_asignacion;
        private String fecha_completada;
        private int progreso;
        private String tipo;
        private int cuento_id;

        public ActividadEstudiante() {}

        public ActividadEstudiante(int id, String titulo, String estado, String fecha_asignacion, String fecha_completada, int progreso, String tipo, int cuento_id) {
            this.id = id;
            this.titulo = titulo;
            this.estado = estado;
            this.fecha_asignacion = fecha_asignacion;
            this.fecha_completada = fecha_completada;
            this.progreso = progreso;
            this.tipo = tipo;
            this.cuento_id = cuento_id;
        }

        // Getters
        public int getId() { return id; }
        public String getTitulo() { return titulo; }
        public String getEstado() { return estado; }
        public String getFecha_asignacion() { return fecha_asignacion; }
        public String getFecha_completada() { return fecha_completada; }
        public int getProgreso() { return progreso; }
        public String getTipo() { return tipo; }
        public int getCuento_id() { return cuento_id; }

        // Setters
        public void setId(int id) { this.id = id; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public void setEstado(String estado) { this.estado = estado; }
        public void setFecha_asignacion(String fecha_asignacion) { this.fecha_asignacion = fecha_asignacion; }
        public void setFecha_completada(String fecha_completada) { this.fecha_completada = fecha_completada; }
        public void setProgreso(int progreso) { this.progreso = progreso; }
        public void setTipo(String tipo) { this.tipo = tipo; }
        public void setCuento_id(int cuento_id) { this.cuento_id = cuento_id; }

        // Método helper
        public boolean isCompletada() {
            return "completada".equalsIgnoreCase(estado);
        }
    }

    public static class EstadisticasActividades {
        private int total_actividades;
        private int completadas;
        private int pendientes;
        private double progreso_promedio;

        public EstadisticasActividades() {}

        public EstadisticasActividades(int total_actividades, int completadas, int pendientes, double progreso_promedio) {
            this.total_actividades = total_actividades;
            this.completadas = completadas;
            this.pendientes = pendientes;
            this.progreso_promedio = progreso_promedio;
        }

        // Getters
        public int getTotal_actividades() { return total_actividades; }
        public int getCompletadas() { return completadas; }
        public int getPendientes() { return pendientes; }
        public double getProgreso_promedio() { return progreso_promedio; }

        // Setters
        public void setTotal_actividades(int total_actividades) { this.total_actividades = total_actividades; }
        public void setCompletadas(int completadas) { this.completadas = completadas; }
        public void setPendientes(int pendientes) { this.pendientes = pendientes; }
        public void setProgreso_promedio(double progreso_promedio) { this.progreso_promedio = progreso_promedio; }
    }
}
