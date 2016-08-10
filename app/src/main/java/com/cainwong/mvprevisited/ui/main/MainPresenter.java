package com.cainwong.mvprevisited.ui.main;

import android.content.res.Resources;

import com.ath.fuel.Lazy;
import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.ui.common.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.ui.common.lifecycle.LifecycleManager;
import com.cainwong.mvprevisited.ui.common.mvp.BasePresenter;
import com.jakewharton.rxrelay.PublishRelay;

import timber.log.Timber;


class MainPresenter extends BasePresenter<MainVu> {

    private final Lazy<Resources> mResources = Lazy.attain(this, Resources.class);
    private final Lazy<LifecycleManager> mLifecycleManager = Lazy.attain(this, LifecycleManager.class);
    private final PublishRelay<String> msgRelay = PublishRelay.create();

    @Override
    public void attachView(MainVu view) {
       super.attachView(view);
        addToAutoUnsubscribe(
                msgRelay.startWith(getInitHello())
                        .subscribe(msg -> {
                            getVu().setMessage(msg);
                        })
        );
        LifecycleManager lifecycleManager = mLifecycleManager.get();
        Lifecycle lifecycle = lifecycleManager.lifeCycle();
        if(lifecycle != null) {
            addToAutoUnsubscribe(
                    lifecycle.onLifeCycleEvent().subscribe(event -> {
                        Timber.d(lifecycle.getClass().getSimpleName() + ": " + event.name());
                    })
            );
        }
    }

    private String getInitHello(){
//        return TextUtils.isEmpty(getPresentationModel().getMsg())
//                ? mResources.get().getString(R.string.hello) : getPresentationModel().getMsg();
        return mResources.get().getString(R.string.hello);
    }

}
