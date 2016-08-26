package com.cainwong.mvprevisited.icndb;

import com.cainwong.mvprevisited.core.di.ScopeConfig;
import com.cainwong.mvprevisited.core.places.SimplePlace;
import com.cainwong.mvprevisited.icndb.IcndbModule;

@ScopeConfig(modules = IcndbModule.class)
public class IcndbPlace extends SimplePlace {
}
