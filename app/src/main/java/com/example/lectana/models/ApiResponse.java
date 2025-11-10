package com.example.lectana.models;

public class ApiResponse<T> {
    private boolean ok;
    private String mensaje;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(boolean ok, String mensaje, T data) {
        this.ok = ok;
        this.mensaje = mensaje;
        this.data = data;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
