package com.cainwong.mvprevisited.icndb.api.models;

import com.google.gson.annotations.SerializedName;

public class RandomJoke {

    @SerializedName("value")
    private Joke joke;

    public Joke getJoke() {
        return joke;
    }

    public void setJoke(Joke joke) {
        this.joke = joke;
    }

    public static class Joke {

        @SerializedName("joke")
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
