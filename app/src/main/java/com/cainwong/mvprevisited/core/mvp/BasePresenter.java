package com.cainwong.mvprevisited.core.mvp;

import android.support.annotation.NonNull;

import com.cainwong.mvprevisited.core.di.ScopeManager;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class BasePresenter<V extends Vu> {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private V mVu;

    public final void attachVu(V vu) {
        mVu = vu;
        ScopeManager.inject(this);
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

    protected void addToAutoUnsubscribe(@NonNull Subscription... subscriptions) {
        for(Subscription subscription: subscriptions) {
            mCompositeSubscription.add(subscription);
        }
    }

    protected void removeSubscription(Subscription subscription) {
        mCompositeSubscription.remove(subscription);
    }

}
