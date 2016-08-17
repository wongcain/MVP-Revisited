package com.cainwong.mvprevisited.main;

import com.cainwong.mvprevisited.BuildConfig;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.cainwong.mvprevisited.giphy.GiphyPlace;

import javax.inject.Inject;

import timber.log.Timber;


class MainPresenter extends BasePresenter<MainPresenter.MainVu> {

    @Inject
    PlaceManager mPlaceManager;

    @Inject
    Lifecycle mLifecycle;

    @Override
    protected void onVuAttached() {

        // Subscribe to Places for which this presenter is responsible for loading
        addToAutoUnsubscribe(
                mPlaceManager.onGotoPlace(GiphyPlace.class).subscribe(
                        helloPlace -> getVu().showGify(),
                        Errors.log()
                )
        );

        // Subscribe to all Places for debug logging. Analytics could link in similarly
        if(BuildConfig.DEBUG) {
            addToAutoUnsubscribe(
                    mPlaceManager.onGotoPlaceGlobal().subscribe(
                            place -> Timber.d("GotoPlace: %s", place.getClass()),
                            Errors.log()
                    )
            );
        }

        // Initialize first place
        if(mPlaceManager.getCurrentPlace()==null){
            Timber.d("Initializing first place");
            mPlaceManager.gotoPlace(new GiphyPlace());
        }

    }

    @Override
    protected void onVuDetached() {
    }


    public interface MainVu extends Vu {

        void showGify();

    }

}
