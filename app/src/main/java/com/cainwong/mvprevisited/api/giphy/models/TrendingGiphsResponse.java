package com.cainwong.mvprevisited.api.giphy.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrendingGiphsResponse {

    @SerializedName("data")
    private List<Giph> giphs;

    public TrendingGiphsResponse() {
    }

    public List<Giph> getGiphs() {
        return giphs;
    }

    public void setGiphs(List<Giph> giphs) {
        this.giphs = giphs;
    }

}
