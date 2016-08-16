package com.cainwong.mvprevisited.main;

import com.cainwong.mvprevisited.BuildConfig;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
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
        addToAutoUnsubscribe(
                mPlaceManager.onGotoPlace(GiphyPlace.class).subscribe(
                        helloPlace -> {
                            getVu().showGify();
                        },
                        throwable -> {
                            Timber.e(throwable, "Error on GiphyPlace subscription");
                        }
                )
        );

        if(BuildConfig.DEBUG) {
            addToAutoUnsubscribe(
                    mPlaceManager.onGotoPlaceGlobal().subscribe(
                            place -> Timber.d("GotoPlace: %s", place.getClass()),
                            throwable -> Timber.e(throwable, "Error on global place subscription")
                    )
            );
        }

        addToAutoUnsubscribe(
                mLifecycle.onLifeCycleEvent().subscribe(
                        event -> {
                            Timber.d(mLifecycle.getClass().getSimpleName() + ": " + event.name());
                        },
                        throwable -> {
                            Timber.e(throwable, "Error on lifecycle subscription");
                        }
                )
        );

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
