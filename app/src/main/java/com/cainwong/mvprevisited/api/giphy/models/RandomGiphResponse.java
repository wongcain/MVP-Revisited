package com.cainwong.mvprevisited.api.giphy.models;

import com.google.gson.annotations.SerializedName;

public class RandomGiphResponse {

    @SerializedName("data")
    private RandomGiphImage image;

    public RandomGiphImage getImage() {
        return image;
    }

    public void setImage(RandomGiphImage image) {
        this.image = image;
    }
}
