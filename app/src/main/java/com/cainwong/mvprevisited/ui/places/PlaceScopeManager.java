package com.cainwong.mvprevisited.ui.places;

import android.app.Activity;

import com.cainwong.mvprevisited.ui.places.annotations.PlaceConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;


import javax.inject.Inject;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class PlaceScopeManager {

    @Inject
    Activity mActivity;

    @Inject
    PlaceManager mPlaceManager;

    private final Set<Class<? extends Place>> openPlaceScopes = new HashSet<>();

    private Scope mScope;

    public PlaceScopeManager() {
        mPlaceManager.onGotoPlaceGlobal().subscribe(place -> setScopeForPlace(place));
    }

    private synchronized void setScopeForPlace(Place place){

        // stack for holding the hierarchy of place classes
        Stack<Class<? extends Place>> hierarchyStack = getParentHierarchyForPlace(place.getClass());

        // create list for holding scope keys, and pre-populate with application and context
        List<Object> scopeKeys = new ArrayList<>();
        scopeKeys.add(mActivity.getApplication());
        scopeKeys.add(mActivity);

        // initialize default scope (application, context)
        Scope scope = Toothpick.openScopes(scopeKeys.toArray());

        // iterate through hierarchy updating scope and loading any modules defined for each place
        List<Class<? extends Place>> newPlaceScopes = new ArrayList<>(hierarchyStack.size());
        while(!hierarchyStack.isEmpty()){
            Class<? extends Place> placeClass = hierarchyStack.pop();
            scopeKeys.add(placeClass);
            scope = Toothpick.openScopes(scopeKeys.toArray());
            installModulesForScopePlace(scope, placeClass);
            newPlaceScopes.add(placeClass);
        }

        // close unused scopes
        closeUnusedPlaceScopes(newPlaceScopes);

        // update current scope state
        openPlaceScopes.clear();
        openPlaceScopes.addAll(newPlaceScopes);
        mScope = scope;
    }

    /**
     * build a hierarchy stack by iterating through parent place classes
     */
    private Stack<Class<? extends Place>> getParentHierarchyForPlace(Class<? extends Place> placeClass){
        Stack<Class<? extends Place>> hierarchyStack = new Stack<>();
        while(placeClass!=null){
            hierarchyStack.add(placeClass);
            PlaceConfig config = placeClass.getAnnotation(PlaceConfig.class);
            placeClass = ((config==null) || VoidPlace.class.equals(config.parent()))
                    ? null : config.parent();
        }
        return hierarchyStack;
    }

    /**
     * Given a scope and a place class, instantiate and install any modules configured for
     * the place into the scope
     */
    private void installModulesForScopePlace(Scope scope, Class<? extends Place> placeClass){
        PlaceConfig config = placeClass.getAnnotation(PlaceConfig.class);
        if(config != null){
            List<Module> modules = new ArrayList<>();
            for(Class<? extends Module> moduleClass: config.modules()){
                try {
                    modules.add(moduleClass.newInstance());
                } catch (Exception e) {
                    Timber.e(e, "Error loading module %s", moduleClass.getName());
                }
            }
            if(!modules.isEmpty()){
                scope.installModules(modules.toArray(new Module[modules.size()]));
            }
        }
    }

    private void closeUnusedPlaceScopes(Collection<Class<? extends Place>> usedScopes){

        Set<Class<? extends Place>> toClose = new HashSet<>();
        toClose.addAll(openPlaceScopes);
        toClose.removeAll(usedScopes);
        for(Class<? extends Place> c: toClose){
            Toothpick.closeScope(c);
        }
    }

    public Scope getScope(){
        return mScope;
    }

}
