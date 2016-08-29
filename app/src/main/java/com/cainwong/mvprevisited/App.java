package com.cainwong.mvprevisited;

import android.app.Application;
import android.support.v4.util.LruCache;

import com.cainwong.mvprevisited.core.BaseApplication;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import toothpick.config.Module;

public class App extends BaseApplication {

    @Override
    protected List<Module> getAppModules() {
        List<Module> appMoules = new ArrayList<>();
        appMoules.add(new AppModule(this));
        return appMoules;
    }


    private static class AppModule extends Module {

        public AppModule(Application app) {
            bind(LruCache.class).toInstance(new LruCache<String, Object>(1024 * 1024));

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            bind(OkHttpClient.class).toInstance(client);

            final Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            bind(Gson.class).toInstance(gson);

        }

    }

}
