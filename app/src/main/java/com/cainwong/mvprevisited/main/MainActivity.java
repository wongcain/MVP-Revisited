package com.cainwong.mvprevisited.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.giphy.GiphyVuFragment;
import com.cainwong.mvprevisited.icndb.IcndbVuFragment;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxrelay.PublishRelay;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MainPresenter.MainVu {

    private final MainPresenter mPresenter = new MainPresenter();

    private final PublishRelay<Void> mGiphyRelay = PublishRelay.create();
    private final PublishRelay<Void> mIcnbdRelay = PublishRelay.create();

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_vu_activity);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_giphy:
                mGiphyRelay.call(null);
                return true;
            case R.id.action_icndb:
                mIcnbdRelay.call(null);
                return true;
            default:
                Timber.wtf("Unhandled menu item: %s - %s", item.getItemId(), item.toString());
        }
        return false;
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
        return mGiphyRelay;
    }

    @Override
    public Observable<Void> onIcndbRequest() {
        return mIcnbdRelay;
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

}
