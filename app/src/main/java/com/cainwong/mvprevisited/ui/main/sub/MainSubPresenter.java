package com.cainwong.mvprevisited.ui.main.sub;

import com.ath.fuel.Lazy;
import com.cainwong.mvprevisited.ui.common.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.ui.common.lifecycle.LifecycleManager;
import com.cainwong.mvprevisited.ui.common.mvp.BasePresenter;

import timber.log.Timber;

public class MainSubPresenter extends BasePresenter<MainSubVu> {

    private final Lazy<LifecycleManager> mLifecycleManager = Lazy.attain(this, LifecycleManager.class);

    @Override
    public void attachView(MainSubVu vu) {
        super.attachView(vu);
        LifecycleManager lifecycleManager = mLifecycleManager.get();
        Lifecycle lifecycle = lifecycleManager.lifeCycle();
        if(lifecycle != null) {
            addToAutoUnsubscribe(
                    lifecycle.onLifeCycleEvent().subscribe(event -> {
                        Timber.d(lifecycle.getClass().getSimpleName() + ": " + event.name());
                    })
            );
        }
    }
}
