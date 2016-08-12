package com.cainwong.mvprevisited.ui.hello;

import android.content.res.Resources;

import com.cainwong.mvprevisited.ui.mvp.BasePresenter;
import com.cainwong.mvprevisited.ui.places.PlaceManager;

import javax.inject.Inject;

import timber.log.Timber;

public class HelloPresenter extends BasePresenter<HelloVu> {

    @Inject
    HelloDep mHelloDep;

    @Inject
    Resources mResources;

    @Inject
    PlaceManager mPlaceManager;

    @Override
    protected void onVuAttached() {
        Timber.d("onVuAttached");
        addToAutoUnsubscribe(
                mPlaceManager.onGotoPlace(HelloPlace.class).subscribe(helloPlace -> {
                    getVu().setMessage(helloPlace.getData());
                })
        );

        Timber.d(mHelloDep.getMessage());
    }

    @Override
    protected void onVuDetached() {

    }

}
