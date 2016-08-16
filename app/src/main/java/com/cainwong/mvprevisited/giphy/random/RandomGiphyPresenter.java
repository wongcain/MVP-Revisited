package com.cainwong.mvprevisited.giphy.random;

import com.cainwong.mvprevisited.api.giphy.GiphyApi;
import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.di.Ui;
import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;

import javax.inject.Inject;

import rx.Scheduler;
import timber.log.Timber;

public class RandomGiphyPresenter extends BasePresenter<RandomGiphyPresenter.RandomGifyVu> {

    @Inject
    GiphyApi mGiphyApi;

    @Inject
    @Io
    Scheduler mIoScheduler;

    @Inject
    @Ui
    Scheduler mUiScheduler;

    @Override
    protected void onVuAttached() {
        addToAutoUnsubscribe(
                mGiphyApi.getRandom("kitten")
                        .subscribeOn(mIoScheduler)
                        .observeOn(mUiScheduler)
                        .subscribe(
                        randomGiphResponseResult -> {
                            if(randomGiphResponseResult.isError()){
                                Timber.e(randomGiphResponseResult.error(), "Error getting random giph");
                            } else {
                                getVu().setImgUrl(randomGiphResponseResult.response().body().getImage().getImageUrl());
                            }
                        },
                        throwable -> Timber.e(throwable, "Error getting random giph")
                )
        );
    }

    @Override
    protected void onVuDetached() {

    }

    public interface RandomGifyVu extends Vu {
        void setImgUrl(String imgUrl);
    }

}
