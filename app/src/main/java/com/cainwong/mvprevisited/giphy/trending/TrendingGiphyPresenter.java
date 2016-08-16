package com.cainwong.mvprevisited.giphy.trending;

import android.net.Uri;

import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;

import java.util.List;

public class TrendingGiphyPresenter extends BasePresenter<TrendingGiphyPresenter.TrendingGifyVu> {

    @Override
    protected void onVuAttached() {

    }

    @Override
    protected void onVuDetached() {

    }

    public interface TrendingGifyVu extends Vu {
        void setList(List<Uri> imgUris);
        void appendToList(List<Uri> imgUris);
        void scrollToTop();
    }

}
