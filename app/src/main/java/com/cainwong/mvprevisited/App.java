package com.cainwong.mvprevisited;

import android.app.Application;
import android.util.Log;

import com.ath.fuel.FuelInjector;

import timber.log.Timber;

/**
 * Created by cainwong on 8/7/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FuelInjector.initializeModule(new AppModule(this));

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
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

}
