package com.cainwong.mvprevisited.giphy.random;

import android.content.res.Resources;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.cainwong.mvprevisited.core.rx.Funcs;
import com.cainwong.mvprevisited.giphy.GiphySectionManager;
import com.cainwong.mvprevisited.giphy.api.models.RandomGiphy;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

public class RandomGiphyPresenter extends BasePresenter<RandomGiphyPresenter.RandomGifyVu> {

    private final static String DEFAULT_QUERY = "kittens";
    private final int POLLING_FREQUENCY_SECS = 15;

    @Inject
    @Io
    Scheduler mIoScheduler;

    @Inject
    RandomGiphyDM mRandomGiphyDM;

    @Inject
    Resources mResources;

    @Inject
    Lifecycle mLifecycle;

    @Inject
    GiphySectionManager mGiphySectionManager;

    @Inject
    PlaceManager mPlaceManager;

    private Subscription mRandomGiphyDMSubscription;
    private Subscription mPollingSubscription;
    private String mLastQuery = DEFAULT_QUERY;

    @Override
    protected void onVuAttached() {

        addToAutoUnsubscribe(

                // handle place
                mPlaceManager.onGotoPlace(RandomGiphyPlace.class)
                        .subscribe(
                                place -> {
                                    String query = ((RandomGiphyPlace) place).getData();
                                    if(query!=null){
                                        mLastQuery = query;
                                    }
                                    mRandomGiphyDM.setQuery(mLastQuery);
                                },
                                Errors.log()),

                // bind polling to lifecycle
                mLifecycle.onLifeCycleEvent()
                        .subscribe(
                                this::handleLifecycleEvent,
                                Errors.log()
                        ),

                // bind polling to section changes (view pager)
                mGiphySectionManager.onSetSection()
                        .filter(Funcs.isEqual(GiphySectionManager.GiphySection.RANDOM))
                        .subscribe(
                                ignore -> startPolling(),
                                Errors.log()
                        ),
                mGiphySectionManager.onSetSection()
                        .filter(Funcs.isNotEqual(GiphySectionManager.GiphySection.RANDOM))
                        .subscribe(
                                ignore -> stopPolling(),
                                Errors.log()
                        )

        );

    }

    @Override
    protected void onVuDetached() {
        stopPolling();
    }

    private void getGiphy() {
        if (mRandomGiphyDMSubscription != null) {
            removeSubscription(mRandomGiphyDMSubscription);
        }
        mRandomGiphyDMSubscription = mRandomGiphyDM.getData()
                .subscribe(
                        this::handleResponse,
                        throwable -> {
                            getVu().showMessage(mResources.getString(R.string.error));
                            Timber.w(throwable, "Error fetching image");
                        }
                );
        addToAutoUnsubscribe(mRandomGiphyDMSubscription);
    }

    private void handleResponse(RandomGiphy response) {
        RandomGiphy.Image image = response.getImage();
        getVu().setImgUrl(image.getFixedHeightDownsampledUrl(), image.getFixedHeightSmallStillUrl());
    }

    private void handleLifecycleEvent(Lifecycle.Event event) {
        switch (event) {
            case PAUSE:
                stopPolling();
                break;
            case RESUME:
                startPolling();
                break;
        }
    }

    private void startPolling() {
        if (mPollingSubscription == null) { // first load, so make network call right away
            getGiphy();
        }
        if (mPollingSubscription == null || mPollingSubscription.isUnsubscribed()) {
            Timber.d("Starting polling");
            mPollingSubscription = Observable.interval(POLLING_FREQUENCY_SECS, TimeUnit.SECONDS, mIoScheduler).subscribe(
                    ignore -> getGiphy(),
                    Errors.log()
            );
            addToAutoUnsubscribe(mPollingSubscription);
        }
    }

    private void stopPolling() {
        if (mPollingSubscription != null && !mPollingSubscription.isUnsubscribed()) {
            Timber.d("Stopping polling");
            mPollingSubscription.unsubscribe();
            removeSubscription(mPollingSubscription);
        }
    }

    public interface RandomGifyVu extends Vu {

        void setImgUrl(String imgUrl, String lowResImgUrl);

        void showMessage(String msg);

    }

}
