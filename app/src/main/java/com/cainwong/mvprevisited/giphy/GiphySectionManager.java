package com.cainwong.mvprevisited.giphy;

import com.jakewharton.rxrelay.PublishRelay;

import rx.Observable;

public class GiphySectionManager {

    public enum GiphySection {
        TRENDING(0),
        RANDOM(1);

        private final int mVal;

        GiphySection(int val) {
            mVal = val;
        }

        public int getVal() {
            return mVal;
        }

        public static GiphySection fromVal(int val){
            switch(val){
                case 0: return TRENDING;
                case 1: return RANDOM;
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
