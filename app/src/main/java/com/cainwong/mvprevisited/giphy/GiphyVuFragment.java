package com.cainwong.mvprevisited.giphy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.core.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiphyVuFragment extends BaseFragment implements GiphyPresenter.GiphyVu {

    private final GiphyPresenter mPresenter = new GiphyPresenter();

    @BindView(R.id.giphy_container)
    ViewGroup giphyContainer;

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
        mPresenter.attachVu(this);
    }

    @Override
    public void onDestroyView() {
        mPresenter.detachVu();
        super.onDestroyView();
    }

    @Override
    public void showTrending() {

    }

    @Override
    public void showRandom() {
        giphyContainer.removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.random_giphy_vu_linearlayout, giphyContainer);
    }
}
