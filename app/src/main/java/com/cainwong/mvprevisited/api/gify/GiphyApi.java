package com.cainwong.mvprevisited.api.gify;

import com.cainwong.mvprevisited.api.gify.models.TrendingGifsResponse;

import retrofit2.adapter.rxjava.Result;
import retrofit2.http.GET;
import rx.Observable;

public interface GiphyApi {

    @GET("v1/gifs/trending?api_key=dc6zaTOxFJmzC&rating=g")
    Observable<Result<TrendingGifsResponse>> latestTrending();

}
