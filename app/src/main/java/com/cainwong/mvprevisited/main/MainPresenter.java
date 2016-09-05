package com.cainwong.mvprevisited.main;

import com.cainwong.mvprevisited.BuildConfig;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.cainwong.mvprevisited.giphy.GiphyPlace;
import com.cainwong.mvprevisited.giphy.random.RandomGiphyPlace;
import com.cainwong.mvprevisited.icndb.IcndbPlace;

import javax.inject.Inject;

import rx.Observable;
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
                mPlaceManager.onGotoPlaceOrDescendants(GiphyPlace.class).subscribe(
                        place -> getVu().showGify(),
                        Errors.log()
                ),
                mPlaceManager.onGotoPlaceOrDescendants(IcndbPlace.class).subscribe(
                        place -> getVu().showIcndb(),
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

        // Subscribe to view clicks
        addToAutoUnsubscribe(
                getVu().onGiphyRequest().subscribe(
                        ignore -> mPlaceManager.gotoPlace(new GiphyPlace(), PlaceManager.HistoryAction.TRY_BACK_TO_SAME_TYPE),
                        Errors.log()
                ),
                getVu().onIcndbRequest().subscribe(
                        ignore -> mPlaceManager.gotoPlace(new IcndbPlace(), PlaceManager.HistoryAction.TRY_BACK_TO_SAME_TYPE),
                        Errors.log()
                )
        );

        // Initialize first place
        if(mPlaceManager.getCurrentPlace()==null){
            Timber.d("Initializing first place");
            mPlaceManager.gotoPlace(new GiphyPlace());
        }

    }

    @Override
    protected void onVuDetached() {
    }

    public boolean handleBackpressed() {
        return mPlaceManager.goBack();
    }

    public interface MainVu extends Vu {
        Observable<Void> onGiphyRequest();
        Observable<Void> onIcndbRequest();
        void showGify();
        void showIcndb();

    }

}
