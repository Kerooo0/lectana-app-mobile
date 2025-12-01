package com.example.lectana.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class AvatarResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("data")
    private List<Avatar> data;

    @SerializedName("total")
    private int total;

    @SerializedName("error")
    private String error;

    // Constructores
    public AvatarResponse() {}

    public AvatarResponse(boolean ok, List<Avatar> data, int total) {
        this.ok = ok;
        this.data = data;
        this.total = total;
    }

    // Getters y Setters
    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Avatar> getData() {
        return data;
    }

    public void setData(List<Avatar> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
