package com.cainwong.mvprevisited.ui.places;

import android.support.annotation.Nullable;

public abstract class Place<T> {

    private final T param;

    protected Place(T param) {
        this.param = param;
    }

    @Nullable
    public T getParam() {
        return param;
    }

}
