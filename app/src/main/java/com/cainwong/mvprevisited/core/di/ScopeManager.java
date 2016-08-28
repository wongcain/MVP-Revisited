package com.cainwong.mvprevisited.core.di;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class ScopeManager {

    private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final Lock r = rwl.readLock();
    private static final Lock w = rwl.writeLock();
    private static final List<Object> mPreviousKeys = new LinkedList<>();
    private static Context mAppContext;
    private static Scope mScope;

    public static void init(Context context){
        mAppContext = context.getApplicationContext();
        mScope = Toothpick.openScopes(mAppContext);
    }

    public static void setScope(List<Object> keys){
        w.lock();
        try {

            // close previous scope hierarchy starting at first place of difference
            Object keyToClose = null;
            int i = 0;
            for (Object key : mPreviousKeys) {
                if ((i >= keys.size()) || !key.equals(keys.get(i++))) {
                    keyToClose = key;
                    break;
                }
            }
            if (keyToClose != null) {
                Toothpick.closeScope(keyToClose);
            }

            // Create list for holding scope keys, and pre-populate with application context
            List<Object> listToOpen = new ArrayList<>();
            listToOpen.add(mAppContext);

            // Iterate through keys and open cumulative hierarchical scopes, installing any
            // modules associated with each along the way
            for (Object key : keys) {
                listToOpen.add(key);
                mScope = Toothpick.openScopes(listToOpen.toArray());
                installModulesForScopeKey(key);
            }

            // update current scope state
            mPreviousKeys.clear();
            mPreviousKeys.addAll(keys);
        } catch (Exception e) {
            Timber.wtf(e, "Error initializing scopes");
        } finally {
            w.unlock();
        }
    }
    public static void installModules(Module... modules){
        mScope.installModules(modules);
    }

    public static void inject(Object obj){
        r.lock();
        try {
            Toothpick.inject(obj, mScope);
        } finally {
            r.unlock();
        }
    }

    /**
     * Given a scope and a place class, instantiate and install any modules configured for
     * the place into the scope
     */
    private static void installModulesForScopeKey(Object key){
        Class clazz;
        if(key instanceof Class){
            clazz = (Class)key;
        } else {
            clazz = key.getClass();
        }
        ScopeConfig config = (ScopeConfig) clazz.getAnnotation(ScopeConfig.class);
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
                mScope.installModules(modules.toArray(new Module[modules.size()]));
            }
        }
    }

}
