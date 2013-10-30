package com.github.drinking_buddies;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WText;

public class Application extends WApplication {
    public Application(WEnvironment env) {
        super(env);
        
        setTitle("Drinking buddies");

        getRoot().addWidget(new WText("Tata: the drinking buddies app!"));
    }
}
