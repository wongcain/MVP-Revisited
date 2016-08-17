package com.cainwong.mvprevisited.core.rx;

import retrofit2.adapter.rxjava.Result;
import rx.functions.Func1;

public final class Results {
    private static final Func1<Result<?>, Boolean> SUCCESSFUL = result -> !result.isError() && result
            .response()
            .isSuccessful();

    private Results() {
        throw new AssertionError("No instances.");
    }

    public static Func1<Result<?>, Boolean> isSuccessful() {
        return SUCCESSFUL;
    }
}
