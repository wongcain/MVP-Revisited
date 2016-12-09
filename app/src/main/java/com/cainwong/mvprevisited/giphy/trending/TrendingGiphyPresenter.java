package com.cainwong.mvprevisited.giphy.trending;

import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.cainwong.mvprevisited.giphy.api.models.TrendingGiphys;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class TrendingGiphyPresenter extends BasePresenter<TrendingGiphyPresenter.TrendingGifyVu> {

    @Inject
    @TrendingGiphyList
    List<TrendingGiphys.Giphy> mList;

    @Inject
    TrendingGiphyDM mTrendingGiphyDM;

    @Inject
    PlaceManager mPlaceManager;

    @Override
    protected void onVuAttached() {
        addToAutoUnsubscribe(
                //handle Place
                mPlaceManager.onGotoPlace(TrendingGiphyPlace.class).subscribe(
                        place -> query(),
                        Errors.log()
                )
        );
    }

    private void query() {
        addToAutoUnsubscribe(
                mTrendingGiphyDM.getData().subscribe(
                        this::handleResults,
                        Errors.log()
                )
        );
    }

    private void handleResults(TrendingGiphys trendingGiphys) {
        mList.clear();
        mList.addAll(trendingGiphys.getGiphys());
        List<String> imgUrls = new ArrayList<>(mList.size());
        for (TrendingGiphys.Giphy giphy : mList) {
            TrendingGiphys.Image image = giphy.getImage(TrendingGiphys.Giphy.ImageType.fixed_height_small_still);
            if (image != null) {
                imgUrls.add(image.getUrl());
            } else {
                Timber.w("Image size not found: %s - %s",
                        TrendingGiphys.Giphy.ImageType.fixed_height_small_still.toString(),
                        giphy.toString());
            }
        }
        getVu().setUriList(imgUrls);
    }

    interface TrendingGifyVu extends Vu {
        void setUriList(List<String> imgUrls);
    }

}
