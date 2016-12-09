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
import com.facebook.imagepipeline.request.ImageRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RandomGiphyFramelayout extends FrameLayout implements RandomGiphyPresenter.RandomGifyVu {

    private final RandomGiphyPresenter mPresenter = new RandomGiphyPresenter();

    @BindView(R.id.random_image)
    SimpleDraweeView mRandomImageView;

    public RandomGiphyFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                .setProgressBarImage(new ProgressBarDrawable()).build();
        mRandomImageView.setHierarchy(hierarchy);
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
    public void setImgUrl(String imgUrl, String lowResImgUrl) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setLowResImageRequest(ImageRequest.fromUri(lowResImgUrl))
                .setImageRequest(ImageRequest.fromUri(imgUrl))
                .setOldController(mRandomImageView.getController())
                .setAutoPlayAnimations(true)
                .build();
        mRandomImageView.setController(controller);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
