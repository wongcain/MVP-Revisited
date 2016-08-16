package com.cainwong.mvprevisited.giphy.random;

import com.cainwong.mvprevisited.core.di.ScopeConfig;
import com.cainwong.mvprevisited.core.places.SimplePlace;
import com.cainwong.mvprevisited.giphy.GiphyPlace;

@ScopeConfig(parent = GiphyPlace.class)
public class RandomGiphyPlace extends SimplePlace {}
