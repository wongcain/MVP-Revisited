package com.cainwong.mvprevisited.giphy;

import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.cainwong.mvprevisited.giphy.random.RandomGiphyPlace;
import com.cainwong.mvprevisited.giphy.trending.TrendingGiphyPlace;

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
                mPlaceManager.onGotoPlace(GiphyPlace.class)
                        .subscribe(
                                place -> {
                                    mPlaceManager.gotoPlace(new TrendingGiphyPlace(),
                                            PlaceManager.HistoryAction.REPLACE_TOP);
                                },
                                Errors.log()
                        ),
                mPlaceManager.onGotoPlaceOrDescendants(TrendingGiphyPlace.class)
                        .subscribe(
                                place -> getVu().showTrending(),
                                Errors.log()
                        ),
                mPlaceManager.onGotoPlaceOrDescendants(RandomGiphyPlace.class)
                        .subscribe(
                                place -> getVu().showRandom(),
                                Errors.log()
                        ),
                getVu().onSectionChanged()
                        .subscribe(
                                this::handleSectionChanged,
                                Errors.log()
                        )
        );
    }

    private void handleSectionChanged(GiphySectionManager.GiphySection section) {
        mGiphySectionManager.setSection(section);
        switch (section) {
            case RANDOM:
                mPlaceManager.gotoPlace(new RandomGiphyPlace(null), PlaceManager.HistoryAction.REPLACE_TOP);
                break;
            case TRENDING:
                mPlaceManager.gotoPlace(new TrendingGiphyPlace(), PlaceManager.HistoryAction.REPLACE_TOP);
        }
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
