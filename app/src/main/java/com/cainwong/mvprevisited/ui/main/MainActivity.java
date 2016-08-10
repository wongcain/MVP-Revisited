package com.cainwong.mvprevisited.ui.main;

import android.os.Bundle;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.ui.common.BaseActivity;

import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class MainActivity extends BaseActivity {

    private final CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCompositeSubscription.add(
                getLifecycle().onLifeCycleEvent().subscribe(event -> {
                    Timber.d(getLifecycle().getClass().getSimpleName() + ": " + event.name());
                })
        );
    }

}
