package com.cainwong.mvprevisited.ui.common.lifecycle;

import com.ath.fuel.Lazy;
import com.ath.fuel.err.FuelInjectionException;

public class LifecycleManager {

    public Lifecycle lifeCycle() {
        Lifecycle lifecycle = null;
        try {
            lifecycle = Lazy.attain(this, FragmentLifecycle.class).get();
        } catch (FuelInjectionException e) {
            // noop
        }

        if (lifecycle == null) {
            try {
                lifecycle = Lazy.attain(this, ActivityLifecycle.class).get();
            } catch (FuelInjectionException e) {
                // noop
            }
        }

        return lifecycle;
    }

}
