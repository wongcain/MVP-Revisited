package com.cainwong.mvprevisited.giphy.random;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cainwong.mvprevisited.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomGiphyVuFramelayout extends FrameLayout implements RandomGiphyPresenter.RandomGifyVu {

    private final RandomGiphyPresenter mPresenter = new RandomGiphyPresenter();

    @BindView(R.id.random_image)
    SimpleDraweeView randomImage;

    public RandomGiphyVuFramelayout(Context context, AttributeSet attrs) {
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
    public void setImgUrl(String imgUrl) {
        randomImage.setImageURI(imgUrl);
    }
}
