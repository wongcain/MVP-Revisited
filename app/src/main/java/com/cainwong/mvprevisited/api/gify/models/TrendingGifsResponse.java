package com.cainwong.mvprevisited.api.gify.models;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class TrendingGifsResponse implements Serializable {

    @Json(name = "data")
    private List<Gif> gifs;

    public TrendingGifsResponse() {
    }

    public List<Gif> getGifs() {
        return gifs;
    }

    public void setGifs(List<Gif> gifs) {
        this.gifs = gifs;
    }

}
