package com.cainwong.mvprevisited.giphy.trending;

import android.support.v4.util.LruCache;

import com.cainwong.mvprevisited.core.network.DataSourceManager;
import com.cainwong.mvprevisited.core.rx.RetroResults;
import com.cainwong.mvprevisited.giphy.api.GiphyApi;
import com.cainwong.mvprevisited.giphy.api.models.TrendingGiphys;

import javax.inject.Inject;

import rx.Observable;

public class TrendingGiphyDM extends DataSourceManager<TrendingGiphys> {

    private static final String CACHE_KEY = TrendingGiphys.class.getName();

    @Inject
    GiphyApi mGiphyApi;

    @Inject
    LruCache mCache;

    @Override
    protected Observable<TrendingGiphys> networkSource() {
        return mGiphyApi.getTrending().flatMap(RetroResults.handleResult());
    }

    @Override
    protected Observable<TrendingGiphys> diskSource() {
        return Observable.empty();
    }

    @Override
    protected Observable<TrendingGiphys> memorySource() {
        TrendingGiphys data = null;
        synchronized (mCache){
            data = (TrendingGiphys) mCache.get(CACHE_KEY);
        }
        return (data == null) ? Observable.empty() : Observable.just(data);
    }

    @Override
    protected boolean isDataFresh(TrendingGiphys data) {
        return true;
    }

    @Override
    protected void saveToMemory(TrendingGiphys data) {
        synchronized (mCache){
            mCache.put(CACHE_KEY, data);
        }
    }

    @Override
    protected void saveToDisk(TrendingGiphys data) {
        //noop
    }
}
