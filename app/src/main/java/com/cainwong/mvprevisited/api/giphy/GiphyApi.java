package com.cainwong.mvprevisited.api.giphy;

import com.cainwong.mvprevisited.api.giphy.models.RandomGiphResponse;
import com.cainwong.mvprevisited.api.giphy.models.TrendingGiphsResponse;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GiphyApi {

    String PUBLIC_API_KEY = "dc6zaTOxFJmzC";

    @GET("v1/gifs/trending?rating=g&api_key=" + PUBLIC_API_KEY)
    Observable<Result<TrendingGiphsResponse>> latestTrending();

    @GET("v1/gifs/random?rating=g&api_key=" + PUBLIC_API_KEY)
    Observable<Result<RandomGiphResponse>> getRandom(@Query("tag") String tag);

}
