package com.cainwong.mvprevisited.ui.places;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import rx.Observable;
import timber.log.Timber;

@SuppressWarnings("unchecked")
public class PlaceManager {

    private final PublishRelay<Place> globalRelay = PublishRelay.create();
    private final Map<Class, BehaviorRelay> placeRelays = new HashMap<>();
    private final Stack<Place> history = new Stack<>();

    public Observable<Place> onGotoPlaceGlobal(){
        return globalRelay;
    }

    public <T> Observable<T> onGotoPlace(Class<T> clazz){
        if(!placeRelays.containsKey(clazz)) {
            BehaviorRelay<T> relay = BehaviorRelay.create();
            placeRelays.put(clazz, relay);
        }
        return (Observable<T>) placeRelays.get(clazz);
    }

    public void gotoPlace(Place place){
        gotoPlace(place, false);
    }

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

    public boolean goBack(){
        boolean success = false;
        if(history.size() > 0) {
            Place place = history.pop();
            callPlace(place);
            success = true;
        }
        return success;
    }

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

    private void callPlace(Place place){
        Class clazz = place.getClass();
        globalRelay.call(place);
        placeRelays.get(clazz).call(place);
    }

    public Place getCurrentPlace(){
        return history.isEmpty() ? null : history.peek();
    }

}
