package com.cainwong.mvprevisited.main;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.Button;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.core.BaseActivity;
import com.cainwong.mvprevisited.giphy.GiphyVuFragment;
import com.cainwong.mvprevisited.icndb.IcndbVuFragment;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class MainActivity extends BaseActivity implements MainPresenter.MainVu {

    private final MainPresenter mPresenter = new MainPresenter();

    @BindView(R.id.icndb_button)
    Button mIcndbButton;

    @BindView(R.id.giphy_button)
    Button mGiphyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_vu_activity);
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
    public Observable<Void> onGiphyRequest() {
        return RxView.clicks(mGiphyButton);
    }

    @Override
    public Observable<Void> onIcndbRequest() {
        return RxView.clicks(mIcndbButton);
    }

    @Override
    public void showGify() {
        String tag = GiphyVuFragment.class.getName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if(fragment==null || !fragment.isVisible()){
            if(fragment==null){
                fragment = GiphyVuFragment.newInstance();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public void showIcndb() {
        String tag = IcndbVuFragment.class.getName();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if(fragment==null || !fragment.isVisible()){
            if(fragment==null){
                fragment = IcndbVuFragment.newInstance();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, tag)
                    .commit();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
