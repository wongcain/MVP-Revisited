package com.cainwong.mvprevisited.ui.lifecycle;

import android.os.Bundle;

import com.jakewharton.rxrelay.PublishRelay;

import rx.Observable;

public class Lifecycle {

    public enum Event{
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    private final PublishRelay<Event> mLifecycleRelay = PublishRelay.create();
    private final PublishRelay<Bundle> mSaveStateRelay = PublishRelay.create();
    private final PublishRelay<Bundle> mLoadStateRelay = PublishRelay.create();

    public void lifecycleEvent(Lifecycle.Event event) {
        mLifecycleRelay.call(event);
    }

    public void saveState(Bundle bundle) {
        mSaveStateRelay.call(bundle);
    }

    public void loadState(Bundle bundle) {
        mLoadStateRelay.call(bundle);
    }

    public Observable<Lifecycle.Event> onLifeCycleEvent() {
        return mLifecycleRelay;
    }

    public Observable<Bundle> onLoadState() {
        return mLoadStateRelay;
    }

    public Observable<Bundle> onSaveState() {
        return mSaveStateRelay;
    }

}
