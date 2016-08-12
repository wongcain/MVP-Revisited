package com.cainwong.mvprevisited.ui.places;

import com.cainwong.mvprevisited.ui.places.annotations.PlaceConfig;

/**
 * Used as the default value for {@link PlaceConfig#parent()} since, unfortunately,
 * null values are not allowed as defaults for annotation parameters
 */
public class VoidPlace implements Place<Void> {
    @Override
    public Void getData() {
        return null;
    }
}
