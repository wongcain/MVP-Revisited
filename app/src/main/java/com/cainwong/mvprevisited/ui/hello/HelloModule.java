package com.cainwong.mvprevisited.ui.hello;

import toothpick.config.Module;

public class HelloModule extends Module {

    public HelloModule() {
        bind(String.class).withName("test").toInstance("Hello from module!");
    }
}
