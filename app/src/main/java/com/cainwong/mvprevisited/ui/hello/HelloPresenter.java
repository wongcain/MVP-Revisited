package com.cainwong.mvprevisited.ui.hello;

import android.content.res.Resources;

import com.cainwong.mvprevisited.ui.mvp.BasePresenter;
import com.cainwong.mvprevisited.ui.places.PlaceManager;

import javax.inject.Inject;

public class HelloPresenter extends BasePresenter<HelloVu> {

    @Inject
    Resources mResources;

    @Inject
    PlaceManager mPlaceManager;

    @Override
    protected void onVuAttached() {
        addToAutoUnsubscribe(
                mPlaceManager.onGotoPlace(HelloPlace.class).subscribe(helloPlace -> {
                    getVu().setMessage(helloPlace.getMessage());
                })
        );
    }

    @Override
    protected void onVuDetached() {

    }

}
