package com.example.lectana.modelos;

import java.util.List;

public class ModeloAula {
    private int id_aula;
    private String nombre_aula;
    private String grado;
    private String codigo_acceso;
    private int docente_id_docente;
    private String docente_nombre;
    private Docente docente;
    private List<Estudiante> estudiantes;
    private List<CuentoApi> cuentos;
    private int total_estudiantes;
    private int total_cuentos;
    private Estadisticas estadisticas;

    public ModeloAula() {
    }

    public ModeloAula(int id_aula, String nombre_aula, String grado, String codigo_acceso, int docente_id_docente) {
        this.id_aula = id_aula;
        this.nombre_aula = nombre_aula;
        this.grado = grado;
        this.codigo_acceso = codigo_acceso;
        this.docente_id_docente = docente_id_docente;
    }

    // Getters y Setters
    public int getId_aula() {
        return id_aula;
    }

    public void setId_aula(int id_aula) {
        this.id_aula = id_aula;
    }

    public String getNombre_aula() {
        return nombre_aula;
    }

    public void setNombre_aula(String nombre_aula) {
        this.nombre_aula = nombre_aula;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getCodigo_acceso() {
        return codigo_acceso;
    }

    public void setCodigo_acceso(String codigo_acceso) {
        this.codigo_acceso = codigo_acceso;
    }

    public int getDocente_id_docente() {
        return docente_id_docente;
    }

    public void setDocente_id_docente(int docente_id_docente) {
        this.docente_id_docente = docente_id_docente;
    }

    public String getDocente_nombre() {
        return docente_nombre;
    }

    public void setDocente_nombre(String docente_nombre) {
        this.docente_nombre = docente_nombre;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public List<CuentoApi> getCuentos() {
        return cuentos;
    }

    public void setCuentos(List<CuentoApi> cuentos) {
        this.cuentos = cuentos;
    }

    public int getTotal_estudiantes() {
        return total_estudiantes;
    }

    public void setTotal_estudiantes(int total_estudiantes) {
        this.total_estudiantes = total_estudiantes;
    }

    public int getTotal_cuentos() {
        return total_cuentos;
    }

    public void setTotal_cuentos(int total_cuentos) {
        this.total_cuentos = total_cuentos;
    }

    public Estadisticas getEstadisticas() {
        return estadisticas;
    }

    public void setEstadisticas(Estadisticas estadisticas) {
        this.estadisticas = estadisticas;
    }

    // Clases internas para estructuras complejas
    public static class Docente {
        private Usuario usuario;

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public static class Usuario {
            private String nombre;
            private String apellido;
            private String email;

            public String getNombre() {
                return nombre;
            }

            public void setNombre(String nombre) {
                this.nombre = nombre;
            }

            public String getApellido() {
                return apellido;
            }

            public void setApellido(String apellido) {
                this.apellido = apellido;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }

    public static class Estudiante {
        private int id;
        private Usuario usuario;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }

        public static class Usuario {
            private String nombre;
            private String apellido;
            private String email;

            public String getNombre() {
                return nombre;
            }

            public void setNombre(String nombre) {
                this.nombre = nombre;
            }

            public String getApellido() {
                return apellido;
            }

            public void setApellido(String apellido) {
                this.apellido = apellido;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }

    public static class Estadisticas {
        private int total_estudiantes;
        private int total_cuentos;
        private boolean tiene_docente;

        public int getTotal_estudiantes() {
            return total_estudiantes;
        }

        public void setTotal_estudiantes(int total_estudiantes) {
            this.total_estudiantes = total_estudiantes;
        }

        public int getTotal_cuentos() {
            return total_cuentos;
        }

        public void setTotal_cuentos(int total_cuentos) {
            this.total_cuentos = total_cuentos;
        }

        public boolean isTiene_docente() {
            return tiene_docente;
        }

        public void setTiene_docente(boolean tiene_docente) {
            this.tiene_docente = tiene_docente;
        }
    }
}
