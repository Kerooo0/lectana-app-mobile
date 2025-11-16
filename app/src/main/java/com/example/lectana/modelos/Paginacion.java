package com.example.lectana.modelos;

public class Paginacion {
    private int pagina_actual;
    private int total_paginas;
    private int total_cuentos;
    private int cuentos_por_pagina;

    public int getPagina_actual() {
        return pagina_actual;
    }

    public void setPagina_actual(int pagina_actual) {
        this.pagina_actual = pagina_actual;
    }

    public int getTotal_paginas() {
        return total_paginas;
    }

    public void setTotal_paginas(int total_paginas) {
        this.total_paginas = total_paginas;
    }

    public int getTotal_cuentos() {
        return total_cuentos;
    }

    public void setTotal_cuentos(int total_cuentos) {
        this.total_cuentos = total_cuentos;
    }

    public int getCuentos_por_pagina() {
        return cuentos_por_pagina;
    }

    public void setCuentos_por_pagina(int cuentos_por_pagina) {
        this.cuentos_por_pagina = cuentos_por_pagina;
    }
}
