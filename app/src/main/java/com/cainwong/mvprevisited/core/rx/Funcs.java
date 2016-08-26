package com.cainwong.mvprevisited.core.rx;

import rx.functions.Func1;

public final class Funcs {

    private Funcs() {
        throw new AssertionError("No instances.");
    }

    public static <T> Func1<T, Boolean> not(final Func1<T, Boolean> func) {
        return value -> !func.call(value);
    }

    public static <T> Func1<T, Boolean> isEqual(T b){
        return b::equals;
    }

    public static <T> Func1<T, Boolean> isNotEqual(T b){
        return a -> !b.equals(a);
    }

    public static <T> Func1<T, Boolean> instanceOf(Class clazz) {
        return clazz::isInstance;
    }

    public static <T> Func1<T, Boolean> instanceOfAny(Class... clazzes) {
        return obj -> {
            for(Class clazz: clazzes){
                if(clazz.isInstance(obj)){
                    return true;
                }
            }
            return false;
        };
    }

}
