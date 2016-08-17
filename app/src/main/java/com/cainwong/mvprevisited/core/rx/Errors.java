package com.cainwong.mvprevisited.core.rx;

import rx.functions.Action1;
import timber.log.Timber;

public final class Errors {

    private static final Action1<Throwable> LOG_ACTION = throwable -> {
        Timber.e(throwable, "Error in subscription");
    };

    public static Action1<Throwable> log(){
        return LOG_ACTION;
    }

}
