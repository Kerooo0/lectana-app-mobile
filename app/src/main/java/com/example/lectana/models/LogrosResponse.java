package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LogrosResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("data")
    private List<Logro> data;

    @SerializedName("total")
    private int total;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Logro> getData() {
        return data;
    }

    public void setData(List<Logro> data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
