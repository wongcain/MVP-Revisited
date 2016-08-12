package com.cainwong.mvprevisited;

import android.app.Application;
import android.util.Log;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

/**
 * Created by cainwong on 8/7/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initDI();
        initLogging();
    }

    private void initDI(){
        Scope appScope = Toothpick.openScope(this);
        appScope.installModules(new AppModule(this));
    }

    private void initLogging(){
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
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
        }
    }

}
