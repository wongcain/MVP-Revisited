package com.cainwong.mvprevisited.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.cainwong.mvprevisited.ui.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.ui.places.PlaceManager;
import com.cainwong.mvprevisited.ui.places.PlaceScopeManager;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieSupportActivityModule;


public class BaseActivity extends AppCompatActivity {

    @Inject
    Lifecycle mLifecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDI();
        super.onCreate(savedInstanceState);
        initLifecycle(savedInstanceState);
    }

    private void initDI(){
        Scope scope = Toothpick.openScopes(getApplication(), this);
        scope.installModules(new BaseActivityModule(this));
        Toothpick.inject(this, scope);
    }

    private void initLifecycle(Bundle savedInstanceState){
        mLifecycle.loadState(savedInstanceState);
        mLifecycle.lifecycleEvent(Lifecycle.Event.CREATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mLifecycle.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        mLifecycle.lifecycleEvent(Lifecycle.Event.STOP);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLifecycle.lifecycleEvent(Lifecycle.Event.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLifecycle.lifecycleEvent(Lifecycle.Event.RESUME);
    }

    @Override
    protected void onPause() {
        mLifecycle.lifecycleEvent(Lifecycle.Event.PAUSE);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLifecycle.lifecycleEvent(Lifecycle.Event.DESTROY);
        Toothpick.closeScope(this);
        super.onDestroy();
    }

    protected Lifecycle getLifecycle() {
        return mLifecycle;
    }

    private static class BaseActivityModule extends SmoothieSupportActivityModule {

        public BaseActivityModule(FragmentActivity activity) {
            super(activity);
            bind(Lifecycle.class).toInstance(new Lifecycle());
            bind(PlaceManager.class).toInstance(new PlaceManager());
            bind(PlaceScopeManager.class).toInstance(new PlaceScopeManager());
        }

    }

}
