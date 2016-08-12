package com.cainwong.mvprevisited.ui.hello;

import toothpick.config.Module;

public class HelloModule extends Module {

    public HelloModule() {
        bind(HelloDep.class).toInstance(new HelloDep("Hello from module!"));
    }
}
