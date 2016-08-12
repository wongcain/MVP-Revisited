package com.cainwong.mvprevisited.ui.hello;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.cainwong.mvprevisited.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import toothpick.Scope;
import toothpick.Toothpick;

public class HelloView extends FrameLayout implements HelloVu {

    @Inject
    HelloPresenter mPresenter;

    @BindView(R.id.msg)
    TextView mMsgView;

    public HelloView(Context context, AttributeSet attrs) {
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
    public void setMessage(String message) {
        mMsgView.setText(message);
    }

}
