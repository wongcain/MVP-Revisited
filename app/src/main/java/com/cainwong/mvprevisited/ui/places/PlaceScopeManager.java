package com.cainwong.mvprevisited.ui.places;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import javax.inject.Inject;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class PlaceScopeManager {

    @Inject
    Activity mActivity;

    @Inject
    PlaceManager mPlaceManager;

    private final Map<Class, Class> mParents = new HashMap<>();
    private final Map<Class, List<Module>> mModules = new HashMap<>();
    private final Set<Class> openModules = new HashSet<>();

    private Scope mScope;

    public PlaceScopeManager() {
        //TODO Populate hierarchy
        mPlaceManager.onGotoPlaceGlobal().subscribe(place -> setScopeForPlace(place));
    }

    private synchronized void setScopeForPlace(Place place){
        List<Class> hier = new ArrayList<>();
        Class placeClass = place.getClass();
        while(placeClass!=null){
            hier.add(placeClass);
            placeClass = mParents.get(placeClass);
        }
        Collections.reverse(hier);
        List<Object> keys = new ArrayList<>();
        keys.add(mActivity.getApplication());
        keys.add(mActivity);
        Scope scope = Toothpick.openScopes(mActivity.getApplication(), mActivity);
        for(Class c: hier){
            keys.add(c);
            scope = Toothpick.openScopes(keys.toArray());
            if(mModules.containsKey(c)){
                List<Module> modules = mModules.get(c);
                scope.installModules(modules.toArray(new Module[modules.size()]));
            }
        }

        Set<Class> toClose = new HashSet<>();
        toClose.addAll(openModules);
        toClose.removeAll(hier);
        for(Class c: toClose){
            Toothpick.closeScope(c);
        }
        openModules.clear();
        openModules.addAll(hier);
        mScope = scope;
    }

    public Scope getScope(){
        return mScope;
    }

}
