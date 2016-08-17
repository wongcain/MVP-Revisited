package com.cainwong.mvprevisited.giphy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.core.BaseFragment;
import com.jakewharton.rxbinding.support.v4.view.RxViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class GiphyVuFragment extends BaseFragment implements GiphyPresenter.GiphyVu {

    private final GiphyPresenter mPresenter = new GiphyPresenter();

    @BindView(R.id.pager)
    ViewPager mPager;

    @BindView(R.id.tabs)
    TabLayout mTabs;

    public static GiphyVuFragment newInstance(){
        return new GiphyVuFragment();
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
        mPager.setAdapter(new GiphyPagerAdapter(view.getContext()));
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
        mPager.setCurrentItem(GiphySectionManager.GiphySection.RANDOM.getVal());
    }

    @Override
    public void showRandom() {
        mPager.setCurrentItem(GiphySectionManager.GiphySection.TRENDING.getVal());

    }

    @Override
    public Observable<GiphySectionManager.GiphySection> onSectionChanged() {
        return RxViewPager.pageSelections(mPager).map(GiphySectionManager.GiphySection::fromVal);
    }

    static class GiphyPagerAdapter extends PagerAdapter {

        Context mContext;

        GiphyPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if(GiphySectionManager.GiphySection.RANDOM.getVal() == position) {
                return LayoutInflater.from(mContext).inflate(R.layout.random_giphy_vu_linearlayout, container);
            } else if(GiphySectionManager.GiphySection.TRENDING.getVal() == position) {
                return new TextView(container.getContext(), null);
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(GiphySectionManager.GiphySection.RANDOM.getVal() == position) {
                return mContext.getResources().getString(R.string.random);
            } else if(GiphySectionManager.GiphySection.TRENDING.getVal() == position) {
                return mContext.getResources().getString(R.string.trending);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object instanceof View;
        }
    }
}