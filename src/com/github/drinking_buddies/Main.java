package com.github.drinking_buddies;

import eu.webtoolkit.jwt.WApplication;
import eu.webtoolkit.jwt.WEnvironment;
import eu.webtoolkit.jwt.WtServlet;

public class Main extends WtServlet {
    private static final long serialVersionUID = 1L;

    public Main() {
        super();
    }

    @Override
    public WApplication createApplication(WEnvironment env) {
        return new Application(env, this.getServletContext());
    }
}
