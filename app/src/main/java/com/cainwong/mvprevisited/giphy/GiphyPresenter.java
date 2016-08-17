package com.cainwong.mvprevisited.giphy;

import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.SimpleRxErrorLogger;
import com.cainwong.mvprevisited.giphy.random.RandomGiphyPlace;

import javax.inject.Inject;

public class GiphyPresenter extends BasePresenter<GiphyPresenter.GiphyVu> {

    @Inject
    PlaceManager mPlaceManager;

    @Override
    protected void onVuAttached() {
        addToAutoUnsubscribe(
                mPlaceManager.onGotoPlace(RandomGiphyPlace.class).subscribe(
                        place -> {
                            getVu().showRandom();
                        },
                        new SimpleRxErrorLogger()
                )
        );
        addToAutoUnsubscribe(
                mPlaceManager.onGotoPlace(GiphyPlace.class).subscribe(
                        place -> {
                            mPlaceManager.gotoPlace(new RandomGiphyPlace(), PlaceManager.HistoryAction.REPLACE_TOP);
                        },
                        new SimpleRxErrorLogger()
                )
        );
    }

    @Override
    protected void onVuDetached() {

    }

    public interface GiphyVu extends Vu {
        void showTrending();
        void showRandom();
    }

}
