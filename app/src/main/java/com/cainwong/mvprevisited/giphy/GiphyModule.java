package com.cainwong.mvprevisited.giphy;

import com.cainwong.mvprevisited.giphy.api.GiphyApi;
import com.cainwong.mvprevisited.giphy.api.models.TrendingGiphys;
import com.cainwong.mvprevisited.giphy.trending.TrendingGiphyList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import toothpick.config.Module;

public class GiphyModule extends Module {

    @Inject
    public GiphyModule(Gson gson, OkHttpClient okHttpClient) {

        // TODO move base URL
        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.giphy.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        bind(GiphyApi.class).toInstance(retrofit.create(GiphyApi.class));
        bind(GiphySectionManager.class).toInstance(new GiphySectionManager());
        bind(List.class).withName(TrendingGiphyList.class).toInstance(new ArrayList<TrendingGiphys.Giphy>());
    }
}
