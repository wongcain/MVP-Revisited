package com.cainwong.mvprevisited.core.places;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public abstract class Place<T> {

     private final T mData;

     public Place(T mData) {
          this.mData = mData;
     }

     public T getData() {
          return mData;
     }

     public List<Class<? extends Place>> getHierarchy(){
          ArrayDeque<Class<? extends Place>> hierarchyQueue = new ArrayDeque<>();
          Class<? extends Place> clazz = this.getClass();
          while(clazz!=null){
               hierarchyQueue.add(clazz);
               PlaceConfig config = clazz.getAnnotation(PlaceConfig.class);
               clazz = ((config==null) || RootPlace.class.equals(config.parent()))
                       ? null : config.parent();
          }
          return new ArrayList<>(hierarchyQueue);
     }

     @Override
     public String toString() {
          return this.getClass().getSimpleName() + ": " + mData;
     }
}
