package com.cainwong.mvprevisited.ui.places.annotations;

import com.cainwong.mvprevisited.ui.places.Place;
import com.cainwong.mvprevisited.ui.places.VoidPlace;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import toothpick.config.Module;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ TYPE })
public @interface PlaceConfig {
    Class<? extends Place> parent() default VoidPlace.class;
    Class<? extends Module>[] modules() default {};
}
