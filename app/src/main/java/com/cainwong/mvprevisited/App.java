package com.cainwong.mvprevisited;

import android.app.Application;
import android.util.Log;

import com.cainwong.mvprevisited.core.di.Io;
import com.cainwong.mvprevisited.core.di.ScopeManager;
import com.cainwong.mvprevisited.core.di.Ui;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.facebook.drawee.backends.pipeline.Fresco;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDI();
        initLogging();
        initFresco();
    }

    private void initDI(){
        Scope appScope = ScopeManager.getCurrentScope(this);
        appScope.installModules(new AppModule(this));
    }

    private void initLogging(){
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
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

    private static class AppModule extends SmoothieApplicationModule{

        public AppModule(Application application) {
            super(application);
            bind(PlaceManager.class).toInstance(new PlaceManager());
            bind(Scheduler.class).withName(Io.class).toInstance(Schedulers.io());
            bind(Scheduler.class).withName(Ui.class).toInstance(AndroidSchedulers.mainThread());
        }
    }

}
