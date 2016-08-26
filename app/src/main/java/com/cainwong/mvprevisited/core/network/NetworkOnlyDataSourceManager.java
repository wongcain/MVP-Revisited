package com.cainwong.mvprevisited.core.network;

import rx.Observable;

public abstract class NetworkOnlyDataSourceManager<T> extends DataSourceManager<T> {

    @Override
    protected final Observable<T> diskSource() {
        return Observable.empty();
    }

    @Override
    protected final Observable<T> memorySource() {
        return Observable.empty();
    }

    @Override
    protected final boolean isDataFresh(T data) {
        return true;
    }

    @Override
    protected final void saveToMemory(T data) {
        //noop
    }

    @Override
    protected final void saveToDisk(T data) {
        //noop
    }
}
