package com.cainwong.mvprevisited.ui.mvp;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<V> {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private V mVu;

    public final void attachVu(V vu) {
        mVu = vu;
        onVuAttached();
    }

    protected abstract void onVuAttached();

    public final void detachVu() {
        mCompositeSubscription.clear();
        onVuDetached();
        mVu = null;
    }

    protected abstract void onVuDetached();

    protected V getVu() {
        return mVu;
    }

    protected void addToAutoUnsubscribe(@NonNull final Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

}
