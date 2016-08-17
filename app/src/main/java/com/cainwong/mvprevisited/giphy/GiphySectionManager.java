package com.cainwong.mvprevisited.giphy;

import com.jakewharton.rxrelay.PublishRelay;

import rx.Observable;

public class GiphySectionManager {

    public enum GiphySection {
        RANDOM(0),
        TRENDING(1);

        private final int mVal;

        GiphySection(int val) {
            mVal = val;
        }

        public int getVal() {
            return mVal;
        }

        public static GiphySection fromVal(int val){
            switch(val){
                case 0: return RANDOM;
                case 1: return TRENDING;
            }
            return null;
        }
    }

    private final PublishRelay<GiphySection> mSectionRelay = PublishRelay.create();

    public void setSection(GiphySection section){
        mSectionRelay.call(section);
    }

    public Observable<GiphySection> onSetSection(){
        return mSectionRelay;
    }

}
