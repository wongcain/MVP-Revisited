package com.cainwong.mvprevisited.core;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.cainwong.mvprevisited.core.di.ScopeManager;
import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;

import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieSupportActivityModule;


public class BaseActivity extends AppCompatActivity {

    @Inject
    Lifecycle mLifecycle;

    @Inject
    PlaceManager mPlaceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDI();
        initLifecycle(savedInstanceState);
    }

    private void initDI(){
        Scope scope = ScopeManager.getCurrentScope(this);
        scope.installModules(new BaseActivityModule(this));
        Toothpick.inject(this, scope);
        mPlaceManager.onGotoPlaceGlobal().subscribe(
                place -> ScopeManager.initScope(this, place),
                Errors.log()
        );
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
        ScopeManager.closeAll(this);
        super.onDestroy();
    }

    private static class BaseActivityModule extends SmoothieSupportActivityModule {

        public BaseActivityModule(FragmentActivity activity) {
            super(activity);
            bind(Lifecycle.class).toInstance(new Lifecycle());
        }

    }

    @Override
    public void onBackPressed() {
        if(!mPlaceManager.goBack()) {
            super.onBackPressed();
        }
    }
}
