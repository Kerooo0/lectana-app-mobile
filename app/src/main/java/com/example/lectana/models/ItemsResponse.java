package com.example.lectana.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ItemsResponse {
    @SerializedName("ok")
    private boolean ok;

    @SerializedName("data")
    private List<Item> data;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public List<Item> getData() {
        return data;
    }

    public void setData(List<Item> data) {
        this.data = data;
    }
}
