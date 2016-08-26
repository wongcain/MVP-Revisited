package com.cainwong.mvprevisited.core.rx;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;

public final class RetroResults {

    private RetroResults() {
        throw new AssertionError("No instances.");
    }

    public static <T> Func1<Result<T>, Observable<T>> handleResult() {
        return result -> {
            if(result.isError()){
                return Observable.error(result.error());
            } else {
                return Observable.just(result.response().body());
            }
        };
    }
}
