package com.cainwong.mvprevisited.ui.places;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import rx.Observable;
import timber.log.Timber;

/**
 * Singleton class for handling app navigation
 */
@SuppressWarnings("unchecked")
public class PlaceManager {

    private final BehaviorRelay<Place> globalRelay = BehaviorRelay.create();
    private final Map<Class, BehaviorRelay> placeRelays = new HashMap<>();
    private final Stack<Place> history = new Stack<>();

    /**
     * Returns an observable that emits every Place request that comes in.
     * The behavior of the observable is "Behavior", so when a subscriber subscribes,
     * the most recent event is emitted as well as all subsequent events.
     * after a given subscription are emitted.
     */
    public Observable<Place> onGotoPlaceGlobal(){
        return globalRelay;
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
        if(!placeRelays.containsKey(clazz)) {
            BehaviorRelay<T> relay = BehaviorRelay.create();
            placeRelays.put(clazz, relay);
        }
        return (Observable<T>) placeRelays.get(clazz);
    }

    /**
     * Use this method to request navigation to a Place, without regard to the history stack.
     */
    public void gotoPlace(Place place){
        gotoPlace(place, false);
    }

    /**
     * Use this method to request navigation to a Place. If the parameter tryBack is True,
     * the history is searched for an instance of the same place, and if found, goes back to
     * the Place in the history. If tryBack is False, or if the Place is not found on the history,
     * a new Place is added to the history stack.
     */
    public void gotoPlace(Place place, boolean tryBack){
        if(!tryBack || goBackToPlace(place)) {
            Class clazz = place.getClass();
            if (!placeRelays.containsKey(clazz)) {
                Timber.e("No handler registered for Place: %s", clazz.getSimpleName());
            } else {
                history.add(place);
                callPlace(place);
            }
        }
    }

    /**
     * Attempts to go back to one previous Place in the history. If successful, returns True.
     * If not (i.e. history is empty) returns False.
     */
    public boolean goBack(){
        boolean success = false;
        if(history.size() > 0) {
            Place place = history.pop();
            callPlace(place);
            success = true;
        }
        return success;
    }

    /**
     * Attempts to go back in history to the given Place.  If successful, returns True.
     * If not (i.e. the Place is not found in the history) returns False.
     */
    public boolean goBackToPlace(Place place){
        boolean success = false;
        int pos = history.search(place);
        if(pos > 0){
            Timber.e("Place not found in histort=y: %s", place.toString());
        } else {
            for(int i = 0; i<pos; i++){
                history.pop();
            }
            callPlace(place);
            success = true;
        }
        return success;
    }

    /**
     * Returns the current Place (top of the history stack).  If the stack is empty, returns null.
     */
    public Place getCurrentPlace(){
        return history.isEmpty() ? null : history.peek();
    }

    /**
     * Convenience method for pushing events to both observables
     */
    private void callPlace(Place place){
        Class clazz = place.getClass();
        globalRelay.call(place);
        placeRelays.get(clazz).call(place);
    }

}
