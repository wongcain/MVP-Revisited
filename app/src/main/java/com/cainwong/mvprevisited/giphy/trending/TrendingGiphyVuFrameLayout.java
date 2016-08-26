package com.cainwong.mvprevisited.giphy.trending;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cainwong.mvprevisited.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrendingGiphyVuFrameLayout extends FrameLayout implements TrendingGiphyPresenter.TrendingGifyVu {

    private final TrendingGiphyPresenter mPresenter = new TrendingGiphyPresenter();

    private final RecyclerView.Adapter mAdapter = new TrendingGiphyRecyclerViewAdapter();

    private final List<String> mList = new ArrayList<>();

    @BindView(R.id.trending_recylcer)
    RecyclerView mTrendingRecyclerView;

    public TrendingGiphyVuFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        mTrendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mTrendingRecyclerView.setAdapter(mAdapter);
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
    public void setUriList(List<String> imgUrls) {
        mList.clear();
        mList.addAll(imgUrls);
        mAdapter.notifyDataSetChanged();
    }

    class TrendingGiphyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.trending_image)
        SimpleDraweeView mTrendingImageView;

        public TrendingGiphyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setImgUrl(String imgUrl){
            mTrendingImageView.setImageURI(imgUrl);
        }
    }

    class TrendingGiphyRecyclerViewAdapter extends RecyclerView.Adapter<TrendingGiphyViewHolder>{

        @Override
        public TrendingGiphyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_giphy_list_item, parent, false);
            return new TrendingGiphyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TrendingGiphyViewHolder holder, int position) {
            holder.setImgUrl(mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

}
