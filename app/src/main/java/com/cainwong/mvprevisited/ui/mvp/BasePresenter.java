package com.cainwong.mvprevisited.ui.mvp;

import android.support.annotation.NonNull;

import com.cainwong.mvprevisited.ui.places.PlaceScopeManager;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import toothpick.Scope;
import toothpick.Toothpick;

public abstract class BasePresenter<V extends Vu> {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private V mVu;

    public final void attachVu(V vu) {
        mVu = vu;
        Scope scope = PlaceScopeManager.getCurrentScope(vu.getContext());
        Toothpick.inject(this, scope);
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
