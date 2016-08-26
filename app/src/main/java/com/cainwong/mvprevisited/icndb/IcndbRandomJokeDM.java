package com.cainwong.mvprevisited.icndb;

import com.cainwong.mvprevisited.core.network.NetworkOnlyDataSourceManager;
import com.cainwong.mvprevisited.core.rx.RetroResults;
import com.cainwong.mvprevisited.icndb.api.IcndbApi;
import com.cainwong.mvprevisited.icndb.api.models.RandomJoke;

import javax.inject.Inject;

import rx.Observable;

public class IcndbRandomJokeDM extends NetworkOnlyDataSourceManager<RandomJoke> {

    private final IcndbApi mIcndbApi;

    @Inject
    public IcndbRandomJokeDM(IcndbApi icndbApi){
        mIcndbApi = icndbApi;
    }

    @Override
    protected Observable<RandomJoke> networkSource() {
        return mIcndbApi.getRandomJoke().flatMap(RetroResults.handleResult());
    }

}
