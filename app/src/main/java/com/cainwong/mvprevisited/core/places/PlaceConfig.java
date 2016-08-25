package com.cainwong.mvprevisited.core.places;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
public @interface PlaceConfig {
    Class<? extends Place> parent() default RootPlace.class;
}
