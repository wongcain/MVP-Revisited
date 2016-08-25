package com.cainwong.mvprevisited.core.di;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class ScopeManager {

    private static final WeakHashMap<Context, ScopeManager> MANAGERS = new WeakHashMap<>();

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();
    private final List<Object> mPreviousKeys = new LinkedList<>();
    private final WeakReference<Context> mContext;
    private final boolean isApplicationContext;
    private Scope mScope;

    private ScopeManager(Context context) {
        mContext = new WeakReference<>(context);
        isApplicationContext = context.equals(context.getApplicationContext());
        mScope = isApplicationContext ? Toothpick.openScopes(context)
            : Toothpick.openScopes(context.getApplicationContext(), context);
    }

    private synchronized void initScope(List<Object> keys){
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

            // Create list for holding scope keys, and pre-populate with application and
            // context (if different from application).
            List<Object> listToOpen = new ArrayList<>();
            if (!isApplicationContext) {
                listToOpen.add(mContext.get().getApplicationContext());
            }
            listToOpen.add(mContext.get());

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

    /**
     * Given a scope and a place class, instantiate and install any modules configured for
     * the place into the scope
     */
    private void installModulesForScopeKey(Object key){
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

    private Scope getCurrentScope(){
        r.lock();
        try {
            return mScope;
        } finally {
            r.unlock();
        }
    }

    private void closeAll(){
        w.lock();
        try {
            Toothpick.closeScope(mContext.get().getApplicationContext());
        } finally {
            w.unlock();
        }
    }



    public static void initScope(Context context, List<Object> keys){
        getManager(context).initScope(keys);
    }

    public static Scope getCurrentScope(Context context){
        return getManager(context).getCurrentScope();
    }

    public static void closeAll(Context context){
        getManager(context).closeAll();
    }

    private static ScopeManager getManager(Context context){
        if(!MANAGERS.containsKey(context)){
            MANAGERS.put(context, new ScopeManager(context));
        }
        return MANAGERS.get(context);
    }

}
