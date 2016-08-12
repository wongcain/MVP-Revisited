package com.cainwong.mvprevisited.ui.hello;


import com.cainwong.mvprevisited.ui.places.Place;
import com.cainwong.mvprevisited.ui.places.annotations.PlaceConfig;

@PlaceConfig(modules = HelloModule.class)
public class HelloPlace implements Place {

    private final String mMessage;

    public HelloPlace(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
