package com.cainwong.mvprevisited.ui.main.sub;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.ath.fuel.FuelInjector;
import com.ath.fuel.Lazy;

public class MainSubView extends TextView implements MainSubVu {

    private Lazy<MainSubPresenter> mPresenter = Lazy.attain(this, MainSubPresenter.class);

    public MainSubView(Context context, AttributeSet attrs) {
        super(context, attrs);
        FuelInjector.ignite(context, this);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenter.get().attachView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenter.get().detachView();
    }

}
