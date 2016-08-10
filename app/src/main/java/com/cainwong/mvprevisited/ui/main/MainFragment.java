package com.cainwong.mvprevisited.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ath.fuel.Lazy;
import com.cainwong.mvprevisited.R;
import com.cainwong.mvprevisited.ui.common.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cainwong on 8/8/16.
 */
public class MainFragment extends BaseFragment implements MainVu {

    private Lazy<MainPresenter> mPresenter = Lazy.attain(this, MainPresenter.class);

    @BindView(R.id.msg)
    TextView msgView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container);
        ButterKnife.bind(this, view);
        mPresenter.get().attachView(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        mPresenter.get().detachView();
        super.onDestroyView();
    }

    @Override
    public void setMessage(String msg) {
        msgView.setText(msg);
    }

}
