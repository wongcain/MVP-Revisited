package com.cainwong.mvprevisited.giphy;

import android.support.v4.util.LruCache;

import com.cainwong.mvprevisited.giphy.api.GiphyApi;
import com.cainwong.mvprevisited.giphy.api.models.TrendingGiphys;
import com.cainwong.mvprevisited.giphy.trending.TrendingGiphyList;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import toothpick.config.Module;

public class GiphyModule extends Module {

    public GiphyModule() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        // TODO move base URL
        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.giphy.com/")
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        bind(GiphyApi.class).toInstance(retrofit.create(GiphyApi.class));
        bind(GiphySectionManager.class).toInstance(new GiphySectionManager());
        bind(List.class).withName(TrendingGiphyList.class).toInstance(new ArrayList<TrendingGiphys.Giphy>());
    }
}
