package com.cainwong.mvprevisited.core.places;

import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rx.Observable;

/**
 * Singleton class for handling app navigation
 */
@SuppressWarnings("unchecked")
public class PlaceManager {

    /**
     * Used when calling "GoToPlace" to control the effect on the history stack:
     *  - ADD: puts place on top of the stack
     *  - REPLACE_TOP: replaces the place on the top of the stack. (If stack is empty, same as ADD.)
     *  - TRY_BACK_TO_EXACT_SAME: searches history for the same place. If found, pops the history
     *                            stack to the found place.  If not, performs ADD.  Places are found
     *                            using Stack.search(). You may need to override the equals() and
     *                            hashcode() in your Place object if you want matches on different
     *                            instances with the same payload.
     *  - TRY_BACK_TO_SAME_TYPE: searches history for same place type (by class). If found, pops the history
     *                            stack to the found place.  If not, performs ADD.
     *  - NONE: no change to history stack
     */
    public enum HistoryAction {
        ADD,
        REPLACE_TOP,
        TRY_BACK_TO_EXACT_SAME,
        TRY_BACK_TO_SAME_TYPE,
        NONE
    }

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    private final BehaviorRelay<Place> mGlobalRelay = BehaviorRelay.create();
    private final Map<Class, BehaviorRelay> mPlaceRelays = new HashMap<>();
    private final Deque<Place> mHistory = new ArrayDeque<>();
    private Place mCurrentPlace;

    /**
     * Returns an observable that emits every Place request that comes in.
     * The behavior of the observable is "Behavior", so when a subscriber subscribes,
     * the most recent event is emitted as well as all subsequent events.
     * after a given subscription are emitted.
     */
    public Observable<Place> onGotoPlaceGlobal(){
        return mGlobalRelay;
    }

    /**
     * Returns an observable that emits Place requests of a given type (class).
     * The behavior of the observable is "Behavior", so when a subscriber subscribes,
     * the most recent event is emitted as well as all subsequent events.
     * This observable should be used to inform a component to load a sub-component
     * associated with the Place.  Additionally the sub-component should subscribe to this
     * observable upon loading in order to receive any payload that comes along with the Place
     * request.
     */
    public <T> Observable<T> onGotoPlace(Class<T> clazz){
        if(!mPlaceRelays.containsKey(clazz)) {
            BehaviorRelay<T> relay = BehaviorRelay.create();
            mPlaceRelays.put(clazz, relay);
        }
        return (Observable<T>) mPlaceRelays.get(clazz);
    }

    /**
     * Use this method to request navigation to a Place, with default history action ADD.
     */
    public void gotoPlace(Place place){
        gotoPlace(place, HistoryAction.ADD);
    }

    /**
     * Use this method to request navigation to a Place with appropriate {@link HistoryAction}
     */
    public void gotoPlace(Place place, HistoryAction historyAction){
        w.lock();
        try {
            switch (historyAction) {
                case ADD:
                    mHistory.add(place);
                    callPlace(place);
                    break;
                case REPLACE_TOP:
                    mHistory.poll();
                    callPlace(place);
                    break;
                case TRY_BACK_TO_EXACT_SAME:
                    if (!goBackToPlaceExact(place)) {
                        mHistory.add(place);
                        callPlace(place);
                    }
                    break;
                case TRY_BACK_TO_SAME_TYPE:
                    if (!goBackToPlaceByType(place)) {
                        mHistory.add(place);
                        callPlace(place);
                    }
                    break;
                case NONE:
                    callPlace(place);
                    break;
            }
        } finally {
            w.unlock();
        }
    }

    /**
     * Attempts to go back to one previous Place in the mHistory. If successful, returns True.
     * If not (i.e. mHistory is empty) returns False.
     */
    public boolean goBack(){
        boolean success = false;
        w.lock();
        try {
            if (mHistory.size() > 0) {
                Place place = mHistory.pop();
                callPlace(place);
                success = true;
            } else {
                mCurrentPlace = null;
            }
        } finally {
            w.unlock();
        }
        return success;
    }

    /**
     * Returns the current Place (top of the mHistory stack).  If the stack is empty, returns null.
     */
    public Place getCurrentPlace(){
        r.lock();
        try {
            return mCurrentPlace;
        } finally {
            r.unlock();
        }
    }

    /**
     * Attempts to go back in mHistory to the given Place.  If successful, returns True.
     * If not (i.e. the Place is not found in the mHistory) returns False.
     * Places are found using Stack.search(). You may need to override the equals() and hashcode()
     * in your Place object if you want matches on different instances with the same payload.
     */
    private boolean goBackToPlaceExact(Place place){
        boolean success = false;
        int i = 0;
        for(Place p: mHistory){
            if(p.equals(place)){
                for(int j=0; j<i; j++){
                    mHistory.poll();
                }
                callPlace(p);
                success = true;
                break;
            }
            i++;
        }
        return success;
    }

    /**
     * Attempts to go back in mHistory to the given Place by type (class).  If successful, returns True.
     * If not (i.e. the Place is not found in the mHistory) returns False.
     */
    private boolean goBackToPlaceByType(Place place){
        boolean success = false;
        int i = 0;
        for(Place p: mHistory){
            if(p.getClass().equals(place.getClass())){
                for(int j=0; j<i; j++){
                    mHistory.poll();
                }
                callPlace(p);
                success = true;
                break;
            }
            i++;
        }
        return success;
    }

    /**
     * Convenience method for pushing events to both observables
     */
    private void callPlace(Place place){
        Class clazz = place.getClass();
        mGlobalRelay.call(place);
        mPlaceRelays.get(clazz).call(place);
        mCurrentPlace = place;
    }

}
