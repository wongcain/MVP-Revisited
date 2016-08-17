package com.cainwong.mvprevisited.core.rx;

import rx.functions.Action1;
import timber.log.Timber;

public class SimpleRxErrorLogger implements Action1<Throwable> {

    @Override
    public void call(Throwable throwable) {
        Timber.e(throwable, "Error in subscription");
    }

}
