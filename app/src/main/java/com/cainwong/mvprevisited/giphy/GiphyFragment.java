package com.cainwong.mvprevisited.giphy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cainwong.mvprevisited.R;
import com.jakewharton.rxbinding.support.v4.view.RxViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class GiphyFragment extends Fragment implements GiphyPresenter.GiphyVu {

    private final GiphyPresenter mPresenter = new GiphyPresenter();

    @BindView(R.id.pager)
    ViewPager mPager;

    @BindView(R.id.tabs)
    TabLayout mTabs;

    public static GiphyFragment newInstance(){
        return new GiphyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gify_vu_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mPager.setAdapter(new GiphyPagerAdapter(getContext()));
        mTabs.setupWithViewPager(mPager);
        mPresenter.attachVu(this);
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachVu();
        super.onDestroyView();
    }

    @Override
    public void showTrending() {
        int val = GiphySectionManager.GiphySection.TRENDING.getVal();
        if(mPager.getCurrentItem()!=val) {
            mPager.setCurrentItem(val);
        }
    }

    @Override
    public void showRandom() {
        int val = GiphySectionManager.GiphySection.RANDOM.getVal();
        if(mPager.getCurrentItem()!=val) {
            mPager.setCurrentItem(val);
        }

    }

    @Override
    public Observable<GiphySectionManager.GiphySection> onSectionChanged() {
        return RxViewPager.pageSelections(mPager).map(GiphySectionManager.GiphySection::fromVal);
    }


}
