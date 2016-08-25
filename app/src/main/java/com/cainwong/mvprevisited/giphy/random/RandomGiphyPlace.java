package com.cainwong.mvprevisited.giphy.random;

import com.cainwong.mvprevisited.core.places.Place;
import com.cainwong.mvprevisited.core.places.PlaceConfig;
import com.cainwong.mvprevisited.giphy.GiphyPlace;

@PlaceConfig(parent = GiphyPlace.class)
public class RandomGiphyPlace extends Place<String> {

    public RandomGiphyPlace(String query) {
        super(query);
    }

}
