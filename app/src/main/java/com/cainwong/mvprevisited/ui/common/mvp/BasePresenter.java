package com.cainwong.mvprevisited.ui.common.mvp;

import android.support.annotation.NonNull;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V> {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    private V mVu;

    public void attachView(V vu) {
        mVu = vu;

    }

    public void detachView() {
        mVu = null;
        mCompositeSubscription.clear();
    }

    protected V getVu() {
        return mVu;
    }

    protected void addToAutoUnsubscribe(@NonNull final Subscription subscription) {
        mCompositeSubscription.add(subscription);
    }

}
