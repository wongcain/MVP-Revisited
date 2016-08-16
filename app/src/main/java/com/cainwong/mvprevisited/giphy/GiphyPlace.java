package com.cainwong.mvprevisited.giphy;

import com.cainwong.mvprevisited.api.giphy.GiphyModule;
import com.cainwong.mvprevisited.core.di.ScopeConfig;
import com.cainwong.mvprevisited.core.places.SimplePlace;

@ScopeConfig(modules = GiphyModule.class)
public class GiphyPlace extends SimplePlace{}