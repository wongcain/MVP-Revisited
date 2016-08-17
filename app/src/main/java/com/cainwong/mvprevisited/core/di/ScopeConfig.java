package com.cainwong.mvprevisited.core.di;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import toothpick.config.Module;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ TYPE })
public @interface ScopeConfig {
    Class parent() default Void.class;
    Class<? extends Module>[] modules() default {};
}