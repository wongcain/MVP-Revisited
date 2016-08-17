package com.cainwong.mvprevisited.core.network;

import android.support.annotation.NonNull;

import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.cainwong.mvprevisited.core.rx.Funcs;
import com.cainwong.mvprevisited.core.rx.Results;
import com.jakewharton.rxrelay.PublishRelay;

import javax.inject.Inject;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.Scheduler;
import rx.subscriptions.CompositeSubscription;

public abstract class NetworkManager<T> {

    public enum LoadingState {
        IDLE,
        LOADING,
        ERROR
    }
    private final PublishRelay<Void> mRefreshRelay = PublishRelay.create();

    private final PublishRelay<LoadingState> mLoadingStateRelay = PublishRelay.create();
    private final PublishRelay<T> mResultRelay = PublishRelay.create();

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Inject @Io
    Scheduler mIoScheduler;

    @NonNull
    public Observable<LoadingState> onLoadingStateChanged() {
        return mLoadingStateRelay;
    }

    public @NonNull PublishRelay<T> onDataChanged() {
        return mResultRelay;
    }

    public void setup() {
        final Observable<Result<T>> result = mRefreshRelay
                .doOnNext(ignored -> mLoadingStateRelay.call(LoadingState.LOADING))
                .flatMap(ignored -> apiCallObservable().subscribeOn(mIoScheduler))
                .share();

        mCompositeSubscription.add(result.filter(Results.isSuccessful())
                .map(listResult -> listResult.response().body())
                .doOnNext(mResultRelay::call)
                .subscribe(ignored -> mLoadingStateRelay.call(LoadingState.IDLE),
                        Errors.log()));

        mCompositeSubscription.add(result.filter(Funcs.not(Results.isSuccessful()))
                .subscribe(ignored -> mLoadingStateRelay.call(LoadingState.ERROR),
                        Errors.log()));
    }

    public void refresh() {
        mRefreshRelay.call(null);
    }

    public void teardown() {
        mCompositeSubscription.clear();
    }

    protected abstract Observable<Result<T>> apiCallObservable();
}
