package com.cainwong.mvprevisited.ui.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ath.fuel.FuelInjector;
import com.ath.fuel.Lazy;
import com.cainwong.mvprevisited.ui.common.lifecycle.FragmentLifecycle;
import com.cainwong.mvprevisited.ui.common.lifecycle.Lifecycle;

public class BaseFragment extends Fragment {

    private final Lazy<FragmentLifecycle> mLifecycle = Lazy.attain(this, FragmentLifecycle.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuelInjector.ignite(getContext(), this);
        mLifecycle.get().loadState(savedInstanceState);
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.CREATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mLifecycle.get().saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.STOP);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.RESUME);
    }

    @Override
    public void onPause() {
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.PAUSE);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mLifecycle.get().lifecycleEvent(Lifecycle.Event.DESTROY);
        super.onDestroy();
    }

    protected Lifecycle getLifecycle() {
        return mLifecycle.get();
    }

}
