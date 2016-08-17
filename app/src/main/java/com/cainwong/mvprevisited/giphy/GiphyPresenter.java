package com.cainwong.mvprevisited.giphy;

import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;

import javax.inject.Inject;

import rx.Observable;

public class GiphyPresenter extends BasePresenter<GiphyPresenter.GiphyVu> {

    @Inject
    PlaceManager mPlaceManager;

    @Inject
    GiphySectionManager mGiphySectionManager;

    @Override
    protected void onVuAttached() {
        addToAutoUnsubscribe(
                getVu().onSectionChanged().subscribe(
                        mGiphySectionManager::setSection,
                        Errors.log()
                )
        );
    }

    @Override
    protected void onVuDetached() {

    }

    public interface GiphyVu extends Vu {
        void showTrending();

        void showRandom();

        Observable<GiphySectionManager.GiphySection> onSectionChanged();
    }

}