package com.cainwong.mvprevisited;

import android.app.Application;
import android.support.v4.util.LruCache;

import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.di.Ui;
import com.cainwong.mvprevisited.core.places.PlaceManager;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class AppModule extends SmoothieApplicationModule {

    public AppModule(Application application) {
        super(application);
        bind(PlaceManager.class).toInstance(new PlaceManager());
        bind(Scheduler.class).withName(Io.class).toInstance(Schedulers.io());
        bind(Scheduler.class).withName(Ui.class).toInstance(AndroidSchedulers.mainThread());
        bind(LruCache.class).toInstance(new LruCache<String, Object>(1024 * 1024));
    }
}
