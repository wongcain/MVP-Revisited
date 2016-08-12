package com.cainwong.mvprevisited.ui.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.ui.places.PlaceScopeManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

public class MainView extends FrameLayout implements MainVu {

    private final MainPresenter mPresenter = new MainPresenter();

    @BindView(R.id.main_container)
    ViewGroup mMainContainer;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenter.attachVu(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        mPresenter.detachVu();
        super.onDetachedFromWindow();
    }

    @Override
    public void loadHelloVu() {
        mMainContainer.removeAllViews();
        LayoutInflater.from(getContext()).inflate(R.layout.hello_view, mMainContainer, true);
    }

}
