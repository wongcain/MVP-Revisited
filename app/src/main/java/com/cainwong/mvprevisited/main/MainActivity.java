package com.cainwong.mvprevisited.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.core.BaseActivity;
import com.cainwong.mvprevisited.giphy.GiphyVuFragment;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainPresenter.MainVu {

    private final MainPresenter mPresenter = new MainPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_vu_framelayout);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mPresenter.attachVu(this);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachVu();
        super.onDestroy();
    }

    @Override
    public void showGify() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, GiphyVuFragment.newInstance())
                .commit();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
