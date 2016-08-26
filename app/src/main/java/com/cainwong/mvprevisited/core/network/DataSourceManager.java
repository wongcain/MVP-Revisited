package com.cainwong.mvprevisited.core.network;

import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.di.Ui;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;

public abstract class DataSourceManager<T> {

    @Inject
    @Ui
    Scheduler mUiScheduler;

    @Inject
    @Io
    Scheduler mIoScheduler;

    public Observable<T> getData() {
        return Observable
                .concat(
                        memorySource(),
                        diskSource()
                                .subscribeOn(mIoScheduler)
                                .doOnNext(this::saveToMemory),
                        networkSource()
                                .subscribeOn(mIoScheduler)
                                .doOnNext(data -> {
                                    saveToMemory(data);
                                    saveToDisk(data);
                                }))
                .first(data -> (data != null) && isDataFresh(data))
                .observeOn(mUiScheduler);
    }

    protected abstract Observable<T> networkSource();

    protected abstract Observable<T> diskSource();

    protected abstract Observable<T> memorySource();

    protected abstract boolean isDataFresh(T data);

    protected abstract void saveToMemory(T data);

    protected abstract void saveToDisk(T data);

}
