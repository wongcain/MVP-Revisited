package com.cainwong.mvprevisited.icndb;

import com.cainwong.mvprevisited.icndb.api.IcndbApi;
import com.google.gson.Gson;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import toothpick.config.Module;

public class IcndbModule extends Module {

    @Inject
    public IcndbModule(Gson gson, OkHttpClient okHttpClient) {
        // TODO move base URL
        final Retrofit retrofit = new Retrofit.Builder().baseUrl("http://api.icndb.com")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        bind(IcndbApi.class).toInstance(retrofit.create(IcndbApi.class));
    }
}
