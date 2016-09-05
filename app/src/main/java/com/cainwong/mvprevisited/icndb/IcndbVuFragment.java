package com.cainwong.mvprevisited.icndb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cainwong.mvprevisited.R;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

public class IcndbVuFragment extends Fragment implements IcndbPresenter.IcndbVu {

    private final IcndbPresenter mPresenter = new IcndbPresenter();

    @BindView(R.id.joke_text)
    TextView mJokeTextView;

    @BindView(R.id.refresh_button)
    Button mRefreshButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.icndb_vu_fragment, container, false);
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
    public void setJoke(String joke) {
        mJokeTextView.setText(Html.fromHtml(joke));
    }

    @Override
    public Observable<Void> onRefreshRequest() {
        return RxView.clicks(mRefreshButton);
    }

    public static IcndbVuFragment newInstance(){
        return new IcndbVuFragment();
    }
}
