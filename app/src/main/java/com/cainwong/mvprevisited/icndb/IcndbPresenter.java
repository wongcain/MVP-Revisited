package com.cainwong.mvprevisited.icndb;

import android.text.Html;

import com.cainwong.mvprevisited.core.lifecycle.Lifecycle;
import com.cainwong.mvprevisited.core.mvp.BasePresenter;
import com.cainwong.mvprevisited.core.mvp.Vu;
import com.cainwong.mvprevisited.core.places.PlaceManager;
import com.cainwong.mvprevisited.core.rx.Errors;
import com.cainwong.mvprevisited.icndb.api.models.RandomJoke;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

public class IcndbPresenter extends BasePresenter<IcndbPresenter.IcndbVu> {

    private final static String JOKE_KEY = IcndbPresenter.class.getName() + ".joke";

    @Inject
    IcndbRandomJokeDM mIcndbRandomJokeDM;

    @Inject
    PlaceManager mPlaceManager;

    @Inject
    Lifecycle mLifecycle;

    private Subscription mIcndbRandomJokeDMSubscription;
    private String mLastJoke = "";

    @Override
    protected void onVuAttached() {
        addToAutoUnsubscribe(

                mPlaceManager.onGotoPlace(IcndbPlace.class).subscribe(
                        place -> fetchJoke(),
                        Errors.log()
                ),

                getVu().onRefreshRequest().subscribe(
                        ignore -> fetchJoke(),
                        Errors.log()
                ),

                mLifecycle.onLoadState().subscribe(
                        bundle -> {
                            mLastJoke = bundle.getString(JOKE_KEY);
                            if(mLastJoke!=null){
                                getVu().setJoke(mLastJoke);
                            }
                        },
                        Errors.log()
                ),
                mLifecycle.onSaveState().subscribe(
                        bundle -> bundle.putString(JOKE_KEY, mLastJoke),
                        Errors.log()
                )
        );
    }

    @Override
    protected void onVuDetached() {

    }

    private void fetchJoke() {

        if (mIcndbRandomJokeDMSubscription != null) {
            removeSubscription(mIcndbRandomJokeDMSubscription);
        }
        mIcndbRandomJokeDMSubscription = mIcndbRandomJokeDM.getData().subscribe(
                this::handleJokeResponse,
                Errors.log()
        );
        addToAutoUnsubscribe(mIcndbRandomJokeDMSubscription);
    }

    private void handleJokeResponse(RandomJoke response){
        mLastJoke = response.getJoke().getText();
        getVu().setJoke(mLastJoke);
    }

    public interface IcndbVu extends Vu {

        void setJoke(String joke);
        Observable<Void> onRefreshRequest();

    }
}
