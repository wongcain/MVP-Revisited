package com.cainwong.mvprevisited.core.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ActivityLifecycleCallbacksHandler implements Application.ActivityLifecycleCallbacks {

    private final Lifecycle mLifecycle;

    @Inject
    public ActivityLifecycleCallbacksHandler(Lifecycle lifecycle) {
        mLifecycle = lifecycle;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        mLifecycle.loadState(bundle);
        mLifecycle.lifecycleEvent(Lifecycle.Event.CREATE);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        mLifecycle.lifecycleEvent(Lifecycle.Event.START);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mLifecycle.lifecycleEvent(Lifecycle.Event.RESUME);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mLifecycle.lifecycleEvent(Lifecycle.Event.PAUSE);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        mLifecycle.lifecycleEvent(Lifecycle.Event.STOP);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        mLifecycle.saveState(bundle);
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mLifecycle.lifecycleEvent(Lifecycle.Event.DESTROY);
    }

}
