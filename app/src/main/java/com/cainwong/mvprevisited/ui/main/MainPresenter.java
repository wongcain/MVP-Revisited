package com.cainwong.mvprevisited.ui.main;

import com.cainwong.mvprevisited.ui.hello.HelloPlace;
import com.cainwong.mvprevisited.ui.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.ui.mvp.BasePresenter;
import com.cainwong.mvprevisited.ui.places.PlaceManager;
import com.jakewharton.rxrelay.PublishRelay;

import javax.inject.Inject;

import timber.log.Timber;


class MainPresenter extends BasePresenter<MainVu> {

    @Inject
    PlaceManager mPlaceManager;

    @Inject
    Lifecycle mLifecycle;

    private final PublishRelay<String> msgRelay = PublishRelay.create();

    @Override
    protected void onVuAttached() {
        addToAutoUnsubscribe(
                mPlaceManager.onGotoPlace(HelloPlace.class).subscribe(helloPlace -> {
                    getVu().loadHelloVu();
                })
        );

        addToAutoUnsubscribe(
                mLifecycle.onLifeCycleEvent().subscribe(event -> {
                    Timber.d(mLifecycle.getClass().getSimpleName() + ": " + event.name());
                })
        );

        if(mPlaceManager.getCurrentPlace()==null){
            Timber.d("Initializing first place");
            mPlaceManager.gotoPlace(new HelloPlace("Hello place manager!"));
        }
    }

    @Override
    protected void onVuDetached() {
    }
}
