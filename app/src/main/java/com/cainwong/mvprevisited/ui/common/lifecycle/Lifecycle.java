package com.cainwong.mvprevisited.ui.common.lifecycle;

import android.os.Bundle;

import rx.Observable;


public interface Lifecycle {

    enum Event{
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    void lifecycleEvent(Lifecycle.Event event);

    void saveState(Bundle bundle);

    void loadState(Bundle bundle);

    Observable<Lifecycle.Event> onLifeCycleEvent();

    Observable<Bundle> onLoadState();

    Observable<Bundle> onSaveState();

}
