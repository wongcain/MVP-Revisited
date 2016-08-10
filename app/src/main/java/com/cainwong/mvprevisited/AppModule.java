package com.cainwong.mvprevisited;

import android.app.Application;
import android.content.res.Resources;

import com.ath.fuel.FuelModule;
import com.ath.fuel.Lazy;

public class AppModule extends FuelModule {

    public AppModule(Application app) {
        super(app);
    }

    @Override
    protected void configure() {
        super.configure();
        bind(Resources.class, new FuelProvider() {
            @Override
            public Object provide(Lazy lazy, Object parent) {
                return lazy.getContext().getResources();
            }
        });
    }
}
