package com.cainwong.mvprevisited.ui.main;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cainwong.mvprevisited.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

public class MainView extends FrameLayout implements MainVu {

    @Inject
    MainPresenter mPresenter;

    @Inject
    LayoutInflater mLayoutInflater;

    @BindView(R.id.main_container)
    ViewGroup mMainContainer;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Scope scope = Toothpick.openScopes(context.getApplicationContext(), context);
        Toothpick.inject(this, scope);
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
        mLayoutInflater.inflate(R.layout.hello_view, mMainContainer, true);
    }

}
