package com.cainwong.mvprevisited.core.places;

import com.cainwong.mvprevisited.core.rx.Funcs;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import rx.Observable;
import timber.log.Timber;

/**
 * Singleton class for handling app navigation
 */
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
    private final BehaviorRelay<Place> mPlaceRelay = BehaviorRelay.create();
    private final Deque<Place> mHistory = new ArrayDeque<>();
    private Place mCurrentPlace;

    /**
     * Returns an observable that emits every Place request that comes in.
     * The behavior of the observable is "Behavior", so when a subscriber subscribes,
     * the most recent event is emitted as well as all subsequent events.
     * after a given subscription are emitted.
     */
    public Observable<Place> onGotoPlaceGlobal(){
        return mPlaceRelay.doOnNext(
                place -> Timber.d("Global Place Event: %s", place)
        );
    }

    /**
     * Returns an observable that emits Place requests of a given type (class).
     * The behavior of the observable is "Behavior", so when a subscriber subscribes,
     * the most recent event is emitted as well as all subsequent events.
     */
    public Observable<Place> onGotoPlace(Class<? extends Place> clazz){
        return mPlaceRelay.filter(Funcs.instanceOf(clazz)).doOnNext(
                place -> Timber.d("Filtered Place Event: %s", place)
        );
    }

    /**
     * Returns an observable that emits Place if either the Place is an instance of a given class
     * OR any ancestor of the Place is an instance of a given class.
     * The purpose of this observable is that a module that is responsible for loading UI for a
     * given Place will also be informed of all children of that Place, allowing for responding
     * to deep links.
     * The behavior of the observable is "Behavior", so when a subscriber subscribes,
     * the most recent event is emitted as well as all subsequent events.
     */
    public Observable<Place> onGotoPlaceOrDescendants(Class<? extends Place> clazz){
        return mPlaceRelay.filter(place -> {
            List<Class<? extends Place>> ancestors = place.getHierarchy();
            for(Class<? extends Place> clazz1 : ancestors){
                if(clazz1.equals(clazz)){
                    return true;
                }
            }
            return false;
        }).doOnNext(
                place -> Timber.d("Descendants Place Event for %s: %s", clazz.getSimpleName(), place)
        );
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
            if (mHistory.size() > 1) {
                Place place = mHistory.pop();
                callPlace(place);
                success = true;
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
        mPlaceRelay.call(place);
        mCurrentPlace = place;
    }

}
