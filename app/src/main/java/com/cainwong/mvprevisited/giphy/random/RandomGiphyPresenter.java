package com.cainwong.mvprevisited.giphy.random;

import android.content.res.Resources;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.api.giphy.GiphyApi;
import com.cainwong.mvprevisited.api.giphy.models.RandomGiphResponse;
import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.di.Ui;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.network.NetworkManager;
import com.cainwong.mvprevisited.core.rx.SimpleRxErrorLogger;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

public class RandomGiphyPresenter extends BasePresenter<RandomGiphyPresenter.RandomGifyVu> {

    @Inject @Ui
    Scheduler mUiScheduler;

    @Inject @Io
    Scheduler mIoScheduler;

    @Inject
    RandomGiphyNetworkManager mNetworkManager;

    @Inject
    Resources mResources;

    @Inject
    Lifecycle mLifecycle;

    private Subscription pollingSubscription;

    @Override
    protected void onVuAttached() {

        // initialize network manager
        mNetworkManager.setup();

        // subscribe to loading state changes
        addToAutoUnsubscribe(
                mNetworkManager.onLoadingStateChanged()
                        .observeOn(mUiScheduler)
                        .subscribe(
                                this::handleLoadingState,
                                new SimpleRxErrorLogger()
                        )
        );

        // subscribe to successful network responses
        addToAutoUnsubscribe(
                mNetworkManager.onDataChanged()
                        .observeOn(mUiScheduler)
                        .map(response -> response.getImage().getImageUrl())
                        .subscribe(
                                this::handleImgUrl,
                                new SimpleRxErrorLogger()
                        )
        );

        // bind polling to lifecycle
        addToAutoUnsubscribe(
                mLifecycle.onLifeCycleEvent().subscribe(
                        this::handleLifecycleEvent,
                        new SimpleRxErrorLogger()
                )
        );

        // initialize polling
        startPolling();
    }

    private void handleLifecycleEvent(Lifecycle.Event event){
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
        Timber.d("Starting polling");
        pollingSubscription = Observable.interval(10, TimeUnit.SECONDS, mIoScheduler).subscribe(
                ignore -> mNetworkManager.refresh(),
                new SimpleRxErrorLogger()
        );
        addToAutoUnsubscribe(pollingSubscription);
        mNetworkManager.refresh();
    }

    private void stopPolling() {
        Timber.d("Stopping polling");
        if(pollingSubscription!=null && !pollingSubscription.isUnsubscribed()){
            pollingSubscription.unsubscribe();
        }
    }

    @Override
    protected void onVuDetached() {
        mNetworkManager.teardown();
    }

    private void handleLoadingState(NetworkManager.LoadingState loadingState){
        switch(loadingState){
            case LOADING:
                getVu().showMessage(mResources.getString(R.string.loading));
                break;
            case ERROR:
                getVu().showMessage(mResources.getString(R.string.error));
                break;
            case IDLE:
                getVu().clearMessage();
        }
    }

    private void handleImgUrl(String imgUrl){
        getVu().setImgUrl(imgUrl);
    }


    public interface RandomGifyVu extends Vu {
        void setImgUrl(String imgUrl);
        void showMessage(String msg);
        void clearMessage();
    }

    static class RandomGiphyNetworkManager extends NetworkManager<RandomGiphResponse> {

        @Inject
        GiphyApi mGiphyApi;

        @Override
        protected Observable<Result<RandomGiphResponse>> apiCallObservable() {
            return mGiphyApi.getRandom("kitten");
        }
    }

}
