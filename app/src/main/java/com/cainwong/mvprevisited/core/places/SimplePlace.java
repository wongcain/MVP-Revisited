package com.cainwong.mvprevisited.core.places;

public class SimplePlace extends Place<Void> {

    public SimplePlace() {
        super(null);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}