package com.cainwong.mvprevisited.ui.hello;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cainwong.mvprevisited.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HelloView extends FrameLayout implements HelloVu {

    private final HelloPresenter mPresenter = new HelloPresenter();

    @BindView(R.id.msg)
    TextView mMsgView;

    public HelloView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
        Timber.d("onAttachedToWindow");
        super.onAttachedToWindow();
        mPresenter.attachVu(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        mPresenter.detachVu();
        super.onDetachedFromWindow();
    }

    @Override
    public void setMessage(String message) {
        mMsgView.setText(message);
    }

}
