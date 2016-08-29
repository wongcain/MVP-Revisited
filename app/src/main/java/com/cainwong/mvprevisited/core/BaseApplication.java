package com.cainwong.mvprevisited.core;

import android.app.Application;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.cainwong.mvprevisited.BuildConfig;
import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.di.ScopeManager;
import com.cainwong.mvprevisited.core.di.Ui;
import com.cainwong.mvprevisited.core.lifecycle.ActivityLifecycleCallbacksHandler;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import toothpick.Toothpick;
import toothpick.config.Module;
import toothpick.smoothie.module.SmoothieApplicationModule;

public abstract class BaseApplication extends Application {

    @Inject
    PlaceManager mPlaceManager;

    @Inject
    ActivityLifecycleCallbacks mActivityLifecycleCallbacks;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogging();
        initDI();
        initPlaceScoper();
        initFresco();
        initActivityLifecycleCallbacks();
    }

    private void initLogging(){
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    private void initDI(){
        ScopeManager.init(this);
        List<Module> appModules = new ArrayList<>();
        appModules.add(new BaseAppModule(this));
        List<Module> configuredAppModules = getAppModules();
        if(configuredAppModules!=null){
            appModules.addAll(configuredAppModules);
        }
        ScopeManager.installModules(appModules.toArray(new Module[appModules.size()]));
        ScopeManager.inject(this);
    }

    private void initPlaceScoper(){
        mPlaceManager.onGotoPlaceGlobal().subscribe(
                place -> ScopeManager.setScope(place.getHierarchy()),
                Errors.log()
        );
    }

    private void initActivityLifecycleCallbacks(){
        this.registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
    }

    private void initFresco(){
        Fresco.initialize(this);
    }

    @Override
    public void onTerminate() {
        Toothpick.closeScope(this);
        super.onTerminate();
    }

    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            // Log to crash reporting

            if (t != null) {
                if (priority == Log.ERROR) {
                    // Log throwable as error to crash reporting
                } else if (priority == Log.WARN) {
                    // Log throwable as warning to crash reporting
                }
            }
        }
    }

    protected abstract List<Module> getAppModules();

    private static class BaseAppModule extends SmoothieApplicationModule {

        public BaseAppModule(Application app) {
            super(app);
            bind(Lifecycle.class);
            bind(ActivityLifecycleCallbacks.class).to(ActivityLifecycleCallbacksHandler.class);
            bind(Scheduler.class).withName(Io.class).toInstance(Schedulers.io());
            bind(Scheduler.class).withName(Ui.class).toInstance(AndroidSchedulers.mainThread());
        }

    }

}
