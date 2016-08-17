package com.cainwong.mvprevisited.giphy.random;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.cainwong.mvprevisited.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomGiphyVuFramelayout extends FrameLayout implements RandomGiphyPresenter.RandomGifyVu {

    private final RandomGiphyPresenter mPresenter = new RandomGiphyPresenter();

    @BindView(R.id.random_image)
    SimpleDraweeView randomImage;

    Toast mToast;

    public RandomGiphyVuFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setProgressBarImage(new ProgressBarDrawable()).build();
        randomImage.setHierarchy(hierarchy);
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
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(imgUrl).setAutoPlayAnimations(true).build();
        randomImage.setController(controller);
    }

    @Override
    public void showMessage(String msg) {
        mToast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void clearMessage() {
        if(mToast!=null){
            mToast.cancel();
            mToast = null;
        }
    }

}
