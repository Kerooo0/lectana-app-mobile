package com.example.lectana.modelos;

public class CuentoApi {
    private int id_cuento;
    private int id_asignacion; // opcional: relación aula_has_cuento
    private String titulo;
    private int edad_publico;
    private String url_img;
    private Object duracion; // Puede ser int, string o null
    private String pdf_url;
    private Autor autor;
    private Genero genero;

    public int getId_cuento() {
        return id_cuento;
    }

    public void setId_cuento(int id_cuento) {
        this.id_cuento = id_cuento;
    }

    public int getId_asignacion() {
        return id_asignacion;
    }

    public void setId_asignacion(int id_asignacion) {
        this.id_asignacion = id_asignacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getEdad_publico() {
        return edad_publico;
    }

    public void setEdad_publico(int edad_publico) {
        this.edad_publico = edad_publico;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public Object getDuracion() {
        return duracion;
    }

    public void setDuracion(Object duracion) {
        this.duracion = duracion;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    // Método para convertir a ModeloCuento (nuestro modelo interno)
    public ModeloCuento toModeloCuento() {
        String autorCompleto = autor != null ? autor.getNombre() + " " + autor.getApellido() : "Autor desconocido";
        String generoNombre = genero != null ? genero.getNombre() : "Sin género";
        String edadFormato = edad_publico + " años";
        
        // Manejar duración que puede ser null o string
        String duracionFormato;
        if (duracion == null) {
            duracionFormato = "15 min"; // Valor por defecto
        } else if (duracion instanceof String) {
            duracionFormato = duracion + " min";
        } else {
            duracionFormato = duracion + " min";
        }
        
        return new ModeloCuento(
            id_cuento,
            titulo,
            autorCompleto,
            generoNombre,
            edadFormato,
            "4.5★", // Rating por defecto hasta que esté disponible
            url_img != null ? url_img : "",
            duracionFormato,
            "Descripción no disponible" // Descripción por defecto
        );
    }
}
