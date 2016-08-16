package com.cainwong.mvprevisited.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.cainwong.mvprevisited.core.di.ScopeManager;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class BaseFragment extends Fragment {

    @Inject
    Lifecycle mLifecycle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDI();
        initLifecycle(savedInstanceState);
    }

    private void initDI(){
        Scope scope = ScopeManager.getCurrentScope(getContext());
        scope.installModules(new BaseFragmentModule(this));
        Toothpick.inject(this, scope);
    }

    private void initLifecycle(Bundle savedInstanceState){
        mLifecycle.loadState(savedInstanceState);
        mLifecycle.lifecycleEvent(Lifecycle.Event.CREATE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mLifecycle.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        mLifecycle.lifecycleEvent(Lifecycle.Event.STOP);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mLifecycle.lifecycleEvent(Lifecycle.Event.START);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLifecycle.lifecycleEvent(Lifecycle.Event.RESUME);
    }

    @Override
    public void onPause() {
        mLifecycle.lifecycleEvent(Lifecycle.Event.PAUSE);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mLifecycle.lifecycleEvent(Lifecycle.Event.DESTROY);
        super.onDestroy();
    }

    private static class BaseFragmentModule extends Module {

        public BaseFragmentModule(Fragment fragment) {
            bind(Lifecycle.class).toInstance(new Lifecycle());
        }

    }

}
