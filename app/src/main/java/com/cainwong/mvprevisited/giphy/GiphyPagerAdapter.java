package com.cainwong.mvprevisited.giphy;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cainwong.mvprevisited.R;

public class GiphyPagerAdapter extends PagerAdapter {

    private final Context mContext;

    GiphyPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewGroup v = null;
        if (GiphySectionManager.GiphySection.RANDOM.getVal() == position) {
            v = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.random_giphy_vu_linearlayout, container, false);
        } else if (GiphySectionManager.GiphySection.TRENDING.getVal() == position) {
            v = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.trending_giphy_vu_framelayout, container, false);
        }
        if (v != null) {
            container.addView(v);
        }
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (GiphySectionManager.GiphySection.RANDOM.getVal() == position) {
            return mContext.getResources().getString(R.string.random);
        } else if (GiphySectionManager.GiphySection.TRENDING.getVal() == position) {
            return mContext.getResources().getString(R.string.trending);
        }
        return null;
    }

    @Override
    public int getCount() {
        return GiphySectionManager.GiphySection.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
