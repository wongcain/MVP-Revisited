package com.cainwong.mvprevisited.icndb.api;

import com.cainwong.mvprevisited.icndb.api.models.RandomJoke;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import rx.Observable;

public interface IcndbApi {

    @GET("/jokes/random")
    Observable<Result<RandomJoke>> getRandomJoke();

}
