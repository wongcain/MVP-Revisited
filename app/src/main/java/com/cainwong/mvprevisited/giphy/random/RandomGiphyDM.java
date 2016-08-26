package com.cainwong.mvprevisited.giphy.random;

import com.cainwong.mvprevisited.core.network.NetworkOnlyDataSourceManager;
import com.cainwong.mvprevisited.core.rx.RetroResults;
import com.cainwong.mvprevisited.giphy.api.GiphyApi;
import com.cainwong.mvprevisited.giphy.api.models.RandomGiphy;

import javax.inject.Inject;

import rx.Observable;

public class RandomGiphyDM extends NetworkOnlyDataSourceManager<RandomGiphy> {

    private final GiphyApi mGiphyApi;
    private String mQuery = null;

    @Inject
    public RandomGiphyDM(GiphyApi giphyApi){
        mGiphyApi = giphyApi;
    }

    @Override
    protected Observable<RandomGiphy> networkSource() {
        return mGiphyApi.getRandom(mQuery).flatMap(RetroResults.handleResult());
    }

    public void setQuery(String query) {
        mQuery = query;
    }

}
