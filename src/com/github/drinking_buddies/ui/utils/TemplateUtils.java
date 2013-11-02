package com.github.drinking_buddies.ui.utils;

import com.github.drinking_buddies.Application;

import eu.webtoolkit.jwt.WTemplate;

public class TemplateUtils {
    public static void configureDefault(Application app, WTemplate t) {
        t.addFunction("tr", WTemplate.Functions.tr);
        t.bindString("app.url", app.resolveRelativeUrl(app.getBookmarkUrl("/")));
        t.bindString("app.base.url", app.getEnvironment().getDeploymentPath());
        t.bindString("app.context", app.getServletContext().getContextPath());
    }
}
