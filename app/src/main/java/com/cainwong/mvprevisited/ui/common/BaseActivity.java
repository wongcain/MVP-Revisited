package com.cainwong.mvprevisited.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ath.fuel.FuelInjector;
import com.ath.fuel.Lazy;
import com.cainwong.mvprevisited.ui.common.lifecycle.ActivityLifecycle;
import com.cainwong.mvprevisited.ui.common.lifecycle.Lifecycle;


public class BaseActivity extends AppCompatActivity {

    private final Lazy<ActivityLifecycle> mLifecycle = Lazy.attain(this, ActivityLifecycle.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuelInjector.ignite(this, this);
        mLifecycle.get().loadState(savedInstanceState);
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.CREATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mLifecycle.get().saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.STOP);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.RESUME);
    }

    @Override
    protected void onPause() {
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.PAUSE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.DESTROY);
        super.onDestroy();
    }

    protected Lifecycle getLifecycle() {
        return mLifecycle.get();
    }

}
