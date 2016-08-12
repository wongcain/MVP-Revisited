package com.cainwong.mvprevisited.ui.places;

import android.content.Context;

import com.cainwong.mvprevisited.ui.places.annotations.PlaceConfig;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.WeakHashMap;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class PlaceScopeManager {

    private static final WeakHashMap<Context, PlaceScopeManager> MANAGERS = new WeakHashMap<>();

    private final Set<Class<? extends Place>> openPlaceScopes = new HashSet<>();
    private WeakReference<Context> mContext;
    private Scope mScope;

    private PlaceScopeManager(Context context) {
        mContext = new WeakReference<Context>(context);
        mScope = Toothpick.openScopes(mContext.get().getApplicationContext(), mContext.get());
    }

    private synchronized void initScopeForPlace(Place place){

        // stack for holding the hierarchy of place classes
        Stack<Class<? extends Place>> hierarchyStack = getParentHierarchyForPlace(place.getClass());

        // create list for holding scope keys, and pre-populate with application and context
        List<Object> scopeKeys = new ArrayList<>();
        scopeKeys.add(mContext.get().getApplicationContext());
        scopeKeys.add(mContext.get());

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

    private Scope getCurrentScope(){
        return mScope;
    }

    private void closeAll(){
        for(Class placeClass: openPlaceScopes){
            Toothpick.closeScope(placeClass);
        }
        openPlaceScopes.clear();
        Toothpick.closeScope(mContext);
        mContext = null;
    }



    public static void initScopeForPlace(Context context, Place place){
        getManager(context).initScopeForPlace(place);
    }

    public static Scope getCurrentScope(Context context){
        return getManager(context).getCurrentScope();
    }

    public static void closeAll(Context context){
        getManager(context).closeAll();
    }

    private static PlaceScopeManager getManager(Context context){
        if(!MANAGERS.containsKey(context)){
            MANAGERS.put(context, new PlaceScopeManager(context));
        }
        return MANAGERS.get(context);
    }
}
