package com.cainwong.mvprevisited.core.di;

import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
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
    private final Set<Class> mOpenScopes = new HashSet<>();
    private WeakReference<Context> mContext;
    private Scope mScope;

    private ScopeManager(Context context) {
        mContext = new WeakReference<>(context);
        mScope = context.equals(context.getApplicationContext())
            ? Toothpick.openScopes(context)
            : Toothpick.openScopes(context.getApplicationContext(), context);
    }

    private synchronized void initScope(Object obj){
        w.lock();
        try {
            // stack for holding the hierarchy of obj classes
            Stack<Class> hierarchyStack = getParentHierarchyForObject(obj.getClass());

            // create list for holding scope keys, and pre-populate with application and context
            List<Object> scopeKeys = new ArrayList<>();
            scopeKeys.add(mContext.get().getApplicationContext());
            scopeKeys.add(mContext.get());

            // initialize default scope (application, context)
            Scope scope = Toothpick.openScopes(scopeKeys.toArray());

            // iterate through hierarchy updating scope and loading any modules defined for each obj
            List<Class> newScopes = new ArrayList<>(hierarchyStack.size());
            while (!hierarchyStack.isEmpty()) {
                Class clazz = hierarchyStack.pop();
                scopeKeys.add(clazz);
                scope = Toothpick.openScopes(scopeKeys.toArray());
                installModulesForScopePlace(scope, clazz);
                newScopes.add(clazz);
            }

            // close unused scopes
            closeUnusedScopes(newScopes);

            // update current scope state
            mOpenScopes.clear();
            mOpenScopes.addAll(newScopes);
            mScope = scope;
        } finally {
            w.unlock();
        }
    }

    /**
     * build a hierarchy stack by iterating through parent place classes
     */
    private Stack<Class> getParentHierarchyForObject(Class clazz){
        Stack<Class> hierarchyStack = new Stack<>();
        while(clazz!=null){
            hierarchyStack.add(clazz);
            ScopeConfig config = (ScopeConfig) clazz.getAnnotation(ScopeConfig.class);
            clazz = ((config==null) || Void.class.equals(config.parent()))
                    ? null : config.parent();
        }
        return hierarchyStack;
    }

    /**
     * Given a scope and a place class, instantiate and install any modules configured for
     * the place into the scope
     */
    private void installModulesForScopePlace(Scope scope, Class clazz){
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
                scope.installModules(modules.toArray(new Module[modules.size()]));
            }
        }
    }

    private void closeUnusedScopes(Collection<Class> usedScopes){
        Set<Class> toClose = new HashSet<>();
        toClose.addAll(mOpenScopes);
        toClose.removeAll(usedScopes);
        for(Class c: toClose){
            Toothpick.closeScope(c);
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
            for (Class placeClass : mOpenScopes) {
                Toothpick.closeScope(placeClass);
            }
            mOpenScopes.clear();
            Toothpick.closeScope(mContext);
            mContext = null;
        } finally {
            w.unlock();
        }
    }



    public static void initScope(Context context, Object obj){
        getManager(context).initScope(obj);
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
