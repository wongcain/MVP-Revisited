package com.cainwong.mvprevisited.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.ui.BaseActivity;

import timber.log.Timber;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_view);
    }

}
